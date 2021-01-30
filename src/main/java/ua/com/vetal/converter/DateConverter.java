package ua.com.vetal.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ua.com.vetal.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

@Component
@ConfigurationPropertiesBinding
@Slf4j
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        log.info("Get STRING Date: {}", source);
        if (source == null || source.isEmpty()) {
            return null;
        }
        DateFormat df = DateUtils.SIMPLE_DATE_FORMAT;

        try {
            return df.parse(source);
        } catch (ParseException e) {
            log.error("Date '{}' parser error :", source, e.getMessage());
            return null;
        }
    }

}
