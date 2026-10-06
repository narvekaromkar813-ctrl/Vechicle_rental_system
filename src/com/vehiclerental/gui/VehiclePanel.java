package com.vehiclerental.gui;

import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vehicle Management Panel implementing full CRUD operations:
 * - CREATE : Add new vehicle with validation
 * - READ   : Display fleet in JTable
 * - UPDATE : Edit selected vehicle details
 * - DELETE : Remove vehicle (validates not currently rented)
 */
public class VehiclePanel extends JPanel {

    private final VehicleService vehicleService;
    private final Runnable onDataChangedCallback;

    // Form Fields
    private JTextField txtVehicleId;
    private JTextField txtRegNo;
    private JTextField txtBrand;
    private JTextField txtModel;
    private JComboBox<String> cmbType;
    private JTextField txtPricePerDay;
    private JComboBox<String> cmbAvailability;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;

    // Table
    private DefaultTableModel tableModel;
    private JTable tableVehicles;

    public VehiclePanel(Runnable onDataChangedCallback) {
        this.vehicleService = new VehicleService();
        this.onDataChangedCallback = onDataChangedCallback;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadTableData();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Vehicle Management (CRUD Operations)",
                "Create, View, Update, and Delete vehicles from the fleet."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Main split panel (Form on Left, Table on Right)
        JPanel centerPanel = new JPanel(new BorderLayout(12, 10));
        centerPanel.setOpaque(false);

        // Left Form Panel
        JPanel formCard = UIStyle.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(360, 0));

        JLabel formTitle = new JLabel("Vehicle Details Form");
        formTitle.setFont(UIStyle.FONT_TITLE);
        formTitle.setForeground(UIStyle.COLOR_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        txtVehicleId = new JTextField(vehicleService.generateNextId());
        txtVehicleId.setEditable(false);
        txtVehicleId.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtVehicleId);

        txtRegNo = new JTextField();
        UIStyle.styleTextField(txtRegNo);

        txtBrand = new JTextField();
        UIStyle.styleTextField(txtBrand);

        txtModel = new JTextField();
        UIStyle.styleTextField(txtModel);

        String[] types = {"Sedan", "SUV", "Hatchback", "Bike", "Van"};
        cmbType = new JComboBox<>(types);
        cmbType.setFont(UIStyle.FONT_BODY);

        txtPricePerDay = new JTextField();
        UIStyle.styleTextField(txtPricePerDay);

        String[] availOptions = {"Available", "Rented"};
        cmbAvailability = new JComboBox<>(availOptions);
        cmbAvailability.setFont(UIStyle.FONT_BODY);

        // Add fields to GridBagLayout
        int row = 0;
        addField(fieldsPanel, gbc, "Vehicle ID:", txtVehicleId, row++);
        addField(fieldsPanel, gbc, "Reg. Number (e.g. MH01AB1234):", txtRegNo, row++);
        addField(fieldsPanel, gbc, "Brand (e.g. Honda):", txtBrand, row++);
        addField(fieldsPanel, gbc, "Model (e.g. City):", txtModel, row++);
        addField(fieldsPanel, gbc, "Vehicle Type:", cmbType, row++);
        addField(fieldsPanel, gbc, "Rental Price / Day (₹):", txtPricePerDay, row++);
        addField(fieldsPanel, gbc, "Availability Status:", cmbAvailability, row++);

        formCard.add(fieldsPanel, BorderLayout.CENTER);

