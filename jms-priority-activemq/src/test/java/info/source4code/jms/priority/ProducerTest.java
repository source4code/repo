package info.source4code.jms.priority;

import static org.junit.Assert.assertEquals;

import javax.jms.JMSException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProducerTest {

    private static Consumer consumer;
    private static Producer producer;

    private static String QUEUE = "priority.q";

    @BeforeClass
    public static void setUpBeforeClass() throws JMSException {
        producer = new Producer();
        producer.openConnection();
        producer.createProducer(QUEUE);

        consumer = new Consumer();
        consumer.openConnection();
        consumer.createConsumer(QUEUE);
    }

    @AfterClass
    public static void tearDownAfterClass() throws JMSException {
        producer.closeConnection();
        consumer.closeConnection();
    }

    @Test
    public void testSend() throws JMSException, InterruptedException {
        producer.send("message1");
        producer.send("message2");
        producer.send("message3");

        Thread.sleep(1000);

        // Messages should be received FIFO as priority is the same for all
        assertEquals("message1", consumer.receive(5000));
        assertEquals("message2", consumer.receive(5000));
        assertEquals("message3", consumer.receive(5000));
    }

    @Test
    public void testSendWithPriority() throws JMSException,
            InterruptedException {
        producer.send("message1", 1);
        producer.send("message2", 2);
        producer.send("message3", 3);

        Thread.sleep(1000);

        // Messages should be received LIFO as priority=1 is lower than
        // priority=3
        assertEquals("message3", consumer.receive(5000));
        assertEquals("message2", consumer.receive(5000));
        assertEquals("message1", consumer.receive(5000));
    }
}
