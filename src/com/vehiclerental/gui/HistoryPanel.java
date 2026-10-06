package com.vehiclerental.gui;

import com.vehiclerental.model.Rental;
import com.vehiclerental.service.RentalService;
import com.vehiclerental.util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Rental History Panel demonstrating the Case Study requirement:
 * LinkedList<Rental> rentalHistory
 *
 * Displays all completed and returned rental records maintained
 * chronologically inside a Java LinkedList.
 */
public class HistoryPanel extends JPanel {

    private final RentalService rentalService;
    private final Consumer<String> navigationHandler;

    private DefaultTableModel tableModel;
    private JTable tableHistory;
    private JLabel lblHistoryStats;

    public HistoryPanel(Consumer<String> navigationHandler) {
        this.rentalService = new RentalService();
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadHistoryData();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Completed Rental History (Demonstrating: LinkedList<Rental>)",
                "Maintaining closed transactions in chronological order using Java Collections LinkedList."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Card
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh History");
        UIStyle.stylePrimaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadHistoryData());
        toolbar.add(btnRefresh);

        JButton btnViewBill = new JButton("View Invoice for Selected Rental");
        UIStyle.styleSecondaryButton(btnViewBill);
        btnViewBill.addActionListener(e -> handleViewSelectedBill());
        toolbar.add(btnViewBill);

        card.add(toolbar, BorderLayout.NORTH);

        // JTable
        String[] cols = {
                "Rental ID",
                "Customer Name",
                "Customer Phone",
                "Vehicle Reg No",
                "Model",
                "Booking Date",
                "Actual Return Date",
                "Days",
                "Total Amount (₹)",
                "Status"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableHistory = new JTable(tableModel);
        UIStyle.styleTable(tableHistory);
        card.add(new JScrollPane(tableHistory), BorderLayout.CENTER);

        // Bottom Stats Bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(238, 242, 246));
        bottomBar.setBorder(new EmptyBorder(8, 12, 8, 12));

        lblHistoryStats = new JLabel("Records in LinkedList: 0");
        lblHistoryStats.setFont(UIStyle.FONT_BODY_BOLD);
        lblHistoryStats.setForeground(UIStyle.COLOR_PRIMARY);

        JLabel lblLinkedListNote = new JLabel("Data Structure: java.util.LinkedList<Rental>");
        lblLinkedListNote.setFont(UIStyle.FONT_SMALL);
        lblLinkedListNote.setForeground(UIStyle.COLOR_TEXT_MUTED);

        bottomBar.add(lblHistoryStats, BorderLayout.WEST);
        bottomBar.add(lblLinkedListNote, BorderLayout.EAST);

        card.add(bottomBar, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
    }

    private void handleViewSelectedBill() {
        int row = tableHistory.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a completed rental record from the table.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rentalId = tableModel.getValueAt(row, 0).toString();
        if (navigationHandler != null) {
            // Switch to BillingPanel and we can pass the rentalId
            navigationHandler.accept("BILLING:" + rentalId);
        }
    }

    public void loadHistoryData() {
        tableModel.setRowCount(0);

        // Fetch LinkedList<Rental> from service/datastore
        LinkedList<Rental> history = rentalService.getRentalHistory();
        double totalRevenue = 0.0;

        for (Rental r : history) {
            totalRevenue += r.getTotalCharge();
            tableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer() != null ? r.getCustomer().getName() : "N/A",
                    r.getCustomer() != null ? r.getCustomer().getPhone() : "N/A",
                    r.getVehicle() != null ? r.getVehicle().getRegistrationNumber() : "N/A",
                    r.getVehicle() != null ? (r.getVehicle().getBrand() + " " + r.getVehicle().getModel()) : "N/A",
                    DateUtil.formatDate(r.getBookingDate()),
                    DateUtil.formatDate(r.getActualReturnDate()),
                    r.getNumberOfDays(),
                    String.format("%.2f", r.getTotalCharge()),
                    r.getStatus()
            });
        }

        lblHistoryStats.setText(String.format(
                "Completed Rentals in LinkedList: %d | Total Completed Revenue: ₹%.2f",
                history.size(), totalRevenue
        ));
    }
}
