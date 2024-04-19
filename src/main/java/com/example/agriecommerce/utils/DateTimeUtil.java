package com.example.agriecommerce.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.agriecommerce.utils.AppConstants.DATE_PATTERN;

public class DateTimeUtil {
    public static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
