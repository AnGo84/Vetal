package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.repositories.UserRoleRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserRoleServiceImplTest {
	@Autowired
	private UserRoleServiceImpl userRoleService;
	@MockBean
	private UserRoleRepository mockUserRoleRepository;
	private UserRole userRole;
	private Set<UserRole> userRoleSet;

	@BeforeEach
	public void beforeEach() {
		userRole = TestBuildersUtils.getUserRole(1l, "USER");
		userRoleSet = new HashSet<>(Arrays.asList(userRole, TestBuildersUtils.getUserRole(2l, "ADMIN")));
	}

	@Test
	void whenFindById_thenReturnUser() {
		when(mockUserRoleRepository.getOne(anyLong())).thenReturn(userRole);
		long id = 1;
		UserRole found = userRoleService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), userRole.getId());
		assertEquals(found.getName(), userRole.getName());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockUserRoleRepository.getOne(anyLong())).thenReturn(null);
		long id = 2;
		UserRole found = userRoleService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnUser() {
		when(mockUserRoleRepository.findByName(userRole.getName())).thenReturn(userRole);
		UserRole found = userRoleService.findByName(userRole.getName());

		assertNotNull(found);
		assertEquals(found.getId(), userRole.getId());
		assertEquals(found.getName(), userRole.getName());
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockUserRoleRepository.findByName(userRole.getName())).thenReturn(userRole);
		UserRole found = userRoleService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		UserRole newUserRole = TestBuildersUtils.getUserRole(2l, "ADMIN");
		userRoleService.saveObject(newUserRole);
		verify(mockUserRoleRepository, times(1)).save(newUserRole);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockUserRoleRepository.save(any(UserRole.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			userRoleService.saveObject(userRole);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		UserRole newUserRole = TestBuildersUtils.getUserRole(2l, "ADMIN");
		userRoleService.updateObject(newUserRole);
		verify(mockUserRoleRepository, times(1)).save(newUserRole);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockUserRoleRepository.save(any(UserRole.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			userRoleService.updateObject(userRole);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		userRoleService.deleteById(1l);
		verify(mockUserRoleRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockUserRoleRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			userRoleService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockUserRoleRepository.findAll()).thenReturn(Arrays.asList(userRole));
		List<UserRole> userRoles = userRoleService.findAllObjects();
		assertNotNull(userRoles);
		assertFalse(userRoles.isEmpty());
		assertEquals(userRoles.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockUserRoleRepository.findByName(userRole.getName())).thenReturn(userRole);
		assertTrue(userRoleService.isObjectExist(userRole));
		when(mockUserRoleRepository.findByName(userRole.getName())).thenReturn(userRole);
	}

	@TestConfiguration
	static class UserServiceImplTestContextConfiguration {
		@Bean
		public UserServiceImpl userServiceImpl() {
			return new UserServiceImpl();
		}
	}

}