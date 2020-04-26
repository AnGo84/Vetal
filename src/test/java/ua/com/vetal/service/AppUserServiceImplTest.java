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
import ua.com.vetal.entity.AppUser;
import ua.com.vetal.repositories.AppUserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * https://www.baeldung.com/mockito-verify
 */
@SpringBootTest
class AppUserServiceImplTest {
	@Autowired
	private AppUserServiceImpl appUserService;
	@MockBean
	private AppUserRepository appUserRepository;
	private AppUser appUser;

	@BeforeEach
	public void beforeEach() {
        appUser = TestBuildersUtils.getAppUser();
    }

	@Test
	void whenFindById_thenReturnUser() {
		when(appUserRepository.getOne(1L)).thenReturn(appUser);
		long id = 1;
		AppUser found = appUserService.findById(id);

		assertNotNull(found);
		assertEquals(found.getUserId(), appUser.getUserId());
	}

	@Test
	void whenFindById_thenReturnNull() {
		when(appUserRepository.getOne(1L)).thenReturn(appUser);
		long id = 2;
		AppUser found = appUserService.findById(id);
		assertNull(found);
	}

	@Test
	void whenFindByName_thenReturnUser() {
		when(appUserRepository.findByUserName(appUser.getUserName())).thenReturn(appUser);
		AppUser found = appUserService.findByName("AppUser");

		assertNotNull(found);
		assertEquals(found.getUserId(), appUser.getUserId());
		assertEquals(found.getUserName(), appUser.getUserName());
	}

	@Test
	void whenFindByName_thenReturnNull() {
		when(appUserRepository.findByUserName(appUser.getUserName())).thenReturn(appUser);
		AppUser found = appUserService.findByName("wrong name");
		assertNull(found);
	}

	@Test
	void whenSaveObject_thenSuccess() {
		AppUser newUser = AppUser.builder().userName("User2").enabled(true).encryptedPassword("second pass").build();
		appUserService.saveObject(newUser);
		verify(appUserRepository, times(1)).save(newUser);
	}

	@Test
	void whenSaveObject_thenNPE() {
		when(appUserRepository.save(any(AppUser.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			appUserService.saveObject(appUser);
		});
	}

	@Test
	void whenUpdateObject_thenSuccess() {
		AppUser newUser = AppUser.builder().userName("User2").enabled(true).encryptedPassword("second pass").build();
		appUserService.updateObject(newUser);
		verify(appUserRepository, times(1)).save(newUser);
	}

	@Test
	void whenUpdateObject_thenThrow() {
		when(appUserRepository.save(any(AppUser.class))).thenThrow(NullPointerException.class);
		assertThrows(NullPointerException.class, () -> {
			appUserService.updateObject(appUser);
		});
	}

	@Test
	void whenDeleteById_thenSuccess() {
		appUserService.deleteById(1l);
		verify(appUserRepository, times(1)).deleteById(1l);
	}

	@Test
	void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		doThrow(new EmptyResultDataAccessException(0)).when(appUserRepository).deleteById(anyLong());
		assertThrows(EmptyResultDataAccessException.class, () -> {
			appUserService.deleteById(1000000l);
		});
	}

	@Test
	void findAllObjects() {
		when(appUserRepository.findAll()).thenReturn(Arrays.asList(appUser));
		List<AppUser> userList = appUserService.findAllObjects();
		assertNotNull(userList);
		assertFalse(userList.isEmpty());
		assertEquals(userList.size(), 1);
	}

	@Test
	void isObjectExist() {
		when(appUserRepository.findByUserName(appUser.getUserName())).thenReturn(appUser);
		assertTrue(appUserService.isObjectExist(appUser));
		when(appUserRepository.findByUserName(appUser.getUserName())).thenReturn(appUser);
	}

	@TestConfiguration
	static class AppUserServiceImplTestContextConfiguration {
		@Bean
		public AppUserServiceImpl appUserServiceImpl() {
			return new AppUserServiceImpl();
		}
	}
}