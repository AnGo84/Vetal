package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.NumberBaseDirectory;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.NumberBaseDirectoryRepository;
import ua.com.vetal.repositories.NumberBaseDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        directory = TestBuildersUtils.getNumberBaseDirectory(null, NumberBaseDirectoryRepositoryTest.DIRECTORY_NAME);
    }

    @Test
    void whenFindById_thenReturnObject() {
        //when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        long id = 1;
        NumberBaseDirectory found = directoryService.get(id);

        assertNotNull(found);
        assertEquals(found.getId(), directory.getId());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        long id = 221121;
        NumberBaseDirectory found = directoryService.get(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        NumberBaseDirectory found = directoryService.getByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        NumberBaseDirectory newDirectory = TestBuildersUtils.getNumberBaseDirectory(null, NumberBaseDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.save(newDirectory);
        verify(mockDirectoryRepository, times(1)).save(newDirectory);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockDirectoryRepository.save(any(NumberBaseDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.save(directory);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        directory.setName(NumberBaseDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.update(directory);
        verify(mockDirectoryRepository, times(1)).save(directory);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockDirectoryRepository.save(any(NumberBaseDirectory.class))).thenThrow(NullPointerException.class);
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
        List<NumberBaseDirectory> directoriesList = directoryService.getAll();
        assertNotNull(directoriesList);
        assertFalse(directoriesList.isEmpty());
        assertEquals(directoriesList.size(), 1);
    }

    @Test
    void isObjectExist() {
        assertFalse(directoryService.isExist(null));
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(null);
        assertFalse(directoryService.isExist(directory));

        NumberBaseDirectory findDirectory = TestBuildersUtils.getNumberBaseDirectory(1l, directory.getName());
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertTrue(directoryService.isExist(directory));

        directory.setId(1l);
        findDirectory.setName("New name");
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertFalse(directoryService.isExist(directory));
    }
}