package ua.com.vetal.entity.attributeConverter;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyConverterTest {
	private MoneyConverter moneyConverter = new MoneyConverter();

	@Test
	public void shouldConvertProperly() {
		//
		assertEquals(0, moneyConverter.convertToDatabaseColumn(null));
		assertEquals(0, moneyConverter.convertToDatabaseColumn(0d));
		assertEquals(12, moneyConverter.convertToDatabaseColumn(0.12));
		assertEquals(12, moneyConverter.convertToDatabaseColumn(0.121));

		//
		assertEquals(0, moneyConverter.convertToEntityAttribute(null));
		assertEquals(0, moneyConverter.convertToEntityAttribute(0l));
		assertEquals(0.12, moneyConverter.convertToEntityAttribute(12l));
		assertEquals(12.12, moneyConverter.convertToEntityAttribute(1212l));

		//
		Long lVal = 1236l;
		assertEquals(lVal, moneyConverter.convertToDatabaseColumn(moneyConverter.convertToEntityAttribute(lVal)));

		Double dVal = Double.valueOf(1236.14);
		assertTrue(dVal.equals(moneyConverter.convertToEntityAttribute(moneyConverter.convertToDatabaseColumn(dVal))));
	}
}