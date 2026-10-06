package com.vehiclerental.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for formatting, parsing, and calculating dates.
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Formats a LocalDate into yyyy-MM-dd string.
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(FORMATTER);
    }

    /**
     * Parses a string in yyyy-MM-dd format to a LocalDate.
     * Throws DateTimeParseException if invalid.
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new DateTimeParseException("Date string is empty", "", 0);
        }
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }

    /**
     * Calculates the number of days between start and end date inclusive of at least 1 day.
     */
    public static long calculateDays(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 1;
        long days = ChronoUnit.DAYS.between(start, end);
        return days <= 0 ? 1 : days;
    }

    /**
     * Returns today's date formatted as yyyy-MM-dd.
     */
    public static String getTodayString() {
        return LocalDate.now().format(FORMATTER);
    }
}
