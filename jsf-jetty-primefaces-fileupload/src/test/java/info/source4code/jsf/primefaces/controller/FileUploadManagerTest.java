package info.source4code.jsf.primefaces.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.NativeUploadedFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ FacesContext.class })
public class FileUploadManagerTest {

    private FileUploadManager fileUploadManager;

    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;
    @Mock
    private UIComponent uiComponent;
    @Mock
    private NativeUploadedFile nativeUploadedFile;

    @Before
    public void setUp() throws Exception {
        fileUploadManager = new FileUploadManager();
        fileUploadManager.init();

        InputStream inputStream = new ByteArrayInputStream(
                "image1".getBytes());

        // nativeUploadedFile
        when(nativeUploadedFile.getContentType()).thenReturn(
                "image/png");
        when(nativeUploadedFile.getInputstream()).thenReturn(
                inputStream);

        PowerMockito.mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance())
                .thenReturn(facesContext);
    }

    @Test
    public void testInit() {
        assertNotNull(fileUploadManager.getUploadId());
        assertTrue(fileUploadManager.getUploadedFiles().isEmpty());
        assertEquals(Integer.toString(FileUploadManager.FILE_LIMIT),
                fileUploadManager.getCurrentFileLimit());
    }

    @Test
    public void testHandleFileUploadOne() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        assertEquals(1, fileUploadManager.getUploadedFiles().size());
        assertEquals("2", fileUploadManager.getCurrentFileLimit());
    }

    @Test
    public void testHandleFileUploadThree() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        for (int i = 0; i < 3; i++) {
            fileUploadManager.handleFileUpload(event);
        }

        assertEquals(3, fileUploadManager.getUploadedFiles().size());
        assertEquals("-1", fileUploadManager.getCurrentFileLimit());
    }

    @Test
    public void testHandleFileUploadError() {
        try {
            when(nativeUploadedFile.getInputstream()).thenThrow(
                    new IOException());

            FileUploadEvent event = new FileUploadEvent(uiComponent,
                    nativeUploadedFile);
            fileUploadManager.handleFileUpload(event);

            assertEquals(0, fileUploadManager.getUploadedFiles().size());
            assertEquals("3", fileUploadManager.getCurrentFileLimit());
        } catch (IOException ioException) {
            fail("no exception should be thrown");
        }
    }

    @Test
    public void testHandleFileRemoval() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        fileUploadManager.handleFileRemoval(fileUploadManager
                .getUploadedFiles().get(0).getId());
        verify(facesContext, times(2)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testHandleFileRemovalNotFound() {
        fileUploadManager.handleFileRemoval("123");

        verify(facesContext, times(1)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testHandleFileSubmit() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        assertEquals("submitted?faces-redirect=true&uploadId="
                + fileUploadManager.getUploadId(),
                fileUploadManager.handleFileSubmit());
    }

    @Test
    public void testHandleFileSubmitNone() {
        assertEquals("", fileUploadManager.handleFileSubmit());
        verify(facesContext).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testGetCurrentFileLimitInit() {
        assertEquals("3", fileUploadManager.getCurrentFileLimit());
    }

    @Test
    public void testGetImageFileRenderResponse() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        when(facesContext.getCurrentPhaseId()).thenReturn(
                PhaseId.RENDER_RESPONSE);

        assertNotNull(fileUploadManager.getImage());
    }

    @Test
    public void testGetImageFile() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        Map<String, String> requestParameterMap = new HashMap<String, String>();
        requestParameterMap.put("uploadedFileId", fileUploadManager
                .getUploadedFiles().get(0).getId());

        when(facesContext.getCurrentPhaseId()).thenReturn(null);
        when(facesContext.getExternalContext()).thenReturn(
                externalContext);
        when(externalContext.getRequestParameterMap()).thenReturn(
                requestParameterMap);

        assertEquals("image/png", fileUploadManager.getImage()
                .getContentType());
    }

    @Test
    public void testGetImageFileError() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadManager.handleFileUpload(event);

        Map<String, String> requestParameterMap = new HashMap<String, String>();
        requestParameterMap.put("uploadedFileId", "123");

        when(facesContext.getCurrentPhaseId()).thenReturn(null);
        when(facesContext.getExternalContext()).thenReturn(
                externalContext);
        when(externalContext.getRequestParameterMap()).thenReturn(
                requestParameterMap);

        assertNotNull(fileUploadManager.getImage());
    }
}
