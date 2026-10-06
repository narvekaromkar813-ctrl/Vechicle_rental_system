package com.vehiclerental.service;

import com.vehiclerental.model.Invoice;
import com.vehiclerental.model.Rental;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class handling invoice creation and bill text generation.
 * Demonstrates separation of concerns and formatting.
 */
public class BillingService {

    private int nextInvoiceId = 101;
    private final Map<String, Invoice> invoiceCache = new HashMap<>();

    public Invoice generateInvoice(Rental rental) {
        if (rental == null) return null;

        // Check if invoice already generated for this rental
        if (invoiceCache.containsKey(rental.getRentalId())) {
            return invoiceCache.get(rental.getRentalId());
        }

        String invoiceId = String.format("INV-%04d", nextInvoiceId++);
        String paymentStatus = "RETURNED".equalsIgnoreCase(rental.getStatus()) ? "PAID" : "ACTIVE / PENDING";
        Invoice invoice = new Invoice(
                invoiceId,
                rental,
                LocalDate.now(),
                rental.getTotalCharge(),
                paymentStatus
        );

        invoiceCache.put(rental.getRentalId(), invoice);
        return invoice;
    }
}
