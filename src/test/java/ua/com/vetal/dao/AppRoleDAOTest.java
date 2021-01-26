package ua.com.vetal.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ua.com.vetal.TestBuildersUtils;
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
@ComponentScan(basePackages = "ua.com.vetal.dao")
public class AppRoleDAOTest {

    private static Set<UserRole> userRoleSet;
    @Autowired
    private AppRoleDAO appRoleDAO;
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
        userRoleRepository.saveAll(userRole);

        userRoleSet = new HashSet<>(userRoleRepository.findAll());

        user = userRepository.save(TestBuildersUtils.getUser(null, "User", "password", true, userRoleSet));

        userRoleSet = new HashSet<UserRole>() {{
            add(userRoleRepository.findAll().get(0));
        }};
        secondUser = userRepository.save(TestBuildersUtils.getUser(null, "User2", "password", true, userRoleSet));
    }


    @Test
    public void whenGetRoleNames_thenReturnListOfRoleNames() {
        List<String> userRoleNames = appRoleDAO.getRoleNames(user.getId());
        assertNotNull(userRoleNames);
        assertFalse(userRoleNames.isEmpty());
        assertEquals(userRoleNames.size(), 2);
        assertTrue(userRoleNames.contains("ADMIN"));
        assertTrue(userRoleNames.contains("MANAGER"));

        List<String> secondUserRoleNames = appRoleDAO.getRoleNames(secondUser.getId());
        assertNotNull(secondUserRoleNames);
        assertFalse(secondUserRoleNames.isEmpty());
        assertEquals(secondUserRoleNames.size(), 1);

    }
}