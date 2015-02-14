package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import info.source4code.services.helloworld.Person;

import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.interceptor.security.JAASLoginInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
public class HelloWorldImplTest {

    private static final String ENDPOINT_ADDRESS = "http://localhost:9090/s4c/services/jaaslogininterceptor/helloworld";

    @Autowired
    private HelloWorldClient helloWorldClient;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // set the location of the jaas-login.conf
        Properties properties = System.getProperties();
        properties.setProperty("java.security.auth.login.config",
                "./src/test/resources/jaas-login.conf");

        // start the HelloWorld service using jaxWsServerFactoryBean
        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();

        // add logging interceptors to print the received/sent messages
        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
        loggingInInterceptor.setPrettyLogging(true);
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
        loggingOutInterceptor.setPrettyLogging(true);

        jaxWsServerFactoryBean.getInInterceptors().add(loggingInInterceptor);
        jaxWsServerFactoryBean.getOutInterceptors().add(loggingOutInterceptor);

        // add JAASLoginInterceptor to enable authentication
        JAASLoginInterceptor jaasLoginInterceptor = new JAASLoginInterceptor();
        jaasLoginInterceptor.setContextName("jaasContext");
        jaxWsServerFactoryBean.getInInterceptors().add(jaasLoginInterceptor);

        // set the implementation
        HelloWorldImpl implementor = new HelloWorldImpl();
        jaxWsServerFactoryBean.setServiceBean(implementor);

        // set the endpoint
        jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);
        jaxWsServerFactoryBean.create();
    }

    @AfterClass
    public static void setUpAfterClass() throws Exception {
        System.clearProperty("java.security.auth.login.config");
    }

    @Test
    public void testSayHello() {

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
