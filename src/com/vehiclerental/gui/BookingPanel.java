package com.vehiclerental.gui;

import com.vehiclerental.exception.InvalidRentalException;
import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.exception.VehicleNotAvailableException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.model.Rental;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.CustomerService;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.service.VehicleService;
import com.vehiclerental.util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Rental Booking Panel implementing end-to-end booking workflow:
 * - Customer & Available Vehicle selection
 * - Automatic duration and charge calculation (Days x Rate)
 * - VehicleNotAvailableException & InvalidRentalException handling
 * - Array & collection synchronization
 */
public class BookingPanel extends JPanel {

    private final RentalService rentalService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final Runnable onDataChangedCallback;

    // Booking Form
    private JComboBox<CustomerComboItem> cmbCustomer;
    private JComboBox<VehicleComboItem> cmbVehicle;
    private JTextField txtBookingDate;
    private JTextField txtReturnDate;
    private JTextField txtDailyRate;
    private JTextField txtDurationDays;
    private JTextField txtTotalCharge;

    private JButton btnCalculate;
    private JButton btnConfirmBooking;
    private JButton btnClear;
    private JButton btnRefresh;

    // Active Rentals Table
    private DefaultTableModel activeTableModel;
    private JTable tableActiveRentals;

    public BookingPanel(Runnable onDataChangedCallback) {
        this.rentalService = new RentalService();
        this.customerService = new CustomerService();
        this.vehicleService = new VehicleService();
        this.onDataChangedCallback = onDataChangedCallback;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadDropdownData();
        loadActiveRentals();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Vehicle Rental Booking (Charge Calculation & Validation)",
                "Create new bookings, calculate charges dynamically, and maintain active rentals."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(12, 10));
        centerPanel.setOpaque(false);

