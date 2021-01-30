package ua.com.vetal.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ua.com.vetal.entity.DBFile;
import ua.com.vetal.repositories.DBFileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DBFileStorageServiceTest {
    @Autowired
    private DBFileStorageService fileService;
    @MockBean
    private DBFileRepository mockDBFileRepository;
    private DBFile dbFile;

    @BeforeEach
    public void beforeEach() {
        dbFile = new DBFile("file1", "text/plain", "file data".getBytes());
        dbFile.setId(1l);
    }

    @Test
    void whenFindById_thenReturnDBFile() throws FileNotFoundException {
        when(mockDBFileRepository.findById(1L)).thenReturn(Optional.of(dbFile));
        long id = 1L;
        DBFile found = fileService.findById(id);

        assertNotNull(found);
        assertEquals(dbFile.getId(), found.getId());
        assertEquals(dbFile.getFileName(), found.getFileName());
        assertEquals(dbFile.getFileType(), found.getFileType());
        assertEquals(dbFile.getData(), found.getData());
    }

    @Test
    void whenFindById_thenThrowFileNotFoundException() {
		assertThrows(FileNotFoundException.class, () -> {
			fileService.storeMultipartFile(null);
		});

		MultipartFile mockMultipartFile = mock(MultipartFile.class);
		when(mockMultipartFile.isEmpty()).thenReturn(true);
		assertThrows(FileNotFoundException.class, () -> {
			fileService.storeMultipartFile(mockMultipartFile);
		});

		when(mockDBFileRepository.getOne(1L)).thenReturn(null);
		assertThrows(FileNotFoundException.class, () -> {
			DBFile found = fileService.findById(1l);
		});
	}

    @Test
    void whenSaveDBFile_thenSuccess() {
        when(mockDBFileRepository.save(any(DBFile.class))).thenReturn(dbFile);
        DBFile savedDBFile = fileService.save(dbFile);
        verify(mockDBFileRepository, times(1)).save(dbFile);

        assertNotNull(savedDBFile);
        assertNotNull(savedDBFile.getId());
        assertEquals(dbFile.getFileName(), savedDBFile.getFileName());
        assertEquals(dbFile.getFileType(), savedDBFile.getFileType());
        assertEquals(dbFile.getData(), savedDBFile.getData());
    }

    @Test
    void whenSaveNull_thenThrowInvalidDataAccessApiUsageException() {
        when(mockDBFileRepository.save(any())).thenThrow(InvalidDataAccessApiUsageException.class);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            fileService.save(null);
        });
    }


    @Test
    void whenStoreMultipartFile_thenSuccess() throws FileUploadException, FileNotFoundException {
        MultipartFile multipartFile = new MockMultipartFile(dbFile.getFileName(),
                dbFile.getFileName(), dbFile.getFileType(), dbFile.getData());
        when(mockDBFileRepository.save(any(DBFile.class))).thenReturn(dbFile);
        DBFile savedDBFile = fileService.storeMultipartFile(multipartFile);

        assertNotNull(savedDBFile);
        assertNotNull(savedDBFile.getId());
        assertEquals(dbFile.getFileName(), savedDBFile.getFileName());
        assertEquals(dbFile.getFileType(), savedDBFile.getFileType());
        assertEquals(dbFile.getData(), savedDBFile.getData());
    }

    @Test
    void whenStoreMultipartFileAsDirectory_thenThrowFileNotFoundException() {
        MultipartFile multipartFile = new MockMultipartFile(dbFile.getFileName(),
                dbFile.getFileName() + "..", dbFile.getFileType(), dbFile.getData());
        assertThrows(FileNotFoundException.class, () -> {
            fileService.storeMultipartFile(multipartFile);
        });
    }

    @Test
    void whenStoreMultipartFileNull_thenThrowIFileUploadException() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("OriginalFilename");
        when(multipartFile.getBytes()).thenThrow(IOException.class);
        assertThrows(FileUploadException.class, () -> {
            fileService.storeMultipartFile(multipartFile);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        fileService.deleteById(1l);
        verify(mockDBFileRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockDBFileRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            fileService.deleteById(1000000l);
        });
    }
}