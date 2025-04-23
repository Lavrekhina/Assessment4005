package utils;

import javafx.util.StringConverter;

import java.time.LocalDate;

public class StringDateConverter extends StringConverter<LocalDate> {
    @Override
    public String toString(LocalDate localDate) {
        return DateUtils.format(localDate);
    }

    @Override
    public LocalDate fromString(String s) {
        return DateUtils.toLocalDate(s);
    }
}
