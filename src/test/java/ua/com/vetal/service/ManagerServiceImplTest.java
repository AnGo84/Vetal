package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.ManagerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ManagerServiceImplTest {
	@Autowired
	private ManagerServiceImpl managerService;
	@MockBean
	private ManagerRepository mockManagerRepository;
	private Manager manager;

	@BeforeEach
	public void beforeEach() {
		manager = TestBuildersUtils.getManager(null, "firstName", "lastName", "middleName", "email");
	}

	@Test
	void whenFindById_thenReturnManager() {
		when(mockManagerRepository.findById(1L)).thenReturn(Optional.of(manager));
		long id = 1;
		Manager found = managerService.get(id);

		assertNotNull(found);
		assertEquals(found.getId(), manager.getId());
		assertEquals(found.getFirstName(), manager.getFirstName());
		assertEquals(found.getLastName(), manager.getLastName());
		assertEquals(found.getMiddleName(), manager.getMiddleName());
		assertEquals(found.getPhone(), manager.getPhone());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockManagerRepository.findById(1L)).thenReturn(Optional.of(manager));
		long id = 2;
		Manager found = managerService.get(id);
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Manager newManager = TestBuildersUtils.getManager(null, "firstName2", "lastName2", "middleName2", "email2");
		managerService.save(newManager);
		verify(mockManagerRepository, times(1)).save(newManager);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockManagerRepository.save(any(Manager.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			managerService.save(manager);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		manager.setLastName("corpName2");
		managerService.update(manager);
		verify(mockManagerRepository, times(1)).save(manager);
	}

	@Test
	void whenUpdateObject_thenThrowNPE() {
		when(mockManagerRepository.save(any(Manager.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			managerService.update(manager);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		manager.setId(1L);
		when(mockManagerRepository.findById(1L)).thenReturn(Optional.of(manager));
		managerService.deleteById(1l);
		verify(mockManagerRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockManagerRepository).deleteById(anyLong());
		assertThrows(EntityException.class, () -> {
			managerService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockManagerRepository.findAll()).thenReturn(Arrays.asList(manager));
		List<Manager> objects = managerService.getAll();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void isObjectExist() {
		assertFalse(managerService.isExist(null));

		manager.setId(1L);
		when(mockManagerRepository.findById(1L)).thenReturn(Optional.of(manager));
		assertTrue(managerService.isExist(manager));

		when(mockManagerRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertFalse(managerService.isExist(manager));
	}

}