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
            //log.info("servletContext is null: {}", (servletContext==null));
            String mineType = servletContext.getMimeType(fileName);
            //log.info("String mineType= {}", mineType);
            MediaType mediaType = MediaType.parseMediaType(mineType);
            //log.info("MediaType= {}", mediaType);
            return mediaType;
        } catch (Exception e) {
            log.error("Wrong file extension for filename '{}' with error: {}", fileName, e.getMessage());
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
