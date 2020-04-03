package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.repositories.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * https://www.baeldung.com/mockito-verify
 */
@SpringBootTest
class UserServiceImplTest {
	@Autowired
	private UserServiceImpl userService;
	@MockBean
	private UserRepository mockUserRepository;
	private User user;
	private Set<UserRole> userRoleSet;

	@BeforeEach
	public void beforeEach() {
		userRoleSet = new HashSet<>(Arrays.asList(TestDataUtils.getUserRole(1l, "USER")));
		user = TestDataUtils.getUser("User", "password", true, userRoleSet);
	}

	@Test
	void whenFindById_thenReturnUser() {
		when(mockUserRepository.getOne(anyLong())).thenReturn(user);
		long id = 1;
		User found = userService.findById(id);

		assertNotNull(found);
		assertEquals(found.getId(), user.getId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(mockUserRepository.getOne(anyLong())).thenReturn(null);
		long id = 2;
		User found = userService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnUser() {
		when(mockUserRepository.findByName(user.getName())).thenReturn(user);
		User found = userService.findByName("User");

		assertNotNull(found);
		assertEquals(found.getId(), user.getId());
		assertEquals(found.getName(), user.getName());
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(mockUserRepository.findByName(user.getName())).thenReturn(user);
		User found = userService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		User newUser = TestDataUtils.getUser("User2", "second pass", true,userRoleSet);
		userService.saveObject(newUser);
		verify(mockUserRepository, times(1)).save(newUser);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(mockUserRepository.save(any(User.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			userService.saveObject(user);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		User newUser = TestDataUtils.getUser("User2", "second pass", true,userRoleSet);
		userService.saveObject(newUser);
		verify(mockUserRepository, times(1)).save(newUser);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(mockUserRepository.save(any(User.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			userService.updateObject(user);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		userService.deleteById(1l);
		verify(mockUserRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(mockUserRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			userService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(mockUserRepository.findAll()).thenReturn(Arrays.asList(user));
		List<User> userList = userService.findAllObjects();
		assertNotNull(userList);
		assertFalse(userList.isEmpty());
		assertEquals(userList.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(mockUserRepository.findByName(user.getName())).thenReturn(user);
		assertTrue(userService.isObjectExist(user));
		when(mockUserRepository.findByName(user.getName())).thenReturn(user);
	}

	@TestConfiguration
	static class UserServiceImplTestContextConfiguration {
		@Bean
		public UserServiceImpl userServiceImpl() {
			return new UserServiceImpl();
		}
	}
}