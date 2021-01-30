package ua.com.vetal.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static final String DATE_PATTERN_DD_MM_YYYY = "dd.MM.yyyy";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN_DD_MM_YYYY);

    public static String getDatePattern() {
        return DATE_PATTERN_DD_MM_YYYY;
    }

    public static Date addSecondsToDate(Date date, int seconds) {
        return addToDate(date, Calendar.SECOND, seconds);
    }

    public static Date addDaysToDate(Date date, int days) {
        return addToDate(date, Calendar.DATE, days);
    }


    public static Date addToDate(Date date, int calendarType, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, value);
        Date newDate = calendar.getTime();
        return newDate;
    }

    public static Date firstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date newDate = calendar.getTime();
        return newDate;
    }


}
