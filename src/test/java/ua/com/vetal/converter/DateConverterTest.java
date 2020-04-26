package ua.com.vetal.converter;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateConverterTest {
	private DateConverter dateConverter = new DateConverter();


	@Test
	public void whenConvertReturnResult() {
		Date convertedDate = dateConverter.convert("20.03.2010");
		assertNotNull(convertedDate);
		assertEquals(getTestDate(), convertedDate);
	}

	private Date getTestDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2010);
		calendar.set(Calendar.MONTH, 2);
		calendar.set(Calendar.DATE, 20);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@Test
	public void whenConvertReturnNull() {
		assertNull(dateConverter.convert(null));
		assertNull(dateConverter.convert(""));
		assertNull(dateConverter.convert("wrong format"));
	}

}