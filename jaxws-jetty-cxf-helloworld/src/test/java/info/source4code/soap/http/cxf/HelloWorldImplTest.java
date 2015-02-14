package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.services.helloworld.Greeting;
import info.source4code.services.helloworld.HelloWorldPortType;
import info.source4code.services.helloworld.Person;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
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
    private HelloWorldClient helloWorldClientBean;

    private static HelloWorldPortType helloWorldRequesterProxy;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        createServerEndpoint();

        helloWorldRequesterProxy = createClientProxy();
    }

    @Test
    public void testSayHello() {
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        assertEquals("Hello Jane Doe!", helloWorldClientBean.sayHello(person));
    }

    @Test
    public void testSayHelloProxy() {
        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Doe");

        Greeting greeting = helloWorldRequesterProxy.sayHello(person);
        assertEquals("Hello Jane Doe!", greeting.getText());
    }

    private static void createServerEndpoint() {
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

    private static HelloWorldPortType createClientProxy() {
        // create a CXF JaxWsProxyFactoryBean for creating JAX-WS proxies
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();

        // // set the HelloWorld portType class
        jaxWsProxyFactoryBean.setServiceClass(HelloWorldPortType.class);
        // set the address at which the HelloWorld endpoint will be called
        jaxWsProxyFactoryBean.setAddress(ENDPOINT_ADDRESS);

        // create a JAX-WS proxy for the HelloWorld portType
        return (HelloWorldPortType) jaxWsProxyFactoryBean.create();
    }
}
