package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import info.source4code.services.helloworld.Person;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/context-requester.xml" })
// subsequent tests will be supplied a new context
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class HelloWorldImplIT {

    @Autowired
    private HelloWorldClient helloWorldClient;

    @Test
    public void testSayHelloSecured() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        assertEquals("Hello John Doe!", helloWorldClient.sayHello(person));
    }

    @Test
    public void testSayHelloUnknownUser() {

        Person person = new Person();
        person.setFirstName("Jon");
        person.setLastName("Doe");

        // override the credentials specified on the jaxws:client
        helloWorldClient.setCredentials("jon.doe", "source4code");

        try {
            helloWorldClient.sayHello(person);
            fail("unknown user should fail");

        } catch (SOAPFaultException soapFaultException) {
            assertEquals(
                    "Authentication failed (details can be found in server log)",
                    soapFaultException.getFault().getFaultString());
        }
    }

    @Test
    public void testSayHelloIncorrectPassword() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        // override the credentials specified on the jaxws:client
        helloWorldClient.setCredentials("john.doe", "sourceFOURcode");

        try {
            helloWorldClient.sayHello(person);
            fail("incorrect password should fail");

        } catch (SOAPFaultException soapFaultException) {
            assertEquals(
                    "Authentication failed (details can be found in server log)",
                    soapFaultException.getFault().getFaultString());
        }
    }
}
