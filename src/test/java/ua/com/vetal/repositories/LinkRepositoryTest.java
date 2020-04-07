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
import ua.com.vetal.entity.Link;
import ua.com.vetal.entity.LinkType;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LinkRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinkTypeRepository linkTypeRepository;

    private List<LinkType> linkTypes;

    private Link link;

    @BeforeEach
    public void beforeEach() {
        linkRepository.deleteAll();
        linkTypeRepository.deleteAll();
        //LinkType = entityManager.persistAndFlush(TestDataUtils.getLinkType("LinkTypeFirstName", "LinkTypeLastName", "LinkTypeMiddleName", "LinkTypeEmail"));
        linkTypes = linkTypeRepository.saveAll(getLinkTypes());

        link = entityManager.persistAndFlush(TestDataUtils.getLink(null, "fullName", "shortName", linkTypes.get(0), "path"));
    }

    @Test
    public void whenFindByFullName_thenReturnObject() {
        // when
        Link foundLink = linkRepository.findByFullName(link.getFullName());

        // then
        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), link.getFullName());
        assertEquals(foundLink.getPath(), link.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), link.getLinkType());
    }

    @Test
    public void whenFindByFullName_thenReturnEmpty() {
        assertNull(linkRepository.findByFullName("wrong name"));
    }

    @Test
    public void whenFindByLinkTypeId_thenReturnObject() {
        // when
        List<Link> foundLinks = linkRepository.findByLinkType_Id(link.getLinkType().getId());

        // then
        assertNotNull(foundLinks);
        assertEquals(foundLinks.size(), 1);
        Link foundLink = foundLinks.get(0);
        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), link.getFullName());
        assertEquals(foundLink.getShortName(), link.getShortName());
        assertEquals(foundLink.getPath(), link.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), link.getLinkType());
    }

    @Test
    public void whenFindByLinkTypeId_thenReturnEmpty() {
        assertTrue(linkRepository.findByLinkType_Id(null).isEmpty());
    }


    @Test
    public void whenFindByID_thenReturnLink() {
        //User user = userRepository.findByName("User");
        // when
        Optional<Link> foundLink = linkRepository.findById(link.getId());
        // then
        assertTrue(foundLink.isPresent());
        assertEquals(foundLink.get(), link);
    }

    @Test
    public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            linkRepository.findById(null);
        });
    }

    @Test
    public void whenFindByID_thenReturnEmpty() {
        // when
        Long wrongId = 123654L;
        Optional<Link> foundLink = linkRepository.findById(wrongId);
        // then
        assertFalse(foundLink.isPresent());
    }

    @Test
    public void whenFindAll_thenReturnListOfLinkTypes() {
        //given
        Link newLink = TestDataUtils.getLink(null, "fullName2", "shortName2", linkTypes.get(1), "path2");
        entityManager.persistAndFlush(newLink);
        // when
        List<Link> links = linkRepository.findAll();
        // then
        assertNotNull(links);
        assertFalse(links.isEmpty());
        assertEquals(links.size(), 2);

    }

    @Test
    public void it_should_save_Link() {
        Link newLink = TestDataUtils.getLink(null, "fullName2", "shortName2", linkTypes.get(1), "path2");
        newLink = linkRepository.save(newLink);
        Link foundLink = linkRepository.findById(newLink.getId()).get();

        // then
        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), newLink.getFullName());
        assertEquals(foundLink.getShortName(), newLink.getShortName());
        assertEquals(foundLink.getPath(), newLink.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), newLink.getLinkType());
    }

    @Test
    public void it_should_save_Link_with_empty_fields() {
        Link newLink = TestDataUtils.getLink(null, "fullName2", null, linkTypes.get(2), "path2");
        newLink = linkRepository.save(newLink);
        Link foundLink = linkRepository.findById(newLink.getId()).get();

        // then
        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), newLink.getFullName());
        assertEquals(foundLink.getShortName(), newLink.getShortName());
        assertEquals(foundLink.getPath(), newLink.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), newLink.getLinkType());
    }

    @Test
    public void whenSaveLinkWithFullNameTooLong_thenThrowDataIntegrityViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullNameWithLengthMoreThen250SymbolsIsTooLongForSavingWithLengthMoreThen250SymbolsIsTooLongForSavingWithLengthMoreThen250SymbolsIsTooLongForSavingWithLengthMoreThen250SymbolsIsTooLongForSavingWithLengthMoreThen250SymbolsIsTooLongForSavingWithLengthMoreThen250SymbolsIsTooLongForSaving", "shortName2", linkTypes.get(1), "path2");
        assertThrows(DataIntegrityViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithFullNameTooShortLength_thenThrowConstraintViolationException() {
        Link newLink = TestDataUtils.getLink(null, "", "shortName2", linkTypes.get(1), "path2");
        assertThrows(ConstraintViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithFullNameNull_thenThrowConstraintViolationException() {
        Link newLink = TestDataUtils.getLink(null, null, "shortName2", linkTypes.get(1), "path2");
        assertThrows(ConstraintViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithShortNameTooLong_thenThrowDataIntegrityViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullName", "shortNameWithLengthMoreThen50SymbolsIsTooLongForSaving", linkTypes.get(1), "path2");
        assertThrows(DataIntegrityViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithPathTooLongLength_thenThrowDataIntegrityViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullName", null, linkTypes.get(1),
                "pathWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSavingWithLengthMoreThen500SymbolsIsTooLongForSaving");
        assertThrows(DataIntegrityViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithPathTooShortLength_thenThrowConstraintViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullName", null, linkTypes.get(1), "");
        assertThrows(ConstraintViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }


    @Test
    public void whenSaveLinkWithPathNull_thenThrowConstraintViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullName", null, linkTypes.get(1), null);
        assertThrows(ConstraintViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithLinkTypeNull_thenThrowConstraintViolationException() {
        Link newLink = TestDataUtils.getLink(null, "fullName2", null, null, null);
        assertThrows(ConstraintViolationException.class, () -> {
            linkRepository.save(newLink);
        });
    }

    @Test
    public void whenSaveLinkWithNotExistLinkType_thenThrowInvalidDataAccessApiUsageException() {

        Link newLink = TestDataUtils.getLink(null, "fullName2", null, TestDataUtils.getLinkType("new Name"), null);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            linkRepository.save(newLink);
        });
    }


    @Test
    public void whenDeleteById_thenOk() {
        //given
        Link newLink = TestDataUtils.getLink(null, "fullName2", "shortName2", linkTypes.get(1), "path2");
        newLink = entityManager.persistAndFlush(newLink);
        assertEquals(linkRepository.findAll().size(), 2);

        // when
        linkRepository.deleteById(newLink.getId());
        // then
        assertEquals(linkRepository.findAll().size(), 1);
    }

    @Test
    public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            linkRepository.deleteById(10000000l);
        });
    }

    private List<LinkType> getLinkTypes() {
        List<LinkType> linkTypes = new ArrayList<>(
                Arrays.asList(TestDataUtils.getLinkType(1l, "link"),
                        TestDataUtils.getLinkType(2l, "file"),
                        TestDataUtils.getLinkType(3l, "folder"))
        );
        return linkTypes;
    }
}