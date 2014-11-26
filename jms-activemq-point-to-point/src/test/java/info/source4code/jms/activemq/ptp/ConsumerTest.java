package info.source4code.jms.activemq.ptp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConsumerTest {

    private static Producer producerPointToPoint, producerOnlyOneConsumer,
            producerNoTimingDependencies, producerAcknowledgeProcessing;
    private static Consumer consumerPointToPoint, consumer1OnlyOneConsumer,
            consumer2OnlyOneConsumer, consumerNoTimingDependencies,
            consumer1AcknowledgeProcessing, consumer2AcknowledgeProcessing;

    @BeforeClass
    public static void setUpBeforeClass() throws JMSException, NamingException {
        producerPointToPoint = new Producer();
        producerPointToPoint.create("producer-pointtopoint", "pointtopoint.q");

        producerOnlyOneConsumer = new Producer();
        producerOnlyOneConsumer.create("producer-onlyoneconsumer",
                "onlyoneconsumer.q");

        producerNoTimingDependencies = new Producer();
        producerNoTimingDependencies.create("producer-notimingdependencies",
                "notimingdependencies.q");

        producerAcknowledgeProcessing = new Producer();
        producerAcknowledgeProcessing.create("producer-acknowledgeprocessing",
                "acknowledgeprocessing.q");

        consumerPointToPoint = new Consumer();
        consumerPointToPoint.create("consumer-pointtopoint", "pointtopoint.q");

        consumer1OnlyOneConsumer = new Consumer();
        consumer1OnlyOneConsumer.create("consumer1-onlyoneconsumer",
                "onlyoneconsumer.q");

        consumer2OnlyOneConsumer = new Consumer();
        consumer2OnlyOneConsumer.create("consumer2-onlyoneconsumer",
                "onlyoneconsumer.q");

        // consumerNoTimingDependencies

        consumer1AcknowledgeProcessing = new Consumer();
        consumer1AcknowledgeProcessing.create(
                "consumer1-acknowledgeprocessing", "acknowledgeprocessing.q");

        consumer2AcknowledgeProcessing = new Consumer();
        consumer2AcknowledgeProcessing.create(
                "consumer2-acknowledgeprocessing", "acknowledgeprocessing.q");
    }

    @AfterClass
    public static void tearDownAfterClass() throws JMSException {
        producerPointToPoint.closeConnection();
        producerOnlyOneConsumer.closeConnection();
        producerNoTimingDependencies.closeConnection();
        producerAcknowledgeProcessing.closeConnection();

        consumerPointToPoint.closeConnection();
        consumer1OnlyOneConsumer.closeConnection();
        consumer2OnlyOneConsumer.closeConnection();
        consumerNoTimingDependencies.closeConnection();
        // consumer1AcknowledgeProcessing
        consumer2AcknowledgeProcessing.closeConnection();
    }

    @Test
    public void testGetGreeting() {
        try {
            producerPointToPoint.sendName("Frodo", "Baggins");

            String greeting = consumerPointToPoint.getGreeting(1000, true);
            assertEquals("Hello Frodo Baggins!", greeting);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testOnlyOneConsumer() throws InterruptedException {
        try {
            producerOnlyOneConsumer.sendName("Legolas", "Greenleaf");

            String greeting1 = consumer1OnlyOneConsumer.getGreeting(1000, true);
            assertEquals("Hello Legolas Greenleaf!", greeting1);

            Thread.sleep(1000);

            String greeting2 = consumer2OnlyOneConsumer.getGreeting(1000, true);
            // each message has only one consumer
            assertEquals("no greeting", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testNoTimingDependencies() {
        try {
            producerNoTimingDependencies.sendName("Samwise", "Gamgee");
            // a receiver can fetch the message whether or not it was running
            // when the client sent the message
            consumerNoTimingDependencies = new Consumer();
            consumerNoTimingDependencies.create(
                    "consumer-notimingdependencies", "notimingdependencies.q");

            String greeting = consumerNoTimingDependencies.getGreeting(1000,
                    true);
            assertEquals("Hello Samwise Gamgee!", greeting);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testAcknowledgeProcessing() throws InterruptedException {
        try {
            producerAcknowledgeProcessing.sendName("Gandalf", "the Grey");

            // consume the message without an acknowledgment
            String greeting1 = consumer1AcknowledgeProcessing.getGreeting(1000,
                    false);
            assertEquals("Hello Gandalf the Grey!", greeting1);

            // close the MessageConsumer so the broker knows there is no
            // acknowledgment
            consumer1AcknowledgeProcessing.closeConnection();

            String greeting2 = consumer2AcknowledgeProcessing.getGreeting(1000,
                    true);
            assertEquals("Hello Gandalf the Grey!", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }
}
