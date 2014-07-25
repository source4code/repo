package info.source4code.jms.priority;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Consumer.class);

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public void openConnection() throws JMSException {
        // Create a new connection factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void createConsumer(String queue) throws JMSException {
        // Create a session for receiving messages
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination from which messages will be received
        Destination destination = session.createQueue(queue);

        // Create a MessageConsumer for receiving messages
        consumer = session.createConsumer(destination);

        // Start the connection in order to receive messages
        connection.start();
    }

    public String receive(int timeout) throws JMSException {
        // Read a message from the destination
        Message message = consumer.receive(timeout);

        // Cast the message to the correct type
        TextMessage input = (TextMessage) message;

        // Retrieve the message content
        String text = input.getText();
        LOGGER.info("{} received", text);

        return text;
    }
}