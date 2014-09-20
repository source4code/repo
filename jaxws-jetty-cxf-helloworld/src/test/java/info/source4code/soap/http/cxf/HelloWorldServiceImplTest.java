package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.wsdl.helloworld.HelloWorldPort;
import info.source4code.wsdl.helloworld.Person;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
public class HelloWorldServiceImplTest {

    private static String ENDPOINT_ADDRESS = "http://localhost:9090/s4c/services/helloworld";

    @Autowired
    private HelloWorldClientImpl client;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // start the HelloWorld service using jaxWsServerFactoryBean
        HelloWorldServiceImpl implementor = new HelloWorldServiceImpl();
        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();
        jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);
        jaxWsServerFactoryBean.setServiceBean(implementor);
        jaxWsServerFactoryBean.create();
    }

    @Test
    public void testSayHello() {

        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        assertEquals("Hello Jane Doe!", client.sayHello(person));
    }
}
