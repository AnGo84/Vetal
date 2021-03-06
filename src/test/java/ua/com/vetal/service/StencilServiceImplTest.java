package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.dao.StencilDAO;
import ua.com.vetal.email.EmailMessage;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.repositories.StencilRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StencilServiceImplTest {
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private StencilServiceImpl stencilService;

	@MockBean
	private StencilRepository mockStencilRepository;
	@MockBean
	private StencilDAO mockStencilDAO;
	private Stencil stencil;

	@BeforeEach
	public void beforeEach() {
		stencil = TestDataUtils.getStencil(1l, 1);
	}

	@Test
	void whenFindById_thenReturnStencil() {
		when(mockStencilRepository.getOne(1L)).thenReturn(stencil);
		long id = 1;
		Stencil foundStencil = stencilService.findById(id);
		// then
		assertNotNull(foundStencil);
		assertNotNull(foundStencil.getId());
		assertEquals(stencil, foundStencil);
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockStencilRepository.getOne(1L)).thenReturn(stencil);
		long id = 2;
		Stencil found = stencilService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByAccount_thenReturnStencil() {
		when(mockStencilRepository.findByAccount(stencil.getAccount())).thenReturn(stencil);
		Stencil foundStencil = stencilService.findByAccount(stencil.getAccount());

		assertNotNull(foundStencil);
		assertNotNull(foundStencil.getId());
		assertEquals(stencil, foundStencil);
	}

	@Test
	void whenFindByAccount_thenReturnNull() {
		when(mockStencilRepository.findByAccount(anyString())).thenReturn(null);
		Stencil found = stencilService.findByAccount("wrong name");
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnNull() {
		Stencil found = stencilService.findByName(null);
		assertNull(found);
		found = stencilService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveStencil_thenSuccess() {
		Stencil newStencil = TestDataUtils.getStencil(null, 2);
		stencilService.saveObject(newStencil);
		verify(mockStencilRepository, times(1)).save(newStencil);
	}

	@Test
	void whenSaveStencil_thenNPE() {
		when(mockStencilRepository.save(any(Stencil.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			stencilService.saveObject(stencil);
		});
	}

	@Test
	void whenUpdateStencil_thenSuccess() {
		stencil.setAccount("accountNew");
		stencil.setOrderName("orderNameNew");

		stencilService.updateObject(stencil);
		verify(mockStencilRepository, times(1)).save(stencil);
	}

	@Test
	void whenUpdateStencil_thenThrow() {
		when(mockStencilRepository.save(any(Stencil.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			stencilService.updateObject(stencil);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		stencilService.deleteById(1l);
		verify(mockStencilRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockStencilRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			stencilService.deleteById(1000000l);
		});
	}

	@Test
	void whenFindAllObjects() {
		when(mockStencilRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(stencil));
		List<Stencil> objects = stencilService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void whenIsObjectExist() {
		when(mockStencilRepository.getOne(stencil.getId())).thenReturn(stencil);
		assertTrue(stencilService.isObjectExist(stencil));
		when(mockStencilRepository.getOne(anyLong())).thenReturn(null);
		assertFalse(stencilService.isObjectExist(stencil));
	}

	@Test
	void whenGetCurrentKrascoottiskAmount_ThenReturn() {
		double testKrascoottiskAmount = 2564d;
		when(mockStencilRepository.getCurrentKrascoottiskAmount()).thenReturn(testKrascoottiskAmount);
		assertTrue(Double.compare(testKrascoottiskAmount, stencilService.getKraskoottiskAmount()) == 0);
	}

	@Test
	void whenIssAccountValueExist() {
		when(mockStencilRepository.findByAccount(stencil.getAccount())).thenReturn(stencil);
		assertFalse(stencilService.isAccountValueExist(stencil));

		Stencil newStencil = TestDataUtils.getStencil(2l, 2);
		when(mockStencilRepository.findByAccount(newStencil.getAccount())).thenReturn(null);
		assertFalse(stencilService.isAccountValueExist(newStencil));

		newStencil.setAccount(stencil.getAccount());
		assertTrue(stencilService.isAccountValueExist(newStencil));
	}

	@Test
	void whenFindByFilterData() {
        when(mockStencilDAO.findByFilterData(any(OrderViewFilter.class))).thenReturn(Arrays.asList(stencil));
        List<Stencil> objects = stencilService.findByFilterData(new OrderViewFilter());
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);

		when(mockStencilRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(stencil));
		objects = stencilService.findByFilterData(null);
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	public void whenGetEmailMessage() {
		String text = messageSource.getMessage("email.stencil.change_state", null, new Locale("ru"));

		String defaultFrom = "default@email";
		EmailMessage emailMessage = stencilService.getEmailMessage(stencil, defaultFrom);
		assertNotNull(emailMessage);
		assertEquals(defaultFrom, emailMessage.getFrom());
		assertEquals(stencil.getManager().getEmail(), emailMessage.getTo());
		assertTrue(emailMessage.getSubject().contains(stencil.getFullNumber()));
		assertTrue(emailMessage.getText().contains(stencil.getFullNumber()));
		assertNull(emailMessage.getAttachments());
	}

}