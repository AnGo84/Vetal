package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.repositories.NumberBaseDirectoryRepository;
import ua.com.vetal.repositories.NumberBaseDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class NumberBaseDirectoryServiceImplTest {
    @Autowired
    private NumberBaseDirectoryServiceImpl directoryService;
    @MockBean
    private NumberBaseDirectoryRepository mockDirectoryRepository;
    private NumberBaseDirectory directory;

    @BeforeEach
    public void beforeEach() {
        directory = TestDataUtils.getNumberBaseDirectory(NumberBaseDirectoryRepositoryTest.DIRECTORY_NAME);
    }

    @Test
    void whenFindById_thenReturnObject() {
        when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        long id = 1;
        NumberBaseDirectory found = directoryService.findById(id);

        assertNotNull(found);
        assertEquals(found.getId(), directory.getId());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        long id = 221121;
        NumberBaseDirectory found = directoryService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        NumberBaseDirectory found = directoryService.findByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        NumberBaseDirectory newDirector = TestDataUtils.getNumberBaseDirectory(NumberBaseDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.saveObject(newDirector);
        verify(mockDirectoryRepository, times(1)).save(newDirector);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockDirectoryRepository.save(any(NumberBaseDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.saveObject(directory);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        NumberBaseDirectory newDirector = TestDataUtils.getNumberBaseDirectory(NumberBaseDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.saveObject(newDirector);
        verify(mockDirectoryRepository, times(1)).save(newDirector);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockDirectoryRepository.save(any(NumberBaseDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.updateObject(directory);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        directoryService.deleteById(1l);
        verify(mockDirectoryRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockDirectoryRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            directoryService.deleteById(1000000l);
        });
    }

    @Test
    void findAllObjects() {
        when(mockDirectoryRepository.findAll()).thenReturn(Arrays.asList(directory));
        List<NumberBaseDirectory> directoriesList = directoryService.findAllObjects();
        assertNotNull(directoriesList);
        assertFalse(directoriesList.isEmpty());
        assertEquals(directoriesList.size(), 1);
    }

    @Test
    void isObjectExist() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        assertTrue(directoryService.isObjectExist(directory));
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
    }
}