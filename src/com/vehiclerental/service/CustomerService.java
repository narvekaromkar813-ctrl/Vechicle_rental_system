package com.vehiclerental.service;

import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class handling all business logic for Customer operations.
 * Demonstrates:
 * - CRUD operations on Customer records
 * - ArrayList<Customer> manipulation
 * - Input validation & exception handling
 */
public class CustomerService {

    private final DataStore dataStore;

    public CustomerService() {
        this.dataStore = DataStore.getInstance();
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(dataStore.getCustomers());
    }

    public Customer getCustomerById(String customerId) {
        return dataStore.findCustomerById(customerId);
    }

    /**
     * CREATE: Registers a new customer.
     */
    public void addCustomer(Customer customer) throws ValidationException {
        ValidationUtil.validateNotEmpty("Customer ID", customer.getCustomerId());
        ValidationUtil.validateNotEmpty("Name", customer.getName());
        ValidationUtil.validatePhone(customer.getPhone());
        ValidationUtil.validateEmail(customer.getEmail());
        ValidationUtil.validateNotEmpty("Driving License Number", customer.getDrivingLicenseNumber());
        ValidationUtil.validateNotEmpty("Address", customer.getAddress());

        if (dataStore.findCustomerById(customer.getCustomerId()) != null) {
            throw new ValidationException("Customer ID '" + customer.getCustomerId() + "' already exists!");
        }

        dataStore.addCustomer(customer);
    }

    /**
     * UPDATE: Updates existing customer details.
     */
    public void updateCustomer(Customer customer) throws ValidationException {
        ValidationUtil.validateNotEmpty("Customer ID", customer.getCustomerId());
        ValidationUtil.validateNotEmpty("Name", customer.getName());
        ValidationUtil.validatePhone(customer.getPhone());
        ValidationUtil.validateEmail(customer.getEmail());
        ValidationUtil.validateNotEmpty("Driving License Number", customer.getDrivingLicenseNumber());
        ValidationUtil.validateNotEmpty("Address", customer.getAddress());

        if (dataStore.findCustomerById(customer.getCustomerId()) == null) {
            throw new ValidationException("Customer not found.");
        }

        dataStore.updateCustomer(customer);
    }

    /**
     * DELETE: Removes a customer only if they don't have an active rental.
     */
    public void deleteCustomer(String customerId) throws ValidationException {
        Customer c = dataStore.findCustomerById(customerId);
        if (c == null) {
            throw new ValidationException("Customer not found.");
        }

        // Prevent deletion if customer has an active rental
        for (Rental r : dataStore.getActiveRentals()) {
            if (r.getCustomer() != null && r.getCustomer().getCustomerId().equalsIgnoreCase(customerId)) {
                throw new ValidationException("Cannot delete customer '" + c.getName() + "' because they have an active rental booking!");
            }
        }

        dataStore.deleteCustomer(customerId);
    }

    public String generateNextId() {
        return dataStore.generateNextCustomerId();
    }
}
