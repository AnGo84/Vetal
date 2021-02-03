package ua.com.vetal.repositories;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Task;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private TaskRepository taskRepository;

	private Task task;

	@BeforeEach
	public void beforeEach() {
		taskRepository.deleteAll();

		task = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 1), entityManager);
		task = entityManager.persistAndFlush(task);
	}

	@Test
	public void whenFindByAccount_thenReturnObject() {
		// when
		Task foundTask = taskRepository.findByAccount(task.getAccount());

		// then
		assertNotNull(foundTask);
		assertNotNull(foundTask.getId());
		assertEquals(task.getId(), foundTask.getId());
		assertEquals(task, foundTask);
	}

	@Test
	public void whenFindByAccount_thenReturnEmpty() {
		assertNull(taskRepository.findByAccount("wrong name"));
	}


	@Test
	public void whenFindByID_thenReturnManager() {

		// when
		Optional<Task> foundTask = taskRepository.findById(task.getId());
		// then
		assertTrue(foundTask.isPresent());
		assertEquals(task, foundTask.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			taskRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Long wrongId = 123654L;
		Optional<Task> foundTask = taskRepository.findById(wrongId);
		// then
		assertFalse(foundTask.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfManagers() {
		//given
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask = entityManager.persistAndFlush(newTask);

		entityManager.persistAndFlush(newTask);
		// when
		List<Task> tasks = taskRepository.findAll();
		// then
		assertNotNull(tasks);
		assertFalse(tasks.isEmpty());
		assertEquals(tasks.size(), 2);

	}

	@Test
	public void it_should_save_Client() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask = taskRepository.save(newTask);
		Task foundTask = taskRepository.findById(newTask.getId()).get();

		// then
		assertNotNull(foundTask);
		assertNotNull(foundTask.getId());
		assertEquals(newTask, foundTask);
	}

	@Test
	public void whenSaveClientWithAccountTooLong_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setAccount("accountWithLengthMoreThen50SymbolsIsTooLongForSaving");

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutManager_thenThrowDataIntegrityViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setManager(null);

		assertThrows(DataIntegrityViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithNullWorkName_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setWorkName(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithEmptyWorkName_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setWorkName("");

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutContractor_thenThrowDataIntegrityViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setContractor(null);

		assertThrows(DataIntegrityViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutProduction_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setProduction(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutProductionType_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setProductionType(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutDateBegin_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setDateBegin(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutDateEnd_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setDateEnd(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutClient_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setClient(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutStock_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setStock(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutPrintingUnit_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setPrintingUnit(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutChromaticitygUnit_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setChromaticity(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutPrintingFormat_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setPrintingFormat(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutLaminateFormat_thenThrowDataIntegrityViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setLaminate(null);

		assertThrows(DataIntegrityViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutPaperFormat_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setPaper(null);

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithOutCringleFormat_thenThrowDataIntegrityViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setCringle(null);

		assertThrows(DataIntegrityViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenSaveClientWithNoteTooLong_thenThrowConstraintViolationException() {
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask.setNote(StringUtils.repeat("noteWithLengthMoreThen1000SymbolsIsTooLongForSaving", 20));

		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newTask);
		});
	}

	@Test
	public void whenDeleteById_thenOk() {
		//given
		Task newTask = TestDataServiceUtils.saveTaskParts(TestDataUtils.getTask(null, 2), entityManager);
		newTask = entityManager.persistAndFlush(newTask);
		assertEquals(taskRepository.findAll().size(), 2);

		// when
		taskRepository.deleteById(newTask.getId());
		// then
		assertEquals(taskRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			taskRepository.deleteById(10000000l);
		});
	}

}