package ua.com.vetal.entity.attributeConverter;

import ua.com.vetal.entity.ProductionAvailability;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
public class ProductionAvailabilityConverter implements AttributeConverter<ProductionAvailability, Long> {
	@Override
	public Long convertToDatabaseColumn(ProductionAvailability productionAvailability) {
		if (productionAvailability == null) {
			return null;
		}
		return productionAvailability.getDbIndex();
	}

	@Override
	public ProductionAvailability convertToEntityAttribute(Long aLong) {
		return Stream.of(ProductionAvailability.values()).filter(el -> el.getDbIndex().equals(aLong)).findFirst().orElse(null);
	}
}
