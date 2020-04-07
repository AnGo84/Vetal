package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import ua.com.vetal.TestDataUtils;
import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;
import ua.com.vetal.entity.UserRole;
import ua.com.vetal.handler.PasswordResetTokenHandler;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PasswordResetTokenRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordResetToken passwordResetToken;
    private User user;

    @BeforeEach
    public void beforeEach() {
        Set<UserRole> userRoleSet = new HashSet<>();
        UserRole userRole = testEntityManager.persistAndFlush(TestDataUtils.getUserRole(null, "USER"));
        userRoleSet.add(userRole);
        userRole = testEntityManager.persistAndFlush(TestDataUtils.getUserRole(null, "ADMIN"));
        userRoleSet.add(userRole);

        passwordResetTokenRepository.deleteAll();
        user = testEntityManager.persistAndFlush(getUser(userRoleSet));
        //passwordResetToken = testEntityManager.persistAndFlush(passwordResetToken);
    }

    @Test
    public void it_should_save_token() {
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(user);
        PasswordResetToken foundPasswordResetToken = passwordResetTokenRepository.save(passwordResetToken);
        // then
        assertNotNull(foundPasswordResetToken);
        assertNotNull(foundPasswordResetToken.getId());
        assertEquals(passwordResetToken.getUser(), foundPasswordResetToken.getUser());
        assertEquals(passwordResetToken.getToken(), foundPasswordResetToken.getToken());
        assertEquals(passwordResetToken.getExpiryDate(), foundPasswordResetToken.getExpiryDate());
    }

    @Test
    public void whenSaveWithTokenNull_thenThrowDataIntegrityViolationException() {
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(user);
        passwordResetToken.setToken(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            passwordResetTokenRepository.save(passwordResetToken);
        });
    }

    @Test
    public void whenSaveWithUserNull_thenThrowDataIntegrityViolationException() {
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            passwordResetTokenRepository.save(passwordResetToken);
        });
    }

    @Test
    public void whenSaveWithExpiryDateNull_thenThrowDataIntegrityViolationException() {
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(user);
        passwordResetToken.setExpiryDate(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            passwordResetTokenRepository.save(passwordResetToken);
        });
    }

    @Test
    public void whenFindByToken_thenReturnPasswordResetToken() {
        // when
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(user);
        passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
        PasswordResetToken foundPasswordResetToken = passwordResetTokenRepository.findByToken(passwordResetToken.getToken());

        // then
        assertNotNull(foundPasswordResetToken);
        assertNotNull(foundPasswordResetToken.getId());
        assertEquals(passwordResetToken.getId(), foundPasswordResetToken.getId());
        assertEquals(passwordResetToken.getUser(), foundPasswordResetToken.getUser());
        assertEquals(passwordResetToken.getToken(), foundPasswordResetToken.getToken());
        assertEquals(passwordResetToken.getExpiryDate(), foundPasswordResetToken.getExpiryDate());
    }

    @Test
    public void whenFindByToken_thenReturnEmpty() {
        passwordResetToken = PasswordResetTokenHandler.getPasswordResetToken(user);
        passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
        assertNull(passwordResetTokenRepository.findByToken("wrong token"));
    }

    private User getUser(Set<UserRole> userRoleSet) {
        User user = TestDataUtils.getUser("User", "password", true, userRoleSet);
        return user;
    }
}