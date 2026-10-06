package com.vehiclerental.model;

import java.time.LocalDate;

/**
 * Model class representing a Vehicle Rental transaction.
 * Demonstrates: Classes & Objects, Constructors, Associations.
 */
public class Rental {
    private String rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private LocalDate bookingDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private long numberOfDays;
    private double rentalRate;
    private double totalCharge;
    private String status; // BOOKED, ACTIVE, RETURNED, CANCELLED

    /**
     * Default constructor
     */
    public Rental() {
    }

    /**
     * Parameterized constructor for Rental initialization
     */
    public Rental(String rentalId, Customer customer, Vehicle vehicle,
                  LocalDate bookingDate, LocalDate expectedReturnDate,
                  long numberOfDays, double rentalRate, double totalCharge, String status) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.bookingDate = bookingDate;
        this.expectedReturnDate = expectedReturnDate;
        this.numberOfDays = numberOfDays;
        this.rentalRate = rentalRate;
        this.totalCharge = totalCharge;
        this.status = status;
    }

    // Getters and Setters
    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return rentalId + " - " + (customer != null ? customer.getName() : "Unknown") + " | " +
                (vehicle != null ? vehicle.getRegistrationNumber() : "Unknown") + " [" + status + "]";
    }
}
