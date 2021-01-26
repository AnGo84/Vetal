package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Contractor;
import ua.com.vetal.entity.Manager;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContractorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private ManagerRepository managerRepository;

    private Manager manager;

    private Contractor contractor;

    @BeforeEach
    public void beforeEach() {
        contractorRepository.deleteAll();
        managerRepository.deleteAll();
        manager = entityManager.persistAndFlush(TestBuildersUtils.getManager(null, "managerFirstName", "managerLastName", "managerMiddleName", "managerEmail"));

        contractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
        contractor.setManager(manager);
        contractor = entityManager.persistAndFlush(contractor);
    }

    @Test
    public void whenFindByCorpName_thenReturnObject() {
        // when
        Contractor foundContractor = contractorRepository.findByCorpName(contractor.getCorpName());

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
    public void whenFindByCorpName_thenReturnEmpty() {
        assertNull(contractorRepository.findByCorpName("wrong name"));
    }


    @Test
    public void whenFindByID_thenReturnContractor() {
        // when
        Optional<Contractor> foundContractor = contractorRepository.findById(contractor.getId());
        // then
        assertTrue(foundContractor.isPresent());
        assertEquals(foundContractor.get(), contractor);
    }

    @Test
    public void whenFindByIDNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            contractorRepository.findById(null);
        });
    }

    @Test
    public void whenFindByEmail_thenReturnContractor() {
        // when
        Contractor foundContractor = contractorRepository.findByEmail(contractor.getEmail());
        // then
        assertEquals(foundContractor, contractor);
    }

    //@Test
    public void whenFindByEmailNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            contractorRepository.findByEmail(null);
        });
    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Long wrongId = 123654L;
        Optional<Contractor> foundClient = contractorRepository.findById(wrongId);
        // then
        assertFalse(foundClient.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfContractors() {
        //given
        Contractor newContractor = getSecondContractor();

        entityManager.persistAndFlush(newContractor);
        // when
        List<Contractor> contractors = contractorRepository.findAll();
        // then
        assertNotNull(contractors);
        assertFalse(contractors.isEmpty());
        assertEquals(contractors.size(), 2);

    }

    @Test
    public void it_should_save_Contractor() {
        Contractor newContractor = getSecondContractor();
        newContractor = contractorRepository.save(newContractor);
        Contractor foundContractor = contractorRepository.findById(newContractor.getId()).get();

        // then
        assertNotNull(foundContractor);
        assertNotNull(foundContractor.getId());
        assertEquals(foundContractor.getCorpName(), newContractor.getCorpName());
        assertEquals(foundContractor.getShortName(), newContractor.getShortName());
        assertEquals(foundContractor.getFirstName(), newContractor.getFirstName());
        assertEquals(foundContractor.getLastName(), newContractor.getLastName());
        assertEquals(foundContractor.getMiddleName(), newContractor.getMiddleName());
        assertEquals(foundContractor.getAddress(), newContractor.getAddress());
        assertEquals(foundContractor.getEmail(), newContractor.getEmail());
        assertEquals(foundContractor.getPhone(), newContractor.getPhone());
        assertEquals(foundContractor.getSiteURL(), newContractor.getSiteURL());
        assertNotNull(foundContractor.getManager());
        assertEquals(foundContractor.getManager(), newContractor.getManager());
    }

    @Test
    public void whenSaveContractorWithCorpNameTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSaving",
                "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithCorpNameTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "", "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithCorpNameNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, null, "shortName", "firstName", "lastName", "middleName", "address", "email", "phone", "siteURL");
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithFirstNameTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName", "middleName", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithFirstNameTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "", "lastName", "middleName", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithFirstNameNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", null, "lastName", "middleName", "address", "email", "phone", "siteURL");
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithLastNameTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName",
                "lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName",
                "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithLastNameTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(
                null, "corpName", "shortName", "firstName", "", "middleName",
                "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithLastNameNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(
                null, "corpName", "shortName", "firstName", null, "middleName", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithMiddleNameTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName", "lastName", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "address", "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithAddressTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName",
                "lastName", "middleName",
                "addressWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving",
                "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithAddressTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", "lastName", "middleName", "",
                "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveClientWithAddressNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", null, "middleName", null,
                "email", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithEmailTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName",
                "lastName", "middleName", "address",
                "emailWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving"
                , "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithEmailTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", "lastName", "middleName", "address",
                "", "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithEmailNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", null, "middleName", "address",
                null, "phone", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithPhoneTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName",
                "lastName", "middleName", "address",
                "email", "WithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving"
                , "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithPhoneTooShortLength_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", "lastName", "middleName", "address",
                "email", "", "siteURL");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }


    @Test
    public void whenSaveContractorWithPhoneNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", null, "middleName", "address",
                "email", null, "siteuLR");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithSiteURLTooLong_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName", "firstName",
                "lastName", "middleName", "address",
                "email", "phone"
                , "siteURLWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSavingWithLengthMoreThen255SymbolsIsTooLongForSaving");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithSiteURLNull_thenThrowConstraintViolationException() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName", "shortName",
                "firstName", null, "middleName", "address",
                "email", null, "siteuLR");
        newContractor.setManager(manager);
        assertThrows(ConstraintViolationException.class, () -> {
            contractorRepository.save(newContractor);
        });
    }

    @Test
    public void whenSaveContractorWithManagerNull_thenThrowConstraintViolationException() {
        Contractor newContractor = getSecondContractor();
        newContractor.setManager(null);
        newContractor = contractorRepository.save(newContractor);

        Contractor foundContractor = contractorRepository.findById(newContractor.getId()).get();

        // then
        assertNotNull(foundContractor);
        assertNotNull(foundContractor.getId());
        assertEquals(foundContractor.getCorpName(), newContractor.getCorpName());
        assertEquals(foundContractor.getShortName(), newContractor.getShortName());
        assertEquals(foundContractor.getFirstName(), newContractor.getFirstName());
        assertEquals(foundContractor.getLastName(), newContractor.getLastName());
        assertEquals(foundContractor.getMiddleName(), newContractor.getMiddleName());
        assertEquals(foundContractor.getAddress(), newContractor.getAddress());
        assertEquals(foundContractor.getEmail(), newContractor.getEmail());
        assertEquals(foundContractor.getPhone(), newContractor.getPhone());
        assertEquals(foundContractor.getSiteURL(), newContractor.getSiteURL());
        assertNull(foundContractor.getManager());
    }

    @Test
    public void whenSaveContractorWithNotExistManager_thenOk() {
        Contractor newContractor = getSecondContractor();
        newContractor.setManager(new Manager());
        newContractor = contractorRepository.save(newContractor);
        Contractor foundContractor = contractorRepository.findById(newContractor.getId()).get();
        // then
        assertNotNull(foundContractor);
        assertNotNull(foundContractor.getId());
        assertEquals(foundContractor.getCorpName(), newContractor.getCorpName());
        assertEquals(foundContractor.getShortName(), newContractor.getShortName());
        assertEquals(foundContractor.getFirstName(), newContractor.getFirstName());
        assertEquals(foundContractor.getLastName(), newContractor.getLastName());
        assertEquals(foundContractor.getMiddleName(), newContractor.getMiddleName());
        assertEquals(foundContractor.getAddress(), newContractor.getAddress());
        assertEquals(foundContractor.getEmail(), newContractor.getEmail());
        assertEquals(foundContractor.getPhone(), newContractor.getPhone());
        assertEquals(foundContractor.getSiteURL(), newContractor.getSiteURL());
        assertNotNull(foundContractor.getManager());
    }


    @Test
    public void whenDeleteById_thenOk() {
        //given
        Contractor newContractor = getSecondContractor();
        newContractor.setManager(manager);
        newContractor = entityManager.persistAndFlush(newContractor);
        assertEquals(contractorRepository.findAll().size(), 2);

        // when
        contractorRepository.deleteById(newContractor.getId());
        // then
        assertEquals(contractorRepository.findAll().size(), 1);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            contractorRepository.deleteById(10000000l);
        });
    }

    private Contractor getSecondContractor() {
        Contractor newContractor = TestBuildersUtils.getContractor(null, "corpName2", "shortName2", "firstName2", "lastName2", "middleName2", "address2", "email2", "phone2", "siteURL2");
        newContractor.setManager(manager);
        return newContractor;
    }
}