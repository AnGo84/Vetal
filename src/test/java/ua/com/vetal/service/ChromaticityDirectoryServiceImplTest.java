package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.ChromaticityDirectory;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.ChromaticityDirectoryRepository;
import ua.com.vetal.repositories.ChromaticityDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChromaticityDirectoryServiceImplTest {
    @Autowired
    private ChromaticityDirectoryServiceImpl directoryService;
    @MockBean
    private ChromaticityDirectoryRepository mockDirectoryRepository;
    private ChromaticityDirectory directory;

    @BeforeEach
    public void beforeEach() {
        directory = TestBuildersUtils.getChromaticityDirectory(null, ChromaticityDirectoryRepositoryTest.DIRECTORY_NAME);
    }

    @Test
    void whenFindById_thenReturnObject() {
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        long id = 1;
        ChromaticityDirectory found = directoryService.get(id);

        assertNotNull(found);
        assertEquals(found.getId(), directory.getId());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
        long id = 221121;
        ChromaticityDirectory found = directoryService.get(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
        ChromaticityDirectory found = directoryService.getByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveObject_thenSuccess() {
        when(mockDirectoryRepository.save(any())).thenReturn(directory);
        ChromaticityDirectory newDirectory = TestBuildersUtils.getChromaticityDirectory(null, ChromaticityDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        ChromaticityDirectory savedDirectory = directoryService.save(newDirectory);
        assertNotNull(savedDirectory);
        verify(mockDirectoryRepository, times(1)).save(newDirectory);
    }

    @Test
    void whenSaveObject_thenNPE() {
        when(mockDirectoryRepository.save(any(ChromaticityDirectory.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            directoryService.save(directory);
        });
    }

    @Test
    void whenUpdateObject_thenSuccess() {
        directory.setName(ChromaticityDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
        when(mockDirectoryRepository.save(any())).thenReturn(directory);
        ChromaticityDirectory updatedDirectory = directoryService.update(directory);
        assertNotNull(updatedDirectory);
        verify(mockDirectoryRepository, times(1)).save(directory);
    }

    @Test
    void whenUpdateObject_thenThrow() {
        when(mockDirectoryRepository.save(any(ChromaticityDirectory.class))).thenThrow(NullPointerException.class);
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
    void whenDeleteById_thenThrowEntityException() {
        assertThrows(EntityException.class, () -> {
            directoryService.deleteById(1000000l);
        });
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        when(mockDirectoryRepository.findById(anyLong())).thenReturn(Optional.of(directory));
        doThrow(new EmptyResultDataAccessException(0)).when(mockDirectoryRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            directoryService.deleteById(1000000l);
        });
    }

    @Test
    void findAllObjects() {
        when(mockDirectoryRepository.findAll()).thenReturn(Arrays.asList(directory));
        List<ChromaticityDirectory> directorieList = directoryService.getAll();
        assertNotNull(directorieList);
        assertFalse(directorieList.isEmpty());
        assertEquals(directorieList.size(), 1);
    }

    @Test
    void isObjectExist() {
        assertFalse(directoryService.isExist(null));
        when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(null);
        assertFalse(directoryService.isExist(directory));
        ChromaticityDirectory findDirectory = TestBuildersUtils.getChromaticityDirectory(1l, directory.getName());
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertTrue(directoryService.isExist(directory));

        directory.setId(1l);
        findDirectory.setName("New name");
        when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
        assertFalse(directoryService.isExist(directory));
    }

}