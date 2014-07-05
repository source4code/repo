package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;
import info.source4code.jsf.primefaces.HelloWorld;

import org.junit.Test;

public class HelloWorldTest {

    @Test
    public void testShowGreeting() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setFirstName("John");
        helloWorld.setLastName("Doe");

        assertEquals(helloWorld.showGreeting(), "Hello John Doe!");
    }
}
