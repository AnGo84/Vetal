package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.CringleDirectory;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CringleDirectoryRepositoryTest {
	public static final String SECOND_DIRECTORY_NAME = "Second Cringle Directory";
	public static final String NAME_WITH_LENGTH_MORE_THEN_250_SYMBOLS = "NameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSavingNameWithLengthMoreThen250SymbolsIsTooLongForSaving";
	public static final String DIRECTORY_NAME = "CringleDirectory";

	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private CringleDirectoryRepository directoryRepository;
	private CringleDirectory directory;

	@BeforeEach
	public void beforeEach() {
        directoryRepository.deleteAll();
        directory = TestBuildersUtils.getCringleDirectory(null, DIRECTORY_NAME);

        entityManager.persistAndFlush(directory);
    }

	@Test
	public void whenFindByName_thenReturnObject() {
		// when
		CringleDirectory foundDirectory = directoryRepository.findByName(directory.getName());

		// then
		assertNotNull(foundDirectory);
		assertNotNull(foundDirectory.getId());
		assertEquals(foundDirectory.getName(), directory.getName());
	}

	@Test
	public void whenFindByName_thenReturnEmpty() {
		assertNull(directoryRepository.findByName("wrong name"));
	}


	@Test
	public void whenFindByID_thenReturnObject() {
		// when
		CringleDirectory directory = directoryRepository.findByName(this.directory.getName());
		Optional<CringleDirectory> foundDirectory = directoryRepository.findById(directory.getId());
		// then
		assertTrue(foundDirectory.isPresent());
		assertEquals(foundDirectory.get(), directory);
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			directoryRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Optional<CringleDirectory> foundDirectory = directoryRepository.findById(1123l);
		// then
		assertFalse(foundDirectory.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfRecords() {
        //given
        CringleDirectory newDirectory = TestBuildersUtils.getCringleDirectory(null, SECOND_DIRECTORY_NAME);
        entityManager.persistAndFlush(newDirectory);
        // when
        List<CringleDirectory> directoryList = directoryRepository.findAll();
        // then
        assertNotNull(directoryList);
        assertFalse(directoryList.isEmpty());
        assertEquals(directoryList.size(), 2);
    }

	@Test
	public void it_should_save_object() {
        CringleDirectory newDirectory = TestBuildersUtils.getCringleDirectory(null, SECOND_DIRECTORY_NAME);
        directoryRepository.save(newDirectory);
        CringleDirectory foundDirectory = directoryRepository.findByName(newDirectory.getName());
        // then
        assertNotNull(foundDirectory);
        assertNotNull(foundDirectory.getId());
        assertEquals(foundDirectory.getName(), newDirectory.getName());
    }

    @Test
    public void whenSaveObjectWithNameTooLong_thenThrowConstraintViolationException() {
        CringleDirectory directory = TestBuildersUtils.getCringleDirectory(null, NAME_WITH_LENGTH_MORE_THEN_250_SYMBOLS);
        assertThrows(ConstraintViolationException.class, () -> {
            directoryRepository.save(directory);
        });
    }

    @Test
    public void whenSaveObjectWithNameTooShort_thenThrowConstraintViolationException() {
        CringleDirectory directory = TestBuildersUtils.getCringleDirectory(null, "");
        assertThrows(ConstraintViolationException.class, () -> {
            directoryRepository.save(directory);
        });
    }

    @Test
    public void whenDeleteById_thenOk() {
        //given
        CringleDirectory newDirectory = TestBuildersUtils.getCringleDirectory(null, SECOND_DIRECTORY_NAME);

        entityManager.persistAndFlush(newDirectory);
		assertEquals(directoryRepository.findAll().size(), 2);

		CringleDirectory foundDirectory = directoryRepository.findByName(SECOND_DIRECTORY_NAME);

		// when
		directoryRepository.deleteById(foundDirectory.getId());
		// then
		assertEquals(directoryRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			directoryRepository.deleteById(10000000l);
		});
	}
}