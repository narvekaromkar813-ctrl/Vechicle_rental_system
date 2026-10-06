package com.vehiclerental.gui;

import com.vehiclerental.model.Invoice;
import com.vehiclerental.model.Rental;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.service.BillingService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * Billing & Invoice Panel:
 * Generates and displays a clean, official text invoice receipt for any
 * active or completed vehicle rental transaction.
 */
public class BillingPanel extends JPanel {

    private final BillingService billingService;
    private final DataStore dataStore;

    private JComboBox<RentalComboItem> cmbRentals;
    private JTextField txtManualRentalId;
    private JTextArea txtInvoiceDisplay;
    private JButton btnGenerate;
    private JButton btnCopy;
    private JButton btnRefresh;

    public BillingPanel() {
        this.billingService = new BillingService();
        this.dataStore = DataStore.getInstance();

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadRentalOptions();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Billing & Invoice Generation",
                "Generate official itemized rental invoices and receipts for customers."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Split
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Top Selection Control Toolbar
        JPanel controlCard = UIStyle.createCardPanel();
        controlCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel lblSelect = new JLabel("Select Transaction:");
        lblSelect.setFont(UIStyle.FONT_BODY_BOLD);
        controlCard.add(lblSelect);

        cmbRentals = new JComboBox<>();
        cmbRentals.setFont(UIStyle.FONT_BODY);
        cmbRentals.setPreferredSize(new Dimension(320, 30));
        controlCard.add(cmbRentals);

        JLabel lblOr = new JLabel(" OR Rental ID:");
        lblOr.setFont(UIStyle.FONT_BODY_BOLD);
        controlCard.add(lblOr);

        txtManualRentalId = new JTextField(8);
        UIStyle.styleTextField(txtManualRentalId);
        txtManualRentalId.setToolTipText("e.g. R1001");
        controlCard.add(txtManualRentalId);

        btnGenerate = new JButton("Generate Bill");
        UIStyle.stylePrimaryButton(btnGenerate);
        btnGenerate.addActionListener(e -> generateBill());
        controlCard.add(btnGenerate);

        btnCopy = new JButton("Copy Receipt");
        UIStyle.styleSecondaryButton(btnCopy);
        btnCopy.addActionListener(e -> copyToClipboard());
        controlCard.add(btnCopy);

        btnRefresh = new JButton("Refresh");
        UIStyle.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadRentalOptions());
        controlCard.add(btnRefresh);

        centerPanel.add(controlCard, BorderLayout.NORTH);

        // Center Invoice Display Area
        JPanel displayCard = UIStyle.createCardPanel();
        displayCard.setLayout(new BorderLayout(5, 5));

        JLabel previewLabel = new JLabel("Invoice Receipt Preview");
        previewLabel.setFont(UIStyle.FONT_TITLE);
        previewLabel.setForeground(UIStyle.COLOR_PRIMARY);
        displayCard.add(previewLabel, BorderLayout.NORTH);

        txtInvoiceDisplay = new JTextArea();
        txtInvoiceDisplay.setFont(UIStyle.FONT_MONO);
        txtInvoiceDisplay.setEditable(false);
        txtInvoiceDisplay.setBackground(new Color(250, 252, 255));
        txtInvoiceDisplay.setMargin(new Insets(15, 20, 15, 20));

        JScrollPane scrollPane = new JScrollPane(txtInvoiceDisplay);
        displayCard.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(displayCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void generateBill() {
        String manualId = txtManualRentalId.getText().trim();
        Rental targetRental = null;

        if (!manualId.isEmpty()) {
            // Check in active
            targetRental = dataStore.findActiveRentalById(manualId);
            if (targetRental == null) {
                // Check in history
                for (Rental r : dataStore.getRentalHistory()) {
                    if (r.getRentalId().equalsIgnoreCase(manualId)) {
                        targetRental = r;
                        break;
                    }
                }
            }
        } else {
            RentalComboItem item = (RentalComboItem) cmbRentals.getSelectedItem();
            if (item != null) {
                targetRental = item.rental;
            }
        }

        if (targetRental == null) {
            JOptionPane.showMessageDialog(this, "Please select or enter a valid Rental ID.", "Invoice Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Invoice invoice = billingService.generateInvoice(targetRental);
        if (invoice != null) {
            txtInvoiceDisplay.setText(invoice.generateBillText());
            txtInvoiceDisplay.setCaretPosition(0);
        }
    }

    private void copyToClipboard() {
        String text = txtInvoiceDisplay.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No invoice text to copy.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        JOptionPane.showMessageDialog(this, "Invoice receipt copied to clipboard!", "Copied", JOptionPane.INFORMATION_MESSAGE);
    }

    public void selectRentalById(String rentalId) {
        if (rentalId == null) return;
        loadRentalOptions();
        for (int i = 0; i < cmbRentals.getItemCount(); i++) {
            RentalComboItem item = cmbRentals.getItemAt(i);
            if (item != null && item.rental != null && item.rental.getRentalId().equalsIgnoreCase(rentalId)) {
                cmbRentals.setSelectedIndex(i);
                generateBill();
                return;
            }
        }
        txtManualRentalId.setText(rentalId);
        generateBill();
    }

    public void loadRentalOptions() {
        cmbRentals.removeAllItems();

        List<Rental> all = new ArrayList<>();
        all.addAll(dataStore.getActiveRentals());
        all.addAll(dataStore.getRentalHistory());

        for (Rental r : all) {
            cmbRentals.addItem(new RentalComboItem(r));
        }

        if (cmbRentals.getItemCount() > 0 && txtInvoiceDisplay.getText().isEmpty()) {
            generateBill();
        }
    }

    private static class RentalComboItem {
        final Rental rental;

        RentalComboItem(Rental rental) {
            this.rental = rental;
        }

        @Override
        public String toString() {
            String custName = rental.getCustomer() != null ? rental.getCustomer().getName() : "N/A";
            String regNo = rental.getVehicle() != null ? rental.getVehicle().getRegistrationNumber() : "N/A";
            return rental.getRentalId() + " - " + custName + " (" + regNo + ") [" + rental.getStatus() + "]";
        }
    }
}
