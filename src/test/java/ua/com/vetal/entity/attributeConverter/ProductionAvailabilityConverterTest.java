package ua.com.vetal.entity.attributeConverter;

import org.junit.jupiter.api.Test;
import ua.com.vetal.entity.ProductionAvailability;

import static org.junit.jupiter.api.Assertions.*;

class ProductionAvailabilityConverterTest {
	private ProductionAvailabilityConverter converter = new ProductionAvailabilityConverter();

	@Test
	public void shouldConvertProperly() {
		//
		assertNull(converter.convertToDatabaseColumn(null));
		assertEquals(1, converter.convertToDatabaseColumn(ProductionAvailability.IN_STOCK));
		assertEquals(2, converter.convertToDatabaseColumn(ProductionAvailability.ORDERED));
		assertEquals(3, converter.convertToDatabaseColumn(ProductionAvailability.NEED_ORDERED));
		assertEquals(4, converter.convertToDatabaseColumn(ProductionAvailability.OTHER));

		//
		assertEquals(null, converter.convertToEntityAttribute(null));
		assertEquals(null, converter.convertToEntityAttribute(0l));
		assertEquals(ProductionAvailability.IN_STOCK, converter.convertToEntityAttribute(1l));
		assertEquals(ProductionAvailability.ORDERED, converter.convertToEntityAttribute(2l));
		assertEquals(ProductionAvailability.NEED_ORDERED, converter.convertToEntityAttribute(3l));
		assertEquals(ProductionAvailability.OTHER, converter.convertToEntityAttribute(4l));

		//
		Long lVal = 2l;
		assertEquals(lVal, converter.convertToDatabaseColumn(converter.convertToEntityAttribute(lVal)));

		ProductionAvailability dVal = ProductionAvailability.NEED_ORDERED;
		assertTrue(dVal.equals(converter.convertToEntityAttribute(converter.convertToDatabaseColumn(dVal))));
	}
}