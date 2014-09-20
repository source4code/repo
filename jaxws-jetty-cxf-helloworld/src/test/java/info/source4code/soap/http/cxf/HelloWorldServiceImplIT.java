package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.wsdl.helloworld.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml" })
public class HelloWorldServiceImplIT {

    @Autowired
    private HelloWorldClientImpl client;

    @Test
    public void testSayHello() {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        assertEquals("Hello John Doe!", client.sayHello(person));
    }
}
