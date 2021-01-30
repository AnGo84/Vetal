package ua.com.vetal.utils;

import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class LoggerUtils {

    public static void loggingBindingResultsErrors(BindingResult bindingResult, Logger log) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            log.error(error.getObjectName() +
                    " - " + error.getField() +
                    " - " + error.getDefaultMessage() +
                    " - " + error.getCode());
        }
    }
}
