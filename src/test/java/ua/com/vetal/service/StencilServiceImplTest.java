package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.dao.StencilDAO;
import ua.com.vetal.entity.Stencil;
import ua.com.vetal.entity.filter.FilterData;
import ua.com.vetal.repositories.StencilRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class StencilServiceImplTest {

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
		//when(mockManagerRepository.findByName(anyString())).thenReturn(manager);
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
	void whenIssAccountValueExist() {
		when(mockStencilRepository.findByAccount(stencil.getAccount())).thenReturn(stencil);
		assertFalse(stencilService.isAccountValueExist(stencil));

		Stencil newStencil = TestDataUtils.getStencil(2l, 2);
		when(mockStencilRepository.findByAccount(newStencil.getAccount())).thenReturn(null);
		assertFalse(stencilService.isAccountValueExist(newStencil));

		newStencil.setAccount(stencil.getAccount());
		assertTrue(stencilService.isAccountValueExist(newStencil));
	}

	//@Disabled("Disabled until refactoring filters")
	@Test
	void whenFindByFilterData() {
		when(mockStencilDAO.findByFilterData(any(FilterData.class))).thenReturn(Arrays.asList(stencil));
		List<Stencil> objects = stencilService.findByFilterData(new FilterData());
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);

		/*

		List<Stencil> filteredList = stencilService.findByFilterData(null);
		assertEquals(filteredList.size(), 1);

		FilterData filterData = new FilterData();
		filterData.setAccount(stencil.getAccount());
		filterData.setManager(stencil.getManager());

		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new FilterData();
		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());

		filterData = new FilterData();
		filterData.setAccount(stencil.getAccount());
		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new FilterData();
		filterData.setManager(stencil.getManager());
		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(1, filteredList.size());

		filterData = new FilterData();
		filterData.setManager(new Manager());
		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());

		filterData = new FilterData();
		filterData.setAccount("not exist name");
		filteredList = stencilService.findByFilterData(filterData);
		assertEquals(0, filteredList.size());*/
	}
}