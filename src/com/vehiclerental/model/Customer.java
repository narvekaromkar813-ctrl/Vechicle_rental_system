package com.vehiclerental.model;

/**
 * Model class representing a Customer.
 * Demonstrates: Classes & Objects, Constructors, Encapsulation.
 */
public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;
    private String drivingLicenseNumber;
    private String address;

    /**
     * Default constructor
     */
    public Customer() {
    }

    /**
     * Parameterized constructor for Customer initialization
     */
    public Customer(String customerId, String name, String phone, String email,
                    String drivingLicenseNumber, String address) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return customerId + " - " + name + " (" + phone + ")";
    }
}
