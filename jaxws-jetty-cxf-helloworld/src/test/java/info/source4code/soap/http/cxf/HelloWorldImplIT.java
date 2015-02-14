package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.services.helloworld.Greeting;
import info.source4code.services.helloworld.HelloWorldPortType;
import info.source4code.services.helloworld.Person;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/context-requester.xml" })
public class HelloWorldImplIT {

    private static String ENDPOINT_ADDRESS = "https://localhost:9443/s4c/services/helloworld";

    private static HelloWorldPortType helloWorldRequesterProxy;

    @Autowired
    private HelloWorldClient clientBean;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // create a CXF JaxWsProxyFactoryBean for creating JAX-WS proxies
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();

        // // set the HelloWorld portType class
        jaxWsProxyFactoryBean.setServiceClass(HelloWorldPortType.class);
        // set the address at which the HelloWorld endpoint will be called
        jaxWsProxyFactoryBean.setAddress(ENDPOINT_ADDRESS);

        // create a JAX-WS proxy for the HelloWorld portType
        helloWorldRequesterProxy = (HelloWorldPortType) jaxWsProxyFactoryBean
                .create();
    }

    @Test
    public void testSayHello() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        assertEquals("Hello John Doe!", clientBean.sayHello(person));
    }

    @Test
    public void testSayHelloProxy() {
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        Greeting greeting = helloWorldRequesterProxy.sayHello(person);
        assertEquals("Hello Jane Doe!", greeting.getText());
    }
}
