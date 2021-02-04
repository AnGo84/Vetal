package ua.com.vetal.repositories;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataServiceUtils;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.Stencil;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StencilRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private StencilRepository stencilRepository;

	private Stencil stencil;

	@BeforeEach
	public void beforeEach() {
		stencilRepository.deleteAll();
		stencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 1), entityManager);
		stencil = entityManager.persistAndFlush(stencil);
	}

	@Test
	public void whenFindByAccount_thenReturnObject() {
		// when
		Stencil foundStencil = stencilRepository.findByAccount(stencil.getAccount());

		// then
		assertNotNull(foundStencil);
		assertNotNull(foundStencil.getId());
		assertEquals(stencil.getId(), foundStencil.getId());
		assertEquals(stencil, foundStencil);

	}

	@Test
	public void whenFindByAccount_thenReturnEmpty() {
		assertNull(stencilRepository.findByAccount("wrong name"));
	}


	@Test
	public void whenFindByID_thenReturnManager() {
		// when
		Optional<Stencil> foundStencil = stencilRepository.findById(stencil.getId());
		// then
		assertTrue(foundStencil.isPresent());
		assertEquals(stencil, foundStencil.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			stencilRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Long wrongId = 123654L;
		Optional<Stencil> foundStencil = stencilRepository.findById(wrongId);
		// then
		assertFalse(foundStencil.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfManagers() {
		//given
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil = entityManager.persistAndFlush(newStencil);

		entityManager.persistAndFlush(newStencil);
		// when
		List<Stencil> Stencils = stencilRepository.findAll();
		// then
		assertNotNull(Stencils);
		assertFalse(Stencils.isEmpty());
		assertEquals(Stencils.size(), 2);

	}

	@Test
	public void it_should_save_Client() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil = stencilRepository.save(newStencil);
		Stencil foundStencil = stencilRepository.findById(newStencil.getId()).get();

		// then
		assertNotNull(foundStencil);
		assertNotNull(foundStencil.getId());
		assertEquals(newStencil, foundStencil);
	}

	@Test
	public void whenSaveStencilWithAccountTooLong_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setAccount("accountWithLengthMoreThen50SymbolsIsTooLongForSaving");

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutManager_thenThrowDataIntegrityViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setManager(null);

		assertThrows(DataIntegrityViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}


	@Test
	public void whenSaveStencilWithOutProduction_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setProduction(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutDateBegin_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setDateBegin(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutDateEnd_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setDateEnd(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutClient_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setClient(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutStock_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setStock(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutPrintingUnit_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setPrintingUnit(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithOutPaperFormat_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setPaper(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}


	@Test
	public void whenSaveStencilWithPrintingNoteTooLong_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setPrintingNote(StringUtils.repeat("noteWithLengthMoreThen1000SymbolsIsTooLongForSaving", 20));

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenSaveStencilWithoutProductionAvailability_thenThrowConstraintViolationException() {
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil.setProductionAvailability(null);

		assertThrows(ConstraintViolationException.class, () -> {
			stencilRepository.save(newStencil);
		});
	}

	@Test
	public void whenDeleteById_thenOk() {
		//given
		Stencil newStencil = TestDataServiceUtils.saveStencilParts(TestDataUtils.getStencil(null, 2), entityManager);
		newStencil = entityManager.persistAndFlush(newStencil);
		assertEquals(stencilRepository.findAll().size(), 2);

		// when
		stencilRepository.deleteById(newStencil.getId());
		// then
		assertEquals(stencilRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			stencilRepository.deleteById(10000000l);
		});
	}

}