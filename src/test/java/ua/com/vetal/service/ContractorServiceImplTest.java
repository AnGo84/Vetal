package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.dao.ContractorDAO;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ContragentViewFilter;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.ContractorRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
	@MockBean
	private ContractorDAO mockContractorDAO;
	private Manager manager;
	private Contractor contractor;

	@BeforeEach
	public void beforeEach() {
		manager = TestBuildersUtils.getManager(1l, "firstName", "lastName", "middleName", "email");

		contractor = TestBuildersUtils.getContractor(1l, "corpName",
				"firstName", "lastName", "middleName", "address",
				"email", "phone", "siteURL");
		contractor.setManager(manager);
	}

	@Test
	void whenFindById_thenReturnContractor() {
		when(mockContractorRepository.findById(1L)).thenReturn(Optional.of(contractor));
		Long id = 1l;
		Contractor foundContractor = contractorService.get(id);

		// then
		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
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
		Contractor found = contractorService.get(id);
		assertNull(found);
	}

	@Test
	void whenFindByCorpName_thenReturnContractor() {
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		Contractor foundContractor = contractorService.findByCorpName(contractor.getCorpName());

		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
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
		when(mockContractorRepository.findByEmail(contractor.getEmail())).thenReturn(Arrays.asList(contractor));

		List<Contractor> foundContractorList = contractorService.findByEmail(contractor.getEmail());
		assertNotNull(foundContractorList);
		assertEquals(1, foundContractorList.size());

		Contractor foundContractor = foundContractorList.get(0);
		assertNotNull(foundContractor);
		assertNotNull(foundContractor.getId());
		assertEquals(foundContractor.getCorpName(), contractor.getCorpName());
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
		when(mockContractorRepository.findByEmail(contractor.getEmail())).thenReturn(Arrays.asList(contractor));
		List<Contractor> found = contractorService.findByEmail("wrong email");
		assertNotNull(found);
		assertTrue(found.isEmpty());
	}

	@Test
	void whenFindByCorpName_thenReturnNull() {
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		Contractor found = contractorService.findByCorpName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveContractor_thenSuccess() {
		Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2", "siteURL2");
		newContractor.setManager(manager);
		contractorService.save(newContractor);
		verify(mockContractorRepository, times(1)).save(newContractor);
    }

	@Test
	void whenSaveContractor_thenNPE() {
		when(mockContractorRepository.save(any(Contractor.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			contractorService.save(contractor);
		});
	}

	@Test
	void whenUpdateContractor_thenSuccess() {
		contractor.setCorpName("corpName2");
		contractorService.update(contractor);
		verify(mockContractorRepository, times(1)).save(contractor);
	}

	@Test
	void whenUpdateContractor_thenThrow() {
		when(mockContractorRepository.save(any(Contractor.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			contractorService.update(contractor);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		when(mockContractorRepository.findById(1L)).thenReturn(Optional.of(contractor));
		contractorService.deleteById(1l);
		verify(mockContractorRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockContractorRepository).deleteById(anyLong());
		assertThrows(EntityException.class, () -> {
			contractorService.deleteById(1000000l);
		});
	}

	@Test
	void whenFindAllObjects() {
		when(mockContractorRepository.findAll()).thenReturn(Arrays.asList(contractor));
		List<Contractor> objects = contractorService.getAll();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void whenIsObjectExist() {
		assertFalse(contractorService.isExist(null));

		Contractor existContractor = TestDataUtils.getContractor(2l);
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(existContractor);
		existContractor.setId(null);
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(existContractor);
		assertTrue(contractorService.isExist(contractor));

		when(mockContractorRepository.findByCorpName(anyString())).thenReturn(null);
		assertFalse(contractorService.isExist(contractor));
	}

	@Test
	void whenIsCorpNameExist() {

		contractor.setId(null);
		when(mockContractorRepository.findByCorpName(contractor.getCorpName())).thenReturn(contractor);
		assertTrue(contractorService.isExist(contractor));

		when(mockContractorRepository.findByCorpName(anyString())).thenReturn(null);
		assertFalse(contractorService.isExist(contractor));
	}

	@Test
	void whenFindByFilterData() {
		when(mockContractorDAO.findByFilterData(any(ContragentViewFilter.class))).thenReturn(Arrays.asList(contractor));
		List<Contractor> objects = contractorService.findByFilterData(new ContragentViewFilter());
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);

		when(mockContractorRepository.findAll()).thenReturn(Arrays.asList(contractor));
		objects = contractorService.findByFilterData(null);
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

}