import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {
    public static void main(String[] args) {
        String host = args.length == 0 ? "localhost":args[0];
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("producer");
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Queue queue = session.createQueue("TESTQUEUE");
            Topic topic = session.createTopic("TESTTOPIC");
            MessageProducer messageProducer = session.createProducer(topic);

            int i = 0;
            while (true) {
                TextMessage textMessage = session.createTextMessage("Hello World, " + i++);
                messageProducer.send(textMessage);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}