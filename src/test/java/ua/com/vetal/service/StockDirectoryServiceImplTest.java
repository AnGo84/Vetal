package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.StockDirectory;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.StockDirectoryRepository;
import ua.com.vetal.repositories.StockDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StockDirectoryServiceImplTest {
    @Autowired
    private StockDirectoryServiceImpl directoryService;
    @MockBean
    private StockDirectoryRepository mockDirectoryRepository;
    private StockDirectory directory;

    @BeforeEach
    public void beforeEach() {
        directory = TestBuildersUtils.getStockDirectory(null, StockDirectoryRepositoryTest.DIRECTORY_NAME);
    }

    @Test
    void whenFindById_thenReturnObject() {
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        long id = 1;
        StockDirectory found = directoryService.get(id);

        assertNotNull(found);
        assertEquals(found.getId(), directory.getId());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
        long id = 221121;
        StockDirectory found = directoryService.get(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        StockDirectory found = directoryService.getByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        StockDirectory newDirectory = TestBuildersUtils.getStockDirectory(null, StockDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.save(newDirectory);
        verify(mockDirectoryRepository, times(1)).save(newDirectory);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockDirectoryRepository.save(any(StockDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.save(directory);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        directory.setName(StockDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        directoryService.update(directory);
        verify(mockDirectoryRepository, times(1)).save(directory);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockDirectoryRepository.save(any(StockDirectory.class))).thenThrow(NullPointerException.class);
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
        List<StockDirectory> directoriesList = directoryService.getAll();
        assertNotNull(directoriesList);
        assertFalse(directoriesList.isEmpty());
        assertEquals(directoriesList.size(), 1);
    }

    @Test
    void isObjectExist() {
        assertFalse(directoryService.isExist(null));
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(null);
        assertFalse(directoryService.isExist(directory));

        StockDirectory findDirectory = TestBuildersUtils.getStockDirectory(1l, directory.getName());
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertTrue(directoryService.isExist(directory));

        directory.setId(1l);
        findDirectory.setName("New name");
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertFalse(directoryService.isExist(directory));
    }
}