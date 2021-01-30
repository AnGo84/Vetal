package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ClientViewFilter;
import ua.com.vetal.repositories.ClientRepository;
import ua.com.vetal.repositories.ManagerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class ClientDAOTest {
	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ClientDAO clientDAO;

	private Manager manager;

	private Client client;

	@BeforeEach
	public void beforeEach() {
		clientRepository.deleteAll();
		managerRepository.deleteAll();
		manager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail"));
		Manager secondManager = testEntityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName2", "managerLastName2", "managerMiddleName2", "managerEmail2"));

		client = TestBuildersUtils.getClient(null, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
		client.setManager(manager);
		client = testEntityManager.persistAndFlush(client);

		Client secondClient = TestBuildersUtils.getClient(null, "secondName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2");
		secondClient.setManager(secondManager);
		secondClient = testEntityManager.persistAndFlush(secondClient);
	}

	@Test
	void whenFindByFilterData() {
        int allRecords = 2;
        List<Client> filteredList = clientDAO.findByFilterData(null);
        assertTrue(filteredList.isEmpty());
        assertEquals(0, filteredList.size());

        ClientViewFilter filterData = new ClientViewFilter();
        filterData.setFullName(client.getFullName());
        filterData.setManager(client.getManager());

        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientViewFilter();
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new ClientViewFilter();
        filterData.setFullName(client.getFullName());
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientViewFilter();
        filterData.setManager(client.getManager());
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientViewFilter();
        filterData.setManager(new Manager());
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(allRecords, filteredList.size());

        filterData = new ClientViewFilter();
        Manager newManager = new Manager();
        newManager.setId(1021L);
        filterData.setManager(newManager);
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new ClientViewFilter();
        filterData.setFullName("not exist name");
        filteredList = clientDAO.findByFilterData(filterData);
        assertEquals(0, filteredList.size());
    }
}