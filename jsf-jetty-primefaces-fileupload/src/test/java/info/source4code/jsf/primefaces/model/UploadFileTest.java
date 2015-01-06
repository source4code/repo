package info.source4code.jsf.primefaces.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class UploadFileTest {

    private UploadFile uploadedFile;

    @Before
    public void setUp() {
        uploadedFile = new UploadFile("test.png", "image/jpeg", 1024,
                "no_image".getBytes());
    }

    @Test
    public void testGetName() {
        uploadedFile.setName("flag.png");
        assertEquals("flag.png", uploadedFile.getName());
    }

    @Test
    public void testGetContentType() {
        uploadedFile.setContentType("image/png");
        assertEquals("image/png", uploadedFile.getContentType());
    }

    @Test
    public void testGetSize() {
        uploadedFile.setSize(2048);
        assertEquals(2048, uploadedFile.getSize());
    }

    @Test
    public void testGetContents() {
        uploadedFile.setContents("image".getBytes());
        assertTrue(Arrays.equals("image".getBytes(),
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
                + ", name=test.png, contentType=image/jpeg, size=1KB]",
                uploadedFile.toString());
    }
}
