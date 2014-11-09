package info.source4code.jsf.primefaces.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class })
public class UserManagerTest {

    private UserManager userManager;

    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;

    @Before
    public void setUp() throws Exception {
        userManager = new UserManager();

        // mock all static methods of FacesContext
        PowerMockito.mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
    }

    @Test
    public void testLoginSucces() {
        userManager.setUserName("admin");
        userManager.setPassword("1234");

        assertEquals("/secured/home.xhtml?faces-redirect=true",
                userManager.login());
    }

    @Test
    public void testLoginFailUnknownUserName() {
        // unknown user name
        userManager.setUserName("user");
        userManager.setPassword("1234");

        assertNull(userManager.login());
    }

    @Test
    public void testLoginFailIncorrectPassword() {
        userManager.setUserName("admin");
        // incorrect password
        userManager.setPassword("5678");

        assertNull(userManager.login());
    }

    @Test
    public void testLogout() {
        userManager.setUserName("admin");
        userManager.setPassword("1234");
        userManager.login();

        assertEquals("/logout.xhtml?faces-redirect=true", userManager.logout());
    }

    @Test
    public void testIsLoggedInTrue() {
        userManager.setUserName("admin");
        userManager.setPassword("1234");
        userManager.login();

        assertEquals(true, userManager.isLoggedIn());
    }

    @Test
    public void testIsLoggedInFalse() {
        assertEquals(false, userManager.isLoggedIn());
    }

    @Test
    public void testGetUserName() {
        userManager.setUserName("admin");

        assertEquals("admin", userManager.getUserName());
    }

    @Test
    public void testGetPassword() {
        userManager.setPassword("p455w0rd");

        assertEquals("p455w0rd", userManager.getPassword());
    }

    @Test
    public void testGetUser() {
        userManager.setUserName("admin");
        userManager.setPassword("1234");
        userManager.login();

        assertNotNull(userManager.getUser());
        assertEquals("John", userManager.getUser().getFirstName());
        assertEquals("Doe", userManager.getUser().getLastName());
    }
}