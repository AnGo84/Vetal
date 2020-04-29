package ua.com.vetal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.vetal.entity.Kraskoottisk;
import ua.com.vetal.repositories.KraskoottiskRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class KraskoottiskServiceTest {
	@Autowired
	private KraskoottiskService kraskoottiskService;
	@MockBean
	private KraskoottiskRepository mockKraskoottiskRepository;
	private List<Kraskoottisk> kraskoottisks;

	@BeforeEach
	public void beforeEach() {

		int min = 100;
		int max = 2000;
		kraskoottisks = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Kraskoottisk kraskoottisk = new Kraskoottisk(Math.random() * (max - min + 1) + min);
			kraskoottisks.add(kraskoottisk);
		}
	}

	@Test
	void findAllObjects() {
		when(mockKraskoottiskRepository.findAll()).thenReturn(kraskoottisks);
		List<Kraskoottisk> objects = kraskoottiskService.findAllObjects();
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		assertEquals(objects.size(), 5);
	}

}