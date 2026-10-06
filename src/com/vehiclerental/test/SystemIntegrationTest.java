package com.vehiclerental.test;

import com.vehiclerental.exception.InvalidRentalException;
import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Invoice;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.service.*;
import com.vehiclerental.util.DateUtil;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Automated Verification & Demonstration Test Suite.
 * Exercises all 10 modules, verifies all required Java concepts,
 * exception scenarios, collections, and array behaviors.
 */
public class SystemIntegrationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("   VEHICLE RENTAL MANAGEMENT SYSTEM - ACADEMIC TEST SUITE        ");
        System.out.println("   Verifying All Required Java Concepts & Business Scenarios     ");
        System.out.println("=================================================================\n");

        VehicleService vehicleService = new VehicleService();
        CustomerService customerService = new CustomerService();
        RentalService rentalService = new RentalService();
        BillingService billingService = new BillingService();
        ReportService reportService = new ReportService();
        DataStore dataStore = DataStore.getInstance();

        // -------------------------------------------------------------
        // TEST 1: Initial Sample Data & Collections Verification
        // -------------------------------------------------------------
        test("Initial Vehicles Loaded in ArrayList", () -> {
            assertCondition(vehicleService.getAllVehicles().size() >= 5, "Initial fleet count >= 5");
        });

        test("Initial Customers Loaded in ArrayList", () -> {
            assertCondition(customerService.getAllCustomers().size() >= 3, "Initial customers count >= 3");
        });

        test("Initial Completed Rentals in LinkedList", () -> {
            LinkedList<Rental> history = dataStore.getRentalHistory();
            assertCondition(history != null && history.size() >= 1, "Rental history LinkedList populated");
        });

        // -------------------------------------------------------------
        // TEST 2: Array Requirement (boolean[] availabilityStatus)
        // -------------------------------------------------------------
        test("Primitive boolean[] availabilityStatus Synchronization", () -> {
            boolean[] array = dataStore.getAvailabilityStatus();
            List<Vehicle> list = dataStore.getVehicles();
            assertCondition(array.length == list.size(), "Array length strictly equals vehicles list size");

            for (int i = 0; i < list.size(); i++) {
                assertCondition(array[i] == list.get(i).isAvailable(),
                        "Array index " + i + " matches vehicle.isAvailable()");
            }
        });

        // -------------------------------------------------------------
        // TEST 3: HashMap Requirement (Registration Number -> Vehicle)
        // -------------------------------------------------------------
        test("HashMap O(1) Fast Vehicle Lookup", () -> {
            Vehicle found = vehicleService.searchByRegistrationNumber("MH01AB1234");
            assertCondition(found != null, "Vehicle found in HashMap");
            assertCondition(found.getBrand().equals("Honda"), "Brand matches Honda");
            assertCondition(found.getModel().equals("City"), "Model matches City");

            Vehicle missing = vehicleService.searchByRegistrationNumber("NON_EXISTENT");
            assertCondition(missing == null, "Non-existent returns null from HashMap");
        });

        // -------------------------------------------------------------
        // TEST 4: TreeMap Requirement (Sorted by Registration Number)
        // -------------------------------------------------------------
        test("TreeMap Natural Key Ordering", () -> {
            List<Vehicle> sorted = vehicleService.getVehiclesSortedByRegistrationTreeMap();
            assertCondition(sorted.size() > 1, "Sorted list has elements");
            for (int i = 0; i < sorted.size() - 1; i++) {
                String reg1 = sorted.get(i).getRegistrationNumber();
                String reg2 = sorted.get(i + 1).getRegistrationNumber();
                assertCondition(reg1.compareTo(reg2) <= 0, "Reg " + reg1 + " <= " + reg2);
            }
        });

        // -------------------------------------------------------------
        // TEST 5: Comparator Sorting (Price, Model, Type)
        // -------------------------------------------------------------
        test("Comparator Sorting by Rental Price Ascending", () -> {
            List<Vehicle> sorted = vehicleService.getVehiclesSorted("Rental Price: Low to High");
            for (int i = 0; i < sorted.size() - 1; i++) {
                assertCondition(sorted.get(i).getRentalPricePerDay() <= sorted.get(i + 1).getRentalPricePerDay(),
                        "Price ascending order maintained");
            }
        });

        // -------------------------------------------------------------
        // TEST 6: Vehicle CRUD (Create, Read, Update, Delete)
        // -------------------------------------------------------------
        test("Vehicle CRUD: Add New Vehicle", () -> {
            Vehicle vNew = new Vehicle("V099", "MH12XY9999", "Tata", "Nexon", "SUV", 2200.0, true);
            vehicleService.addVehicle(vNew);
            Vehicle fetched = vehicleService.searchByRegistrationNumber("MH12XY9999");
            assertCondition(fetched != null && fetched.getModel().equals("Nexon"), "Vehicle added and retrieved via HashMap");
        });

        test("Vehicle CRUD: Duplicate Registration Number Rejected", () -> {
            boolean caught = false;
            try {
                Vehicle vDup = new Vehicle("V100", "MH12XY9999", "Another", "Car", "Sedan", 1000.0, true);
                vehicleService.addVehicle(vDup);
            } catch (ValidationException ve) {
                caught = true;
            }
            assertCondition(caught, "ValidationException thrown for duplicate registration number");
        });

        test("Vehicle CRUD: Update Vehicle", () -> {
            Vehicle v = vehicleService.searchByRegistrationNumber("MH12XY9999");
            v.setRentalPricePerDay(2400.0);
            vehicleService.updateVehicle(v);
            Vehicle updated = vehicleService.searchByRegistrationNumber("MH12XY9999");
            assertCondition(updated.getRentalPricePerDay() == 2400.0, "Vehicle price updated successfully");
        });

        test("Vehicle CRUD: Delete Vehicle", () -> {
            vehicleService.deleteVehicle("V099");
            Vehicle deleted = vehicleService.searchByRegistrationNumber("MH12XY9999");
            assertCondition(deleted == null, "Vehicle removed from fleet and HashMap");
        });

        // -------------------------------------------------------------
        // TEST 7: Customer CRUD & Validation
        // -------------------------------------------------------------
        test("Customer CRUD: Add, Validate, and Retrieve", () -> {
            Customer cust = new Customer("C099", "Test User", "9988776655", "test@domain.com", "DL998877", "Navi Mumbai");
            customerService.addCustomer(cust);
            Customer found = customerService.getCustomerById("C099");
            assertCondition(found != null && found.getName().equals("Test User"), "Customer successfully registered");
        });

        test("Customer Validation: Invalid Phone Rejected", () -> {
            boolean caught = false;
            try {
                Customer invalid = new Customer("C100", "Bad Phone", "12345", "test@test.com", "DL1234", "Pune");
                customerService.addCustomer(invalid);
            } catch (ValidationException ve) {
                caught = true;
            }
            assertCondition(caught, "ValidationException caught for invalid phone number");
        });

        // -------------------------------------------------------------
        // TEST 8: Rental Booking, Charge Calculation & Exceptions
        // -------------------------------------------------------------
        test("Rental Booking & Charge Calculation", () -> {
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(4); // 4 days

            // MH03EF9012 is Toyota Innova (3000/day)
            Rental rental = rentalService.bookVehicle("C001", "MH03EF9012", start, end);
            assertCondition(rental != null, "Rental object created");
            assertCondition(rental.getNumberOfDays() == 4, "Duration correctly calculated as 4 days");
            assertCondition(rental.getTotalCharge() == 12000.0, "Total charge = 4 * 3000 = ₹12000");

            // Vehicle must now be marked unavailable
            Vehicle v = dataStore.findVehicleByRegNo("MH03EF9012");
            assertCondition(!v.isAvailable(), "Vehicle availability set to false");

            // Check boolean[] availabilityStatus array
            boolean[] array = dataStore.getAvailabilityStatus();
            int vIndex = dataStore.getVehicles().indexOf(v);
            assertCondition(!array[vIndex], "boolean[] array index reflects false for booked vehicle");
        });

        test("Rental Exception: Cannot Book Unavailable Vehicle", () -> {
            boolean caught = false;
            try {
                LocalDate start = LocalDate.now();
                LocalDate end = start.plusDays(2);
                // Try booking MH03EF9012 again
                rentalService.bookVehicle("C002", "MH03EF9012", start, end);
            } catch (VehicleNotAvailableException vna) {
                caught = true;
            }
            assertCondition(caught, "VehicleNotAvailableException thrown when attempting to book rented vehicle");
        });

        test("Rental Exception: Invalid Dates Rejected", () -> {
            boolean caught = false;
            try {
                LocalDate start = LocalDate.now();
                LocalDate invalidEnd = start.minusDays(2); // End before start
                rentalService.bookVehicle("C001", "MH04GH3456", start, invalidEnd);
            } catch (InvalidRentalException ire) {
                caught = true;
            }
            assertCondition(caught, "InvalidRentalException thrown when return date is before booking date");
        });

        // -------------------------------------------------------------
        // TEST 9: Vehicle Return, LinkedList Insertion & Availability Restore
        // -------------------------------------------------------------
        test("Vehicle Return & LinkedList History Maintenance", () -> {
            Rental active = dataStore.findActiveRentalByVehicleReg("MH03EF9012");
            assertCondition(active != null, "Active rental retrieved for return");

            int initialHistorySize = dataStore.getRentalHistory().size();
            LocalDate actualReturn = active.getBookingDate().plusDays(4);

            Rental returned = rentalService.processVehicleReturn(active.getRentalId(), actualReturn);
            assertCondition(returned.getStatus().equals("RETURNED"), "Status updated to RETURNED");

            // Vehicle should now be AVAILABLE again
            Vehicle v = dataStore.findVehicleByRegNo("MH03EF9012");
            assertCondition(v.isAvailable(), "Vehicle availability restored to true");

            // boolean[] array updated
            boolean[] array = dataStore.getAvailabilityStatus();
            int vIndex = dataStore.getVehicles().indexOf(v);
            assertCondition(array[vIndex], "boolean[] array index restored to true");

            // LinkedList rentalHistory updated
            LinkedList<Rental> history = dataStore.getRentalHistory();
            assertCondition(history.size() == initialHistorySize + 1, "Rental prepended to LinkedList<Rental>");
            assertCondition(history.getFirst().getRentalId().equals(returned.getRentalId()), "First item in LinkedList is the newly returned rental");
        });

        // -------------------------------------------------------------
        // TEST 10: Billing & Invoice Generation
        // -------------------------------------------------------------
        test("Billing & Invoice Text Generation", () -> {
            Rental past = dataStore.getRentalHistory().getFirst();
            Invoice invoice = billingService.generateInvoice(past);
            assertCondition(invoice != null, "Invoice generated");
            assertCondition(invoice.getInvoiceId() != null, "Invoice ID assigned");

            String billText = invoice.generateBillText();
            assertCondition(billText.contains("OFFICIAL INVOICE"), "Bill text contains title");
            assertCondition(billText.contains("TOTAL AMOUNT DUE"), "Bill text contains total due");
            assertCondition(billText.contains(past.getRentalId()), "Bill text contains Rental ID");
        });

        // -------------------------------------------------------------
        // TEST 11: Dynamic Reporting
        // -------------------------------------------------------------
        test("Operational Reporting Dynamic Calculation", () -> {
            int total = reportService.getTotalVehicles();
            int avail = reportService.getAvailableVehiclesCount();
            int rented = reportService.getRentedVehiclesCount();
            assertCondition(total == avail + rented, "Total vehicles = Available + Rented");
            assertCondition(reportService.getTotalRevenue() > 0, "Total revenue calculated > 0");
        });

        // Clean up test customer
        try {
            customerService.deleteCustomer("C099");
        } catch (Exception ignored) {}

        // Summary
        System.out.println("\n=================================================================");
        System.out.printf("   TEST EXECUTION SUMMARY: %d PASSED, %d FAILED\n", testsPassed, testsFailed);
        System.out.println("=================================================================");

        if (testsFailed == 0) {
            System.out.println("[SUCCESS] ALL REQUIRED CONCEPTS, COLLECTIONS, AND OPERATIONAL FLOWS VERIFIED 100%!");
        } else {
            System.err.println("[ERROR] One or more tests failed. Check assertions above.");
            System.exit(1);
        }
    }

    private static void test(String name, TestCase testBody) {
        try {
            testBody.run();
            testsPassed++;
            System.out.println("  [PASS] " + name);
        } catch (Throwable t) {
            testsFailed++;
            System.err.println("  [FAIL] " + name + " -> " + t.getMessage());
        }
    }

    @FunctionalInterface
    interface TestCase {
        void run() throws Throwable;
    }

    private static void assertCondition(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion Failed: " + message);
        }
    }
}
