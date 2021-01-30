package ua.com.vetal.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

	}

	@BeforeEach
	public void beforeEach() {
        userRepository.deleteAll();
        UserRole userRole = TestBuildersUtils.getUserRole(null, "USER");
        entityManager.persistAndFlush(userRole);
        userRoleSet = new HashSet<>(userRoleRepository.findAll());
        // given
        user = TestBuildersUtils.getUser(null, "User", "password", true, userRoleSet);

        entityManager.persistAndFlush(user);
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
        User user = TestBuildersUtils.getUser(null, "User2", "second pass", true, userRoleSet);
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
        User newUser = TestBuildersUtils.getUser(null, "User2", "second pass", true, userRoleSet);
        userRepository.save(newUser);
        User foundUser = userRepository.findByName(newUser.getName());

        // then
        assertNotNull(foundUser);
        assertNotNull(foundUser.getId());
        assertEquals(foundUser.getName(), newUser.getName());
        assertEquals(foundUser.getEncryptedPassword(), newUser.getEncryptedPassword());
        assertEquals(foundUser.isEnabled(), newUser.isEnabled());
    }

	@Test
	public void whenSaveUserWithNameTooLong_thenThrowConstraintViolationException() {
        User user = TestBuildersUtils.getUser(null, "NameWithLengthMoreThen36SymbolsIsTooLongForSaving", "second pass", true, userRoleSet);
        assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(user);
        });
	}

	@Test
	public void whenSaveUserWithNameTooShortLength_thenThrowConstraintViolationException() {
		User user = TestBuildersUtils.getUser(null, "1", "second pass", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			userRepository.save(user);
		});
	}

	@Disabled("Field Pass without @NotEmpty for updating validation")
	@Test
	public void whenSaveUserWithPassWrongLength_thenThrowConstraintViolationException() {
		User user = TestBuildersUtils.getUser(null, "New Name", "", true, userRoleSet);
		assertThrows(ConstraintViolationException.class, () -> {
			userRepository.save(user);
		});
	}

	@Test
	public void whenSaveUserWithEmailWrongLength_thenThrowConstraintViolationException() {
        User user = TestBuildersUtils.getUser(null, "New Name", "", true, userRoleSet);
        user.setEmail("Email_With_Length_More_Then_100_Symbols_Is_Too_Long_For_Saving_And_Should_be_an_error_on_saving_attempt");
        assertThrows(ConstraintViolationException.class, () -> {
			userRepository.save(user);
        });
    }

	@Test
	public void whenSaveUserWithExistName_thenThrowDataIntegrityViolationException() {
		User user = TestBuildersUtils.getUser(null, "User", "", true, userRoleSet);
		assertThrows(DataIntegrityViolationException.class, () -> {
			userRepository.save(user);
		});
	}


	@Test
	public void whenDeleteById_thenOk() {
        //given
        User user = TestBuildersUtils.getUser(null, "User2", "second pass", true, userRoleSet);
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