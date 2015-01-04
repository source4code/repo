package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
public class FileUploadControllerTest {

    private FileUploadController fileUploadController;

    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;
    @Mock
    private UIComponent uiComponent;
    @Mock
    private NativeUploadedFile nativeUploadedFile,
            nativeUploadedFileIOException;
    @Mock
    private UploadFile uploadFileIOException;
    @Mock
    FileUploadEvent event;

    @Before
    public void setUp() throws Exception {
        fileUploadController = new FileUploadController();
        fileUploadController.init();

        InputStream inputStream = new ByteArrayInputStream("image1".getBytes());
        // nativeUploadedFile
        when(nativeUploadedFile.getContentType()).thenReturn("image/png");
        when(nativeUploadedFile.getInputstream()).thenReturn(inputStream);
        // nativeUploadedFileIOException
        when(nativeUploadedFileIOException.getInputstream()).thenThrow(
                new IOException());

        // uploadFileIOException
        when(uploadFileIOException.getContents()).thenThrow(
                new RuntimeException());

        PowerMockito.mockStatic(FacesContext.class);
        when(FacesContext.getCurrentInstance()).thenReturn(facesContext);
    }

    @Test
    public void testGetUploadedFilesInit() {
        assertTrue(fileUploadController.getUploadedFiles().isEmpty());
    }

    @Test
    public void testGetUploadIdInit() {
        assertTrue(null != fileUploadController.getUploadId());
    }

    @Test
    public void testHandleFileUploadOne() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);

        assertEquals(1, fileUploadController.getUploadedFiles().size());
        assertEquals("2", fileUploadController.getCurrentFileLimit());
    }

    @Test
    public void testHandleFileUploadThree() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        for (int i = 0; i < 3; i++) {
            fileUploadController.handleFileUpload(event);
        }

        assertEquals(3, fileUploadController.getUploadedFiles().size());
        assertEquals("-1", fileUploadController.getCurrentFileLimit());
    }

    @Test
    public void testHandleFileUploadError() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFileIOException);
        fileUploadController.handleFileUpload(event);

        assertEquals("3", fileUploadController.getCurrentFileLimit());
    }

    @Test
    public void testRemoveUploadedFileNone() {
        fileUploadController.removeUploadedFile("123");

        verify(facesContext, times(1)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testRemoveUploadedFile() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);
        fileUploadController.removeUploadedFile(fileUploadController
                .getUploadedFiles().get(0).getId());

        verify(facesContext, times(2)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testRemoveUploadedFileNotFound() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);
        fileUploadController.removeUploadedFile("123");

        verify(facesContext, times(2)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testSubmitUploadedFilesNone() {
        assertEquals("", fileUploadController.submitUploadedFiles());
        verify(facesContext).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testSubmitUploadedFiles() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);

        assertEquals("submitted?faces-redirect=true&uploadId="
                + fileUploadController.getUploadId(),
                fileUploadController.submitUploadedFiles());
    }

    @Test
    public void testSubmitUploadedFilesError() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);
        fileUploadController.getUploadedFiles().remove(0);
        fileUploadController.getUploadedFiles().add(0, uploadFileIOException);

        assertEquals("", fileUploadController.submitUploadedFiles());
        verify(facesContext, times(2)).addMessage(any(String.class),
                any(FacesMessage.class));
    }

    @Test
    public void testGetCurrentFileLimitInit() {
        assertEquals("3", fileUploadController.getCurrentFileLimit());
    }

    @Test
    public void testGetImageFileRenderResponse() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);

        when(facesContext.getCurrentPhaseId()).thenReturn(
                PhaseId.RENDER_RESPONSE);

        assertNotNull(fileUploadController.getImage());
    }

    @Test
    public void testGetImageFile() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);

        Map<String, String> requestParameterMap = new HashMap<String, String>();
        requestParameterMap.put("uploadedFileId", fileUploadController
                .getUploadedFiles().get(0).getId());

        when(facesContext.getCurrentPhaseId()).thenReturn(null);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getRequestParameterMap()).thenReturn(
                requestParameterMap);

        assertEquals("image/png", fileUploadController.getImage()
                .getContentType());
    }

    @Test
    public void testGetImageFileError() {
        FileUploadEvent event = new FileUploadEvent(uiComponent,
                nativeUploadedFile);
        fileUploadController.handleFileUpload(event);

        when(facesContext.getCurrentPhaseId()).thenReturn(null);
        when(facesContext.getExternalContext()).thenReturn(externalContext);
        when(externalContext.getRequestParameterMap()).thenThrow(
                new RuntimeException());

        assertNotNull(fileUploadController.getImage());
    }
}
