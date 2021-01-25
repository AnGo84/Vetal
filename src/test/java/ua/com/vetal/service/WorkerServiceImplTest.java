package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Worker;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.WorkerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

		worker = TestBuildersUtils.getWorker(null, "firstName", "lastName", "middleName", "email");
	}

	@Test
	void whenFindById_thenReturnWorker() {
		//when(mockWorkerRepository.getOne(1L)).thenReturn(worker);
		when(mockWorkerRepository.findById(1L)).thenReturn(Optional.of(worker));
		long id = 1;
		Worker found = workerService.get(id);

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
		Worker found = workerService.get(id);
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "email2");
		workerService.save(newWorker);
		verify(mockWorkerRepository, times(1)).save(newWorker);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockWorkerRepository.save(any(Worker.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			workerService.save(worker);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		worker.setLastName("CorpName2");
		workerService.update(worker);
		verify(mockWorkerRepository, times(1)).save(worker);
	}

	@Test
	void whenUpdateObject_thenThrowNPE() {
		when(mockWorkerRepository.save(any(Worker.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			workerService.update(worker);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		worker.setId(1L);
		when(mockWorkerRepository.findById(1L)).thenReturn(Optional.of(worker));
		workerService.deleteById(1l);
		verify(mockWorkerRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockWorkerRepository).deleteById(anyLong());
		assertThrows(EntityException.class, () -> {
			workerService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockWorkerRepository.findAll()).thenReturn(Arrays.asList(worker));
		List<Worker> objects = workerService.getAll();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void isObjectExist() {
		assertFalse(workerService.isExist(null));

		worker.setId(1L);
		when(mockWorkerRepository.findById(1L)).thenReturn(Optional.of(worker));
		assertTrue(workerService.isExist(worker));

		when(mockWorkerRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertFalse(workerService.isExist(worker));
	}
}