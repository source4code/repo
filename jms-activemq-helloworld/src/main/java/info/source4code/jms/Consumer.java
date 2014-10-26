package info.source4code.jms;

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

    private static String NO_GREETING = "no greeting";

    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;

    public void create(String destinationName) throws JMSException {

        // create a Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_BROKER_URL);

        // create a Connection
        connection = connectionFactory.createConnection();

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the Destination from which messages will be received
        Destination destination = session.createQueue(destinationName);

        // create a Message Consumer for receiving messages
        messageConsumer = session.createConsumer(destination);

        // start the connection in order to receive messages
        connection.start();
    }

    public void close() throws JMSException {
        connection.close();
    }

    public String getGreeting(int timeout) throws JMSException {

        String greeting = NO_GREETING;

        // read a message from the queue destination
        Message message = messageConsumer.receive(timeout);

        // check if a message was received
        if (message != null) {
            // cast the message to the correct type
            TextMessage textMessage = (TextMessage) message;

            // retrieve the message content
            String text = textMessage.getText();
            LOGGER.debug("consumer received message with text='{}'", text);

            // create greeting
            greeting = "Hello " + text + "!";
        } else {
            LOGGER.debug("consumer received no message");
        }

        LOGGER.info("greeting={}", greeting);
        return greeting;
    }
}