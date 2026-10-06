package com.vehiclerental.repository;

import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * DataStore is the centralized in-memory repository for the application.
 * It demonstrates core Java collections and array structures:
 * 1. boolean[] availabilityStatus   -> Primitive array tracking availability
 * 2. ArrayList<Vehicle>             -> Dynamic list storing vehicles
 * 3. ArrayList<Customer>            -> Dynamic list storing registered customers
 * 4. LinkedList<Rental>             -> Linked list storing completed rental history
 * 5. HashMap<String, Vehicle>       -> Key: Reg No -> Vehicle for O(1) fast lookup
 * 6. TreeMap<String, Vehicle>       -> Key: Reg No -> Vehicle sorted alphabetically
 */
public class DataStore {

    private static DataStore instance;

    // 1. Core Collections required by the Case Study
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Customer> customers;
    private LinkedList<Rental> rentalHistory;
    private ArrayList<Rental> activeRentals;

    // 2. Maps required by the Case Study
    private HashMap<String, Vehicle> vehicleMap;
    private TreeMap<String, Vehicle> sortedVehicles;

    // 3. Array required by the Case Study
    private boolean[] availabilityStatus;

    // Counters for auto-generated IDs
    private int nextVehicleId = 6;
    private int nextCustomerId = 4;
    private int nextRentalId = 1003;

