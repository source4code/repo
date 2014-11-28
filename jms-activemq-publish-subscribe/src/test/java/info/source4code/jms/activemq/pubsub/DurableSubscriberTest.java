package info.source4code.jms.activemq.pubsub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jms.JMSException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DurableSubscriberTest {

    private static Publisher publisherPublishSubscribe,
            publisherDurableSubscriber;
    private static DurableSubscriber subscriberPublishSubscribe,

    subscriber1DurableSubscriber, subscriber2DurableSubscriber;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        publisherPublishSubscribe = new Publisher();
        publisherPublishSubscribe.create("publisher-publishsubscribe",
                "publishsubscribe.t");

        publisherDurableSubscriber = new Publisher();
        publisherDurableSubscriber.create("publisher-durablesubscriber",
                "durablesubscriber.t");

        subscriberPublishSubscribe = new DurableSubscriber();
        subscriberPublishSubscribe.create("subscriber-publishsubscribe",
                "publishsubscribe.t", "publishsubscribe");

        subscriber1DurableSubscriber = new DurableSubscriber();
        subscriber1DurableSubscriber.create("subscriber1-durablesubscriber",
                "durablesubscriber.t", "durablesubscriber1");

        subscriber2DurableSubscriber = new DurableSubscriber();
        subscriber2DurableSubscriber.create("subscriber2-durablesubscriber",
                "durablesubscriber.t", "durablesubscriber2");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        publisherPublishSubscribe.closeConnection();
        publisherDurableSubscriber.closeConnection();

        // remove the durable subscriptions
        subscriberPublishSubscribe.removeDurableSubscriber();
        subscriber1DurableSubscriber.removeDurableSubscriber();
        subscriber2DurableSubscriber.removeDurableSubscriber();

        subscriberPublishSubscribe.closeConnection();
        subscriber1DurableSubscriber.closeConnection();
        subscriber2DurableSubscriber.closeConnection();
    }

    @Test
    public void testSend() {
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
    public void testNonDurableSubscriber() {
        try {
            // durable subscriptions, receive messages sent while the
            // subscribers are not active
            subscriber2DurableSubscriber.closeConnection();

            publisherDurableSubscriber.sendName("Bilbo", "Baggins");

            // recreate a connection for the durable subscription
            subscriber2DurableSubscriber.create(
                    "subscriber2-durablesubscriber", "durablesubscriber.t",
                    "durablesubscriber2");

            publisherDurableSubscriber.sendName("Frodo", "Baggins");

            String greeting1 = subscriber1DurableSubscriber.getGreeting(1000);
            assertEquals("Hello Bilbo Baggins!", greeting1);
            String greeting2 = subscriber2DurableSubscriber.getGreeting(1000);
            assertEquals("Hello Bilbo Baggins!", greeting2);

            String greeting3 = subscriber1DurableSubscriber.getGreeting(1000);
            assertEquals("Hello Frodo Baggins!", greeting3);
            String greeting4 = subscriber2DurableSubscriber.getGreeting(1000);
            assertEquals("Hello Frodo Baggins!", greeting4);

        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }
}
