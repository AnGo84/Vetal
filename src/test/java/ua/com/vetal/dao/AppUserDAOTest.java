package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestBuildersUtils;
import ua.com.vetal.entity.AppUser;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.repositories.UserRepository;
import ua.com.vetal.repositories.UserRoleRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ua.com.vetal.dao")
public class AppUserDAOTest {
    private static Set<UserRole> userRoleSet;
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private AppUserDAO appUserDAO;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    private User user;
    private User secondUser;

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();

        List<UserRole> userRole = Arrays.asList(TestBuildersUtils.getUserRole(null, "ADMIN"), TestBuildersUtils.getUserRole(null, "MANAGER"));
        userRole.forEach(role -> testEntityManager.persistAndFlush(role));
        userRoleSet = new HashSet<>(userRoleRepository.findAll());

		user = testEntityManager.persistAndFlush(TestBuildersUtils.getUser(null, "User", "password", true, userRoleSet));

        userRoleSet = new HashSet<UserRole>() {{
            add(userRoleRepository.findAll().get(0));
        }};
        secondUser = testEntityManager.persistAndFlush(TestBuildersUtils.getUser(null, "User2", "password", true, userRoleSet));
    }


    @Test
    public void whenFindUserAccount_thenReturnAppUser() {
        AppUser findAppUser = appUserDAO.findUserAccount(user.getName());
        assertNotNull(findAppUser);
        assertEquals(user.getId(), findAppUser.getUserId());
        assertEquals(user.getName(), findAppUser.getUserName());
        assertEquals(user.isEnabled(), findAppUser.isEnabled());
        assertEquals(user.getEncryptedPassword(), findAppUser.getEncryptedPassword());

        AppUser findSecondAppUser = appUserDAO.findUserAccount(secondUser.getName());
        assertNotNull(findSecondAppUser);
    }

    @Test
    public void whenFindUserAccountByNotExistName_thenReturnNull() {
        AppUser findAppUser = appUserDAO.findUserAccount("not exist user name");
        assertNull(findAppUser);
    }
}