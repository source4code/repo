package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HelloWorldTest {

    private HelloWorld helloWorld;

    @Before
    public void setUp() throws Exception {
        helloWorld = new HelloWorld();
        helloWorld.setFirstName("Jane");
        helloWorld.setLastName("Doe");
    }

    @Test
    public void testGetFirstName() {

        assertEquals("Jane", helloWorld.getFirstName());
    }

    @Test
    public void testGetLastName() {

        assertEquals("Doe", helloWorld.getLastName());
    }

    @Test
    public void testShowGreeting() {

        assertEquals("Hello Jane Doe!", helloWorld.showGreeting());
    }
}
