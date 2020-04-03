package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;
import ua.com.vetal.entity.filter.ClientFilter;
import ua.com.vetal.repositories.ClientRepository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClientServiceImplTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ClientServiceImpl clientService;
    @MockBean
    private ClientRepository mockClientRepository;
    private Manager manager;
    private Client client;

    @BeforeEach
    public void beforeEach() {
        manager = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");
        manager.setId(1l);

        client = TestDataUtils.getClient(1l, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        client.setManager(manager);
    }

    @Test
    void whenFindById_thenReturnClient() {
        when(mockClientRepository.getOne(1L)).thenReturn(client);
        long id = 1;
        Client foundClient = clientService.findById(id);

        // then
        assertNotNull(foundClient);
        assertNotNull(foundClient.getId());
        assertEquals(foundClient.getFullName(), client.getFullName());
        assertEquals(foundClient.getFirstName(), client.getFirstName());
        assertEquals(foundClient.getLastName(), client.getLastName());
        assertEquals(foundClient.getMiddleName(), client.getMiddleName());
        assertEquals(foundClient.getAddress(), client.getAddress());
        assertEquals(foundClient.getEmail(), client.getEmail());
        assertEquals(foundClient.getPhone(), client.getPhone());
        assertNotNull(foundClient.getManager());
        assertEquals(foundClient.getManager(), client.getManager());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockClientRepository.getOne(1L)).thenReturn(client);
        long id = 2;
        Client found = clientService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnClient() {
        when(mockClientRepository.findByFullName(client.getFullName())).thenReturn(client);
        Client foundClient = clientService.findByName(client.getFullName());

        assertNotNull(foundClient);
        assertNotNull(foundClient.getId());
        assertEquals(foundClient.getFullName(), client.getFullName());
        assertEquals(foundClient.getFirstName(), client.getFirstName());
        assertEquals(foundClient.getLastName(), client.getLastName());
        assertEquals(foundClient.getMiddleName(), client.getMiddleName());
        assertEquals(foundClient.getAddress(), client.getAddress());
        assertEquals(foundClient.getEmail(), client.getEmail());
        assertEquals(foundClient.getPhone(), client.getPhone());
        assertNotNull(foundClient.getManager());
        assertEquals(foundClient.getManager(), client.getManager());
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockClientRepository.findByFullName(client.getFullName())).thenReturn(client);
        Client found = clientService.findByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenSaveClient_thenSuccess() {
        Client newClient = TestDataUtils.getClient(null, "fullName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2");
        newClient.setManager(manager);
        clientService.saveObject(newClient);
        verify(mockClientRepository, times(1)).save(newClient);
    }

    @Test
    void whenSaveClient_thenNPE() {
        when(mockClientRepository.save(any(Client.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            clientService.saveObject(client);
        });
    }

    @Test
    void whenUpdateClient_thenSuccess() {
        Client newClient = TestDataUtils.getClient(null, "fullName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2");
        newClient.setManager(manager);
        clientService.saveObject(newClient);
        verify(mockClientRepository, times(1)).save(newClient);
    }

    @Test
    void whenUpdateClient_thenThrow() {
        when(mockClientRepository.save(any(Client.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            clientService.updateObject(client);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        clientService.deleteById(1l);
        verify(mockClientRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockClientRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            clientService.deleteById(1000000l);
        });
    }

    @Test
    void whenFindAllObjects() {
        when(mockClientRepository.findAll()).thenReturn(Arrays.asList(client));
        List<Client> objects = clientService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void whenIsObjectExist() {
        when(mockClientRepository.getOne(client.getId())).thenReturn(client);
        assertTrue(clientService.isObjectExist(client));
        when(mockClientRepository.getOne(anyLong())).thenReturn(null);
        assertFalse(clientService.isObjectExist(client));
    }

    @Test
    void whenIsFullNameExist() {
        when(mockClientRepository.findByFullName(client.getFullName())).thenReturn(client);
        assertTrue(clientService.isFullNameExist(client));

        when(mockClientRepository.findByFullName(anyString())).thenReturn(null);
        assertFalse(clientService.isFullNameExist(client));
    }

    @Disabled("Disabled until refactoring filters")
    @Test
    void whenFindByFilterData() {
        List<Client> filteredList = clientService.findByFilterData(null);
        assertEquals(filteredList.size(), 1);

        ClientFilter filterData = new ClientFilter();
        filterData.setFullName(client.getFullName());
        filterData.setManager(client.getManager());

        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientFilter();
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new ClientFilter();
        filterData.setFullName(client.getFullName());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientFilter();
        filterData.setManager(client.getManager());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(1, filteredList.size());

        filterData = new ClientFilter();
        filterData.setManager(new Manager());
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());

        filterData = new ClientFilter();
        filterData.setFullName("not exist name");
        filteredList = clientService.findByFilterData(filterData);
        assertEquals(0, filteredList.size());
    }

}