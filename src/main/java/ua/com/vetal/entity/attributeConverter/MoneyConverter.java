package ua.com.vetal.entity.attributeConverter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Slf4j
public class MoneyConverter implements AttributeConverter<Double, Long> {
	@Override
	public Long convertToDatabaseColumn(Double aDouble) {
		if (aDouble == null) {
			return new Long(0);
		}
		Long result = Double.valueOf(aDouble * 100).longValue();

		return result;
	}

	@Override
	public Double convertToEntityAttribute(Long aLong) {
		if (aLong == null) {
			return Double.valueOf(0);
		}
		Double result = (aLong.doubleValue()) / 100;
		return result;
	}
}
