package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.repositories.PrinterRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PrinterServiceImplTest {
	@Autowired
	private PrinterServiceImpl printerService;
	@MockBean
	private PrinterRepository mockPrinterRepository;
	private Printer printer;

	@BeforeEach
	public void beforeEach() {
        printer = TestBuildersUtils.getPrinter(1l, "firstName", "lastName", "middleName", "email");
    }

	@Test
	void whenFindById_thenReturnPrinter() {
		when(mockPrinterRepository.getOne(1L)).thenReturn(printer);
		long id = 1;
		Printer found = printerService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), printer.getId());
		assertEquals(found.getFirstName(), printer.getFirstName());
		assertEquals(found.getLastName(), printer.getLastName());
		assertEquals(found.getMiddleName(), printer.getMiddleName());
		assertEquals(found.getPhone(), printer.getPhone());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockPrinterRepository.getOne(1L)).thenReturn(printer);
		long id = 2;
		Printer found = printerService.findById(id);
		assertNull(found);
	}


	@Test
	void whenFindByName_thenReturnNull() {
		//when(mockManagerRepository.findByName(anyString())).thenReturn(manager);
		Printer found = printerService.findByName(null);
		assertNull(found);
		found = printerService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "email2");
		printerService.saveObject(newPrinter);
		verify(mockPrinterRepository, times(1)).save(newPrinter);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockPrinterRepository.save(any(Printer.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			printerService.saveObject(printer);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		printer.setCorpName("CorpName2");
		printerService.updateObject(printer);
		verify(mockPrinterRepository, times(1)).save(printer);
	}

	@Test
	void whenUpdateObject_thenThrowNPE() {
		when(mockPrinterRepository.save(any(Printer.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			printerService.updateObject(printer);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		printerService.deleteById(1l);
		verify(mockPrinterRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockPrinterRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			printerService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockPrinterRepository.findAll()).thenReturn(Arrays.asList(printer));
		List<Printer> objects = printerService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockPrinterRepository.getOne(printer.getId())).thenReturn(printer);
		assertTrue(printerService.isObjectExist(printer));

		when(mockPrinterRepository.getOne(anyLong())).thenReturn(null);
		assertFalse(printerService.isObjectExist(printer));
	}

}