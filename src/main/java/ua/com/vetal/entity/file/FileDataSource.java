package ua.com.vetal.entity.file;

import javax.activation.DataSource;

public class FileDataSource {
    private String sourceName;
    private DataSource dataSource;

    public FileDataSource() {
    }

    public FileDataSource(String sourceName, DataSource dataSource) {
        this.sourceName = sourceName;
        this.dataSource = dataSource;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
