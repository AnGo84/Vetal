package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Payment;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PaymentRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private PaymentRepository paymentRepository;
	private Payment payment;

	@BeforeEach
	public void beforeEach() {
        paymentRepository.deleteAll();
        payment = TestBuildersUtils.getPayment(null, "name", "altname");
        entityManager.persistAndFlush(payment);
    }

	@Test
	public void whenFindByName_thenReturnObject() {
		// when
		Payment foundPayment = paymentRepository.findByName(payment.getName());

		// then
		assertNotNull(foundPayment);
		assertNotNull(foundPayment.getId());
		assertEquals(foundPayment.getName(), payment.getName());
		assertEquals(foundPayment.getAltName(), payment.getAltName());
	}

	@Test
	public void whenFindByName_thenReturnEmpty() {
		assertNull(paymentRepository.findByName("wrong name"));
	}

	@Test
	public void whenFindByID_thenReturnObject() {
		// when
		Payment payment = paymentRepository.findByName(this.payment.getName());
		Optional<Payment> foundPayment = paymentRepository.findById(payment.getId());
		// then
		assertTrue(foundPayment.isPresent());
		assertEquals(foundPayment.get(), payment);
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			paymentRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Optional<Payment> foundPayment = paymentRepository.findById(1123l);
		// then
		assertFalse(foundPayment.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfRecords() {
        //given
        Payment newPayment = TestBuildersUtils.getPayment(null, "name2", "altname2");
        entityManager.persistAndFlush(newPayment);
        // when
        List<Payment> payments = paymentRepository.findAll();
        // then
        assertNotNull(payments);
        assertFalse(payments.isEmpty());
        assertEquals(payments.size(), 2);
    }

	@Test
	public void it_should_save_object() {
        Payment newPayment = TestBuildersUtils.getPayment(null, "name2", "altname2");
        newPayment = paymentRepository.save(newPayment);
        Payment foundPayment = paymentRepository.findByName(newPayment.getName());
        // then
        assertNotNull(foundPayment);
        assertNotNull(foundPayment.getId());
        assertEquals(foundPayment.getId(), newPayment.getId());
        assertEquals(foundPayment.getName(), newPayment.getName());
        assertEquals(foundPayment.getAltName(), newPayment.getAltName());
    }

	@Test
	public void it_should_save_object_with_empty_fields() {
        Payment newPayment = TestBuildersUtils.getPayment(null, "name2", "");
        newPayment = paymentRepository.save(newPayment);
        Payment foundPayment = paymentRepository.findByName(newPayment.getName());
        // then
        assertNotNull(foundPayment);
        assertNotNull(foundPayment.getId());
        assertEquals(foundPayment.getId(), newPayment.getId());
        assertEquals(foundPayment.getName(), newPayment.getName());
        assertEquals(foundPayment.getAltName(), newPayment.getAltName());
    }

	@Test
	public void it_should_save_object_with_null_fields() {
        Payment newPayment = TestBuildersUtils.getPayment(null, "name2", null);
        newPayment = paymentRepository.save(newPayment);
        Payment foundPayment = paymentRepository.findByName(newPayment.getName());
        // then
        assertNotNull(foundPayment);
        assertNotNull(foundPayment.getId());
        assertEquals(foundPayment.getId(), newPayment.getId());
        assertEquals(foundPayment.getName(), newPayment.getName());
        assertEquals(foundPayment.getAltName(), newPayment.getAltName());
    }

	@Test
	public void whenSaveObjectWithNameTooLong_thenThrowDataIntegrityViolationException() {

        Payment newPayment = TestBuildersUtils.getPayment(null, "nameWithLengthMoreThen30SymbolsIsTooLongForSaving", "altname2");
        assertThrows(DataIntegrityViolationException.class, () -> {
            paymentRepository.save(newPayment);
        });
    }

	@Test
	public void whenSaveObjectWithNameTooShort_thenThrowConstraintViolationException() {
        Payment newPayment = TestBuildersUtils.getPayment(null, "", "altname2");
        assertThrows(ConstraintViolationException.class, () -> {
            paymentRepository.save(newPayment);
        });
    }


	@Test
	public void whenSaveObjectWithAltNameTooLong_thenThrowDataIntegrityViolationException() {
        Payment newPayment = TestBuildersUtils.getPayment(null, "name", "altNameWithLengthMoreThen30SymbolsIsTooLongForSaving");
        assertThrows(DataIntegrityViolationException.class, () -> {
            paymentRepository.save(newPayment);
        });
    }

	@Test
	public void whenDeleteById_thenOk() {
        //given
        Payment newPayment = TestBuildersUtils.getPayment(null, "name2", "altname2");

        entityManager.persistAndFlush(newPayment);
        assertEquals(paymentRepository.findAll().size(), 2);

        Payment foundPayment = paymentRepository.findByName(newPayment.getName());

        // when
        paymentRepository.deleteById(foundPayment.getId());
        // then
        assertEquals(paymentRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			paymentRepository.deleteById(10000000l);
		});
	}

}