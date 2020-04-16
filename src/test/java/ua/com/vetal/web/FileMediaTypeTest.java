package ua.com.vetal.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration
@WebAppConfiguration
public class FileMediaTypeTest {
	private MediaType defaultMediaType = MediaType.APPLICATION_OCTET_STREAM;
	@Autowired
	private ServletContext servletContext;
	//private MockServletContext servletContext;

	private FileMediaType fileMediaType;

	@BeforeEach
	public void beforeEach() {
		fileMediaType = new FileMediaType(servletContext);
	}


	@Test
	void whenGetMediaTypeForFileName_thenReturnExtractedMediaType() {
		assertEquals(MediaType.parseMediaType("application/msword"), fileMediaType.getMediaTypeForFileName("java.doc"));
		assertEquals(MediaType.TEXT_PLAIN, fileMediaType.getMediaTypeForFileName("file.txt"));
		assertEquals(MediaType.TEXT_HTML, fileMediaType.getMediaTypeForFileName("file.html"));
		assertEquals(MediaType.IMAGE_JPEG, fileMediaType.getMediaTypeForFileName("picture.jpeg"));
		assertEquals(MediaType.IMAGE_PNG, fileMediaType.getMediaTypeForFileName("picture.png"));
		assertEquals(MediaType.APPLICATION_JSON, fileMediaType.getMediaTypeForFileName("application.json"));
		assertEquals(MediaType.APPLICATION_XML, fileMediaType.getMediaTypeForFileName("application.xml"));
	}

	@Test
	void whenGetMediaTypeForFileName_thenReturnDefaultMediaType() {
		assertEquals(defaultMediaType, fileMediaType.getMediaTypeForFileName(""));
		assertEquals(defaultMediaType, fileMediaType.getMediaTypeForFileName(null));
		assertEquals(defaultMediaType, fileMediaType.getMediaTypeForFileName("wrong file name"));
		assertEquals(defaultMediaType, fileMediaType.getMediaTypeForFileName("wrong_file.extension"));
	}
}