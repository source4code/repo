package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileUploadControllerTest {

    private static FileUploadController fileUploadController;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        fileUploadController = new FileUploadController();
        fileUploadController.init();
    }

    @Test
    public void testGetUploadedFiles() {
        assertTrue(fileUploadController.getUploadedFiles().isEmpty());
    }

    @Test
    public void testGetUploadId() {
        assertTrue(null != fileUploadController.getUploadId());
    }

    @Test
    public void testGetCurrentFileLimit() {
        assertEquals("3", fileUploadController.getCurrentFileLimit());
    }

}
