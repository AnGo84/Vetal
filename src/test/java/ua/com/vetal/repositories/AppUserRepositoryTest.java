package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.AppUser;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AppUserRepository appUserRepository;

	private AppUser appUser;

	@BeforeEach
	public void beforeEach() {
		appUserRepository.deleteAll();
		// given
		appUser = TestDataUtils.getAppUser();
		entityManager.persistAndFlush(appUser);
	}

	@Test
	public void whenFindByUserName_thenReturnUser() {
		// when
		AppUser foundAppUser = appUserRepository.findByUserName(appUser.getUserName());

		// then
		assertNotNull(foundAppUser);
		assertNotNull(foundAppUser.getUserId());
		assertEquals(foundAppUser.getUserName(), appUser.getUserName());
		assertEquals(foundAppUser.getEncryptedPassword(), appUser.getEncryptedPassword());
		assertEquals(foundAppUser.isEnabled(), appUser.isEnabled());
	}

	@Test
	public void whenFindByUserName_thenReturnEmpty() {
		assertNull(appUserRepository.findByUserName("wrong name"));
	}

	@Test
	public void whenFindByID_thenReturnUser() {
		AppUser appUser = appUserRepository.findByUserName("AppUser");
		// when
		Optional<AppUser> foundAppUser = appUserRepository.findById(appUser.getUserId());
		// then
		assertTrue(foundAppUser.isPresent());
		assertEquals(appUser, foundAppUser.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			appUserRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Optional<AppUser> foundAppUser = appUserRepository.findById(10l);
		// then
		assertFalse(foundAppUser.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfUser() {
		//given
		appUser = AppUser.builder().userName("User2").enabled(true).encryptedPassword("second pass").build();
		entityManager.persistAndFlush(appUser);
		// when
		List<AppUser> users = appUserRepository.findAll();
		// then
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertEquals(users.size(), 2);

	}

	@Test
	public void it_should_save_user() {
		// when
		AppUser foundAppUser = appUserRepository.findByUserName(appUser.getUserName());

		// then
		assertNotNull(foundAppUser);
		assertNotNull(foundAppUser.getUserId());
		assertEquals(foundAppUser.getUserName(), appUser.getUserName());
		assertEquals(foundAppUser.getEncryptedPassword(), appUser.getEncryptedPassword());
		assertEquals(foundAppUser.isEnabled(), appUser.isEnabled());
	}

	@Test
	public void whenSaveAppUserWithNameLengthMoreThen36_thenThrowPersistenceException() {
		AppUser appUser = AppUser.builder().userName("NameWithLengthMoreThen36SymbolsIsTooLongForSaving").enabled(true).encryptedPassword("second pass").build();
		assertThrows(PersistenceException.class, () -> {
			entityManager.persistAndFlush(appUser);
		});

	}

	@Test
	public void whenSaveAppUserWithExistName_thenThrowPersistenceException() {
		// given
		assertThrows(PersistenceException.class, () -> {
			entityManager.persistAndFlush(TestDataUtils.getAppUser());
		});
	}


	@Test
	public void whenDeleteById_thenOk() {
		//given
		appUser = AppUser.builder().userName("User2").enabled(true).encryptedPassword("second pass").build();
		entityManager.persistAndFlush(appUser);
		assertEquals(appUserRepository.findAll().size(), 2);

		AppUser foundAppUser = appUserRepository.findByUserName("User2");

		// when
		appUserRepository.deleteById(foundAppUser.getUserId());
		// then
		assertEquals(appUserRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		appUser = AppUser.builder().userName("User2").enabled(true).encryptedPassword("second pass").build();
		entityManager.persistAndFlush(appUser);
		assertEquals(appUserRepository.findAll().size(), 2);
		assertThrows(EmptyResultDataAccessException.class, () -> {
			appUserRepository.deleteById(10000000l);
		});
	}
}