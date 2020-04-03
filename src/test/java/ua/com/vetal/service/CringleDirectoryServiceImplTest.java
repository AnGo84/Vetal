package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.CringleDirectory;
import ua.com.vetal.repositories.CringleDirectoryRepository;
import ua.com.vetal.repositories.CringleDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CringleDirectoryServiceImplTest {
	@Autowired
	private CringleDirectoryServiceImpl directoryService;
	@MockBean
	private CringleDirectoryRepository mockDirectoryRepository;
	private CringleDirectory directory;

	@BeforeEach
	public void beforeEach() {
		directory = TestDataUtils.getCringleDirectory(CringleDirectoryRepositoryTest.DIRECTORY_NAME);
	}

	@Test
	void whenFindById_thenReturnObject() {
		when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
		long id = 1;
		CringleDirectory found = directoryService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), directory.getId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
		long id = 221121;
		CringleDirectory found = directoryService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
		CringleDirectory found = directoryService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		CringleDirectory newDirector = TestDataUtils.getCringleDirectory(CringleDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.saveObject(newDirector);
		verify(mockDirectoryRepository, times(1)).save(newDirector);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockDirectoryRepository.save(any(CringleDirectory.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			directoryService.saveObject(directory);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		CringleDirectory newDirector = TestDataUtils.getCringleDirectory(CringleDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.saveObject(newDirector);
		verify(mockDirectoryRepository, times(1)).save(newDirector);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockDirectoryRepository.save(any(CringleDirectory.class))).thenThrow(NullPointerException.class);
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
		List<CringleDirectory> directorieList = directoryService.findAllObjects();
		assertNotNull(directorieList);
		assertFalse(directorieList.isEmpty());
		assertEquals(directorieList.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
		assertTrue(directoryService.isObjectExist(directory));
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
	}
}