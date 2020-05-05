package ua.com.vetal.entity.attributeConverter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Slf4j
public class MoneyConverter implements AttributeConverter<Double, Long> {
	@Override
	public Long convertToDatabaseColumn(Double aDouble) {
		log.info("convertToDatabaseColumn Double: {}", aDouble);
		if (aDouble == null) {
			return new Long(0);
		}
		Long result = Double.valueOf(aDouble * 100).longValue();
		log.info("Get Long: {}", result);
		return result;
	}

	@Override
	public Double convertToEntityAttribute(Long aLong) {
		log.info("convertToDatabaseColumn Long: {}", aLong);
		if (aLong == null) {
			return Double.valueOf(0);
		}
		Double result = (aLong.doubleValue()) / 100;
		log.info("Get Double: {}", result);
		return result;
	}
}
