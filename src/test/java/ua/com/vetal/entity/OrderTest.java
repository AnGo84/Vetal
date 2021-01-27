package ua.com.vetal.entity;

import org.junit.jupiter.api.Test;
import ua.com.vetal.TestDataUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

	@Test
	public void whenConstructFullNumber_thenReturnResult() {
		Stencil stencil = TestDataUtils.getStencil(1l, 12);
		String fullNumber = stencil.constructFullNumber();
		assertNotNull(fullNumber);
		assertTrue(fullNumber.contains(String.valueOf(stencil.getNumber())));
		assertTrue(fullNumber.contains(stencil.getNumberBase().getName()));
		assertTrue(fullNumber.contains(String.valueOf(stencil.getNumberSuffix())));
	}
}