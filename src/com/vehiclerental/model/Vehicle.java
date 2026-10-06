package com.vehiclerental.model;

/**
 * Model class representing a Vehicle in the rental fleet.
 * Demonstrates: Classes & Objects, Constructors, Encapsulation.
 */
public class Vehicle {
    private String vehicleId;
    private String registrationNumber;
    private String brand;
    private String model;
    private String type; // Sedan, SUV, Hatchback, Bike, Van
    private double rentalPricePerDay;
    private boolean available;

    /**
     * Default constructor
     */
    public Vehicle() {
        this.available = true;
    }

    /**
     * Parameterized constructor for Vehicle initialization
     */
    public Vehicle(String vehicleId, String registrationNumber, String brand, String model,
                   String type, double rentalPricePerDay, boolean available) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber != null ? registrationNumber.toUpperCase().trim() : "";
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.rentalPricePerDay = rentalPricePerDay;
        this.available = available;
    }

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber != null ? registrationNumber.toUpperCase().trim() : "";
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return registrationNumber + " - " + brand + " " + model + " (" + type + ") [₹" + rentalPricePerDay + "/day]";
    }
}
