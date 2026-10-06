package com.vehiclerental.gui;

import com.vehiclerental.model.Rental;
import com.vehiclerental.service.ReportService;
import com.vehiclerental.util.DateUtil;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Dashboard Panel providing an executive operational summary of fleet,
 * customers, rentals, and revenue, along with direct navigation shortcuts.
 */
public class DashboardPanel extends JPanel {

    private final ReportService reportService;
    private final Consumer<String> navigationHandler;

    private JLabel lblTotalVehicles;
    private JLabel lblAvailableVehicles;
    private JLabel lblRentedVehicles;
    private JLabel lblTotalCustomers;
    private JLabel lblActiveRentals;
    private JLabel lblCompletedRentals;
    private JLabel lblTotalRevenue;

    private DefaultTableModel recentTableModel;
    private JTable recentTable;

    public DashboardPanel(Consumer<String> navigationHandler) {
        this.reportService = new ReportService();
        this.navigationHandler = navigationHandler;

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        refreshData();
    }

    private void initComponents() {
        // 1. Top Section Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "System Overview & Operational Dashboard",
                "ITM Skills University | School of Future Tech | B.Tech CSE Semester III"
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content (Metrics Grid + Recent Table)
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Metric Statistics Grid (2 rows x 4 cols)
        JPanel statsGrid = new JPanel(new GridLayout(2, 4, 10, 10));
        statsGrid.setOpaque(false);
        statsGrid.setBorder(new EmptyBorder(5, 0, 10, 0));

        lblTotalVehicles = new JLabel("0", SwingConstants.CENTER);
        lblAvailableVehicles = new JLabel("0", SwingConstants.CENTER);
        lblRentedVehicles = new JLabel("0", SwingConstants.CENTER);
        lblTotalCustomers = new JLabel("0", SwingConstants.CENTER);
        lblActiveRentals = new JLabel("0", SwingConstants.CENTER);
        lblCompletedRentals = new JLabel("0", SwingConstants.CENTER);
        lblTotalRevenue = new JLabel("₹0.00", SwingConstants.CENTER);

        statsGrid.add(createMetricCard("Total Vehicles", lblTotalVehicles, new Color(41, 128, 185)));
        statsGrid.add(createMetricCard("Available Vehicles", lblAvailableVehicles, new Color(39, 174, 96)));
        statsGrid.add(createMetricCard("Rented Vehicles", lblRentedVehicles, new Color(211, 84, 0)));
        statsGrid.add(createMetricCard("Total Customers", lblTotalCustomers, new Color(142, 68, 173)));
        statsGrid.add(createMetricCard("Active Rentals", lblActiveRentals, new Color(192, 57, 43)));
        statsGrid.add(createMetricCard("Completed Rentals", lblCompletedRentals, new Color(22, 160, 133)));
        statsGrid.add(createMetricCard("Total Revenue", lblTotalRevenue, new Color(44, 62, 80)));

        // Refresh Card / Action card in 8th slot
        JPanel refreshCard = UIStyle.createCardPanel();
        refreshCard.setLayout(new BorderLayout(5, 5));
        JLabel refTitle = new JLabel("Quick Actions", SwingConstants.CENTER);
        refTitle.setFont(UIStyle.FONT_BODY_BOLD);
        JButton btnRefresh = new JButton("Refresh Dashboard");
        UIStyle.stylePrimaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> refreshData());
        refreshCard.add(refTitle, BorderLayout.NORTH);
        refreshCard.add(btnRefresh, BorderLayout.CENTER);
        statsGrid.add(refreshCard);

        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Recent Rentals Summary Table
        JPanel tableContainer = UIStyle.createCardPanel();
        tableContainer.setLayout(new BorderLayout(5, 5));

        JLabel tblHeader = new JLabel("Recent Rental Activity & Active Bookings");
        tblHeader.setFont(UIStyle.FONT_TITLE);
        tblHeader.setForeground(UIStyle.COLOR_PRIMARY);
        tableContainer.add(tblHeader, BorderLayout.NORTH);

        String[] cols = {"Rental ID", "Customer", "Vehicle (Reg No)", "Booking Date", "Expected Return", "Total Charge (₹)", "Status"};
        recentTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        recentTable = new JTable(recentTableModel);
        UIStyle.styleTable(recentTable);
        JScrollPane scrollPane = new JScrollPane(recentTable);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableContainer, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 3. Bottom Quick Navigation Bar
        JPanel navShortcuts = UIStyle.createCardPanel();
        navShortcuts.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

        JLabel navLabel = new JLabel("Quick Shortcuts: ");
        navLabel.setFont(UIStyle.FONT_BODY_BOLD);
        navShortcuts.add(navLabel);

        addNavButton(navShortcuts, "Vehicles", "VEHICLES");
        addNavButton(navShortcuts, "Customers", "CUSTOMERS");
        addNavButton(navShortcuts, "Availability", "AVAILABILITY");
        addNavButton(navShortcuts, "Search", "SEARCH");
        addNavButton(navShortcuts, "Book Vehicle", "BOOKING");
        addNavButton(navShortcuts, "Return Vehicle", "RETURN");
        addNavButton(navShortcuts, "Rental History", "HISTORY");
        addNavButton(navShortcuts, "Billing", "BILLING");
        addNavButton(navShortcuts, "Reports", "REPORTS");

        add(navShortcuts, BorderLayout.SOUTH);
    }

    private JPanel createMetricCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(2, 4));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(UIStyle.FONT_SMALL);
        titleLbl.setForeground(UIStyle.COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        // Subtle accent bar at top
        JPanel topBar = new JPanel();
        topBar.setPreferredSize(new Dimension(0, 3));
        topBar.setBackground(accentColor);

        card.add(topBar, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLbl, BorderLayout.SOUTH);
        return card;
    }

    private void addNavButton(JPanel panel, String label, String cardName) {
        JButton btn = new JButton(label);
        UIStyle.styleSecondaryButton(btn);
        btn.setFont(UIStyle.FONT_SMALL);
        btn.addActionListener(e -> {
            if (navigationHandler != null) {
                navigationHandler.accept(cardName);
            }
        });
        panel.add(btn);
    }

    public void refreshData() {
        lblTotalVehicles.setText(String.valueOf(reportService.getTotalVehicles()));
        lblAvailableVehicles.setText(String.valueOf(reportService.getAvailableVehiclesCount()));
        lblRentedVehicles.setText(String.valueOf(reportService.getRentedVehiclesCount()));
        lblTotalCustomers.setText(String.valueOf(reportService.getTotalCustomers()));
        lblActiveRentals.setText(String.valueOf(reportService.getActiveRentalsCount()));
        lblCompletedRentals.setText(String.valueOf(reportService.getCompletedRentalsCount()));
        lblTotalRevenue.setText(String.format("₹%.2f", reportService.getTotalRevenue()));

        recentTableModel.setRowCount(0);
        List<Rental> all = reportService.getAllTransactions();
        for (Rental r : all) {
            recentTableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer() != null ? r.getCustomer().getName() : "N/A",
                    r.getVehicle() != null ? r.getVehicle().getRegistrationNumber() : "N/A",
                    DateUtil.formatDate(r.getBookingDate()),
                    DateUtil.formatDate(r.getExpectedReturnDate()),
                    String.format("%.2f", r.getTotalCharge()),
                    r.getStatus()
            });
        }
    }
}
