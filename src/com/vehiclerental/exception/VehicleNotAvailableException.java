package com.vehiclerental.exception;

/**
 * Custom Exception thrown when an operation attempts to book or rent
 * a vehicle that is currently marked as unavailable or already rented.
 */
public class VehicleNotAvailableException extends Exception {
    public VehicleNotAvailableException(String message) {
        super(message);
    }
}
