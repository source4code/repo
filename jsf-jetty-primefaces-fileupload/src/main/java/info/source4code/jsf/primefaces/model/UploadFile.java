package info.source4code.jsf.primefaces.model;

import java.io.Serializable;
import java.util.UUID;

public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String contentType;
    private long size;
    private byte[] contents;

    public UploadFile(String name, String contentType, long size,
            byte[] contents) {

        id = UUID.randomUUID().toString();

        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;

    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public long getSizeKB() {
        return size / 1024;
    }

    public String toString() {
        return "UploadedFile [" + "id=" + id + ", name=" + name
                + ", contentType=" + contentType + ", size="
                + getSizeKB() + "KB]";
    }
}
