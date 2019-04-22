package ua.com.vetal.entity.file;

import java.io.File;

public class LocalFile {
    private String displayName;
    private File file;
    private boolean isFolder;

    public LocalFile() {
    }

    public LocalFile(String displayName, File file, boolean isFolder) {
        this.displayName = displayName;
        this.file = file;
        this.isFolder = isFolder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LocalFile{");
        sb.append("displayName='").append(displayName).append('\'');
        sb.append(", file=").append(file.getAbsolutePath());
        sb.append(", isFolder=").append(isFolder);
        sb.append('}');
        return sb.toString();
    }
}