    private DataStore() {
        vehicles = new ArrayList<>();
        customers = new ArrayList<>();
        rentalHistory = new LinkedList<>();
        activeRentals = new ArrayList<>();
        vehicleMap = new HashMap<>();
        sortedVehicles = new TreeMap<>();

        initSampleData();
        syncAvailabilityArray();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Initializes realistic sample data for testing and viva demonstration.
     */
    private void initSampleData() {
        // Sample Vehicles
        Vehicle v1 = new Vehicle("V001", "MH01AB1234", "Honda", "City", "Sedan", 1800.0, true);
        Vehicle v2 = new Vehicle("V002", "MH02CD5678", "Hyundai", "Creta", "SUV", 2500.0, false); // Currently rented
        Vehicle v3 = new Vehicle("V003", "MH03EF9012", "Toyota", "Innova", "SUV", 3000.0, true);
        Vehicle v4 = new Vehicle("V004", "MH04GH3456", "Maruti", "Swift", "Hatchback", 1500.0, true);
        Vehicle v5 = new Vehicle("V005", "MH05IJ7890", "Royal Enfield", "Classic", "Bike", 1000.0, true);

        addVehicleInternal(v1);
        addVehicleInternal(v2);
        addVehicleInternal(v3);
        addVehicleInternal(v4);
        addVehicleInternal(v5);

        // Sample Customers
        Customer c1 = new Customer("C001", "Omkar Narvekar", "9876543210", "omkar.narvekar@itm.edu", "MH0120220012345", "Dadar, Mumbai");
        Customer c2 = new Customer("C002", "Rahul Sharma", "9823456789", "rahul.sharma@gmail.com", "MH0220210098765", "Kothrud, Pune");
        Customer c3 = new Customer("C003", "Aditya Patil", "9812345678", "aditya.patil@yahoo.com", "MH0320230045678", "Naupada, Thane");

        customers.add(c1);
        customers.add(c2);
        customers.add(c3);

        // 1 Completed Rental in LinkedList rentalHistory
        LocalDate pastBooking = LocalDate.now().minusDays(5);
        LocalDate pastReturn = LocalDate.now().minusDays(2);
        Rental pastRental = new Rental("R1001", c1, v1, pastBooking, pastReturn, 3, 1800.0, 5400.0, "RETURNED");
        pastRental.setActualReturnDate(pastReturn);
        rentalHistory.add(pastRental);

        // 1 Active Rental currently using v2 (Hyundai Creta)
        LocalDate activeBooking = LocalDate.now().minusDays(1);
        LocalDate activeExpectedReturn = LocalDate.now().plusDays(2);
        Rental activeRental = new Rental("R1002", c2, v2, activeBooking, activeExpectedReturn, 3, 2500.0, 7500.0, "ACTIVE");
        activeRentals.add(activeRental);
    }

    /**
     * Internal helper to insert vehicle into list, HashMap, and TreeMap.
     */
    private void addVehicleInternal(Vehicle v) {
        vehicles.add(v);
        vehicleMap.put(v.getRegistrationNumber(), v);
        sortedVehicles.put(v.getRegistrationNumber(), v);
    }

    /**
     * Synchronizes the boolean[] availabilityStatus array with the current vehicles list.
     * Index i in availabilityStatus corresponds strictly to vehicles.get(i).
     */
    public synchronized void syncAvailabilityArray() {
        availabilityStatus = new boolean[vehicles.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            availabilityStatus[i] = vehicles.get(i).isAvailable();
        }
    }

    // -------------------------------------------------------------
    // VEHICLE DATA ACCESS & CRUD
    // -------------------------------------------------------------

    public synchronized ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public synchronized HashMap<String, Vehicle> getVehicleMap() {
        return vehicleMap;
    }

    public synchronized TreeMap<String, Vehicle> getSortedVehicles() {
        return sortedVehicles;
    }

    public synchronized boolean[] getAvailabilityStatus() {
        return availabilityStatus;
    }

    public synchronized Vehicle findVehicleByRegNo(String regNo) {
        if (regNo == null) return null;
        return vehicleMap.get(regNo.trim().toUpperCase());
    }

    public synchronized Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null) return null;
        for (Vehicle v : vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(vehicleId.trim())) {
                return v;
            }
        }
        return null;
    }

    public synchronized boolean isRegNoTaken(String regNo, String excludeVehicleId) {
        Vehicle existing = findVehicleByRegNo(regNo);
        if (existing == null) return false;
        if (excludeVehicleId != null && existing.getVehicleId().equalsIgnoreCase(excludeVehicleId)) {
            return false;
        }
        return true;
    }

    public synchronized void addVehicle(Vehicle v) {
        addVehicleInternal(v);
        syncAvailabilityArray();
    }

    public synchronized void updateVehicle(Vehicle updated) {
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle current = vehicles.get(i);
            if (current.getVehicleId().equalsIgnoreCase(updated.getVehicleId())) {
                // If reg number changed, clean old key from maps
                if (!current.getRegistrationNumber().equalsIgnoreCase(updated.getRegistrationNumber())) {
                    vehicleMap.remove(current.getRegistrationNumber());
                    sortedVehicles.remove(current.getRegistrationNumber());
                }
                vehicles.set(i, updated);
                vehicleMap.put(updated.getRegistrationNumber(), updated);
                sortedVehicles.put(updated.getRegistrationNumber(), updated);
                break;
            }
        }
        syncAvailabilityArray();
    }

    public synchronized boolean deleteVehicle(String vehicleId) {
        Vehicle target = findVehicleById(vehicleId);
        if (target == null) return false;

        vehicles.remove(target);
        vehicleMap.remove(target.getRegistrationNumber());
        sortedVehicles.remove(target.getRegistrationNumber());
        syncAvailabilityArray();
        return true;
    }

    public synchronized String generateNextVehicleId() {
        return String.format("V%03d", nextVehicleId++);
    }

    // -------------------------------------------------------------
    // CUSTOMER DATA ACCESS & CRUD
    // -------------------------------------------------------------

    public synchronized ArrayList<Customer> getCustomers() {
        return customers;
    }

    public synchronized Customer findCustomerById(String customerId) {
        if (customerId == null) return null;
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(customerId.trim())) {
                return c;
            }
        }
        return null;
    }

    public synchronized void addCustomer(Customer c) {
        customers.add(c);
    }

    public synchronized void updateCustomer(Customer updated) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equalsIgnoreCase(updated.getCustomerId())) {
                customers.set(i, updated);
                break;
            }
        }
    }

    public synchronized boolean deleteCustomer(String customerId) {
        Customer c = findCustomerById(customerId);
        if (c != null) {
            return customers.remove(c);
        }
        return false;
    }

    public synchronized String generateNextCustomerId() {
        return String.format("C%03d", nextCustomerId++);
    }

    // -------------------------------------------------------------
    // RENTAL TRANSACTIONS & HISTORY
    // -------------------------------------------------------------

    public synchronized ArrayList<Rental> getActiveRentals() {
        return activeRentals;
    }

    public synchronized LinkedList<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public synchronized Rental findActiveRentalById(String rentalId) {
        if (rentalId == null) return null;
        for (Rental r : activeRentals) {
            if (r.getRentalId().equalsIgnoreCase(rentalId.trim())) {
                return r;
            }
        }
        return null;
    }

    public synchronized Rental findActiveRentalByVehicleReg(String regNo) {
        if (regNo == null) return null;
        for (Rental r : activeRentals) {
            if (r.getVehicle() != null && r.getVehicle().getRegistrationNumber().equalsIgnoreCase(regNo.trim())) {
                return r;
            }
        }
        return null;
    }

    public synchronized void addActiveRental(Rental rental) {
        activeRentals.add(rental);
        // Vehicle is now booked
        if (rental.getVehicle() != null) {
            rental.getVehicle().setAvailable(false);
            syncAvailabilityArray();
        }
    }

    public synchronized void processReturn(Rental rental, LocalDate actualReturnDate, double finalCharge) {
        activeRentals.remove(rental);
        rental.setStatus("RETURNED");
        rental.setActualReturnDate(actualReturnDate);
        rental.setTotalCharge(finalCharge);

        if (rental.getVehicle() != null) {
            rental.getVehicle().setAvailable(true);
        }

        // Add to LinkedList rental history (maintaining chronological history)
        rentalHistory.addFirst(rental);

        // Update availability array
        syncAvailabilityArray();
    }

    public synchronized String generateNextRentalId() {
        return String.format("R%04d", nextRentalId++);
    }
}
