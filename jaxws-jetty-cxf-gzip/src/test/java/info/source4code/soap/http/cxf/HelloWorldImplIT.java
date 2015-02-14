package info.source4code.soap.http.cxf;

import static org.junit.Assert.assertEquals;
import info.source4code.services.helloworld.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/context-requester.xml" })
public class HelloWorldImplIT {

    @Autowired
    private HelloWorldClient helloWorldClient;

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
