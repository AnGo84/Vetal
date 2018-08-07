package ua.com.vetal.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, Date> {

	static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

	@Override
	public Date convert(String source) {
		logger.info("Get STRING Date: " + source);
		if (source == null) {
			return null;
		}

		// DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return df.parse(source);
		} catch (ParseException e) {
			logger.info("Date parser error: " + e.getMessage());
			return null;
		}
	}

}
