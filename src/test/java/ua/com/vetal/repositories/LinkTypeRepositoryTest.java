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
import ua.com.vetal.entity.LinkType;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public
class LinkTypeRepositoryTest {
	public static final String SECOND_DIRECTORY_NAME = "Second Link Type";
	public static final String DIRECTORY_NAME = "LinkType";


	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private LinkTypeRepository linkTypeRepository;
	private LinkType linkType;

	@BeforeEach
	public void beforeEach() {
        linkTypeRepository.deleteAll();
        linkType = TestBuildersUtils.getLinkType(DIRECTORY_NAME);

        entityManager.persistAndFlush(linkType);
    }

	@Test
	public void whenFindByName_thenReturnObject() {
		// when
		LinkType foundDirectory = linkTypeRepository.findByName(linkType.getName());

		// then
		assertNotNull(foundDirectory);
		assertNotNull(foundDirectory.getId());
		assertEquals(foundDirectory.getName(), linkType.getName());
	}

	@Test
	public void whenFindByName_thenReturnEmpty() {
		assertNull(linkTypeRepository.findByName("wrong name"));
	}


	@Test
	public void whenFindByID_thenReturnObject() {
		// when
		LinkType directory = linkTypeRepository.findByName(this.linkType.getName());
		Optional<LinkType> foundDirectory = linkTypeRepository.findById(directory.getId());
		// then
		assertTrue(foundDirectory.isPresent());
		assertEquals(foundDirectory.get(), directory);
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			linkTypeRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Optional<LinkType> foundDirectory = linkTypeRepository.findById(1123l);
		// then
		assertFalse(foundDirectory.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfRecords() {
        //given
        LinkType newDirectory = TestBuildersUtils.getLinkType(SECOND_DIRECTORY_NAME);
        entityManager.persistAndFlush(newDirectory);
        // when
        List<LinkType> directoryList = linkTypeRepository.findAll();
        // then
        assertNotNull(directoryList);
        assertFalse(directoryList.isEmpty());
        assertEquals(directoryList.size(), 2);
    }

	@Test
	public void it_should_save_object() {
        LinkType newDirectory = TestBuildersUtils.getLinkType(SECOND_DIRECTORY_NAME);
        linkTypeRepository.save(newDirectory);
        LinkType foundDirectory = linkTypeRepository.findByName(newDirectory.getName());
        // then
        assertNotNull(foundDirectory);
        assertNotNull(foundDirectory.getId());
        assertEquals(foundDirectory.getName(), newDirectory.getName());
    }

	@Test
	public void whenSaveObjectWithNameTooLong_thenThrowConstraintViolationException() {
        LinkType linkType = TestBuildersUtils.getLinkType("NAME_WITH_LENGTH_MORE_THEN_30_SYMBOLS");
        assertThrows(ConstraintViolationException.class, () -> {
            linkTypeRepository.save(linkType);
        });
    }

	@Test
	public void whenSaveObjectWithNameTooShort_thenThrowConstraintViolationException() {
        LinkType linkType = TestBuildersUtils.getLinkType("");
        assertThrows(ConstraintViolationException.class, () -> {
            linkTypeRepository.save(linkType);
        });
    }

	@Test
	public void whenSaveObjectWithExistingName_thenThrowDataIntegrityViolationException() {
        LinkType linkType = TestBuildersUtils.getLinkType(this.linkType.getName());
        assertThrows(DataIntegrityViolationException.class, () -> {
            linkTypeRepository.save(linkType);
        });
    }

	@Test
	public void whenDeleteById_thenOk() {
        //given
        LinkType newDirectory = TestBuildersUtils.getLinkType(SECOND_DIRECTORY_NAME);

        entityManager.persistAndFlush(newDirectory);
        assertEquals(linkTypeRepository.findAll().size(), 2);

        LinkType foundDirectory = linkTypeRepository.findByName(SECOND_DIRECTORY_NAME);

        // when
        linkTypeRepository.deleteById(foundDirectory.getId());
        // then
        assertEquals(linkTypeRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			linkTypeRepository.deleteById(10000000l);
		});
	}
}