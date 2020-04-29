package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Printer;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PrinterRepositoryTest {
	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private PrinterRepository printerRepository;

	private Printer printer;

	@BeforeEach
	public void beforeEach() {
        printerRepository.deleteAll();
        printer = TestBuildersUtils.getPrinter(null, "firstName", "lastName", "middleName", "email");

        printer = testEntityManager.persistAndFlush(printer);
    }

	@Test
	public void whenFindByID_thenReturnPrinter() {
		Optional<Printer> foundUser = printerRepository.findById(printer.getId());
		assertTrue(foundUser.isPresent());
		assertEquals(printer, foundUser.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			printerRepository.findById(null);
		});
	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		Long wrongId = 123654L;
		Optional<Printer> foundUser = printerRepository.findById(wrongId);
		assertFalse(foundUser.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfPrinters() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "email2");
        testEntityManager.persistAndFlush(newPrinter);
        List<Printer> Printers = printerRepository.findAll();
        assertNotNull(Printers);
        assertFalse(Printers.isEmpty());
        assertEquals(Printers.size(), 2);
    }

	@Test
	public void it_should_save_Printer() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "email2");
        newPrinter = printerRepository.save(newPrinter);
        Printer foundPrinter = printerRepository.findById(newPrinter.getId()).get();

        assertNotNull(foundPrinter);
        assertNotNull(foundPrinter.getId());
        assertEquals(foundPrinter.getId(), newPrinter.getId());
        assertEquals(foundPrinter.getFirstName(), newPrinter.getFirstName());
        assertEquals(foundPrinter.getLastName(), newPrinter.getLastName());
        assertEquals(foundPrinter.getMiddleName(), newPrinter.getMiddleName());
        assertEquals(foundPrinter.getEmail(), newPrinter.getEmail());
	}

	@Test
	public void it_should_save_Printer_with_null_fields() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, null, "lastName2", null, "email2");
        newPrinter = printerRepository.save(newPrinter);
        Printer foundPrinter = printerRepository.findById(newPrinter.getId()).get();

        assertNotNull(foundPrinter);
        assertNotNull(foundPrinter.getId());
        assertEquals(foundPrinter.getId(), newPrinter.getId());
        assertEquals(foundPrinter.getFirstName(), newPrinter.getFirstName());
        assertEquals(foundPrinter.getLastName(), newPrinter.getLastName());
        assertEquals(foundPrinter.getMiddleName(), newPrinter.getMiddleName());
        assertEquals(foundPrinter.getEmail(), newPrinter.getEmail());
	}

	@Test
	public void it_should_save_Printer_with_empty_fields() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "", "lastName2", "", "email2");
        newPrinter = printerRepository.save(newPrinter);
        Printer foundPrinter = printerRepository.findById(newPrinter.getId()).get();

        assertNotNull(foundPrinter);
        assertNotNull(foundPrinter.getId());
        assertEquals(foundPrinter.getId(), newPrinter.getId());
        assertEquals(foundPrinter.getFirstName(), newPrinter.getFirstName());
        assertEquals(foundPrinter.getLastName(), newPrinter.getLastName());
        assertEquals(foundPrinter.getMiddleName(), newPrinter.getMiddleName());
        assertEquals(foundPrinter.getEmail(), newPrinter.getEmail());
	}

	@Test
	public void whenSavePrinterWithFirstNameTooLong_thenThrowConstraintViolationException() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "FirstNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "lastName2", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            printerRepository.save(newPrinter);
        });
    }

	@Test
	public void whenSavePrinterWithLastNameTooLong_thenThrowConstraintViolationException() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "FirstName2", "lastNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            printerRepository.save(newPrinter);
        });
    }

	@Test
	public void whenSavePrinterWithLastNameTooShortLength_thenThrowConstraintViolationException() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "", "middleName2", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            printerRepository.save(newPrinter);
        });
    }

	@Test
	public void whenSavePrinterWithMiddleNameTooLong_thenThrowConstraintViolationException() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "FirstName", "lastName2", "middleNameWithLengthMoreThen50SymbolsIsTooLongForSaving", "email2");
        assertThrows(ConstraintViolationException.class, () -> {
            printerRepository.save(newPrinter);
        });
    }

	@Test
	public void whenSavePrinterWithEmailWrongLength_thenThrowConstraintViolationException() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "EmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100SymbolsEmailLengthMoreThen100Symbols");
        assertThrows(ConstraintViolationException.class, () -> {
            printerRepository.save(newPrinter);
        });
    }

	@Test
	public void whenDeleteById_thenOk() {
        Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "email2");
        newPrinter = testEntityManager.persistAndFlush(newPrinter);
        assertEquals(printerRepository.findAll().size(), 2);

        printerRepository.deleteById(newPrinter.getId());

        assertEquals(printerRepository.findAll().size(), 1);
    }

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			printerRepository.deleteById(10000000l);
		});
	}
}