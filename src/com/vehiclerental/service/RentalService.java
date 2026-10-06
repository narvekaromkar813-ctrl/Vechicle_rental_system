package com.vehiclerental.service;

import com.vehiclerental.exception.InvalidRentalException;
import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.util.DateUtil;
import com.vehiclerental.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Service class handling rental booking and vehicle return workflows.
 * Demonstrates:
 * - Business logic separation
 * - Rental charge calculation (Days x Rate)
 * - Array synchronization (availabilityStatus)
 * - LinkedList<Rental> history maintenance
 * - Custom Exception Handling (VehicleNotAvailableException, InvalidRentalException)
 */
public class RentalService {

    private final DataStore dataStore;

    public RentalService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Calculates the duration in days between booking date and return date.
     * Minimum duration is 1 day.
     */
    public long calculateRentalDays(LocalDate bookingDate, LocalDate returnDate) throws InvalidRentalException {
        ValidationUtil.validateRentalDates(bookingDate, returnDate);
        return DateUtil.calculateDays(bookingDate, returnDate);
    }

    /**
     * Calculates total rental charge = Number of Days * Rental Price Per Day
     */
    public double calculateTotalCharge(double dailyRate, long days) {
        if (days <= 0) days = 1;
        return dailyRate * days;
    }

    /**
     * Books a vehicle for a customer.
     * Throws VehicleNotAvailableException if the vehicle is already rented.
     * Throws InvalidRentalException if the dates are invalid.
     * Throws ValidationException if parameters are missing.
     */
    public Rental bookVehicle(String customerId, String registrationNumber,
                              LocalDate bookingDate, LocalDate returnDate)
            throws ValidationException, VehicleNotAvailableException, InvalidRentalException {

        ValidationUtil.validateNotEmpty("Customer", customerId);
        ValidationUtil.validateNotEmpty("Vehicle Registration", registrationNumber);
        ValidationUtil.validateRentalDates(bookingDate, returnDate);

        Customer customer = dataStore.findCustomerById(customerId);
        if (customer == null) {
            throw new ValidationException("Selected customer does not exist.");
        }

        // Fast lookup of vehicle using HashMap
        Vehicle vehicle = dataStore.findVehicleByRegNo(registrationNumber);
        if (vehicle == null) {
            throw new ValidationException("Selected vehicle not found in database.");
        }

        // Check availability
        if (!vehicle.isAvailable()) {
            throw new VehicleNotAvailableException("Vehicle " + vehicle.getRegistrationNumber() + " is currently unavailable / already booked.");
        }

        long days = calculateRentalDays(bookingDate, returnDate);
        double dailyRate = vehicle.getRentalPricePerDay();
        double totalCharge = calculateTotalCharge(dailyRate, days);

        String rentalId = dataStore.generateNextRentalId();
        Rental rental = new Rental(
                rentalId,
                customer,
                vehicle,
                bookingDate,
                returnDate,
                days,
                dailyRate,
                totalCharge,
                "ACTIVE"
        );

        // Add to active rentals and update availability in DataStore (and boolean[] array)
        dataStore.addActiveRental(rental);

        return rental;
    }

    /**
     * Processes return of an actively rented vehicle.
     * Updates status, recalculates final charge based on actual return date,
     * restores vehicle availability (and boolean[] array), and appends to LinkedList rentalHistory.
     */
    public Rental processVehicleReturn(String rentalId, LocalDate actualReturnDate)
            throws ValidationException, InvalidRentalException {

        ValidationUtil.validateNotEmpty("Rental ID", rentalId);
        if (actualReturnDate == null) {
            throw new ValidationException("Actual return date must be provided.");
        }

        Rental rental = dataStore.findActiveRentalById(rentalId);
        if (rental == null) {
            throw new ValidationException("Active rental with ID '" + rentalId + "' was not found.");
        }

        if (actualReturnDate.isBefore(rental.getBookingDate())) {
            throw new InvalidRentalException("Actual return date cannot be before original booking date (" +
                    DateUtil.formatDate(rental.getBookingDate()) + ").");
        }

        // Calculate actual days and final charge
        long actualDays = DateUtil.calculateDays(rental.getBookingDate(), actualReturnDate);
        double finalCharge = calculateTotalCharge(rental.getRentalRate(), actualDays);

        rental.setNumberOfDays(actualDays);
        dataStore.processReturn(rental, actualReturnDate, finalCharge);

        return rental;
    }

    public List<Rental> getActiveRentals() {
        return new ArrayList<>(dataStore.getActiveRentals());
    }

    /**
     * Returns the LinkedList containing completed rental history.
     */
    public LinkedList<Rental> getRentalHistory() {
        return dataStore.getRentalHistory();
    }
}
