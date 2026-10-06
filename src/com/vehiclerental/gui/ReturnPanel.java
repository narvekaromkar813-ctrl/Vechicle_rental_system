package com.vehiclerental.gui;

import com.vehiclerental.exception.InvalidRentalException;
import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Vehicle Return Panel:
 * - Selects active rental transaction
 * - Records actual return date
 * - Recalculates total charge if returned on a different date
 * - Restores vehicle availability and updates boolean[] array
 * - Appends completed transaction to LinkedList<Rental> rentalHistory
 * - Direct shortcut to view/print generated invoice
 */
public class ReturnPanel extends JPanel {

    private final RentalService rentalService;
    private final Runnable onDataChangedCallback;
    private final Consumer<String> navigationHandler;

    // Return Form
    private JComboBox<RentalComboItem> cmbActiveRentals;
    private JTextField txtCustomerInfo;
    private JTextField txtVehicleInfo;
    private JTextField txtBookingDate;
    private JTextField txtExpectedReturn;
    private JTextField txtActualReturn;
    private JTextField txtDailyRate;
    private JTextField txtFinalCharge;

    private JButton btnCalculateFinal;
    private JButton btnCompleteReturn;
    private JButton btnRefresh;

    // Active Rentals Table
    private DefaultTableModel activeTableModel;
    private JTable tableActiveRentals;

    public ReturnPanel(Runnable onDataChangedCallback, Consumer<String> navigationHandler) {
        this.rentalService = new RentalService();
        this.onDataChangedCallback = onDataChangedCallback;
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadActiveRentals();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Process Vehicle Return (LinkedList<Rental> & Availability Restore)",
                "Complete active rentals, calculate final billing, restore fleet availability, and update rental history."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Split
        JPanel centerPanel = new JPanel(new BorderLayout(12, 10));
        centerPanel.setOpaque(false);

        // Left Form Card
        JPanel formCard = UIStyle.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(400, 0));

        JLabel formTitle = new JLabel("Vehicle Return Inspection Form");
        formTitle.setFont(UIStyle.FONT_TITLE);
        formTitle.setForeground(UIStyle.COLOR_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        cmbActiveRentals = new JComboBox<>();
        cmbActiveRentals.setFont(UIStyle.FONT_BODY);
        cmbActiveRentals.addActionListener(e -> populateSelectedRentalDetails());

        txtCustomerInfo = new JTextField();
        txtCustomerInfo.setEditable(false);
        txtCustomerInfo.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtCustomerInfo);

