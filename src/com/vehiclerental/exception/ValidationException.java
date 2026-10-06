package com.vehiclerental.exception;

/**
 * Custom Exception thrown when data input validation fails,
 * such as missing required fields, invalid phone/email formats, or duplicate IDs.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
