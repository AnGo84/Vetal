package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Printer;
import ua.com.vetal.exception.EntityException;
import ua.com.vetal.repositories.PrinterRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		printer = TestBuildersUtils.getPrinter(null, "firstName", "lastName", "middleName", "email");
    }

	@Test
	void whenFindById_thenReturnPrinter() {
		//when(mockPrinterRepository.getOne(1L)).thenReturn(printer);
		when(mockPrinterRepository.findById(1L)).thenReturn(Optional.of(printer));
		long id = 1;
		Printer found = printerService.get(id);

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
		Printer found = printerService.get(id);
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Printer newPrinter = TestBuildersUtils.getPrinter(null, "firstName2", "lastName2", "middleName2", "email2");
		printerService.save(newPrinter);
		verify(mockPrinterRepository, times(1)).save(newPrinter);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockPrinterRepository.save(any(Printer.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			printerService.save(printer);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		printer.setFirstName("CorpName2");
		printerService.update(printer);
		verify(mockPrinterRepository, times(1)).save(printer);
	}

	@Test
	void whenUpdateObject_thenThrowNPE() {
		when(mockPrinterRepository.save(any(Printer.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			printerService.update(printer);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		printer.setId(1L);
		when(mockPrinterRepository.findById(1L)).thenReturn(Optional.of(printer));
		printerService.deleteById(1l);
		verify(mockPrinterRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockPrinterRepository).deleteById(anyLong());
		assertThrows(EntityException.class, () -> {
			printerService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockPrinterRepository.findAll()).thenReturn(Arrays.asList(printer));
		List<Printer> objects = printerService.getAll();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void isObjectExist() {
		assertFalse(printerService.isExist(null));

		printer.setId(1L);
		when(mockPrinterRepository.findById(1L)).thenReturn(Optional.of(printer));
		assertTrue(printerService.isExist(printer));

		when(mockPrinterRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertFalse(printerService.isExist(printer));
	}

}