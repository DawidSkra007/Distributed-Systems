import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {
    public static void main(String[] args) {
        String host = args.length == 0 ? "localhost":args[0];
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("failover://tcp://"+host+":61616");
        try {
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("sender");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Topic topic = session.createTopic("TESTTOPIC");
            // Queue queue = session.createQueue("TESTQUEUE");

            MessageConsumer consumer = session.createConsumer(topic);
            connection.start();
            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    System.out.println("Received: " + ((TextMessage) message).getText());
                    message.acknowledge();
                }
            }
            // connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}