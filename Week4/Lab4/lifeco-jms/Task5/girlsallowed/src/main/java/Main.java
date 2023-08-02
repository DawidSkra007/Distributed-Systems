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
import service.message.QuotationMessage;
import service.girlsallowed.GAQService;

public class Main {
    private static GAQService service = new GAQService();

    public static void main(String[] args) {
        String url;
        if (args.length <= 0) {
            url = "failover://tcp://localhost:61616";
        } else {
            url = args[1];
        }
        System.out.println("Using URL: " + url); //DELTE
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        try {
            Connection connection = factory.createConnection();
            connection.setClientID("girlsallowed");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("QUOTATIONS");
            Topic topic = session.createTopic("APPLICATIONS");

            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue);
            connection.start();

            consumer.setMessageListener(new MessageListener() { 
                @Override
                public void onMessage(Message message) {
                try {
                    ClientMessage request = (ClientMessage) ((ObjectMessage) message).getObject();
                    Quotation quotation = service.generateQuotation(request.getInfo()); 
                    Message response = session.createObjectMessage(new QuotationMessage(request.getToken(), quotation));
                    producer.send(response);
                    message.acknowledge();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                }
            });  
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