        // Action Buttons Grid
        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 6, 6));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAdd = new JButton("Add Vehicle");
        UIStyle.styleSuccessButton(btnAdd);
        btnAdd.addActionListener(e -> handleAddVehicle());

        btnUpdate = new JButton("Update");
        UIStyle.stylePrimaryButton(btnUpdate);
        btnUpdate.addActionListener(e -> handleUpdateVehicle());

        btnDelete = new JButton("Delete");
        UIStyle.styleDangerButton(btnDelete);
        btnDelete.addActionListener(e -> handleDeleteVehicle());

        btnClear = new JButton("Clear");
        UIStyle.styleSecondaryButton(btnClear);
        btnClear.addActionListener(e -> clearForm());

        btnRefresh = new JButton("Refresh");
        UIStyle.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadTableData());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        formCard.add(btnPanel, BorderLayout.SOUTH);
        centerPanel.add(formCard, BorderLayout.WEST);

        // Right Table Panel
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout(5, 5));

        JLabel tblLabel = new JLabel("Registered Fleet Vehicles (ArrayList & HashMap)");
        tblLabel.setFont(UIStyle.FONT_TITLE);
        tblLabel.setForeground(UIStyle.COLOR_PRIMARY);
        tableCard.add(tblLabel, BorderLayout.NORTH);

        String[] cols = {"Vehicle ID", "Reg. Number", "Brand", "Model", "Type", "Price/Day (₹)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableVehicles = new JTable(tableModel);
        UIStyle.styleTable(tableVehicles);
        tableVehicles.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });

        tableCard.add(new JScrollPane(tableVehicles), BorderLayout.CENTER);
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

    private void handleAddVehicle() {
        try {
            String vId = txtVehicleId.getText().trim();
            String regNo = txtRegNo.getText().trim();
            String brand = txtBrand.getText().trim();
            String model = txtModel.getText().trim();
            String type = (String) cmbType.getSelectedItem();
            double price;

            try {
                price = Double.parseDouble(txtPricePerDay.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new ValidationException("Rental price must be a valid number.");
            }

            boolean isAvailable = "Available".equals(cmbAvailability.getSelectedItem());

            Vehicle v = new Vehicle(vId, regNo, brand, model, type, price, isAvailable);
            vehicleService.addVehicle(v);

            JOptionPane.showMessageDialog(this,
                    "Vehicle added successfully!\nRegistration: " + v.getRegistrationNumber(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            loadTableData();
            notifyDataChange();

        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateVehicle() {
        int selectedRow = tableVehicles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String vId = txtVehicleId.getText().trim();
            String regNo = txtRegNo.getText().trim();
            String brand = txtBrand.getText().trim();
            String model = txtModel.getText().trim();
            String type = (String) cmbType.getSelectedItem();
            double price;

            try {
                price = Double.parseDouble(txtPricePerDay.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new ValidationException("Rental price must be a valid number.");
            }

            boolean isAvailable = "Available".equals(cmbAvailability.getSelectedItem());

            Vehicle v = new Vehicle(vId, regNo, brand, model, type, price, isAvailable);
            vehicleService.updateVehicle(v);

            JOptionPane.showMessageDialog(this, "Vehicle updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            notifyDataChange();

        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteVehicle() {
        int selectedRow = tableVehicles.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String vId = txtVehicleId.getText().trim();
        String regNo = txtRegNo.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete vehicle " + regNo + " (" + vId + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                vehicleService.deleteVehicle(vId);
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
                notifyDataChange();
            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(this, ve.getMessage(), "Operation Disallowed", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = tableVehicles.getSelectedRow();
        if (selectedRow >= 0) {
            txtVehicleId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtRegNo.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtBrand.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtModel.setText(tableModel.getValueAt(selectedRow, 3).toString());
            cmbType.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            txtPricePerDay.setText(tableModel.getValueAt(selectedRow, 5).toString());
            cmbAvailability.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());
        }
    }

    private void clearForm() {
        txtVehicleId.setText(vehicleService.generateNextId());
        txtRegNo.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        cmbType.setSelectedIndex(0);
        txtPricePerDay.setText("");
        cmbAvailability.setSelectedIndex(0);
        tableVehicles.clearSelection();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Vehicle> list = vehicleService.getAllVehicles();
        for (Vehicle v : list) {
            tableModel.addRow(new Object[]{
                    v.getVehicleId(),
                    v.getRegistrationNumber(),
                    v.getBrand(),
                    v.getModel(),
                    v.getType(),
                    String.format("%.2f", v.getRentalPricePerDay()),
                    v.isAvailable() ? "Available" : "Rented"
            });
        }
    }

    private void notifyDataChange() {
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
    }
}
