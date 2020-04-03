package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.PersonFilter;
import ua.com.vetal.repositories.ContractorRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContractorServiceImplTest {
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ContractorServiceImpl contractorService;
	@MockBean
	private ContractorRepository mockContractorRepository;
	private Manager manager;
	private Contractor contractor;

	@BeforeEach
	public void beforeEach() {
		manager = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");
		manager.setId(1l);

		contractor = TestDataUtils.getContractor(1l, "corpName", "shortName",
				"firstName", "lastName", "middleName", "address",
				"email", "phone", "siteURL");
		contractor.setManager(manager);
	}

	@Test
	void whenFindById_thenReturnContractor() {
		when(mockContractorRepository.getOne(1L)).thenReturn(contractor);
		long id = 1;
		Contractor foundContractor = contractorService.findById(id);

		// then
		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
		assertEquals(foundContractor.getShortName(), contractor.getShortName());
		assertEquals(foundContractor.getFirstName(), contractor.getFirstName());
		assertEquals(foundContractor.getLastName(), contractor.getLastName());
		assertEquals(foundContractor.getMiddleName(), contractor.getMiddleName());
		assertEquals(foundContractor.getAddress(), contractor.getAddress());
		assertEquals(foundContractor.getEmail(), contractor.getEmail());
		assertEquals(foundContractor.getPhone(), contractor.getPhone());
		assertEquals(foundContractor.getSiteURL(), contractor.getSiteURL());
		assertNotNull(foundContractor.getManager());
		assertEquals(foundContractor.getManager(), contractor.getManager());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockContractorRepository.getOne(1L)).thenReturn(contractor);
		long id = 2;
		Contractor found = contractorService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByCorpName_thenReturnContractor() {
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		Contractor foundContractor = contractorService.findByName(contractor.getCorpName());

		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
		assertEquals(foundContractor.getShortName(), contractor.getShortName());
		assertEquals(foundContractor.getFirstName(), contractor.getFirstName());
		assertEquals(foundContractor.getLastName(), contractor.getLastName());
		assertEquals(foundContractor.getMiddleName(), contractor.getMiddleName());
		assertEquals(foundContractor.getAddress(), contractor.getAddress());
		assertEquals(foundContractor.getEmail(), contractor.getEmail());
		assertEquals(foundContractor.getPhone(), contractor.getPhone());
		assertEquals(foundContractor.getSiteURL(), contractor.getSiteURL());
		assertNotNull(foundContractor.getManager());
		assertEquals(foundContractor.getManager(), contractor.getManager());
	}

	@Test
	void whenFindByEmail_thenReturnContractor() {
		when(mockContractorRepository.findByEmail(contractor.getEmail())).thenReturn(contractor);
		Contractor foundContractor = contractorService.findByEmail(contractor.getEmail());

		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
		assertEquals(foundContractor.getShortName(), contractor.getShortName());
		assertEquals(foundContractor.getFirstName(), contractor.getFirstName());
		assertEquals(foundContractor.getLastName(), contractor.getLastName());
		assertEquals(foundContractor.getMiddleName(), contractor.getMiddleName());
		assertEquals(foundContractor.getAddress(), contractor.getAddress());
		assertEquals(foundContractor.getEmail(), contractor.getEmail());
		assertEquals(foundContractor.getPhone(), contractor.getPhone());
		assertEquals(foundContractor.getSiteURL(), contractor.getSiteURL());
		assertNotNull(foundContractor.getManager());
		assertEquals(foundContractor.getManager(), contractor.getManager());
	}


	@Test
	void whenFindByEmail_thenReturnNull() {
		when(mockContractorRepository.findByEmail(contractor.getEmail())).thenReturn(contractor);
		Contractor found = contractorService.findByEmail("wrong email");
		assertNull(found);
	}

	@Test
	void whenFindByCorpName_thenReturnNull() {
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		Contractor found = contractorService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveContractor_thenSuccess() {
		Contractor newContractor = TestDataUtils.getContractor(null, "corpName2", "shortName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2", "siteURL2");
		newContractor.setManager(manager);
		contractorService.saveObject(newContractor);
		verify(mockContractorRepository, times(1)).save(newContractor);
	}

	@Test
	void whenSaveContractor_thenNPE() {
		when(mockContractorRepository.save(any(Contractor.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			contractorService.saveObject(contractor);
		});
	}

	@Test
	void whenUpdateContractor_thenSuccess() {
		Contractor newContractor = TestDataUtils.getContractor(null, "corpName2", "shortName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2", "siteURL2");
		newContractor.setManager(manager);
		contractorService.saveObject(newContractor);
		verify(mockContractorRepository, times(1)).save(newContractor);
	}

	@Test
	void whenUpdateContractor_thenThrow() {
		when(mockContractorRepository.save(any(Contractor.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			contractorService.updateObject(contractor);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		contractorService.deleteById(1l);
		verify(mockContractorRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockContractorRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			contractorService.deleteById(1000000l);
		});
	}

	@Test
	void whenFindAllObjects() {
		when(mockContractorRepository.findAll()).thenReturn(Arrays.asList(contractor));
		List<Contractor> objects = contractorService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void whenIsObjectExist() {
		when(mockContractorRepository.getOne(contractor.getId())).thenReturn(contractor);
		assertTrue(contractorService.isObjectExist(contractor));
		when(mockContractorRepository.getOne(anyLong())).thenReturn(null);
		assertFalse(contractorService.isObjectExist(contractor));
	}

	@Test
	void whenIsCorpNameExist() {
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		assertTrue(contractorService.isCorpNameExist(contractor));

		when(mockContractorRepository.findByCorpName(anyString())).thenReturn(null);
		assertFalse(contractorService.isCorpNameExist(contractor));
	}

	@Disabled("Disabled until refactoring filters")
	@Test
	void whenFindByFilterData() {
		List<Contractor> filteredList = contractorService.findByFilterData(null);
		assertEquals(filteredList.size(), 1);

		PersonFilter filterData = new PersonFilter();
		filterData.setCorpName(contractor.getCorpName());
		filterData.setManager(contractor.getManager());

		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new PersonFilter();
		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());

		filterData = new PersonFilter();
		filterData.setCorpName(contractor.getCorpName());
		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new PersonFilter();
		filterData.setManager(contractor.getManager());
		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new PersonFilter();
		filterData.setManager(new Manager());
		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());

		filterData = new PersonFilter();
		filterData.setCorpName("not exist name");
		filteredList = contractorService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());
	}

}