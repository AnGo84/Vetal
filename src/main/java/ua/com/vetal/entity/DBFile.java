package ua.com.vetal.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "files")
public class DBFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name = "uuid", strategy = "uuid2")
    private Long id;

    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    private String fileType;

    @Lob
    private byte[] data;

    public DBFile() {

    }

    public DBFile(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DBFile{");
        sb.append("id='").append(id).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", fileType='").append(fileType).append('\'');
        sb.append(", data=").append(Arrays.toString(data));
        sb.append('}');
        return sb.toString();
    }

    public String toShortString() {
        final StringBuilder sb = new StringBuilder("DBFile{");
        sb.append("id='").append(id).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", fileType='").append(fileType).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getFullFileName() {
        final StringBuilder sb = new StringBuilder(fileName);
        sb.append("(").append(id).append(fileType).append(")");
        return sb.toString();
    }
}
