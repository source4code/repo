package info.source4code.easymock;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class })
public class SomeBeanTest {

    private SomeBean someBean;

    private FacesContext facesContext;
    private ExternalContext externalContext;

    @Before
    public void setUp() throws Exception {
        someBean = new SomeBean();

        // mock all static methods of FacesContext using PowerMockito
        PowerMock.mockStatic(FacesContext.class);

        facesContext = createMock(FacesContext.class);
        externalContext = createMock(ExternalContext.class);
    }

    @Test
    public void testAddMessage() {
        // create Capture instances for the clientId and FacesMessage parameters
        // that will be added to the FacesContext
        Capture<String> clientIdCapture = new Capture<String>();
        Capture<FacesMessage> facesMessageCapture = new Capture<FacesMessage>();

        expect(FacesContext.getCurrentInstance()).andReturn(facesContext)
                .once();
        // expect the call to the addMessage() method and capture the arguments
        facesContext.addMessage(capture(clientIdCapture),
                capture(facesMessageCapture));
        expectLastCall().once();

        // replay the class (not the instance)
        PowerMock.replay(FacesContext.class);
        replay(facesContext);

        someBean.addMessage(FacesMessage.SEVERITY_ERROR, "error",
                "something went wrong");

        // verify the class (not the instance)
        PowerMock.verify(FacesContext.class);
        verify(facesContext);

        // check the value of the clientId that was passed
        assertNull(clientIdCapture.getValue());

        // retrieve the captured FacesMessage
        FacesMessage captured = facesMessageCapture.getValue();
        // check if the captured FacesMessage contains the expected values
        assertEquals(FacesMessage.SEVERITY_ERROR, captured.getSeverity());
        assertEquals("error", captured.getSummary());
        assertEquals("something went wrong", captured.getDetail());
    }

    @Test
    public void testLogout() {
        expect(FacesContext.getCurrentInstance()).andReturn(facesContext)
                .once();
        expect(facesContext.getExternalContext()).andReturn(externalContext)
                .once();
        // expect the call to the invalidateSession() method
        externalContext.invalidateSession();
        expectLastCall().once();

        // replay the class (not the instance)
        PowerMock.replay(FacesContext.class);
        replay(facesContext);
        replay(externalContext);

        assertEquals("logout?faces-redirect=true", someBean.logout());

        // verify the class (not the instance)
        PowerMock.verify(FacesContext.class);
        verify(facesContext);
        verify(externalContext);
    }
}
