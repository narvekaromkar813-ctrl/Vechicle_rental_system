package com.vehiclerental.service;

import com.vehiclerental.model.Rental;
import com.vehiclerental.repository.DataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for computing real-time business statistics and reporting.
 * All metrics are calculated dynamically from in-memory collections and array.
 */
public class ReportService {

    private final DataStore dataStore;

    public ReportService() {
        this.dataStore = DataStore.getInstance();
    }

    public int getTotalVehicles() {
        return dataStore.getVehicles().size();
    }

    public int getAvailableVehiclesCount() {
        boolean[] status = dataStore.getAvailabilityStatus();
        int count = 0;
        for (boolean b : status) {
            if (b) count++;
        }
        return count;
    }

    public int getRentedVehiclesCount() {
        return getTotalVehicles() - getAvailableVehiclesCount();
    }

    public int getTotalCustomers() {
        return dataStore.getCustomers().size();
    }

    public int getActiveRentalsCount() {
        return dataStore.getActiveRentals().size();
    }

    public int getCompletedRentalsCount() {
        return dataStore.getRentalHistory().size();
    }

    public double getTotalRevenue() {
        double revenue = 0.0;
        // Revenue from completed rentals
        for (Rental r : dataStore.getRentalHistory()) {
            revenue += r.getTotalCharge();
        }
        // Revenue from active rentals
        for (Rental r : dataStore.getActiveRentals()) {
            revenue += r.getTotalCharge();
        }
        return revenue;
    }

    /**
     * Returns all rental transactions (both active and completed) for reporting.
     */
    public List<Rental> getAllTransactions() {
        List<Rental> all = new ArrayList<>();
        all.addAll(dataStore.getActiveRentals());
        all.addAll(dataStore.getRentalHistory());
        return all;
    }
}
