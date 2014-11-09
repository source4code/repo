package info.source4code.jsf.primefaces.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {

    private User user;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        user = new User("", "");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetFirstName() {
        user.setFirstName("Jane");

        assertEquals("Jane", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        user.setLastName("Doe");

        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testGetName() {
        user.setFirstName("Jane");
        user.setLastName("Doe");

        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testToString() {
        user.setFirstName("Jane");
        user.setLastName("Doe");

        assertEquals("user[firstName=Jane, lastName=Doe]", user.toString());
    }

}
