package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.*;
import ua.com.vetal.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
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

		task = saveTaskParts(getTask(null, 1));
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

		//User user = userRepository.findByName("User");
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
		Task newTask = saveTaskParts(getTask(null, 2));
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
		Task newTask = saveTaskParts(getTask(null, 2));
		newTask = taskRepository.save(newTask);
		Task foundTask = taskRepository.findById(newTask.getId()).get();

		// then
		assertNotNull(foundTask);
		assertNotNull(foundTask.getId());
		assertEquals(newTask, foundTask);
	}

	/*@Test
	public void whenSaveClientWithFullNameTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSaving",
				"firstName", "lastName", "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithFullNameTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "", "firstName", "lastName", "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithFullNameNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, null, "firstName", "lastName", "middleName", "address", "email", "phone");
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithFirstNameTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName", "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithFirstNameTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "", "lastName", "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithFirstNameNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", null, "lastName", "middleName", "address", "email", "phone");
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithLastNameTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName",
				"lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName",
				"address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithLastNameTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName", "", "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithLastNameNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName", null, "middleName", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithMiddleNameTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName", "lastName", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "address", "email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithAddressTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName",
				"lastName", "middleName",
				"addressWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving",
				"email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithAddressTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", "lastName", "middleName", "",
				"email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithAddressNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", null, "middleName", null,
				"email", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithEmailTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName",
				"lastName", "middleName", "address",
				"emailWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving"
				, "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithEmailTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", "lastName", "middleName", "address",
				"", "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithEmailNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", null, "middleName", "address",
				null, "phone");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithPhoneTooLong_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName", "firstName",
				"lastName", "middleName", "address",
				"email", "WithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithPhoneTooShortLength_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", "lastName", "middleName", "address",
				"email", "");
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}


	@Test
	public void whenSaveClientWithPhoneNull_thenThrowConstraintViolationException() {
		Client newClient = TestBuildersUtils.getClient(null, "fullName",
				"firstName", null, "middleName", "address",
				"email", null);
		newClient.setManager(manager);
		assertThrows(ConstraintViolationException.class, () -> {
			taskRepository.save(newClient);
		});
	}

	@Test
	public void whenSaveClientWithManagerNull_thenThrowConstraintViolationException() {
		Client newClient = getSecondClient();
		newClient.setManager(null);
		newClient = taskRepository.save(newClient);

		Client foundClient = taskRepository.findById(newClient.getId()).get();

		// then
		assertNotNull(foundClient);
		assertNotNull(foundClient.getId());
		assertEquals(foundClient.getFullName(), newClient.getFullName());
		assertEquals(foundClient.getFirstName(), newClient.getFirstName());
		assertEquals(foundClient.getLastName(), newClient.getLastName());
		assertEquals(foundClient.getMiddleName(), newClient.getMiddleName());
		assertEquals(foundClient.getAddress(), newClient.getAddress());
		assertEquals(foundClient.getEmail(), newClient.getEmail());
		assertEquals(foundClient.getPhone(), newClient.getPhone());
		assertNull(foundClient.getManager());
	}

	@Test
	public void whenSaveClientWithNotExistManager_thenOk() {
		Client newClient = getSecondClient();
		newClient.setManager(new Manager());
		newClient = taskRepository.save(newClient);
		Client foundClient = taskRepository.findById(newClient.getId()).get();
		// then
		assertNotNull(foundClient);
		assertNotNull(foundClient.getId());
		assertEquals(foundClient.getFullName(), newClient.getFullName());
		assertEquals(foundClient.getFirstName(), newClient.getFirstName());
		assertEquals(foundClient.getLastName(), newClient.getLastName());
		assertEquals(foundClient.getMiddleName(), newClient.getMiddleName());
		assertEquals(foundClient.getAddress(), newClient.getAddress());
		assertEquals(foundClient.getEmail(), newClient.getEmail());
		assertEquals(foundClient.getPhone(), newClient.getPhone());
		assertNotNull(foundClient.getManager());
	}
*/

	@Test
	public void whenDeleteById_thenOk() {
		//given
		Task newTask = saveTaskParts(getTask(null, 2));
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

	private Task getTask(Long id, int taskNumber) {
		Task task = new Task();
		task.setId(id);
		task.setNumber(taskNumber);
		task.setNumberBase(TestBuildersUtils.getNumberBaseDirectory(id, "numberBaseDirectory" + taskNumber));
		task.setNumberSuffix(taskNumber);
		task.setFullNumber("fullNumber" + taskNumber);
		task.setAccount("account" + taskNumber);
		Manager manager = TestBuildersUtils.getManager(id, "managerFirstName" + taskNumber, "managerLastName" + taskNumber, "managerMiddleName" + taskNumber, "managerEmail" + taskNumber);
		task.setManager(manager);
		task.setWorkName("workName" + taskNumber);
		task.setFileName("fileName" + taskNumber);
		task.setDbFile(new DBFile("file" + taskNumber, "content_type" + taskNumber, ("file data" + taskNumber).getBytes()));

		Contractor contractor = TestBuildersUtils.getContractor(id, "corpName", "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
		contractor.setManager(manager);
		task.setContractor(contractor);
		task.setContractorNumber("ContractorNumber" + taskNumber);
		task.setContractorAmount(10 * taskNumber);
		task.setProvider("provider" + taskNumber);
		task.setProviderCost(8 * taskNumber);
		task.setOtherExpenses(3 * taskNumber);

		ProductionTypeDirectory productionType = TestBuildersUtils.getProductionTypeDirectory(id, "Production type" + taskNumber);
		ProductionDirectory production = TestBuildersUtils.getProductionDirectory(id, "fullName" + taskNumber, "shortName" + taskNumber, productionType);
		task.setProduction(production);
		task.setProductionType(productionType);

		task.setDateBegin(DateUtils.addToDate(new Date(), Calendar.DATE, -10 * taskNumber));
		task.setDateEnd(DateUtils.addToDate(new Date(), Calendar.DATE, -1 * taskNumber));

		Client client = TestBuildersUtils.getClient(id, "fullName" + taskNumber, "firstName" + taskNumber, "lastName" + taskNumber, "middleName" + taskNumber, "address" + taskNumber, "email" + taskNumber, "phone" + taskNumber);
		client.setManager(manager);
		task.setClient(client);
		task.setStock(TestBuildersUtils.getStockDirectory(id, "Stock" + taskNumber));
		task.setPrinting(100 * taskNumber);
		task.setPrintingUnit(TestBuildersUtils.getPrintingUnitDirectory(id, "PrintingUnit" + taskNumber));
		task.setChromaticity(TestBuildersUtils.getChromaticityDirectory(id, "Chromaticity" + taskNumber));

		task.setFormat(TestBuildersUtils.getFormatDirectory(id, "format" + taskNumber));
		task.setPrintingFormat("printing format" + taskNumber);

		task.setLaminate(TestBuildersUtils.getLaminateDirectory(id, "Laminate" + taskNumber));
		task.setPaper(TestBuildersUtils.getPaperDirectory(id, "Paper" + taskNumber));
		task.setWares("wares" + taskNumber);
		task.setCringle(TestBuildersUtils.getCringleDirectory(id, "Cringle" + taskNumber));
		boolean taskTrueFalse = (taskNumber % 2) == 0;
		task.setFillet(taskTrueFalse);
		task.setPopup(taskTrueFalse);
		task.setCarving(taskTrueFalse);
		task.setStamping(taskTrueFalse);
		task.setEmbossing(taskTrueFalse);
		task.setBending(taskTrueFalse);
		task.setPlotter(taskTrueFalse);
		task.setAssembly(taskTrueFalse);
		task.setCutting(taskTrueFalse);
		task.setNote("Task description" + taskNumber);
		task.setPackBox(taskTrueFalse);
		task.setPackPellicle(taskTrueFalse);
		task.setPackPaper(taskTrueFalse);
		task.setPackPackage(taskTrueFalse);
		task.setPackNP(taskTrueFalse);
		task.setPackBy("Pack by" + taskNumber);
		task.setNumeration(taskTrueFalse);
		task.setNumerationStart(taskNumber + 1);
		task.setAmount(20 * taskNumber);
		task.setState(TestBuildersUtils.getState(id, "name" + taskNumber, "altname" + taskNumber));
		task.setPayment(TestBuildersUtils.getPayment(id, "name" + taskNumber, "altname" + taskNumber));
		task.setDebtAmount(5 * taskNumber);

		return task;
	}

	private Task saveTaskParts(Task task) {
		NumberBaseDirectory numberBaseDirectory = entityManager.persistAndFlush(task.getNumberBase());
		task.setNumberBase(numberBaseDirectory);
		Manager manager = entityManager.persistAndFlush(task.getManager());
		task.setManager(manager);
		DBFile dbFile = entityManager.persistAndFlush(task.getDbFile());
		task.setDbFile(dbFile);
		Contractor contractor = entityManager.persistAndFlush(task.getContractor());
		task.setContractor(contractor);
		ProductionTypeDirectory productionTypeDirectory = entityManager.persistAndFlush(task.getProductionType());
		task.setProductionType(productionTypeDirectory);
		ProductionDirectory productionDirectory = task.getProduction();
		productionDirectory.setProductionType(productionTypeDirectory);
		productionDirectory = entityManager.persistAndFlush(productionDirectory);
		task.setProduction(productionDirectory);
		Client client = task.getClient();
		client.setManager(manager);
		client = entityManager.persistAndFlush(client);
		task.setClient(client);
		StockDirectory stockDirectory = entityManager.persistAndFlush(task.getStock());
		task.setStock(stockDirectory);
		PrintingUnitDirectory printingUnitDirectory = entityManager.persistAndFlush(task.getPrintingUnit());
		task.setPrintingUnit(printingUnitDirectory);
		ChromaticityDirectory chromaticityDirectory = entityManager.persistAndFlush(task.getChromaticity());
		task.setChromaticity(chromaticityDirectory);
		FormatDirectory formatDirectory = entityManager.persistAndFlush(task.getFormat());
		task.setFormat(formatDirectory);
		LaminateDirectory laminateDirectory = entityManager.persistAndFlush(task.getLaminate());
		task.setLaminate(laminateDirectory);
		PaperDirectory paperDirectory = entityManager.persistAndFlush(task.getPaper());
		task.setPaper(paperDirectory);
		CringleDirectory cringleDirectory = entityManager.persistAndFlush(task.getCringle());
		task.setCringle(cringleDirectory);
		State state = entityManager.persistAndFlush(task.getState());
		task.setState(state);
		Payment payment = entityManager.persistAndFlush(task.getPayment());
		task.setPayment(payment);
		return task;
	}

}