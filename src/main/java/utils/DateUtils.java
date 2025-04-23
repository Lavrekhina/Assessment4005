package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static LocalDate toLocalDate(String date) {
        try {
            return LocalDate.ofInstant(DATE_FORMAT.parse(date).toInstant(), ZoneId.systemDefault());
        } catch (ParseException e) {
            return null;
        }
    }


    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.toString();
    }
}
