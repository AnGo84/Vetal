package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.ProductionTypeDirectoryRepository;
import ua.com.vetal.repositories.ProductionTypeDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductionTypeDirectoryServiceImplTest {
	@Autowired
	private ProductionTypeDirectoryServiceImpl directoryService;
	@MockBean
	private ProductionTypeDirectoryRepository mockDirectoryRepository;
	private ProductionTypeDirectory directory;

	@BeforeEach
	public void beforeEach() {
        directory = TestBuildersUtils.getProductionTypeDirectory(null, ProductionTypeDirectoryRepositoryTest.DIRECTORY_NAME);
    }

	@Test
	void whenFindById_thenReturnObject() {
		when(mockDirectoryRepository.findById(1L)).thenReturn(Optional.of(directory));
		long id = 1;
		ProductionTypeDirectory found = directoryService.get(id);

		assertNotNull(found);
		assertEquals(found.getId(), directory.getId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
		long id = 221121;
		ProductionTypeDirectory found = directoryService.get(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
		ProductionTypeDirectory found = directoryService.getByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
        ProductionTypeDirectory newDirector = TestBuildersUtils.getProductionTypeDirectory(null, ProductionTypeDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.save(newDirector);
		verify(mockDirectoryRepository, times(1)).save(newDirector);
    }

	@Test
	void whenSaveObject_thenNPE() {
		when(mockDirectoryRepository.save(any(ProductionTypeDirectory.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			directoryService.save(directory);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
        ProductionTypeDirectory newDirector = TestBuildersUtils.getProductionTypeDirectory(null, ProductionTypeDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.save(newDirector);
		verify(mockDirectoryRepository, times(1)).save(newDirector);
    }

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockDirectoryRepository.save(any(ProductionTypeDirectory.class))).thenThrow(NullPointerException.class);
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
		List<ProductionTypeDirectory> directoriesList = directoryService.getAll();
		assertNotNull(directoriesList);
		assertFalse(directoriesList.isEmpty());
		assertEquals(directoriesList.size(), 1);
	}

	@Test
	void isObjectExist() {
		assertFalse(directoryService.isExist(null));
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(null);
		assertFalse(directoryService.isExist(directory));

		ProductionTypeDirectory findDirectory = TestBuildersUtils.getProductionTypeDirectory(1l, directory.getName());
		when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
		assertTrue(directoryService.isExist(directory));

		directory.setId(1l);
		findDirectory.setName("New name");
		when(mockDirectoryRepository.findByName(anyString())).thenReturn(findDirectory);
		assertFalse(directoryService.isExist(directory));
	}
}