package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.LinkType;
import ua.com.vetal.repositories.LinkTypeRepository;
import ua.com.vetal.repositories.LinkTypeRepositoryTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LinkTypeServiceImplTest {
	@Autowired
	private LinkTypeServiceImpl linkTypeService;
	@MockBean
	private LinkTypeRepository mockLinkTypeRepository;
	private LinkType linkType;

	@BeforeEach
	public void beforeEach() {
		linkType = TestDataUtils.getLinkType(LinkTypeRepositoryTest.DIRECTORY_NAME);
	}

	@Test
	void whenFindById_thenReturnObject() {
		when(mockLinkTypeRepository.getOne(1L)).thenReturn(linkType);
		long id = 1;
		LinkType found = linkTypeService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), linkType.getId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockLinkTypeRepository.getOne(1L)).thenReturn(linkType);
		long id = 221121;
		LinkType found = linkTypeService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockLinkTypeRepository.findByName(linkType.getName())).thenReturn(linkType);
		LinkType found = linkTypeService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		LinkType newDirector = TestDataUtils.getLinkType(LinkTypeRepositoryTest.SECOND_DIRECTORY_NAME);
		linkTypeService.saveObject(newDirector);
		verify(mockLinkTypeRepository, times(1)).save(newDirector);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockLinkTypeRepository.save(any(LinkType.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			linkTypeService.saveObject(linkType);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		LinkType newDirector = TestDataUtils.getLinkType(LinkTypeRepositoryTest.SECOND_DIRECTORY_NAME);
		linkTypeService.saveObject(newDirector);
		verify(mockLinkTypeRepository, times(1)).save(newDirector);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockLinkTypeRepository.save(any(LinkType.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			linkTypeService.updateObject(linkType);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		linkTypeService.deleteById(1l);
		verify(mockLinkTypeRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockLinkTypeRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			linkTypeService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockLinkTypeRepository.findAll()).thenReturn(Arrays.asList(linkType));
		List<LinkType> linkTypes = linkTypeService.findAllObjects();
		assertNotNull(linkTypes);
		assertFalse(linkTypes.isEmpty());
		assertEquals(linkTypes.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockLinkTypeRepository.findByName(linkType.getName())).thenReturn(linkType);
		assertTrue(linkTypeService.isObjectExist(linkType));
		when(mockLinkTypeRepository.findByName(linkType.getName())).thenReturn(linkType);
	}
}