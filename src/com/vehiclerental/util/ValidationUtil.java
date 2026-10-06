package com.vehiclerental.util;

import com.vehiclerental.exception.InvalidRentalException;
import com.vehiclerental.exception.ValidationException;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Utility class providing validation methods for forms, models, and operations.
 */
public class ValidationUtil {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern REG_NO_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");

    /**
     * Validates that a string field is not null or blank.
     */
    public static void validateNotEmpty(String fieldName, String value) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty.");
        }
    }

    /**
     * Validates vehicle registration number format (e.g., MH01AB1234).
     */
    public static void validateRegistrationNumber(String regNo) throws ValidationException {
        validateNotEmpty("Registration Number", regNo);
        String cleaned = regNo.trim().toUpperCase().replaceAll("\\s+", "");
        if (!REG_NO_PATTERN.matcher(cleaned).matches()) {
            throw new ValidationException("Registration Number must be 6-12 alphanumeric characters (e.g. MH01AB1234).");
        }
    }

    /**
     * Validates rental price per day is strictly positive.
     */
    public static void validateRentalPrice(double price) throws ValidationException {
        if (price <= 0.0) {
            throw new ValidationException("Rental price per day must be greater than zero.");
        }
    }

    /**
     * Validates phone number (10 digits).
     */
    public static void validatePhone(String phone) throws ValidationException {
        validateNotEmpty("Phone number", phone);
        String cleaned = phone.trim().replaceAll("[^0-9]", "");
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new ValidationException("Phone number must contain exactly 10 digits.");
        }
    }

    /**
     * Validates email address format.
     */
    public static void validateEmail(String email) throws ValidationException {
        validateNotEmpty("Email", email);
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("Invalid email format (example: name@domain.com).");
        }
    }

    /**
     * Validates booking date and expected return date.
     */
    public static void validateRentalDates(LocalDate bookingDate, LocalDate returnDate) throws InvalidRentalException {
        if (bookingDate == null || returnDate == null) {
            throw new InvalidRentalException("Both booking date and return date are required.");
        }
        if (returnDate.isBefore(bookingDate)) {
            throw new InvalidRentalException("Return date cannot be earlier than booking date.");
        }
    }
}
