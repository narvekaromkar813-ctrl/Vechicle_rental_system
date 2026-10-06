package com.vehiclerental.gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application window (JFrame).
 * Implements a clean, traditional student-project desktop layout:
 * - Top University Header Banner
 * - Left Navigation Sidebar with CardLayout switcher
 * - Center Card View for all 10 Modules
 * - Bottom Operational Status Bar
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JLabel lblStatusBar;

    // Navigation buttons mapping to update active styling
    private final Map<String, JButton> navButtons = new HashMap<>();

    // Sub-panels
    private DashboardPanel dashboardPanel;
    private VehiclePanel vehiclePanel;
    private CustomerPanel customerPanel;
    private AvailabilityPanel availabilityPanel;
    private SearchPanel searchPanel;
    private BookingPanel bookingPanel;
    private ReturnPanel returnPanel;
    private HistoryPanel historyPanel;
    private BillingPanel billingPanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        setTitle("Vehicle Rental Management System - Case Study 12 | ITM Skills University");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 750);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // 1. Top Institutional Banner
        JPanel topBanner = createTopBanner();
        add(topBanner, BorderLayout.NORTH);

        // 2. Left Navigation Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 3. Center Content Panel with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIStyle.COLOR_BG);

        // Data change callback to sync panels when entities change
        Runnable onDataChanged = this::refreshAllPanels;

        dashboardPanel = new DashboardPanel(this::navigateTo);
        vehiclePanel = new VehiclePanel(onDataChanged);
        customerPanel = new CustomerPanel(onDataChanged);
        availabilityPanel = new AvailabilityPanel();
        searchPanel = new SearchPanel();
        bookingPanel = new BookingPanel(onDataChanged);
        returnPanel = new ReturnPanel(onDataChanged, this::navigateTo);
        historyPanel = new HistoryPanel(this::navigateTo);
        billingPanel = new BillingPanel();
        reportPanel = new ReportPanel();

        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(vehiclePanel, "VEHICLES");
        mainContentPanel.add(customerPanel, "CUSTOMERS");
        mainContentPanel.add(availabilityPanel, "AVAILABILITY");
        mainContentPanel.add(searchPanel, "SEARCH");
        mainContentPanel.add(bookingPanel, "BOOKING");
        mainContentPanel.add(returnPanel, "RETURN");
        mainContentPanel.add(historyPanel, "HISTORY");
        mainContentPanel.add(billingPanel, "BILLING");
        mainContentPanel.add(reportPanel, "REPORTS");

        add(mainContentPanel, BorderLayout.CENTER);

        // 4. Bottom Status Bar
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        // Set initial card
        navigateTo("DASHBOARD");
    }

    private JPanel createTopBanner() {
        JPanel banner = new JPanel(new BorderLayout(10, 0));
        banner.setBackground(UIStyle.COLOR_PRIMARY);
        banner.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Title and Subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        titlePanel.setOpaque(false);

        JLabel mainTitle = new JLabel("VEHICLE RENTAL MANAGEMENT SYSTEM");
        mainTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainTitle.setForeground(Color.WHITE);

        JLabel subTitle = new JLabel("ITM Skills University • School of Future Tech • B.Tech CSE Semester III (Case Study 12)");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setForeground(new Color(200, 220, 240));

        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);
        banner.add(titlePanel, BorderLayout.WEST);

        // Right Quick Badge
        JLabel badge = new JLabel("Academic Project Demo  ");
        badge.setFont(UIStyle.FONT_SMALL);
        badge.setForeground(new Color(180, 210, 240));
        banner.add(badge, BorderLayout.EAST);

        return banner;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIStyle.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(30, 40, 50)));

        // Nav Title
        JLabel navHeader = new JLabel("  NAVIGATION MENU");
        navHeader.setFont(UIStyle.FONT_SMALL);
        navHeader.setForeground(new Color(150, 165, 180));
        navHeader.setBorder(new EmptyBorder(15, 10, 10, 10));
        sidebar.add(navHeader, BorderLayout.NORTH);

        // Button list
        JPanel buttonList = new JPanel();
        buttonList.setLayout(new BoxLayout(buttonList, BoxLayout.Y_AXIS));
        buttonList.setOpaque(false);
        buttonList.setBorder(new EmptyBorder(5, 8, 10, 8));

        addNavSidebarButton(buttonList, "Dashboard", "DASHBOARD");
        addNavSidebarButton(buttonList, "Vehicle Management", "VEHICLES");
        addNavSidebarButton(buttonList, "Customer Registration", "CUSTOMERS");
        addNavSidebarButton(buttonList, "Vehicle Availability", "AVAILABILITY");
        addNavSidebarButton(buttonList, "Search & Sort", "SEARCH");
        addNavSidebarButton(buttonList, "Rental Booking", "BOOKING");
        addNavSidebarButton(buttonList, "Vehicle Return", "RETURN");
        addNavSidebarButton(buttonList, "Rental History", "HISTORY");
        addNavSidebarButton(buttonList, "Billing & Invoice", "BILLING");
        addNavSidebarButton(buttonList, "Rental Reports", "REPORTS");

        sidebar.add(buttonList, BorderLayout.CENTER);

        // Sidebar Footer
        JPanel footer = new JPanel(new GridLayout(2, 1));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 12, 12, 12));

        JLabel techLabel = new JLabel("Java 17+ | Swing | OOP");
        techLabel.setFont(UIStyle.FONT_SMALL);
        techLabel.setForeground(new Color(140, 155, 170));

        JLabel collLabel = new JLabel("Collections & Arrays");
        collLabel.setFont(UIStyle.FONT_SMALL);
        collLabel.setForeground(new Color(140, 155, 170));

        footer.add(techLabel);
        footer.add(collLabel);
        sidebar.add(footer, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addNavSidebarButton(JPanel container, String text, String cardKey) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(UIStyle.FONT_BODY_BOLD);
        btn.setForeground(new Color(220, 230, 240));
        btn.setBackground(UIStyle.COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 12, 8, 12));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addActionListener(e -> navigateTo(cardKey));

        navButtons.put(cardKey, btn);
        container.add(btn);
        container.add(Box.createRigidArea(new Dimension(0, 4)));
    }

    private JPanel createStatusBar() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBackground(new Color(230, 235, 242));
        status.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, UIStyle.COLOR_BORDER),
                new EmptyBorder(4, 15, 4, 15)
        ));

        lblStatusBar = new JLabel("Status: System Operational | Ready | In-Memory DataStore Loaded");
        lblStatusBar.setFont(UIStyle.FONT_SMALL);
        lblStatusBar.setForeground(UIStyle.COLOR_TEXT_DARK);

        JLabel lblCopyright = new JLabel("ITM Skills University - B.Tech CSE 2025-29");
        lblCopyright.setFont(UIStyle.FONT_SMALL);
        lblCopyright.setForeground(UIStyle.COLOR_TEXT_MUTED);

        status.add(lblStatusBar, BorderLayout.WEST);
        status.add(lblCopyright, BorderLayout.EAST);
        return status;
    }

    public void navigateTo(String destination) {
        String cardKey = destination;
        String param = null;

        if (destination.contains(":")) {
            String[] parts = destination.split(":", 2);
            cardKey = parts[0];
            param = parts[1];
        }

        cardLayout.show(mainContentPanel, cardKey);

        // Highlight active button in sidebar
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(cardKey)) {
                entry.getValue().setBackground(UIStyle.COLOR_PRIMARY_LIGHT);
                entry.getValue().setForeground(Color.WHITE);
            } else {
                entry.getValue().setBackground(UIStyle.COLOR_SIDEBAR);
                entry.getValue().setForeground(new Color(220, 230, 240));
            }
        }

        // Panel specific refresh on focus
        switch (cardKey) {
            case "DASHBOARD":
                dashboardPanel.refreshData();
                lblStatusBar.setText("Status: Operational Dashboard | Live Statistics Active");
                break;
            case "VEHICLES":
                vehiclePanel.loadTableData();
                lblStatusBar.setText("Status: Vehicle Fleet Management (ArrayList & HashMap CRUD)");
                break;
            case "CUSTOMERS":
                customerPanel.loadTableData();
                lblStatusBar.setText("Status: Customer Registration Records (ArrayList<Customer>)");
                break;
            case "AVAILABILITY":
                availabilityPanel.loadAvailabilityData();
                lblStatusBar.setText("Status: Fleet Availability Tracker (boolean[] availabilityStatus Primitive Array)");
                break;
            case "SEARCH":
                searchPanel.showAllVehicles();
                lblStatusBar.setText("Status: Search & Sort (HashMap O(1) & TreeMap Sorting Active)");
                break;
            case "BOOKING":
                bookingPanel.loadDropdownData();
                bookingPanel.loadActiveRentals();
                lblStatusBar.setText("Status: Vehicle Rental Booking & Charge Calculation");
                break;
            case "RETURN":
                returnPanel.loadActiveRentals();
                lblStatusBar.setText("Status: Vehicle Return Processing (LinkedList History Maintenance)");
                break;
            case "HISTORY":
                historyPanel.loadHistoryData();
                lblStatusBar.setText("Status: Completed Rental History (LinkedList<Rental>)");
                break;
            case "BILLING":
                billingPanel.loadRentalOptions();
                if (param != null) {
                    billingPanel.selectRentalById(param);
                }
                lblStatusBar.setText("Status: Customer Billing & Invoice Generator");
                break;
            case "REPORTS":
                reportPanel.loadReportData();
                lblStatusBar.setText("Status: Executive Fleet Operations & Revenue Report");
                break;
        }
    }

    private void refreshAllPanels() {
        dashboardPanel.refreshData();
        vehiclePanel.loadTableData();
        customerPanel.loadTableData();
        availabilityPanel.loadAvailabilityData();
        bookingPanel.loadDropdownData();
        bookingPanel.loadActiveRentals();
        returnPanel.loadActiveRentals();
        historyPanel.loadHistoryData();
        billingPanel.loadRentalOptions();
        reportPanel.loadReportData();
    }
}