        txtVehicleInfo = new JTextField();
        txtVehicleInfo.setEditable(false);
        txtVehicleInfo.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtVehicleInfo);

        txtBookingDate = new JTextField();
        txtBookingDate.setEditable(false);
        txtBookingDate.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtBookingDate);

        txtExpectedReturn = new JTextField();
        txtExpectedReturn.setEditable(false);
        txtExpectedReturn.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtExpectedReturn);

        txtActualReturn = new JTextField(DateUtil.getTodayString());
        UIStyle.styleTextField(txtActualReturn);

        txtDailyRate = new JTextField();
        txtDailyRate.setEditable(false);
        txtDailyRate.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtDailyRate);

        txtFinalCharge = new JTextField("₹0.00");
        txtFinalCharge.setEditable(false);
        txtFinalCharge.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtFinalCharge.setForeground(UIStyle.COLOR_PRIMARY);
        txtFinalCharge.setBackground(new Color(230, 240, 250));
        UIStyle.styleTextField(txtFinalCharge);

        int row = 0;
        addField(fieldsPanel, gbc, "Select Active Rental:", cmbActiveRentals, row++);
        addField(fieldsPanel, gbc, "Customer Name:", txtCustomerInfo, row++);
        addField(fieldsPanel, gbc, "Vehicle Info:", txtVehicleInfo, row++);
        addField(fieldsPanel, gbc, "Original Booking Date:", txtBookingDate, row++);
        addField(fieldsPanel, gbc, "Expected Return Date:", txtExpectedReturn, row++);
        addField(fieldsPanel, gbc, "Daily Rate (₹):", txtDailyRate, row++);
        addField(fieldsPanel, gbc, "Actual Return Date (yyyy-MM-dd):", txtActualReturn, row++);
        addField(fieldsPanel, gbc, "Final Calculated Charge (₹):", txtFinalCharge, row++);

        formCard.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnCalculateFinal = new JButton("Calculate Charge");
        UIStyle.stylePrimaryButton(btnCalculateFinal);
        btnCalculateFinal.addActionListener(e -> calculateFinalCharge());

        btnCompleteReturn = new JButton("Complete Return");
        UIStyle.styleSuccessButton(btnCompleteReturn);
        btnCompleteReturn.addActionListener(e -> handleCompleteReturn());

        btnRefresh = new JButton("Refresh");
        UIStyle.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadActiveRentals());

        JButton btnViewBill = new JButton("Go to Billing");
        UIStyle.styleSecondaryButton(btnViewBill);
        btnViewBill.addActionListener(e -> {
            if (navigationHandler != null) {
                navigationHandler.accept("BILLING");
            }
        });

        btnPanel.add(btnCalculateFinal);
        btnPanel.add(btnCompleteReturn);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnViewBill);

        formCard.add(btnPanel, BorderLayout.SOUTH);
        centerPanel.add(formCard, BorderLayout.WEST);

        // Right Active Rentals List
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout(5, 5));

        JLabel tblLabel = new JLabel("Currently Dispatched / Active Fleet Rentals");
        tblLabel.setFont(UIStyle.FONT_TITLE);
        tblLabel.setForeground(UIStyle.COLOR_PRIMARY);
        tableCard.add(tblLabel, BorderLayout.NORTH);

        String[] cols = {"Rental ID", "Customer", "Reg. Number", "Brand & Model", "Booking Date", "Expected Return", "Total Charge (₹)"};
        activeTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableActiveRentals = new JTable(activeTableModel);
        UIStyle.styleTable(tableActiveRentals);
        tableActiveRentals.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableActiveRentals.getSelectedRow() >= 0) {
                String selectedRentalId = tableActiveRentals.getValueAt(tableActiveRentals.getSelectedRow(), 0).toString();
                selectRentalInCombo(selectedRentalId);
            }
        });

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

    private void selectRentalInCombo(String rentalId) {
        for (int i = 0; i < cmbActiveRentals.getItemCount(); i++) {
            RentalComboItem item = cmbActiveRentals.getItemAt(i);
            if (item != null && item.rental != null && item.rental.getRentalId().equalsIgnoreCase(rentalId)) {
                cmbActiveRentals.setSelectedIndex(i);
                break;
            }
        }
    }

    private void populateSelectedRentalDetails() {
        RentalComboItem item = (RentalComboItem) cmbActiveRentals.getSelectedItem();
        if (item != null && item.rental != null) {
            Rental r = item.rental;
            txtCustomerInfo.setText(r.getCustomer() != null ? r.getCustomer().getName() : "N/A");
            txtVehicleInfo.setText(r.getVehicle() != null ? (r.getVehicle().getRegistrationNumber() + " (" + r.getVehicle().getModel() + ")") : "N/A");
            txtBookingDate.setText(DateUtil.formatDate(r.getBookingDate()));
            txtExpectedReturn.setText(DateUtil.formatDate(r.getExpectedReturnDate()));
            txtDailyRate.setText(String.format("%.2f", r.getRentalRate()));
            calculateFinalCharge();
        } else {
            txtCustomerInfo.setText("");
            txtVehicleInfo.setText("");
            txtBookingDate.setText("");
            txtExpectedReturn.setText("");
            txtDailyRate.setText("");
            txtFinalCharge.setText("₹0.00");
        }
    }

    private boolean calculateFinalCharge() {
        RentalComboItem item = (RentalComboItem) cmbActiveRentals.getSelectedItem();
        if (item == null || item.rental == null) {
            return false;
        }

        try {
            LocalDate actualReturn = DateUtil.parseDate(txtActualReturn.getText());
            Rental r = item.rental;

            if (actualReturn.isBefore(r.getBookingDate())) {
                JOptionPane.showMessageDialog(this, "Actual return date cannot be before booking date (" +
                        DateUtil.formatDate(r.getBookingDate()) + ").", "Invalid Return Date", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            long days = DateUtil.calculateDays(r.getBookingDate(), actualReturn);
            double total = rentalService.calculateTotalCharge(r.getRentalRate(), days);
            txtFinalCharge.setText(String.format("₹%.2f (%d days)", total, days));
            return true;

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Actual return date must be in yyyy-MM-dd format.", "Date Format Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private void handleCompleteReturn() {
        RentalComboItem item = (RentalComboItem) cmbActiveRentals.getSelectedItem();
        if (item == null || item.rental == null) {
            JOptionPane.showMessageDialog(this, "Please select an active rental to return.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!calculateFinalCharge()) {
            return;
        }

        try {
            LocalDate actualReturn = DateUtil.parseDate(txtActualReturn.getText());
            String rentalId = item.rental.getRentalId();

            Rental completed = rentalService.processVehicleReturn(rentalId, actualReturn);

            int choice = JOptionPane.showConfirmDialog(this,
                    "Vehicle Return Processed Successfully!\n" +
                            "Rental ID   : " + completed.getRentalId() + "\n" +
                            "Vehicle     : " + completed.getVehicle().getRegistrationNumber() + "\n" +
                            "Status      : RETURNED (Vehicle is now AVAILABLE)\n" +
                            "Final Charge: ₹" + String.format("%.2f", completed.getTotalCharge()) + "\n\n" +
                            "Would you like to view the Billing Invoice now?",
                    "Return Completed", JOptionPane.YES_NO_OPTION);

            loadActiveRentals();
            notifyDataChange();

            if (choice == JOptionPane.YES_OPTION && navigationHandler != null) {
                navigationHandler.accept("BILLING");
            }

        } catch (InvalidRentalException | ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Return Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadActiveRentals() {
        cmbActiveRentals.removeAllItems();
        activeTableModel.setRowCount(0);

        List<Rental> active = rentalService.getActiveRentals();
        for (Rental r : active) {
            cmbActiveRentals.addItem(new RentalComboItem(r));
            activeTableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer() != null ? r.getCustomer().getName() : "N/A",
                    r.getVehicle() != null ? r.getVehicle().getRegistrationNumber() : "N/A",
                    r.getVehicle() != null ? (r.getVehicle().getBrand() + " " + r.getVehicle().getModel()) : "N/A",
                    DateUtil.formatDate(r.getBookingDate()),
                    DateUtil.formatDate(r.getExpectedReturnDate()),
                    String.format("%.2f", r.getTotalCharge())
            });
        }

        if (active.isEmpty()) {
            txtCustomerInfo.setText("");
            txtVehicleInfo.setText("");
            txtBookingDate.setText("");
            txtExpectedReturn.setText("");
            txtDailyRate.setText("");
            txtFinalCharge.setText("₹0.00");
        } else {
            populateSelectedRentalDetails();
        }
    }

    private void notifyDataChange() {
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
    }

    private static class RentalComboItem {
        final Rental rental;

        RentalComboItem(Rental rental) {
            this.rental = rental;
        }

        @Override
        public String toString() {
            return rental.getRentalId() + " - " +
                    (rental.getCustomer() != null ? rental.getCustomer().getName() : "N/A") + " (" +
                    (rental.getVehicle() != null ? rental.getVehicle().getRegistrationNumber() : "N/A") + ")";
        }
    }
}
