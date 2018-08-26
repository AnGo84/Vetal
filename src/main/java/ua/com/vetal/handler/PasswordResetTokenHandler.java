package ua.com.vetal.handler;

import ua.com.vetal.entity.PasswordResetToken;
import ua.com.vetal.entity.User;

import java.util.UUID;

public class PasswordResetTokenHandler {
    public static PasswordResetToken getPasswordResetToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(60); // 1 hour
        return token;
    }
}
