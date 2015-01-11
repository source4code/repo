package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.services.helloworld.Person;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/context-requester.xml" })
public class HelloWorldImplTest {

    private static final String ENDPOINT_ADDRESS = "http://localhost:9090/s4c/services/gzip/helloworld";

    @Autowired
    private HelloWorldClient helloWorldClient;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // start the HelloWorld service using jaxWsServerFactoryBean
        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();

        // add logging interceptors to print the received/sent messages
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
        loggingOutInterceptor.setPrettyLogging(true);
        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
        loggingInInterceptor.setPrettyLogging(true);

        jaxWsServerFactoryBean.getOutInterceptors().add(
                loggingOutInterceptor);
        jaxWsServerFactoryBean.getInInterceptors().add(
                loggingInInterceptor);

        // add JAASLoginInterceptor to enable authentication
        GZIPOutInterceptor gZIPOutInterceptor = new GZIPOutInterceptor();
        GZIPInInterceptor gZIPInInterceptor = new GZIPInInterceptor();

        jaxWsServerFactoryBean.getOutInterceptors().add(
                gZIPOutInterceptor);
        jaxWsServerFactoryBean.getInInterceptors().add(
                gZIPInInterceptor);

        // set the implementation
        HelloWorldImpl implementor = new HelloWorldImpl();
        jaxWsServerFactoryBean.setServiceBean(implementor);

        // set the endpoint
        jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);
        jaxWsServerFactoryBean.create();
    }

    @Test
    public void testSayHello() {
        Person person = new Person();
        person.setLastName("Doe");

        // send medium message ( 512 < message < 1024 )
        String firstNameMedium = TestHelper.generateLongName("John",
                512);
        person.setFirstName(firstNameMedium);
        assertEquals("Hello " + firstNameMedium + " Doe!",
                helloWorldClient.sayHello(person));

        // send small message ( message < 512)
        person.setFirstName("John");
        assertEquals("Hello John Doe!",
                helloWorldClient.sayHello(person));

        // send large message ( 1024 < message )
        String firstNameLarge = TestHelper.generateLongName("John",
                1024);
        person.setFirstName(firstNameLarge);
        assertEquals("Hello " + firstNameLarge + " Doe!",
                helloWorldClient.sayHello(person));
    }
}
