package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Client;
import ua.com.vetal.entity.Manager;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ManagerRepository managerRepository;

    private Manager manager;

    private Client client;

    @BeforeEach
    public void beforeEach() {
        clientRepository.deleteAll();
        managerRepository.deleteAll();
        manager = entityManager.persistAndFlush(TestDataUtils.getManager("managerFirstName", "managerLastName", "managerMiddleName", "managerEmail"));

        client = TestDataUtils.getClient(null, "fullName", "firstName", "lastName", "middleName", "address", "email", "phone");
        client.setManager(manager);
        client = entityManager.persistAndFlush(client);
    }

    @Test
    public void whenFindByFullName_thenReturnObject() {
        // when
        Client foundClient = clientRepository.findByFullName(client.getFullName());

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
    public void whenFindByFullName_thenReturnEmpty() {
        assertNull(clientRepository.findByFullName("wrong name"));
    }


    @Test
    public void whenFindByID_thenReturnManager() {

        //User user = userRepository.findByName("User");
        // when
        Optional<Client> foundClient = clientRepository.findById(client.getId());
        // then
        assertTrue(foundClient.isPresent());
        assertEquals(foundClient.get(), client);
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            clientRepository.findById(null);
        });

    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Long wrongId = 123654L;
        Optional<Client> foundClient = clientRepository.findById(wrongId);
        // then
        assertFalse(foundClient.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        //given
        Client newClient = getSecondClient();

        entityManager.persistAndFlush(newClient);
        // when
        List<Client> clients = clientRepository.findAll();
        // then
        assertNotNull(clients);
        assertFalse(clients.isEmpty());
        assertEquals(clients.size(), 2);

    }

    @Test
    public void it_should_save_Client() {
        Client newClient = getSecondClient();
        newClient = clientRepository.save(newClient);
        Client foundClient = clientRepository.findById(newClient.getId()).get();

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
        assertEquals(foundClient.getManager(), newClient.getManager());
    }

    @Test
    public void whenSaveClientWithFullNameTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSaving",
                "firstName", "lastName", "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithFullNameTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "", "firstName", "lastName", "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithFullNameNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, null, "firstName", "lastName", "middleName", "address", "email", "phone");
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithFirstNameTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName", "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithFirstNameTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "", "lastName", "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithFirstNameNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", null, "lastName", "middleName", "address", "email", "phone");
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithLastNameTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName",
                "lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName",
                "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithLastNameTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName", "", "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithLastNameNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName", null, "middleName", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithMiddleNameTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName", "lastName", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "address", "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithAddressTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName",
                "lastName", "middleName",
                "addressWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving",
                "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithAddressTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", "lastName", "middleName", "",
                "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithAddressNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", null, "middleName", null,
                "email", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithEmailTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName",
                "lastName", "middleName", "address",
                "emailWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving"
                , "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithEmailTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", "lastName", "middleName", "address",
                "", "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithEmailNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", null, "middleName", "address",
                null, "phone");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithPhoneTooLong_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName", "firstName",
                "lastName", "middleName", "address",
                "email", "WithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithPhoneTooShortLength_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", "lastName", "middleName", "address",
                "email", "");
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }


    @Test
    public void whenSaveClientWithPhoneNull_thenThrowConstraintViolationException() {
        Client newClient = TestDataUtils.getClient(null, "fullName",
                "firstName", null, "middleName", "address",
                "email", null);
        newClient.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            clientRepository.save(newClient);
        });
    }

    @Test
    public void whenSaveClientWithManagerNull_thenThrowConstraintViolationException() {
        Client newClient = getSecondClient();
        newClient.setManager(null);
        newClient = clientRepository.save(newClient);

        Client foundClient = clientRepository.findById(newClient.getId()).get();

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
        newClient = clientRepository.save(newClient);
        Client foundClient = clientRepository.findById(newClient.getId()).get();
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


    @Test
    public void whenDeleteById_thenOk() {
        //given
        Client newClient = getSecondClient();
        newClient.setManager(manager);
        newClient = entityManager.persistAndFlush(newClient);
        assertEquals(clientRepository.findAll().size(), 2);

        // when
        clientRepository.deleteById(newClient.getId());
        // then
        assertEquals(clientRepository.findAll().size(), 1);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            clientRepository.deleteById(10000000l);
        });
    }


    private Client getSecondClient() {
        Client newClient = TestDataUtils.getClient(null, "fullName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2");
        newClient.setManager(manager);
        return newClient;
    }
}