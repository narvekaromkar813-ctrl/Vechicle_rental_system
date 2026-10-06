package com.vehiclerental.gui;

import com.vehiclerental.model.Rental;
import com.vehiclerental.service.ReportService;
import com.vehiclerental.util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Rental Report Panel:
 * Generates an aggregated executive operations report with fleet utilization statistics,
 * customer counts, active vs completed bookings, and comprehensive transaction ledger.
 * All metrics computed dynamically from in-memory collections and array.
 */
public class ReportPanel extends JPanel {

    private final ReportService reportService;

    // Stat metric labels
    private JLabel lblTotalVehicles;
    private JLabel lblAvailableVehicles;
    private JLabel lblRentedVehicles;
    private JLabel lblTotalCustomers;
    private JLabel lblActiveRentals;
    private JLabel lblCompletedRentals;
    private JLabel lblTotalRevenue;

    private DefaultTableModel transactionsTableModel;
    private JTable tableTransactions;

    public ReportPanel() {
        this.reportService = new ReportService();

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadReportData();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Fleet Operations & Rental Summary Report",
                "Aggregated financial statistics, vehicle utilization metrics, and complete transaction log."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Split
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Top Summary Cards Grid
        JPanel statsPanel = UIStyle.createCardPanel();
        statsPanel.setLayout(new BorderLayout(5, 8));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel lblTitle = new JLabel("Key Operational Performance Indicators");
        lblTitle.setFont(UIStyle.FONT_TITLE);
        lblTitle.setForeground(UIStyle.COLOR_PRIMARY);

        JButton btnRefresh = new JButton("Refresh Report");
        UIStyle.stylePrimaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadReportData());

        headerRow.add(lblTitle, BorderLayout.WEST);
        headerRow.add(btnRefresh, BorderLayout.EAST);
        statsPanel.add(headerRow, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 7, 8, 8));
        grid.setOpaque(false);

        lblTotalVehicles = new JLabel("0", SwingConstants.CENTER);
        lblAvailableVehicles = new JLabel("0", SwingConstants.CENTER);
        lblRentedVehicles = new JLabel("0", SwingConstants.CENTER);
        lblTotalCustomers = new JLabel("0", SwingConstants.CENTER);
        lblActiveRentals = new JLabel("0", SwingConstants.CENTER);
        lblCompletedRentals = new JLabel("0", SwingConstants.CENTER);
        lblTotalRevenue = new JLabel("₹0.00", SwingConstants.CENTER);

        grid.add(createMiniCard("Total Fleet", lblTotalVehicles, new Color(41, 128, 185)));
        grid.add(createMiniCard("Available", lblAvailableVehicles, new Color(39, 174, 96)));
        grid.add(createMiniCard("Rented", lblRentedVehicles, new Color(211, 84, 0)));
        grid.add(createMiniCard("Customers", lblTotalCustomers, new Color(142, 68, 173)));
        grid.add(createMiniCard("Active", lblActiveRentals, new Color(192, 57, 43)));
        grid.add(createMiniCard("Completed", lblCompletedRentals, new Color(22, 160, 133)));
        grid.add(createMiniCard("Revenue", lblTotalRevenue, new Color(44, 62, 80)));

        statsPanel.add(grid, BorderLayout.CENTER);
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Center Table: All Transactions Ledger
        JPanel tableCard = UIStyle.createCardPanel();
        tableCard.setLayout(new BorderLayout(5, 5));

        JLabel ledgerLabel = new JLabel("All Rental Transactions Ledger (Active & Completed)");
        ledgerLabel.setFont(UIStyle.FONT_TITLE);
        ledgerLabel.setForeground(UIStyle.COLOR_PRIMARY);
        tableCard.add(ledgerLabel, BorderLayout.NORTH);

        String[] cols = {
                "Rental ID",
                "Customer",
                "Registration No",
                "Vehicle",
                "Booking Date",
                "Return Date",
                "Duration",
                "Total Amount (₹)",
                "Status"
        };

        transactionsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableTransactions = new JTable(transactionsTableModel);
        UIStyle.styleTable(tableTransactions);
        tableCard.add(new JScrollPane(tableTransactions), BorderLayout.CENTER);

        centerPanel.add(tableCard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createMiniCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(2, 2));
        card.setBackground(new Color(245, 248, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                new EmptyBorder(6, 6, 6, 6)
        ));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(UIStyle.FONT_SMALL);
        titleLbl.setForeground(UIStyle.COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        valueLabel.setForeground(accentColor);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLbl, BorderLayout.SOUTH);
        return card;
    }

    public void loadReportData() {
        lblTotalVehicles.setText(String.valueOf(reportService.getTotalVehicles()));
        lblAvailableVehicles.setText(String.valueOf(reportService.getAvailableVehiclesCount()));
        lblRentedVehicles.setText(String.valueOf(reportService.getRentedVehiclesCount()));
        lblTotalCustomers.setText(String.valueOf(reportService.getTotalCustomers()));
        lblActiveRentals.setText(String.valueOf(reportService.getActiveRentalsCount()));
        lblCompletedRentals.setText(String.valueOf(reportService.getCompletedRentalsCount()));
        lblTotalRevenue.setText(String.format("₹%.2f", reportService.getTotalRevenue()));

        transactionsTableModel.setRowCount(0);
        List<Rental> all = reportService.getAllTransactions();

        for (Rental r : all) {
            String returnDateStr = r.getActualReturnDate() != null ?
                    DateUtil.formatDate(r.getActualReturnDate()) :
                    (DateUtil.formatDate(r.getExpectedReturnDate()) + " (Exp)");

            transactionsTableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer() != null ? r.getCustomer().getName() : "N/A",
                    r.getVehicle() != null ? r.getVehicle().getRegistrationNumber() : "N/A",
                    r.getVehicle() != null ? (r.getVehicle().getBrand() + " " + r.getVehicle().getModel()) : "N/A",
                    DateUtil.formatDate(r.getBookingDate()),
                    returnDateStr,
                    r.getNumberOfDays() + " Day(s)",
                    String.format("%.2f", r.getTotalCharge()),
                    r.getStatus()
            });
        }
    }
}
