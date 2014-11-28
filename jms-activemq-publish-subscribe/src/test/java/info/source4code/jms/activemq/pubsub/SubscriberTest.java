package info.source4code.jms.activemq.pubsub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubscriberTest {

    private static Publisher publisherPublishSubscribe,
            publisherMultipleConsumers, publisherNonDurableSubscriber;
    private static Subscriber subscriberPublishSubscribe,
            subscriber1MultipleConsumers, subscriber2MultipleConsumers,
            subscriber1NonDurableSubscriber, subscriber2NonDurableSubscriber;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        publisherPublishSubscribe = new Publisher();
        publisherPublishSubscribe.create("publisher-publishsubscribe",
                "publishsubscribe.t");

        publisherMultipleConsumers = new Publisher();
        publisherMultipleConsumers.create("publisher-multipleconsumers",
                "multipleconsumers.t");

        publisherNonDurableSubscriber = new Publisher();
        publisherNonDurableSubscriber.create("publisher-nondurablesubscriber",
                "nondurablesubscriber.t");

        subscriberPublishSubscribe = new Subscriber();
        subscriberPublishSubscribe.create("subscriber-publishsubscribe",
                "publishsubscribe.t");

        subscriber1MultipleConsumers = new Subscriber();
        subscriber1MultipleConsumers.create("subscriber1-multipleconsumers",
                "multipleconsumers.t");

        subscriber2MultipleConsumers = new Subscriber();
        subscriber2MultipleConsumers.create("subscriber2-multipleconsumers",
                "multipleconsumers.t");

        subscriber1NonDurableSubscriber = new Subscriber();
        subscriber1NonDurableSubscriber.create(
                "subscriber1-nondurablesubscriber", "nondurablesubscriber.t");

        subscriber2NonDurableSubscriber = new Subscriber();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        publisherPublishSubscribe.closeConnection();
        publisherMultipleConsumers.closeConnection();
        publisherNonDurableSubscriber.closeConnection();

        subscriberPublishSubscribe.closeConnection();
        subscriber1MultipleConsumers.closeConnection();
        subscriber2MultipleConsumers.closeConnection();
        subscriber1NonDurableSubscriber.closeConnection();
        subscriber2NonDurableSubscriber.closeConnection();
    }

    @Test
    public void testGetGreeting() {
        try {
            publisherPublishSubscribe.sendName("Peregrin", "Took");

            String greeting1 = subscriberPublishSubscribe.getGreeting(1000);
            assertEquals("Hello Peregrin Took!", greeting1);

            String greeting2 = subscriberPublishSubscribe.getGreeting(1000);
            assertEquals("no greeting", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testMultipleConsumers() {
        try {
            publisherMultipleConsumers.sendName("Gandalf", "the Grey");

            String greeting1 = subscriber1MultipleConsumers.getGreeting(1000);
            assertEquals("Hello Gandalf the Grey!", greeting1);

            String greeting2 = subscriber2MultipleConsumers.getGreeting(1000);
            assertEquals("Hello Gandalf the Grey!", greeting2);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testNonDurableSubscriber() {
        try {
            publisherNonDurableSubscriber.sendName("Bilbo", "Baggins");

            // subscriber must continue to be active in order for it to consume
            // messages
            subscriber2NonDurableSubscriber.create(
                    "subscriber2-nondurablesubscriber",
                    "nondurablesubscriber.t");

            publisherNonDurableSubscriber.sendName("Frodo", "Baggins");

            String greeting1 = subscriber1NonDurableSubscriber
                    .getGreeting(1000);
            assertEquals("Hello Bilbo Baggins!", greeting1);
            String greeting2 = subscriber1NonDurableSubscriber
                    .getGreeting(1000);
            assertEquals("Hello Frodo Baggins!", greeting2);

            String greeting3 = subscriber2NonDurableSubscriber
                    .getGreeting(1000);
            assertEquals("Hello Frodo Baggins!", greeting3);
            String greeting4 = subscriber2NonDurableSubscriber
                    .getGreeting(1000);
            assertEquals("no greeting", greeting4);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }
}
