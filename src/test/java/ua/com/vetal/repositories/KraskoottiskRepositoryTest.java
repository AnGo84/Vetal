package ua.com.vetal.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.com.vetal.entity.Kraskoottisk;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class KraskoottiskRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private KraskoottiskRepository kraskoottiskRepository;

	private Kraskoottisk kraskoottisk;

	@BeforeEach
	public void beforeEach() {
		kraskoottiskRepository.deleteAll();
		int min = 100;
		int max = 2000;
		for (int i = 0; i < 5; i++) {
			kraskoottisk = new Kraskoottisk(Math.random() * (max - min + 1) + min);
			entityManager.persistAndFlush(kraskoottisk);
		}
	}

	@Test
	public void whenFindAll_thenReturnListOfManagers() {
		List<Kraskoottisk> kraskoottisks = kraskoottiskRepository.findAll();
		assertNotNull(kraskoottisks);
		assertFalse(kraskoottisks.isEmpty());
		assertEquals(kraskoottisks.size(), 5);
	}

}