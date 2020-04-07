package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Payment;
import ua.com.vetal.repositories.PaymentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceImplTest {
	@Autowired
	private PaymentServiceImpl paymentService;
	@MockBean
	private PaymentRepository mockPaymentRepository;
	private Payment payment;

	@BeforeEach
	public void beforeEach() {
		payment = TestDataUtils.getPayment(1L, "name", "altname");
	}

	@Test
	void whenFindById_thenReturnObject() {
		when(mockPaymentRepository.getOne(1L)).thenReturn(payment);
		long id = 1;
		Payment found = paymentService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), payment.getId());
		assertEquals(found.getName(), payment.getName());
		assertEquals(found.getAltName(), payment.getAltName());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockPaymentRepository.getOne(1L)).thenReturn(payment);
		long id = 221121;
		Payment found = paymentService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockPaymentRepository.findByName(payment.getName())).thenReturn(payment);
		Payment found = paymentService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		Payment newPayment = TestDataUtils.getPayment(null, "name2", "altname2");
		paymentService.saveObject(newPayment);
		verify(mockPaymentRepository, times(1)).save(newPayment);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockPaymentRepository.save(any(Payment.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			paymentService.saveObject(payment);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		Payment newPayment = TestDataUtils.getPayment(null, "name2", "altname2");
		paymentService.saveObject(newPayment);
		verify(mockPaymentRepository, times(1)).save(newPayment);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockPaymentRepository.save(any(Payment.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			paymentService.updateObject(payment);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		paymentService.deleteById(1l);
		verify(mockPaymentRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockPaymentRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			paymentService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockPaymentRepository.findAll()).thenReturn(Arrays.asList(payment));
		List<Payment> directoriesList = paymentService.findAllObjects();
		assertNotNull(directoriesList);
		assertFalse(directoriesList.isEmpty());
		assertEquals(directoriesList.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockPaymentRepository.findByName(payment.getName())).thenReturn(payment);
		assertTrue(paymentService.isObjectExist(payment));
		when(mockPaymentRepository.findByName(payment.getName())).thenReturn(payment);
	}
}