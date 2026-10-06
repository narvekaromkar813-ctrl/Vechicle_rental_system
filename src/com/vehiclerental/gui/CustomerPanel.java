package com.vehiclerental.gui;

import com.vehiclerental.exception.ValidationException;
import com.vehiclerental.model.Customer;
import com.vehiclerental.service.CustomerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Customer Registration Panel implementing full CRUD operations:
 * - CREATE : Register new customer
 * - READ   : Display customer list from ArrayList<Customer>
 * - UPDATE : Modify customer contact / license details
 * - DELETE : Remove customer (checks no active rental exists)
 */
public class CustomerPanel extends JPanel {

    private final CustomerService customerService;
    private final Runnable onDataChangedCallback;

    // Form Fields
    private JTextField txtCustomerId;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtLicenseNo;
    private JTextField txtAddress;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnRefresh;

    // Table
    private DefaultTableModel tableModel;
    private JTable tableCustomers;

    public CustomerPanel(Runnable onDataChangedCallback) {
        this.customerService = new CustomerService();
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
                "Customer Registration & Records (ArrayList<Customer>)",
                "Register, View, Update, and Manage customer profiles and licenses."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Split
        JPanel centerPanel = new JPanel(new BorderLayout(12, 10));
        centerPanel.setOpaque(false);

        // Left Form Card
        JPanel formCard = UIStyle.createCardPanel();
        formCard.setLayout(new BorderLayout(10, 10));
        formCard.setPreferredSize(new Dimension(360, 0));

        JLabel formTitle = new JLabel("Customer Information Form");
        formTitle.setFont(UIStyle.FONT_TITLE);
        formTitle.setForeground(UIStyle.COLOR_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        txtCustomerId = new JTextField(customerService.generateNextId());
        txtCustomerId.setEditable(false);
        txtCustomerId.setBackground(new Color(240, 240, 240));
        UIStyle.styleTextField(txtCustomerId);

        txtName = new JTextField();
        UIStyle.styleTextField(txtName);

        txtPhone = new JTextField();
        UIStyle.styleTextField(txtPhone);

        txtEmail = new JTextField();
        UIStyle.styleTextField(txtEmail);

        txtLicenseNo = new JTextField();
        UIStyle.styleTextField(txtLicenseNo);

        txtAddress = new JTextField();
        UIStyle.styleTextField(txtAddress);

        int row = 0;
        addField(fieldsPanel, gbc, "Customer ID:", txtCustomerId, row++);
        addField(fieldsPanel, gbc, "Full Name:", txtName, row++);
        addField(fieldsPanel, gbc, "Phone (10 Digits):", txtPhone, row++);
        addField(fieldsPanel, gbc, "Email Address:", txtEmail, row++);
        addField(fieldsPanel, gbc, "Driving License No:", txtLicenseNo, row++);
        addField(fieldsPanel, gbc, "City / Address:", txtAddress, row++);

        formCard.add(fieldsPanel, BorderLayout.CENTER);

        // Action Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 6, 6));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnAdd = new JButton("Register");
        UIStyle.styleSuccessButton(btnAdd);
        btnAdd.addActionListener(e -> handleAddCustomer());

        btnUpdate = new JButton("Update");
        UIStyle.stylePrimaryButton(btnUpdate);
        btnUpdate.addActionListener(e -> handleUpdateCustomer());

        btnDelete = new JButton("Delete");
        UIStyle.styleDangerButton(btnDelete);
        btnDelete.addActionListener(e -> handleDeleteCustomer());

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

        // Right Table Card
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout(5, 5));

        JLabel tblLabel = new JLabel("Registered Customers Database");
        tblLabel.setFont(UIStyle.FONT_TITLE);
        tblLabel.setForeground(UIStyle.COLOR_PRIMARY);
        tableCard.add(tblLabel, BorderLayout.NORTH);

        String[] cols = {"Customer ID", "Full Name", "Phone", "Email", "License No", "Address"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableCustomers = new JTable(tableModel);
        UIStyle.styleTable(tableCustomers);
        tableCustomers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });

        tableCard.add(new JScrollPane(tableCustomers), BorderLayout.CENTER);
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

    private void handleAddCustomer() {
        try {
            Customer c = new Customer(
                    txtCustomerId.getText().trim(),
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtLicenseNo.getText().trim(),
                    txtAddress.getText().trim()
            );

            customerService.addCustomer(c);
            JOptionPane.showMessageDialog(this, "Customer registered successfully!\nID: " + c.getCustomerId(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            loadTableData();
            notifyDataChange();

        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateCustomer() {
        int selectedRow = tableCustomers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Customer c = new Customer(
                    txtCustomerId.getText().trim(),
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtEmail.getText().trim(),
                    txtLicenseNo.getText().trim(),
                    txtAddress.getText().trim()
            );

            customerService.updateCustomer(c);
            JOptionPane.showMessageDialog(this, "Customer details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            notifyDataChange();

        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteCustomer() {
        int selectedRow = tableCustomers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cId = txtCustomerId.getText().trim();
        String name = txtName.getText().trim();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete customer " + name + " (" + cId + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                customerService.deleteCustomer(cId);
                JOptionPane.showMessageDialog(this, "Customer record deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTableData();
                notifyDataChange();
            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(this, ve.getMessage(), "Operation Disallowed", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = tableCustomers.getSelectedRow();
        if (selectedRow >= 0) {
            txtCustomerId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtPhone.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtLicenseNo.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtAddress.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    private void clearForm() {
        txtCustomerId.setText(customerService.generateNextId());
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtLicenseNo.setText("");
        txtAddress.setText("");
        tableCustomers.clearSelection();
    }

    public void loadTableData() {
        tableModel.setRowCount(0);
        List<Customer> list = customerService.getAllCustomers();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                    c.getCustomerId(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getDrivingLicenseNumber(),
                    c.getAddress()
            });
        }
    }

    private void notifyDataChange() {
        if (onDataChangedCallback != null) {
            onDataChangedCallback.run();
        }
    }
}
