package ua.com.vetal.entity.file;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class LocalFile {
    private String displayName;
    private File file;
    private boolean isFolder;
}
