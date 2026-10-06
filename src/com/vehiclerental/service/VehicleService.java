package com.vehiclerental.service;

import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.util.ValidationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service class handling all business logic for Vehicle operations.
 * Demonstrates:
 * - CRUD operations on Vehicles
 * - HashMap lookup by registration number (O(1) search)
 * - TreeMap retrieval sorted by registration number
 * - Primitive boolean[] array usage for availability inspection
 * - Comparator usage for flexible sorting
 */
public class VehicleService {

    private final DataStore dataStore;

    public VehicleService() {
        this.dataStore = DataStore.getInstance();
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(dataStore.getVehicles());
    }

    /**
     * Demonstrates explicit use of the required boolean[] availabilityStatus array.
     * Iterates through vehicles and uses the primitive array index to filter available vehicles.
     */
    public List<Vehicle> getAvailableVehicles() {
        ArrayList<Vehicle> availableList = new ArrayList<>();
        ArrayList<Vehicle> allVehicles = dataStore.getVehicles();
        boolean[] statusArray = dataStore.getAvailabilityStatus();

        for (int i = 0; i < allVehicles.size(); i++) {
            // Using the required boolean[] array to check availability
            if (i < statusArray.length && statusArray[i]) {
                availableList.add(allVehicles.get(i));
            }
        }
        return availableList;
    }

    /**
     * CREATE: Adds a new vehicle after validation.
     */
    public void addVehicle(Vehicle vehicle) throws ValidationException {
        ValidationUtil.validateNotEmpty("Vehicle ID", vehicle.getVehicleId());
        ValidationUtil.validateRegistrationNumber(vehicle.getRegistrationNumber());
        ValidationUtil.validateNotEmpty("Brand", vehicle.getBrand());
        ValidationUtil.validateNotEmpty("Model", vehicle.getModel());
        ValidationUtil.validateNotEmpty("Type", vehicle.getType());
        ValidationUtil.validateRentalPrice(vehicle.getRentalPricePerDay());

        if (dataStore.isRegNoTaken(vehicle.getRegistrationNumber(), null)) {
            throw new ValidationException("Registration Number '" + vehicle.getRegistrationNumber() + "' already exists!");
        }

        dataStore.addVehicle(vehicle);
    }

    /**
     * UPDATE: Updates existing vehicle details after validation.
     */
    public void updateVehicle(Vehicle vehicle) throws ValidationException {
        ValidationUtil.validateNotEmpty("Vehicle ID", vehicle.getVehicleId());
        ValidationUtil.validateRegistrationNumber(vehicle.getRegistrationNumber());
        ValidationUtil.validateNotEmpty("Brand", vehicle.getBrand());
        ValidationUtil.validateNotEmpty("Model", vehicle.getModel());
        ValidationUtil.validateNotEmpty("Type", vehicle.getType());
        ValidationUtil.validateRentalPrice(vehicle.getRentalPricePerDay());

        if (dataStore.isRegNoTaken(vehicle.getRegistrationNumber(), vehicle.getVehicleId())) {
            throw new ValidationException("Another vehicle already has Registration Number '" + vehicle.getRegistrationNumber() + "'!");
        }

        dataStore.updateVehicle(vehicle);
    }

    /**
     * DELETE: Deletes vehicle only if not currently rented.
     */
    public void deleteVehicle(String vehicleId) throws ValidationException {
        Vehicle vehicle = dataStore.findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new ValidationException("Vehicle not found.");
        }

        // Check if vehicle is currently rented
        if (!vehicle.isAvailable()) {
            throw new ValidationException("Cannot delete vehicle '" + vehicle.getRegistrationNumber() + "' because it is currently rented!");
        }

        // Double check active rentals
        for (Rental r : dataStore.getActiveRentals()) {
            if (r.getVehicle() != null && r.getVehicle().getVehicleId().equalsIgnoreCase(vehicleId)) {
                throw new ValidationException("Cannot delete vehicle because it is part of an active rental booking.");
            }
        }

        dataStore.deleteVehicle(vehicleId);
    }

    /**
     * SEARCH 1: Demonstrates HashMap fast O(1) lookup by Registration Number.
     */
    public Vehicle searchByRegistrationNumber(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) return null;
        // HashMap lookup requirement
        return dataStore.getVehicleMap().get(regNo.trim().toUpperCase());
    }

    /**
     * SEARCH 2: Linear search across ArrayList for model substring match.
     */
    public List<Vehicle> searchByModel(String modelQuery) {
        List<Vehicle> results = new ArrayList<>();
        if (modelQuery == null || modelQuery.trim().isEmpty()) {
            return results;
        }
        String query = modelQuery.trim().toLowerCase();
        for (Vehicle v : dataStore.getVehicles()) {
            if (v.getModel().toLowerCase().contains(query)) {
                results.add(v);
            }
        }
        return results;
    }

    /**
     * SEARCH 3: Linear search across ArrayList for vehicle type match.
     */
    public List<Vehicle> searchByType(String typeQuery) {
        List<Vehicle> results = new ArrayList<>();
        if (typeQuery == null || typeQuery.trim().isEmpty()) {
            return results;
        }
        String query = typeQuery.trim().toLowerCase();
        for (Vehicle v : dataStore.getVehicles()) {
            if (v.getType().toLowerCase().contains(query)) {
                results.add(v);
            }
        }
        return results;
    }

    /**
     * SORTING 1: Demonstrates TreeMap usage to return vehicles naturally sorted
     * by registration number.
     */
    public List<Vehicle> getVehiclesSortedByRegistrationTreeMap() {
        // TreeMap automatically sorts entries by their natural key order (alphabetical)
        return new ArrayList<>(dataStore.getSortedVehicles().values());
    }

    /**
     * SORTING 2: Demonstrates Java Comparator sorting for various criteria.
     */
    public List<Vehicle> getVehiclesSorted(String criterion) {
        List<Vehicle> list = new ArrayList<>(dataStore.getVehicles());

        switch (criterion) {
            case "Registration Number (TreeMap)":
                return getVehiclesSortedByRegistrationTreeMap();

            case "Model":
                list.sort(Comparator.comparing(Vehicle::getModel, String.CASE_INSENSITIVE_ORDER));
                break;

            case "Rental Price: Low to High":
                list.sort(Comparator.comparingDouble(Vehicle::getRentalPricePerDay));
                break;

            case "Rental Price: High to Low":
                list.sort(Comparator.comparingDouble(Vehicle::getRentalPricePerDay).reversed());
                break;

            case "Type":
                list.sort(Comparator.comparing(Vehicle::getType, String.CASE_INSENSITIVE_ORDER));
                break;

            case "Brand":
                list.sort(Comparator.comparing(Vehicle::getBrand, String.CASE_INSENSITIVE_ORDER));
                break;

            default:
                break;
        }
        return list;
    }

    public String generateNextId() {
        return dataStore.generateNextVehicleId();
    }
}
