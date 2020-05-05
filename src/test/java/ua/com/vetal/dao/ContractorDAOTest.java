package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.PersonViewFilter;
import ua.com.vetal.repositories.ContractorRepository;
import ua.com.vetal.repositories.ManagerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class ContractorDAOTest {
	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private ContractorRepository contractorRepository;
	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ContractorDAO contractorDAO;

	private Manager manager;

	private Contractor contractor;

	@BeforeEach
	public void beforeEach() {
		contractorRepository.deleteAll();
		managerRepository.deleteAll();
		manager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail"));
		Manager secondManager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName2", "managerLastName2", "managerMiddleName2", "managerEmail2"));

		contractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
				"firstName", "lastName", "middleName", "address",
				"email", "phone", "siteURL");
		contractor.setManager(manager);
		contractor = testEntityManager.persistAndFlush(contractor);

		Contractor secondContractor = TestBuildersUtils.getContractor(null, "secondName", "shortName2",
				"firstName2", "lastName2", "middleName2", "address2",
				"email2", "phone2", "siteURL2");
		secondContractor.setManager(secondManager);
		secondContractor = testEntityManager.persistAndFlush(secondContractor);
	}

	@Test
	void whenFindByFilterData() {
        int allRecords = 2;
        List<Contractor> filteredList = contractorDAO.findByFilterData(null);
        assertTrue(filteredList.isEmpty());
        assertEquals(0, filteredList.size());

        PersonViewFilter filterData = new PersonViewFilter();
        filterData.setCorpName(contractor.getFullName());
        filterData.setManager(contractor.getManager());

        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new PersonViewFilter();
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new PersonViewFilter();
        filterData.setCorpName(contractor.getFullName());
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new PersonViewFilter();
        filterData.setManager(contractor.getManager());
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new PersonViewFilter();
        filterData.setManager(new Manager());
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new PersonViewFilter();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        filterData.setManager(newManager);
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new PersonViewFilter();
        filterData.setCorpName("not exist name");
        filteredList = contractorDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());
    }
}