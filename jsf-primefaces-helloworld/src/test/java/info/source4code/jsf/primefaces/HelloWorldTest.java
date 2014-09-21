package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;
import info.source4code.jsf.primefaces.HelloWorld;

import org.junit.Test;

public class HelloWorldTest {

    @Test
    public void testGetFirstName() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setFirstName("Jane");

        assertEquals(helloWorld.getFirstName(), "Jane");
    }

    @Test
    public void testGetLastName() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setLastName("Doe");

        assertEquals(helloWorld.getLastName(), "Doe");
    }

    @Test
    public void testShowGreeting() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setFirstName("Jane");
        helloWorld.setLastName("Doe");

        assertEquals(helloWorld.showGreeting(), "Hello Jane Doe!");
    }
}
