package ua.com.vetal.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class EncodePasswordTest {
	private final String password = "123";
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Test
	void encryptPassword() {
		String encodedPass = encoder.encode(password);
		assertTrue(encoder.matches(password, encodedPass));
	}
}