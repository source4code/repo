package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.services.helloworld.Person;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/context-requester.xml" })
public class HelloWorldImplTest {

    private static String ENDPOINT_ADDRESS = "http://localhost:9090/s4c/services/helloworld";

    @Autowired
    private HelloWorldClient clientBean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // use a CXF JaxWsServerFactoryBean to create JAX-WS endpoints
        JaxWsServerFactoryBean jaxWsServerFactoryBean = new JaxWsServerFactoryBean();

        // set the HelloWorld implementation
        HelloWorldImpl implementor = new HelloWorldImpl();
        jaxWsServerFactoryBean.setServiceBean(implementor);

        // set the address at which the HelloWorld endpoint will be exposed
        jaxWsServerFactoryBean.setAddress(ENDPOINT_ADDRESS);
        
        // create the server
        jaxWsServerFactoryBean.create();
    }

    @Test
    public void testSayHello() {

        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        assertEquals("Hello Jane Doe!", clientBean.sayHello(person));
    }
}
