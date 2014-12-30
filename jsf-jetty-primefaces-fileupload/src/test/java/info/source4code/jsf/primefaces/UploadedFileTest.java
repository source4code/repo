package info.source4code.jsf.primefaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class UploadedFileTest {

    UploadedFile uploadedFile;

    @Before
    public void setUp() throws Exception {
        uploadedFile = new UploadedFile("test.png", "image/png", 1024,
                "no_image".getBytes());
    }

    @Test
    public void testGetName() {
        assertEquals("test.png", uploadedFile.getName());
    }

    @Test
    public void testGetContentType() {
        assertEquals("image/png", uploadedFile.getContentType());
    }

    @Test
    public void testGetSize() {
        assertEquals(1024, uploadedFile.getSize());
    }

    @Test
    public void testGetContents() {
        assertTrue(Arrays.equals("no_image".getBytes(),
                uploadedFile.getContents()));
    }

    @Test
    public void testGetSizeKB() {
        assertEquals(1, uploadedFile.getSizeKB());
    }

    @Test
    public void testToString() {
        String id = uploadedFile.getId();
        assertEquals("UploadedFile [id=" + id
                + ", name=test.png, contentType=image/png, size=1KB]",
                uploadedFile.toString());
    }
}
