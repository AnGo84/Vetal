package ua.com.vetal.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Manager;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ManagerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ManagerRepository managerRepository;

    private Manager manager;

    @BeforeEach
    public void beforeEach() {
        managerRepository.deleteAll();
        manager = TestDataUtils.getManager("firstName", "lastName", "middleName", "email");

        manager = entityManager.persistAndFlush(manager);

    }

    @AfterEach
    public void afterEach() {

    }

    /*private Manager getFirst(){
        List<Manager> managers = managerRepository.findAll();
        return (managers!=null && !managers.isEmpty())? managers.get(0):null;
    }*/

    /*@Test
    public void whenFindByName_thenReturnUser() {
        // when
        Manager foundManager = getFirst();

        // then
        assertNotNull(foundManager);
        assertNotNull(foundManager.getId());
        assertEquals(foundManager.getFirstName(), manager.getFirstName());
        assertEquals(foundManager.getLastName(), manager.getLastName());
        assertEquals(foundManager.getMiddleName(), manager.getMiddleName());
        assertEquals(foundManager.getPhone(), manager.getPhone());
    }

    @Test
    public void whenFindByUserName_thenReturnEmpty() {
        assertNull(managerRepository.findByName("wrong name"));
    }*/

    @Test
    public void whenFindByID_thenReturnManager() {

        //User user = userRepository.findByName("User");
        // when
        Optional<Manager> foundUser = managerRepository.findById(manager.getId());
        // then
        assertTrue(foundUser.isPresent());
        assertEquals(manager, foundUser.get());
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            managerRepository.findById(null);
        });

    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Long wrongId = 123654L;
        Optional<Manager> foundUser = managerRepository.findById(wrongId);
        // then
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfManagers() {
        //given
        Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "middleName2", "email2");
        entityManager.persistAndFlush(newManager);
        // when
        List<Manager> managers = managerRepository.findAll();
        // then
        assertNotNull(managers);
        assertFalse(managers.isEmpty());
        assertEquals(managers.size(), 2);

    }

    @Test
    public void it_should_save_manager() {
        Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "middleName2", "email2");
        newManager = managerRepository.save(newManager);
        Manager foundManager = managerRepository.findById(newManager.getId()).get();

        // then
        assertNotNull(foundManager);
        assertNotNull(foundManager.getId());
        assertEquals(foundManager.getId(), newManager.getId());
        assertEquals(foundManager.getFirstName(), newManager.getFirstName());
        assertEquals(foundManager.getLastName(), newManager.getLastName());
        assertEquals(foundManager.getMiddleName(), newManager.getMiddleName());
        assertEquals(foundManager.getEmail(), newManager.getEmail());
    }

    @Test
    public void it_should_save_manager_with_null_fields() {
        Manager newManager = TestDataUtils.getManager(null, "lastName2", null, "email2");
        newManager = managerRepository.save(newManager);
        Manager foundManager = managerRepository.findById(newManager.getId()).get();

        // then
        assertNotNull(foundManager);
        assertNotNull(foundManager.getId());
        assertEquals(foundManager.getId(), newManager.getId());
        assertEquals(foundManager.getFirstName(), newManager.getFirstName());
        assertEquals(foundManager.getLastName(), newManager.getLastName());
        assertEquals(foundManager.getMiddleName(), newManager.getMiddleName());
        assertEquals(foundManager.getEmail(), newManager.getEmail());
    }

    @Test
    public void it_should_save_manager_with_empty_fields() {
        Manager newManager = TestDataUtils.getManager("", "lastName2", "", "email2");
        newManager = managerRepository.save(newManager);
        Manager foundManager = managerRepository.findById(newManager.getId()).get();

        // then
        assertNotNull(foundManager);
        assertNotNull(foundManager.getId());
        assertEquals(foundManager.getId(), newManager.getId());
        assertEquals(foundManager.getFirstName(), newManager.getFirstName());
        assertEquals(foundManager.getLastName(), newManager.getLastName());
        assertEquals(foundManager.getMiddleName(), newManager.getMiddleName());
        assertEquals(foundManager.getEmail(), newManager.getEmail());
    }

    @Test
    public void whenSaveManagerWithFirstNameTooLong_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("FirstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName2", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }

    /*@Test
    public void whenSaveManagerWithFirstNameTooShortLength_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("", "lastName2", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }*/

    @Test
    public void whenSaveManagerWithLastNameTooLong_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("FirstName2", "lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }

    @Test
    public void whenSaveManagerWithLastNameTooShortLength_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("firstName2", "", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }

    @Test
    public void whenSaveManagerWithMiddleNameTooLong_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("FirstName", "lastName2", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }

    /*@Test
    public void whenSaveManagerWithMiddleNameTooShortLength_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }*/


    @Test
    public void whenSaveManagerWithEmailWrongLength_thenThrowConstraintViolationException() {
        Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "middleName2", "EmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100Symbols");
        assertThrows(ConstraintViolationException.class, () -> {
            managerRepository.save(newManager);
        });
    }

    @Test
    public void whenDeleteById_thenOk() {
        //given
        Manager newManager = TestDataUtils.getManager("firstName2", "lastName2", "middleName2", "email2");
        newManager = entityManager.persistAndFlush(newManager);
        assertEquals(managerRepository.findAll().size(), 2);

        // when
        managerRepository.deleteById(newManager.getId());
        // then
        assertEquals(managerRepository.findAll().size(), 1);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            managerRepository.deleteById(10000000l);
        });
    }
}