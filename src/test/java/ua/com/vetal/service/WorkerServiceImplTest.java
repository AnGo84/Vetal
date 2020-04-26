package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.repositories.WorkerRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WorkerServiceImplTest {
	@Autowired
	private WorkerServiceImpl workerService;
	@MockBean
	private WorkerRepository mockWorkerRepository;
	private Worker worker;

	@BeforeEach
	public void beforeEach() {
		worker = TestBuildersUtils.getWorker(1l, "firstName", "lastName", "middleName", "email");
	}

	@Test
	void whenFindById_thenReturnWorker() {
		when(mockWorkerRepository.getOne(1L)).thenReturn(worker);
		long id = 1;
		Worker found = workerService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), worker.getId());
		assertEquals(found.getFirstName(), worker.getFirstName());
		assertEquals(found.getLastName(), worker.getLastName());
		assertEquals(found.getMiddleName(), worker.getMiddleName());
		assertEquals(found.getPhone(), worker.getPhone());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockWorkerRepository.getOne(1L)).thenReturn(worker);
		long id = 2;
		Worker found = workerService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		//when(mockManagerRepository.findByName(anyString())).thenReturn(manager);
		Worker found = workerService.findByName(null);
		assertNull(found);
		found = workerService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "email2");
		workerService.saveObject(newWorker);
		verify(mockWorkerRepository, times(1)).save(newWorker);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockWorkerRepository.save(any(Worker.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			workerService.saveObject(worker);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		worker.setCorpName("CorpName2");
		workerService.updateObject(worker);
		verify(mockWorkerRepository, times(1)).save(worker);
	}

	@Test
	void whenUpdateObject_thenThrowNPE() {
		when(mockWorkerRepository.save(any(Worker.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			workerService.updateObject(worker);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		workerService.deleteById(1l);
		verify(mockWorkerRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockWorkerRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			workerService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockWorkerRepository.findAll()).thenReturn(Arrays.asList(worker));
		List<Worker> objects = workerService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockWorkerRepository.getOne(worker.getId())).thenReturn(worker);
		assertTrue(workerService.isObjectExist(worker));

		when(mockWorkerRepository.getOne(anyLong())).thenReturn(null);
		assertFalse(workerService.isObjectExist(worker));
	}
}