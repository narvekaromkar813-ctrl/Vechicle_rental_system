package com.vehiclerental.exception;

/**
 * Custom Exception thrown when rental operation parameters are invalid,
 * such as return date being prior to booking date or invalid rental period.
 */
public class InvalidRentalException extends Exception {
    public InvalidRentalException(String message) {
        super(message);
    }
}
