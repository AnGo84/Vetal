package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.Link;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.repositories.LinkRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LinkServiceImplTest {

    @Autowired
    private LinkServiceImpl linkService;
    @MockBean
    private LinkRepository mockLinkRepository;
    private LinkType linkType;
    private Link link;

    @BeforeEach
    public void beforeEach() {
        linkType = TestBuildersUtils.getLinkType(1l, "file");

        link = TestBuildersUtils.getLink(1l, "fullName", "shortName", linkType, "path");
    }

    @Test
    void whenFindById_thenReturnLink() {
        when(mockLinkRepository.getOne(1L)).thenReturn(link);
        long id = 1;
        Link foundLink = linkService.findById(id);

        // then
        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), link.getFullName());
        assertEquals(foundLink.getShortName(), link.getShortName());
        assertEquals(foundLink.getPath(), link.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), link.getLinkType());
    }

    @Test
    void whenFindById_thenReturnNull() {
        when(mockLinkRepository.getOne(anyLong())).thenReturn(null);
        long id = 2;
        Link found = linkService.findById(id);
        assertNull(found);
    }

    @Test
    void whenFindByName_thenReturnLink() {
        when(mockLinkRepository.findByFullName(link.getFullName())).thenReturn(link);
        Link foundLink = linkService.findByName(link.getFullName());

        assertNotNull(foundLink);
        assertNotNull(foundLink.getId());
        assertEquals(foundLink.getFullName(), link.getFullName());
        assertEquals(foundLink.getShortName(), link.getShortName());
        assertEquals(foundLink.getPath(), link.getPath());
        assertNotNull(foundLink.getLinkType());
        assertEquals(foundLink.getLinkType(), link.getLinkType());
    }

    @Test
    void whenFindByName_thenReturnNull() {
        when(mockLinkRepository.findByFullName(link.getFullName())).thenReturn(null);
        Link found = linkService.findByName("wrong name");
        assertNull(found);
    }

    @Test
    void whenFindByLinkTypeId_thenReturnLink() {
        when(mockLinkRepository.findByLinkType_Id(anyLong())).thenReturn(Arrays.asList(link));
        List<Link> foundLinks = linkService.findByLinkTypeId(link.getLinkType().getId());

        assertNotNull(foundLinks);
        assertFalse(foundLinks.isEmpty());
        assertEquals(foundLinks.size(), 1);
    }

    @Test
    void whenFindLinkTypeId_thenReturnNull() {
        when(mockLinkRepository.findByLinkType_Id(anyLong())).thenReturn(null);
        List<Link> foundLinks = linkService.findByLinkTypeId(123654l);
        assertNull(foundLinks);
    }

    @Test
    void whenSaveLink_thenSuccess() {
        Link newLink = TestBuildersUtils.getLink(null, "fullName2", "shortName2", linkType, "path2");
        linkService.saveObject(newLink);
        verify(mockLinkRepository, times(1)).save(newLink);
    }

    @Test
    void whenSaveLink_thenNPE() {
        when(mockLinkRepository.save(any(Link.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            linkService.saveObject(link);
        });
    }

    @Test
    void whenUpdateLink_thenSuccess() {
        link.setFullName("fullName2");
        linkService.updateObject(link);
        verify(mockLinkRepository, times(1)).save(link);
    }

    @Test
    void whenUpdateLink_thenThrow() {
        when(mockLinkRepository.save(any(Link.class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> {
            linkService.updateObject(link);
        });
    }

    @Test
    void whenDeleteById_thenSuccess() {
        linkService.deleteById(1l);
        verify(mockLinkRepository, times(1)).deleteById(1l);
    }

    @Test
    void whenDeleteById_thenThrowEmptyResultDataAccessException() {
        doThrow(new EmptyResultDataAccessException(0)).when(mockLinkRepository).deleteById(anyLong());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            linkService.deleteById(1000000l);
        });
    }

    @Test
    void whenFindAllObjects() {
        when(mockLinkRepository.findAll()).thenReturn(Arrays.asList(link));
        List<Link> objects = linkService.findAllObjects();
        assertNotNull(objects);
        assertFalse(objects.isEmpty());
        assertEquals(objects.size(), 1);
    }

    @Test
    void whenIsObjectExist() {
        when(mockLinkRepository.findByFullName(link.getFullName())).thenReturn(link);
        assertTrue(linkService.isObjectExist(link));
        when(mockLinkRepository.findByFullName(anyString())).thenReturn(null);
        assertFalse(linkService.isObjectExist(link));
    }

}