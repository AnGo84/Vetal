package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.PrintingUnitDirectory;
import ua.com.vetal.repositories.PrintingUnitDirectoryRepository;
import ua.com.vetal.repositories.PrintingUnitDirectoryRepositoryTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PrintingUnitDirectoryServiceImplTest {
	@Autowired
	private PrintingUnitDirectoryServiceImpl directoryService;
	@MockBean
	private PrintingUnitDirectoryRepository mockDirectoryRepository;
	private PrintingUnitDirectory directory;

	@BeforeEach
	public void beforeEach() {
		directory = TestDataUtils.getPrintingUnitDirectory(PrintingUnitDirectoryRepositoryTest.DIRECTORY_NAME);
	}

	@Test
	void whenFindById_thenReturnObject() {
		when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
		long id = 1;
		PrintingUnitDirectory found = directoryService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), directory.getId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockDirectoryRepository.getOne(1L)).thenReturn(directory);
		long id = 221121;
		PrintingUnitDirectory found = directoryService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockDirectoryRepository.findByName(directory.getName())).thenReturn(directory);
		PrintingUnitDirectory found = directoryService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		PrintingUnitDirectory newDirectory = TestDataUtils.getPrintingUnitDirectory(PrintingUnitDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.saveObject(newDirectory);
		verify(mockDirectoryRepository, times(1)).save(newDirectory);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockDirectoryRepository.save(any(PrintingUnitDirectory.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			directoryService.saveObject(directory);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		directory.setName(PrintingUnitDirectoryRepositoryTest.SECOND_DIRECTORY_NAME);
		directoryService.saveObject(directory);
		verify(mockDirectoryRepository, times(1)).save(directory);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockDirectoryRepository.save(any(PrintingUnitDirectory.class))).thenThrow(NullPointerException.class);
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
		List<PrintingUnitDirectory> directoriesList = directoryService.findAllObjects();
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