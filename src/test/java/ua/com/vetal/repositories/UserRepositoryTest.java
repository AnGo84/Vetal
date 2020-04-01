package ua.com.vetal.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;

	private User user;
	private static Set<UserRole> userRoleSet;


	@BeforeAll
	public static void beforeAll() {

		//new HashSet<>(Arrays.asList(userRole));
	}

	@BeforeEach
	public void beforeEach() {
		userRepository.deleteAll();
		UserRole userRole = TestDataUtils.getUserRole(null, "USER");
		entityManager.persistAndFlush(userRole);
		userRoleSet = new HashSet<>(userRoleRepository.findAll());

		// given

		user = TestDataUtils.getUser("User", "password", true, userRoleSet);

		entityManager.persistAndFlush(user);
		//entityManager.flush();
	}
	@AfterEach
	public void afterEach(){

	}


	@Test
	public void whenFindByUserName_thenReturnUser() {
		// when
		User foundUser = userRepository.findByName(user.getName());

		// then
		assertNotNull(foundUser);
		assertNotNull(foundUser.getId());
		assertEquals(foundUser.getName(), user.getName());
		assertEquals(foundUser.getEncryptedPassword(), user.getEncryptedPassword());
		assertEquals(foundUser.isEnabled(), user.isEnabled());
	}

	@Test
	public void whenFindByUserName_thenReturnEmpty() {
		assertNull(userRepository.findByName("wrong name"));
	}

	@Test
	public void whenFindByID_thenReturnUser() {
		//User user = userRepository.findByName("User");
		// when
		Optional<User> foundUser = userRepository.findById(user.getId());
		// then
		assertTrue(foundUser.isPresent());
		assertEquals(user, foundUser.get());
	}

	@Test
	public void whenFindByIDByNull_thenThrowInvalidDataAccessApiUsageException() {
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			userRepository.findById(null);
		});

	}

	@Test
	public void whenFindByID_thenReturnEmpty() {
		// when
		Optional<User> foundUser = userRepository.findById(10l);
		// then
		assertFalse(foundUser.isPresent());
	}

	@Test
	public void whenFindAll_thenReturnListOfUser() {
		//given
		User user = TestDataUtils.getUser("User2", "second pass", true, userRoleSet);
		entityManager.persistAndFlush(user);
		// when
		List<User> users = userRepository.findAll();
		// then
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertEquals(users.size(), 2);

	}

	@Test
	public void it_should_save_user() {
		// when
		User foundUser = userRepository.findByName(user.getName());

		// then
		assertNotNull(foundUser);
		assertNotNull(foundUser.getId());
		assertEquals(foundUser.getName(), user.getName());
		assertEquals(foundUser.getEncryptedPassword(), user.getEncryptedPassword());
		assertEquals(foundUser.isEnabled(), user.isEnabled());
	}

	@Test
	public void whenSaveUserWithNameTooLong_thenThrowConstraintViolationException() {
		User user = TestDataUtils.getUser("NameWithLengthMoreThen36SymbolsIsTooLongForSaving", "second pass", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			userRepository.save(user);
		});
	}

	@Test
	public void whenSaveUserWithNameTooShortLength_thenThrowConstraintViolationException() {
		User user = TestDataUtils.getUser("1", "second pass", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			//entityManager.persistAndFlush(user);
			userRepository.save(user);
		});
	}

	@Test
	public void whenSaveUserWithPassWrongLength_thenThrowConstraintViolationException() {
		User user = TestDataUtils.getUser("New Name", "", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			//entityManager.persistAndFlush(user);
			userRepository.saveAndFlush(user);
		});
	}

	@Test
	public void whenSaveUserWithEmailWrongLength_thenThrowConstraintViolationException() {
		User user = TestDataUtils.getUser("New Name", "", true, userRoleSet);
		user.setEmail("Email_With_Length_More_Then_100_Symbols_Is_Too_Long_For_Saving_And_Should_be_an_error_on_saving_attempt");
		assertThrows(ConstraintViolationException.class, () -> {
			userRepository.saveAndFlush(user);
		});
	}

	@Test
	public void whenSaveUserWithExistName_thenThrowConstraintViolationException() {
		User user = TestDataUtils.getUser("New Name", "", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			//entityManager.persistAndFlush(user);
			userRepository.save(user);
		});
	}


	@Test
	public void whenDeleteById_thenOk() {
		//given
		User user = TestDataUtils.getUser("User2", "second pass", true, userRoleSet);
		entityManager.persistAndFlush(user);
		assertEquals(userRepository.findAll().size(), 2);

		User foundUser = userRepository.findByName("User2");

		// when
		userRepository.deleteById(foundUser.getId());
		// then
		assertEquals(userRepository.findAll().size(), 1);
	}

	@Test
	public void whenDeleteById_thenThrowEmptyResultDataAccessException() {
		assertThrows(EmptyResultDataAccessException.class, () -> {
			userRepository.deleteById(10000000l);
		});
	}
}