        // Left Form Panel
        JPanel formCard = UIStyle.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(380, 0));

        JLabel formTitle = new JLabel("New Booking Form");
        formTitle.setFont(UIStyle.FONT_TITLE);
        formTitle.setForeground(UIStyle.COLOR_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        cmbCustomer = new JComboBox<>();
        cmbCustomer.setFont(UIStyle.FONT_BODY);

        cmbVehicle = new JComboBox<>();
        cmbVehicle.setFont(UIStyle.FONT_BODY);
        cmbVehicle.addActionListener(e -> updateVehicleRateDisplay());

        LocalDate today = LocalDate.now();
        txtBookingDate = new JTextField(DateUtil.formatDate(today));
        UIStyle.styleTextField(txtBookingDate);

        txtReturnDate = new JTextField(DateUtil.formatDate(today.plusDays(3)));
        UIStyle.styleTextField(txtReturnDate);

        txtDailyRate = new JTextField("0.00");
        txtDailyRate.setEditable(false);
        txtDailyRate.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtDailyRate);

        txtDurationDays = new JTextField("3 Days");
        txtDurationDays.setEditable(false);
        txtDurationDays.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtDurationDays);

        txtTotalCharge = new JTextField("₹0.00");
        txtTotalCharge.setEditable(false);
        txtTotalCharge.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtTotalCharge.setForeground(UIStyle.COLOR_PRIMARY);
        txtTotalCharge.setBackground(new Color(230, 240, 250));
        UIStyle.styleTextField(txtTotalCharge);

        int row = 0;
        addField(fieldsPanel, gbc, "Select Customer (ArrayList):", cmbCustomer, row++);
        addField(fieldsPanel, gbc, "Select Available Vehicle:", cmbVehicle, row++);
        addField(fieldsPanel, gbc, "Booking Date (yyyy-MM-dd):", txtBookingDate, row++);
        addField(fieldsPanel, gbc, "Expected Return Date (yyyy-MM-dd):", txtReturnDate, row++);
        addField(fieldsPanel, gbc, "Vehicle Daily Rate (₹):", txtDailyRate, row++);
        addField(fieldsPanel, gbc, "Calculated Duration:", txtDurationDays, row++);
        addField(fieldsPanel, gbc, "Total Estimated Charge (₹):", txtTotalCharge, row++);

        formCard.add(fieldsPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnCalculate = new JButton("Calculate Charge");
        UIStyle.stylePrimaryButton(btnCalculate);
        btnCalculate.addActionListener(e -> calculateCharges());

        btnConfirmBooking = new JButton("Confirm Booking");
        UIStyle.styleSuccessButton(btnConfirmBooking);
        btnConfirmBooking.addActionListener(e -> handleConfirmBooking());

        btnClear = new JButton("Clear Form");
        UIStyle.styleSecondaryButton(btnClear);
        btnClear.addActionListener(e -> resetForm());

        btnRefresh = new JButton("Refresh Lists");
        UIStyle.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            loadDropdownData();
            loadActiveRentals();
        });

        btnPanel.add(btnCalculate);
        btnPanel.add(btnConfirmBooking);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        formCard.add(btnPanel, BorderLayout.SOUTH);
        centerPanel.add(formCard, BorderLayout.WEST);

        // Right Active Rentals Table
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout(5, 5));

        JLabel tblLabel = new JLabel("Currently Active Rentals (Dispatched Vehicles)");
        tblLabel.setFont(UIStyle.FONT_TITLE);
        tblLabel.setForeground(UIStyle.COLOR_PRIMARY);
        tableCard.add(tblLabel, BorderLayout.NORTH);

        String[] cols = {"Rental ID", "Customer", "Reg. Number", "Vehicle Model", "Booking Date", "Expected Return", "Total Charge (₹)", "Status"};
        activeTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableActiveRentals = new JTable(activeTableModel);
        UIStyle.styleTable(tableActiveRentals);
        tableCard.add(new JScrollPane(tableActiveRentals), BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIStyle.FONT_BODY_BOLD);
        panel.add(lbl, gbc);

        gbc.gridy = row * 2 + 1;
        panel.add(field, gbc);
    }

    private void updateVehicleRateDisplay() {
        VehicleComboItem item = (VehicleComboItem) cmbVehicle.getSelectedItem();
        if (item != null && item.vehicle != null) {
            txtDailyRate.setText(String.format("%.2f", item.vehicle.getRentalPricePerDay()));
            calculateChargesSilently();
        } else {
            txtDailyRate.setText("0.00");
        }
    }

    private boolean calculateCharges() {
        try {
            LocalDate bDate = DateUtil.parseDate(txtBookingDate.getText());
            LocalDate rDate = DateUtil.parseDate(txtReturnDate.getText());

            long days = rentalService.calculateRentalDays(bDate, rDate);
            txtDurationDays.setText(days + " Day(s)");

            VehicleComboItem item = (VehicleComboItem) cmbVehicle.getSelectedItem();
            if (item == null || item.vehicle == null) {
                JOptionPane.showMessageDialog(this, "Please select an available vehicle.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            double dailyRate = item.vehicle.getRentalPricePerDay();
            double total = rentalService.calculateTotalCharge(dailyRate, days);
            txtTotalCharge.setText(String.format("₹%.2f", total));
            return true;

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Dates must be formatted as yyyy-MM-dd (e.g. 2026-10-05).", "Invalid Date Format", JOptionPane.WARNING_MESSAGE);
            return false;
        } catch (InvalidRentalException ire) {
            JOptionPane.showMessageDialog(this, ire.getMessage(), "Invalid Rental Dates", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void calculateChargesSilently() {
        try {
            LocalDate bDate = DateUtil.parseDate(txtBookingDate.getText());
            LocalDate rDate = DateUtil.parseDate(txtReturnDate.getText());
            long days = rentalService.calculateRentalDays(bDate, rDate);
            txtDurationDays.setText(days + " Day(s)");

            VehicleComboItem item = (VehicleComboItem) cmbVehicle.getSelectedItem();
            if (item != null && item.vehicle != null) {
                double total = rentalService.calculateTotalCharge(item.vehicle.getRentalPricePerDay(), days);
                txtTotalCharge.setText(String.format("₹%.2f", total));
            }
        } catch (Exception ignored) {
        }
    }

    private void handleConfirmBooking() {
        if (!calculateCharges()) {
            return;
        }

        try {
            CustomerComboItem cItem = (CustomerComboItem) cmbCustomer.getSelectedItem();
            if (cItem == null || cItem.customer == null) {
                throw new ValidationException("Please select a customer.");
            }

            VehicleComboItem vItem = (VehicleComboItem) cmbVehicle.getSelectedItem();
            if (vItem == null || vItem.vehicle == null) {
                throw new ValidationException("Please select an available vehicle.");
            }

            LocalDate bDate = DateUtil.parseDate(txtBookingDate.getText());
            LocalDate rDate = DateUtil.parseDate(txtReturnDate.getText());

            // Calls RentalService which validates and creates the booking
            Rental rental = rentalService.bookVehicle(
                    cItem.customer.getCustomerId(),
                    vItem.vehicle.getRegistrationNumber(),
                    bDate,
                    rDate
            );

            JOptionPane.showMessageDialog(this,
                    "Booking Confirmed Successfully!\n" +
                            "Rental ID   : " + rental.getRentalId() + "\n" +
                            "Customer    : " + rental.getCustomer().getName() + "\n" +
                            "Vehicle     : " + rental.getVehicle().getRegistrationNumber() + "\n" +
                            "Duration    : " + rental.getNumberOfDays() + " Day(s)\n" +
                            "Total Charge: ₹" + String.format("%.2f", rental.getTotalCharge()),
                    "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

            loadDropdownData();
            loadActiveRentals();
            notifyDataChange();

        } catch (VehicleNotAvailableException vna) {
            JOptionPane.showMessageDialog(this, "EXCEPTION: " + vna.getMessage(), "Vehicle Unavailable", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidRentalException ire) {
            JOptionPane.showMessageDialog(this, "EXCEPTION: " + ire.getMessage(), "Invalid Rental", JOptionPane.WARNING_MESSAGE);
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, "VALIDATION: " + ve.getMessage(), "Validation Notice", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        LocalDate today = LocalDate.now();
        txtBookingDate.setText(DateUtil.formatDate(today));
        txtReturnDate.setText(DateUtil.formatDate(today.plusDays(3)));
        if (cmbCustomer.getItemCount() > 0) cmbCustomer.setSelectedIndex(0);
        if (cmbVehicle.getItemCount() > 0) cmbVehicle.setSelectedIndex(0);
        updateVehicleRateDisplay();
    }

    public void loadDropdownData() {
        // Load Customers
        cmbCustomer.removeAllItems();
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer c : customers) {
            cmbCustomer.addItem(new CustomerComboItem(c));
        }

        // Load only Available Vehicles (using the boolean[] availability array logic)
        cmbVehicle.removeAllItems();
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles();
        for (Vehicle v : availableVehicles) {
            cmbVehicle.addItem(new VehicleComboItem(v));
        }

        updateVehicleRateDisplay();
    }

    public void loadActiveRentals() {
        activeTableModel.setRowCount(0);
        List<Rental> active = rentalService.getActiveRentals();
        for (Rental r : active) {
            activeTableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer() != null ? r.getCustomer().getName() : "N/A",
                    r.getVehicle() != null ? r.getVehicle().getRegistrationNumber() : "N/A",
                    r.getVehicle() != null ? (r.getVehicle().getBrand() + " " + r.getVehicle().getModel()) : "N/A",
                    DateUtil.formatDate(r.getBookingDate()),
                    DateUtil.formatDate(r.getExpectedReturnDate()),
                    String.format("%.2f", r.getTotalCharge()),
                    r.getStatus()
            });
        }
    }

    private void notifyDataChange() {
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
    }

    // Helper wrappers for clean JComboBox display
    private static class CustomerComboItem {
        final Customer customer;

        CustomerComboItem(Customer customer) {
            this.customer = customer;
        }

        @Override
        public String toString() {
            return customer.getCustomerId() + " - " + customer.getName() + " (" + customer.getPhone() + ")";
        }
    }

    private static class VehicleComboItem {
        final Vehicle vehicle;

        VehicleComboItem(Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        @Override
        public String toString() {
            return vehicle.getRegistrationNumber() + " - " + vehicle.getBrand() + " " + vehicle.getModel() +
                    " (₹" + vehicle.getRentalPricePerDay() + "/day)";
        }
    }
}
