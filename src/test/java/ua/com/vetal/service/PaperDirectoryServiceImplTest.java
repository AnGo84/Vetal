package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.PaperDirectory;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.PaperDirectoryRepository;
import ua.com.vetal.repositories.PaperDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaperDirectoryServiceImplTest {
    @Autowired
    private PaperDirectoryServiceImpl directoryService;
    @MockBean
    private PaperDirectoryRepository mockDirectoryRepository;
    private PaperDirectory directory;

    @BeforeEach
    public void beforeEach() {
        directory = TestBuildersUtils.getPaperDirectory(null, PaperDirectoryRepositoryTest.DIRECTORY_NAME);
    }

    @Test
    void whenFindById_thenReturnObject() {
        //when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        long id = 1;
        PaperDirectory found = directoryService.get(id);

        assertNotNull(found);
        assertEquals(found.getId(), directory.getId());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        long id = 221121;
        PaperDirectory found = directoryService.get(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        PaperDirectory found = directoryService.getByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        PaperDirectory newDirectory = TestBuildersUtils.getPaperDirectory(null, PaperDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.save(newDirectory);
        verify(mockDirectoryRepository, times(1)).save(newDirectory);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockDirectoryRepository.save(any(PaperDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.save(directory);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        directory.setName(PaperDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.update(directory);
        verify(mockDirectoryRepository, times(1)).save(directory);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockDirectoryRepository.save(any(PaperDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.update(directory);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        directory.setId(1L);
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        directoryService.deleteById(1l);
        verify(mockDirectoryRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EntityException.class, () -> {
            directoryService.deleteById(1000000l);
        });
    }

    @Test
    void findAllObjects() {
        when(mockDirectoryRepository.findAll()).thenReturn(Arrays.asList(directory));
        List<PaperDirectory> directoriesList = directoryService.getAll();
        assertNotNull(directoriesList);
        assertFalse(directoriesList.isEmpty());
        assertEquals(directoriesList.size(), 1);
    }

    @Test
    void isObjectExist() {
        assertFalse(directoryService.isExist(null));
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(null);
        assertFalse(directoryService.isExist(directory));

        PaperDirectory findDirectory = TestBuildersUtils.getPaperDirectory(1l, directory.getName());
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertTrue(directoryService.isExist(directory));

        directory.setId(1l);
        findDirectory.setName("New name");
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertFalse(directoryService.isExist(directory));
    }
}