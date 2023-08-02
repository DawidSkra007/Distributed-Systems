import java.util.HashMap;
import java.util.LinkedList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import service.core.Quotation;
import service.message.ClientMessage;
import service.message.OfferMessage;
import service.message.QuotationMessage;

public class Main {
    public static void main(String[] args) {
        String url;
        if (args.length <= 0) {
            url = "failover://tcp://localhost:61616";
        } else {
            url = args[1];
        }
        System.out.println("Using URL: " + url); //DELETE
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        HashMap<Long, OfferMessage> offerHMap = new HashMap<Long, OfferMessage>();
        try {
            Connection connection = factory.createConnection();
            connection.setClientID("broker");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue QuotationsQueue = session.createQueue("QUOTATIONS");
            Topic ApplicationsTopic = session.createTopic("APPLICATIONS"); //each quotation service returns a quotation to the broker
            Queue OfferQueue = session.createQueue("OFFERS");

            MessageProducer producer = session.createProducer(OfferQueue); // produces offerMessages
            MessageConsumer quotationsConsumer = session.createConsumer(QuotationsQueue); // consumes quotationMessages
            MessageConsumer applicationConsumer = session.createConsumer(ApplicationsTopic); // monitors applicationMessages to match with quotationMessages
            connection.start();
            // get all the quotationMessages from the QuotationsQueue and match them with the the clients in the ApplicationsTopic, output quotes for each client
            
            applicationConsumer.setMessageListener(new MessageListener() { 
                @Override
                public void onMessage(Message message) {
                try {
                    ClientMessage cMessage = (ClientMessage) ((ObjectMessage) message).getObject();
                    OfferMessage msg = new OfferMessage(cMessage.getInfo(), new LinkedList<Quotation>());
                    offerHMap.put(cMessage.getToken(), msg);

                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000); // 3seconds
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            Message reposnse;
                            try {
                                reposnse = session.createObjectMessage(offerHMap.get(cMessage.getToken()));
                                producer.send(reposnse);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }  
                        }
                        }.start();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                }
            });
            
            quotationsConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                try {
                    QuotationMessage request = (QuotationMessage) ((ObjectMessage) message).getObject();
                    Quotation quotation = request.getQuotation();
                    offerHMap.get(request.getToken());

                    OfferMessage offer = offerHMap.get(request.getToken());
                    offer.getQuotations().add(quotation);
                    offerHMap.put(request.getToken(), offer);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
