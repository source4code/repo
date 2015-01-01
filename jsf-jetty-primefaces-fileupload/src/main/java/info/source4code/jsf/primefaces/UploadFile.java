package info.source4code.jsf.primefaces;

import java.io.Serializable;
import java.util.UUID;

public class UploadFile implements Serializable {

    private static final long serialVersionUID = -204227549089956478L;

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

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }

    public byte[] getContents() {
        return contents;
    }

    public long getSizeKB() {
        return size / 1024;
    }

    public String toString() {
        return "UploadedFile [" + "id=" + id + ", name=" + name
                + ", contentType=" + contentType + ", size=" + getSizeKB()
                + "KB]";
    }
}
