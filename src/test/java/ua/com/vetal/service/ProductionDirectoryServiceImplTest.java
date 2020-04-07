package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.ProductionDirectory;
import ua.com.vetal.entity.ProductionTypeDirectory;
import ua.com.vetal.repositories.ProductionDirectoryRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductionDirectoryServiceImplTest {

	@Autowired
	private ProductionDirectoryServiceImpl productionDirectoryService;
	@MockBean
	private ProductionDirectoryRepository mockProductionDirectoryRepository;
	private ProductionTypeDirectory productionType;
	private ProductionDirectory production;

	@BeforeEach
	public void beforeEach() {
		productionType = TestDataUtils.getProductionTypeDirectory("Production type");
		productionType.setId(1l);
		production = TestDataUtils.getProductionDirectory(1l, "fullName", "shortName", productionType);
	}

	@Test
	void whenFindById_thenReturnClient() {
		when(mockProductionDirectoryRepository.getOne(1L)).thenReturn(production);
		long id = 1;
		ProductionDirectory foundProduction = productionDirectoryService.findById(id);

		// then
		assertNotNull(foundProduction);
		assertNotNull(foundProduction.getId());
		assertEquals(foundProduction.getFullName(), production.getFullName());
		assertEquals(foundProduction.getShortName(), production.getShortName());
		assertNotNull(foundProduction.getProductionType());
		assertEquals(foundProduction.getProductionType(), production.getProductionType());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockProductionDirectoryRepository.getOne(1L)).thenReturn(production);
		long id = 2;
		ProductionDirectory found = productionDirectoryService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnClient() {
		when(mockProductionDirectoryRepository.findByFullName(production.getFullName())).thenReturn(production);
		ProductionDirectory foundProduction = productionDirectoryService.findByName(production.getFullName());

		assertNotNull(foundProduction);
		assertNotNull(foundProduction.getId());
		assertEquals(foundProduction.getFullName(), production.getFullName());
		assertEquals(foundProduction.getShortName(), production.getShortName());
		assertNotNull(foundProduction.getProductionType());
		assertEquals(foundProduction.getProductionType(), production.getProductionType());
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockProductionDirectoryRepository.findByFullName(production.getFullName())).thenReturn(production);
		ProductionDirectory found = productionDirectoryService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveClient_thenSuccess() {
		ProductionDirectory newProduction = TestDataUtils.getProductionDirectory(2l, "fullName2", "shortName2", productionType);
		productionDirectoryService.saveObject(newProduction);
		verify(mockProductionDirectoryRepository, times(1)).save(newProduction);
	}

	@Test
	void whenSaveClient_thenNPE() {
		when(mockProductionDirectoryRepository.save(any(ProductionDirectory.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			productionDirectoryService.saveObject(production);
		});
	}

	@Test
	void whenUpdateClient_thenSuccess() {
		production.setFullName("fullName2");
		productionDirectoryService.updateObject(production);
		verify(mockProductionDirectoryRepository, times(1)).save(production);
	}

	@Test
	void whenUpdateClient_thenThrow() {
		when(mockProductionDirectoryRepository.save(any(ProductionDirectory.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			productionDirectoryService.updateObject(production);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		productionDirectoryService.deleteById(1l);
		verify(mockProductionDirectoryRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockProductionDirectoryRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			productionDirectoryService.deleteById(1000000l);
		});
	}

	@Test
	void whenFindAllObjects() {
		when(mockProductionDirectoryRepository.findAll()).thenReturn(Arrays.asList(production));
		List<ProductionDirectory> objects = productionDirectoryService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 1);
	}

	@Test
	void whenIsObjectExist() {
		when(mockProductionDirectoryRepository.findByFullName(production.getFullName())).thenReturn(production);
		assertTrue(productionDirectoryService.isObjectExist(production));
		when(mockProductionDirectoryRepository.findByFullName(production.getFullName())).thenReturn(null);
		assertFalse(productionDirectoryService.isObjectExist(production));
	}

}