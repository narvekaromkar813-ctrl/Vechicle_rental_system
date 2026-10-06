package com.vehiclerental.model;

import com.vehiclerental.util.DateUtil;
import java.time.LocalDate;

/**
 * Model class representing a Billing Invoice for a vehicle rental.
 * Demonstrates: Classes & Objects, Constructors, String formatting.
 */
public class Invoice {
    private String invoiceId;
    private Rental rental;
    private LocalDate invoiceDate;
    private double amountPaid;
    private String paymentStatus; // PAID, PENDING

    public Invoice() {
    }

    public Invoice(String invoiceId, Rental rental, LocalDate invoiceDate, double amountPaid, String paymentStatus) {
        this.invoiceId = invoiceId;
        this.rental = rental;
        this.invoiceDate = invoiceDate;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Generates a clean, formatted text invoice receipt for display and printing.
     */
    public String generateBillText() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================================\n");
        sb.append("                 VEHICLE RENTAL MANAGEMENT SYSTEM                \n");
        sb.append("                 ITM Skills University - Final Project           \n");
        sb.append("                          OFFICIAL INVOICE                       \n");
        sb.append("=================================================================\n");
        sb.append(String.format("Invoice No : %-20s Date   : %s\n", invoiceId, DateUtil.formatDate(invoiceDate)));
        sb.append(String.format("Rental ID  : %-20s Status : %s\n", 
                (rental != null ? rental.getRentalId() : "N/A"), paymentStatus));
        sb.append("-----------------------------------------------------------------\n");
        sb.append(" CUSTOMER DETAILS:\n");
        if (rental != null && rental.getCustomer() != null) {
            Customer c = rental.getCustomer();
            sb.append(String.format("  Customer ID : %s\n", c.getCustomerId()));
            sb.append(String.format("  Name        : %s\n", c.getName()));
            sb.append(String.format("  Phone       : %s\n", c.getPhone()));
            sb.append(String.format("  Email       : %s\n", c.getEmail()));
            sb.append(String.format("  DL Number   : %s\n", c.getDrivingLicenseNumber()));
        } else {
            sb.append("  Customer info not available\n");
        }
        sb.append("-----------------------------------------------------------------\n");
        sb.append(" VEHICLE DETAILS:\n");
        if (rental != null && rental.getVehicle() != null) {
            Vehicle v = rental.getVehicle();
            sb.append(String.format("  Reg. Number : %s\n", v.getRegistrationNumber()));
            sb.append(String.format("  Vehicle     : %s %s\n", v.getBrand(), v.getModel()));
            sb.append(String.format("  Type        : %s\n", v.getType()));
            sb.append(String.format("  Daily Rate  : ₹%.2f\n", v.getRentalPricePerDay()));
        } else {
            sb.append("  Vehicle info not available\n");
        }
        sb.append("-----------------------------------------------------------------\n");
        sb.append(" RENTAL DURATION & CHARGES:\n");
        if (rental != null) {
            sb.append(String.format("  Booking Date       : %s\n", DateUtil.formatDate(rental.getBookingDate())));
            sb.append(String.format("  Expected Return    : %s\n", DateUtil.formatDate(rental.getExpectedReturnDate())));
            if (rental.getActualReturnDate() != null) {
                sb.append(String.format("  Actual Return Date : %s\n", DateUtil.formatDate(rental.getActualReturnDate())));
            }
            sb.append(String.format("  Duration (Days)    : %d day(s)\n", rental.getNumberOfDays()));
            sb.append(String.format("  Rate Per Day       : ₹%.2f\n", rental.getRentalRate()));
            sb.append("-----------------------------------------------------------------\n");
            sb.append(String.format("  TOTAL AMOUNT DUE   : ₹%.2f\n", rental.getTotalCharge()));
            sb.append(String.format("  AMOUNT RECEIVED    : ₹%.2f\n", amountPaid));
        }
        sb.append("-----------------------------------------------------------------\n");
        sb.append(String.format(" PAYMENT STATUS      : %s\n", paymentStatus));
        sb.append("=================================================================\n");
        sb.append("                Thank you for your business!                     \n");
        sb.append("=================================================================\n");
        return sb.toString();
    }
}
