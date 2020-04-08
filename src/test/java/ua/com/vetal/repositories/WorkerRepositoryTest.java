package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Worker;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WorkerRepositoryTest {
	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private WorkerRepository workerRepository;

	private Worker worker;

	@BeforeEach
	public void beforeEach() {
		workerRepository.deleteAll();
		worker = TestBuildersUtils.getWorker(null, "firstName", "lastName", "middleName", "email");

		worker = testEntityManager.persistAndFlush(worker);
	}

	@Test
	public void whenFindByID_thenReturnWorker() {
		Optional<Worker> foundUser = workerRepository.findById(worker.getId());
		assertTrue(foundUser.isPresent());
		assertEquals(worker, foundUser.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			workerRepository.findById(null);
		});
	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		Long wrongId = 123654L;
		Optional<Worker> foundUser = workerRepository.findById(wrongId);
		assertFalse(foundUser.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfWorkers() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "email2");
		testEntityManager.persistAndFlush(newWorker);
		List<Worker> Workers = workerRepository.findAll();
		assertNotNull(Workers);
		assertFalse(Workers.isEmpty());
		assertEquals(Workers.size(), 2);
	}

	@Test
	public void it_should_save_Worker() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "email2");
		newWorker = workerRepository.save(newWorker);
		Worker foundWorker = workerRepository.findById(newWorker.getId()).get();

		assertNotNull(foundWorker);
		assertNotNull(foundWorker.getId());
		assertEquals(foundWorker.getId(), newWorker.getId());
		assertEquals(foundWorker.getFirstName(), newWorker.getFirstName());
		assertEquals(foundWorker.getLastName(), newWorker.getLastName());
		assertEquals(foundWorker.getMiddleName(), newWorker.getMiddleName());
		assertEquals(foundWorker.getEmail(), newWorker.getEmail());
	}

	@Test
	public void it_should_save_Worker_with_null_fields() {
		Worker newWorker = TestBuildersUtils.getWorker(null, null, "lastName2", null, "email2");
		newWorker = workerRepository.save(newWorker);
		Worker foundWorker = workerRepository.findById(newWorker.getId()).get();

		assertNotNull(foundWorker);
		assertNotNull(foundWorker.getId());
		assertEquals(foundWorker.getId(), newWorker.getId());
		assertEquals(foundWorker.getFirstName(), newWorker.getFirstName());
		assertEquals(foundWorker.getLastName(), newWorker.getLastName());
		assertEquals(foundWorker.getMiddleName(), newWorker.getMiddleName());
		assertEquals(foundWorker.getEmail(), newWorker.getEmail());
	}

	@Test
	public void it_should_save_Worker_with_empty_fields() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "", "lastName2", "", "email2");
		newWorker = workerRepository.save(newWorker);
		Worker foundWorker = workerRepository.findById(newWorker.getId()).get();

		assertNotNull(foundWorker);
		assertNotNull(foundWorker.getId());
		assertEquals(foundWorker.getId(), newWorker.getId());
		assertEquals(foundWorker.getFirstName(), newWorker.getFirstName());
		assertEquals(foundWorker.getLastName(), newWorker.getLastName());
		assertEquals(foundWorker.getMiddleName(), newWorker.getMiddleName());
		assertEquals(foundWorker.getEmail(), newWorker.getEmail());
	}

	@Test
	public void whenSaveWorkerWithFirstNameTooLong_thenThrowConstraintViolationException() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "FirstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName2", "middleName2", "email2");
		assertThrows(ConstraintViolationException.class, () -> {
			workerRepository.save(newWorker);
		});
	}

	@Test
	public void whenSaveWorkerWithLastNameTooLong_thenThrowConstraintViolationException() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "FirstName2", "lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName2", "email2");
		assertThrows(ConstraintViolationException.class, () -> {
			workerRepository.save(newWorker);
		});
	}

	@Test
	public void whenSaveWorkerWithLastNameTooShortLength_thenThrowConstraintViolationException() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "", "middleName2", "email2");
		assertThrows(ConstraintViolationException.class, () -> {
			workerRepository.save(newWorker);
		});
	}

	@Test
	public void whenSaveWorkerWithMiddleNameTooLong_thenThrowConstraintViolationException() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "FirstName", "lastName2", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "email2");
		assertThrows(ConstraintViolationException.class, () -> {
			workerRepository.save(newWorker);
		});
	}

	@Test
	public void whenSaveWorkerWithEmailWrongLength_thenThrowConstraintViolationException() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "EmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100Symbols");
		assertThrows(ConstraintViolationException.class, () -> {
			workerRepository.save(newWorker);
		});
	}

	@Test
	public void whenDeleteById_thenOk() {
		Worker newWorker = TestBuildersUtils.getWorker(null, "firstName2", "lastName2", "middleName2", "email2");
		newWorker = testEntityManager.persistAndFlush(newWorker);
		assertEquals(workerRepository.findAll().size(), 2);

		workerRepository.deleteById(newWorker.getId());

		assertEquals(workerRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			workerRepository.deleteById(10000000l);
		});
	}
}