package info.source4code.mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class })
public class SomeBeanTest {

    private SomeBean someBean;

    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;

    @Before
    public void setUp() throws Exception {
        someBean = new SomeBean();

        // mock all static methods of FacesContext using PowerMockito
        PowerMockito.mockStatic(FacesContext.class);

        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
    }

    @Test
    public void testAddMessage() {
        // create Captor instances for the clientId and FacesMessage parameters
        // that will be added to the FacesContext
        ArgumentCaptor<String> clientIdCaptor = ArgumentCaptor
                .forClass(String.class);
        ArgumentCaptor<FacesMessage> facesMessageCaptor = ArgumentCaptor
                .forClass(FacesMessage.class);

        // run the addMessage() method to be tested
        someBean.addMessage(FacesMessage.SEVERITY_ERROR, "error",
                "something went wrong");

        // verify if the call to addMessage() was made and capture the arguments
        verify(facesContext).addMessage(clientIdCaptor.capture(),
                facesMessageCaptor.capture());

        // check the value of the clientId that was passed
        assertNull(clientIdCaptor.getValue());

        // retrieve the captured FacesMessage
        FacesMessage captured = facesMessageCaptor.getValue();
        // check if the captured FacesMessage contains the expected values
        assertEquals(FacesMessage.SEVERITY_ERROR, captured.getSeverity());
        assertEquals("error", captured.getSummary());
        assertEquals("something went wrong", captured.getDetail());
    }

    @Test
    public void testLogout() {
        assertEquals("logout?faces-redirect=true", someBean.logout());
    }
}
