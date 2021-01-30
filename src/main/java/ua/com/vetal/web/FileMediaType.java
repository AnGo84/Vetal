package ua.com.vetal.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;

@RequiredArgsConstructor
@Slf4j
public class FileMediaType {

    private final ServletContext servletContext;

    // abc.zip
    // abc.pdf,..
    public MediaType getMediaTypeForFileName(String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        try {
            String mineType = servletContext.getMimeType(fileName);
            MediaType mediaType = MediaType.parseMediaType(mineType);

            return mediaType;
        } catch (Exception e) {
            log.error("Wrong file extension for filename '{}' with error: {}", fileName, e.getMessage());
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
