package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.ProductionTypeDirectory;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductionDirectoryRepositoryTest {
	@Autowired
	private TestEntityManager testEntityManager;
	@Autowired
	private ProductionDirectoryRepository productionDirectoryRepository;
	@Autowired
	private ProductionTypeDirectoryRepository productionTypeDirectoryRepository;

	private ProductionTypeDirectory productionType;

	private ProductionDirectory production;

	@BeforeEach
	public void beforeEach() {
		productionDirectoryRepository.deleteAll();
		productionTypeDirectoryRepository.deleteAll();
		productionType = testEntityManager.persistAndFlush(TestDataUtils.getProductionTypeDirectory("Production type"));

		production = TestDataUtils.getProductionDirectory(null, "fullName", "shortName", productionType);
		production = testEntityManager.persistAndFlush(production);
	}

	@Test
	public void whenFindByFullName_thenReturnObject() {
		ProductionDirectory foundProduction = productionDirectoryRepository.findByFullName(production.getFullName());

		assertNotNull(foundProduction);
		assertNotNull(foundProduction.getId());
		assertEquals(foundProduction.getFullName(), production.getFullName());
		assertEquals(foundProduction.getShortName(), production.getShortName());
		assertNotNull(foundProduction.getProductionType());
		assertEquals(foundProduction.getProductionType(), production.getProductionType());
	}

	@Test
	public void whenFindByFullName_thenReturnEmpty() {
		assertNull(productionDirectoryRepository.findByFullName("wrong name"));
	}


	@Test
	public void whenFindByID_thenReturnProductionType() {
		Optional<ProductionDirectory> foundProduction = productionDirectoryRepository.findById(production.getId());

		assertTrue(foundProduction.isPresent());
		assertEquals(foundProduction.get(), production);
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			productionDirectoryRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		Long wrongId = 123654L;
		Optional<ProductionDirectory> foundProduction = productionDirectoryRepository.findById(wrongId);

		assertFalse(foundProduction.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfProductionTypes() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortName2", productionType);

		testEntityManager.persistAndFlush(newProduction);

		List<ProductionDirectory> Productions = productionDirectoryRepository.findAll();

		assertNotNull(Productions);
		assertFalse(Productions.isEmpty());
		assertEquals(Productions.size(), 2);

	}

	@Test
	public void it_should_save_Production() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortName2", productionType);
		newProduction = productionDirectoryRepository.save(newProduction);
		ProductionDirectory foundProduction = productionDirectoryRepository.findById(newProduction.getId()).get();

		// then
		assertNotNull(foundProduction);
		assertNotNull(foundProduction.getId());
		assertEquals(foundProduction.getFullName(), newProduction.getFullName());
		assertEquals(foundProduction.getFullName(), newProduction.getFullName());
		assertEquals(foundProduction.getShortName(), newProduction.getShortName());
		assertNotNull(foundProduction.getProductionType());
		assertEquals(foundProduction.getProductionType(), newProduction.getProductionType());
	}

	@Test
	public void whenSaveProductionWithFullNameTooLong_thenThrowDataIntegrityViolationExceptionn() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSavingfullNameWithLengthMoreThen250SymbolsIsTooLongForSaving",
				"shortName2", productionType);
		assertThrows(DataIntegrityViolationException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}

	@Test
	public void whenSaveProductionWithFullNameTooShortLength_thenThrowConstraintViolationException() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "", "shortName2", productionType);
		assertThrows(ConstraintViolationException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}

	@Test
	public void whenSaveProductionWithFullNameNull_thenThrowConstraintViolationException() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, null, "firstName2", productionType);
		assertThrows(ConstraintViolationException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}

	@Test
	public void whenSaveProductionWithShortNameTooLong_thenThrowDataIntegrityViolationException() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortNameWithLengthMoreThen50SymbolsIsTooLongForSaving", productionType);
		assertThrows(DataIntegrityViolationException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}


	@Test
	public void whenSaveProductionWithProductionTypeNull_thenThrowDataIntegrityViolationException() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortName2", null);
		assertThrows(DataIntegrityViolationException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}

	@Test
	public void whenSaveProductionWithNotExistProductionType_thenThrowInvalidDataAccessApiUsageException() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortName2", new ProductionTypeDirectory());

		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			productionDirectoryRepository.save(newProduction);
		});
	}


	@Test
	public void whenDeleteById_thenOk() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(null, "fullName2", "shortName2", productionType);
		newProduction = testEntityManager.persistAndFlush(newProduction);
		assertEquals(productionDirectoryRepository.findAll().size(), 2);

		productionDirectoryRepository.deleteById(newProduction.getId());

		assertEquals(productionDirectoryRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			productionDirectoryRepository.deleteById(10000000l);
		});
	}

}