package ua.com.vetal.entity.attributeConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MoneyConverter implements AttributeConverter<Double, Long> {
	@Override
	public Long convertToDatabaseColumn(Double aDouble) {
		if (aDouble == null) {
			return new Long(0);
		}
		return Double.valueOf(aDouble * 100).longValue();
	}

	@Override
	public Double convertToEntityAttribute(Long aLong) {
		if (aLong == null) {
			return Double.valueOf(0);
		}
		return Double.valueOf(aLong) / 100;
	}
}
