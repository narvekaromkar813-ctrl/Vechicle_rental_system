package com.vehiclerental.gui;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.repository.DataStore;
import com.vehiclerental.service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vehicle Availability Panel demonstrating the Case Study's explicit requirement:
 * Primitive Java Array: boolean[] availabilityStatus
 *
 * This panel visibly proves that availabilityStatus[i] tracks whether
 * the i-th vehicle in the fleet is available or rented.
 */
public class AvailabilityPanel extends JPanel {

    private final VehicleService vehicleService;
    private final DataStore dataStore;

    private JComboBox<String> cmbFilter;
    private JLabel lblArrayStats;
    private DefaultTableModel tableModel;
    private JTable tableAvailability;

    public AvailabilityPanel() {
        this.vehicleService = new VehicleService();
        this.dataStore = DataStore.getInstance();

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        loadAvailabilityData();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Fleet Availability Tracker (Demonstrating: boolean[] availabilityStatus)",
                "Tracking fleet status via synchronized Java primitive boolean array."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Card
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        // Control Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        toolbar.setOpaque(false);

        JLabel lblFilter = new JLabel("Filter View:");
        lblFilter.setFont(UIStyle.FONT_BODY_BOLD);
        toolbar.add(lblFilter);

        String[] options = {"All Vehicles (Array Snapshot)", "Available Vehicles Only"};
        cmbFilter = new JComboBox<>(options);
        cmbFilter.setFont(UIStyle.FONT_BODY);
        cmbFilter.addActionListener(e -> loadAvailabilityData());
        toolbar.add(cmbFilter);

        JButton btnRefresh = new JButton("Refresh Status");
        UIStyle.stylePrimaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> loadAvailabilityData());
        toolbar.add(btnRefresh);

        card.add(toolbar, BorderLayout.NORTH);

        // Availability Table
        String[] cols = {
                "Array Index [i]",
                "Vehicle ID",
                "Registration Number",
                "Vehicle Description",
                "Type",
                "Daily Rate (₹)",
                "Status",
                "Array Value (availabilityStatus[i])"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableAvailability = new JTable(tableModel);
        UIStyle.styleTable(tableAvailability);

        // Custom Cell Renderer to color-code Available vs Rented
        tableAvailability.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    if ("AVAILABLE".equalsIgnoreCase(String.valueOf(value))) {
                        c.setForeground(new Color(39, 174, 96));
                        c.setFont(UIStyle.FONT_BODY_BOLD);
                    } else {
                        c.setForeground(new Color(214, 48, 49));
                        c.setFont(UIStyle.FONT_BODY_BOLD);
                    }
                }
                return c;
            }
        });

        card.add(new JScrollPane(tableAvailability), BorderLayout.CENTER);

        // Bottom Array Statistics Banner
        JPanel bottomBanner = new JPanel(new BorderLayout());
        bottomBanner.setBackground(new Color(238, 242, 246));
        bottomBanner.setBorder(new EmptyBorder(8, 12, 8, 12));

        lblArrayStats = new JLabel("Initializing array statistics...");
        lblArrayStats.setFont(UIStyle.FONT_BODY_BOLD);
        lblArrayStats.setForeground(UIStyle.COLOR_PRIMARY);

        JLabel lblExplanation = new JLabel("Synchronized with DataStore.getAvailabilityStatus()");
        lblExplanation.setFont(UIStyle.FONT_SMALL);
        lblExplanation.setForeground(UIStyle.COLOR_TEXT_MUTED);

        bottomBanner.add(lblArrayStats, BorderLayout.WEST);
        bottomBanner.add(lblExplanation, BorderLayout.EAST);

        card.add(bottomBanner, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
    }

    public void loadAvailabilityData() {
        tableModel.setRowCount(0);

        ArrayList<Vehicle> vehicles = dataStore.getVehicles();
        boolean[] statusArray = dataStore.getAvailabilityStatus();

        int availableCount = 0;
        int rentedCount = 0;
        boolean filterOnlyAvailable = cmbFilter.getSelectedIndex() == 1;

        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            boolean isAvail = (i < statusArray.length) ? statusArray[i] : v.isAvailable();

            if (isAvail) {
                availableCount++;
            } else {
                rentedCount++;
            }

            if (filterOnlyAvailable && !isAvail) {
                continue; // Skip rented if filter is set to Available Only
            }

            tableModel.addRow(new Object[]{
                    "[" + i + "]",
                    v.getVehicleId(),
                    v.getRegistrationNumber(),
                    v.getBrand() + " " + v.getModel(),
                    v.getType(),
                    String.format("%.2f", v.getRentalPricePerDay()),
                    isAvail ? "AVAILABLE" : "RENTED",
                    isAvail ? "true (Ready for rent)" : "false (Rented out)"
            });
        }

        lblArrayStats.setText(String.format(
                "Array Size: %d elements | Available (true): %d | Rented (false): %d",
                statusArray.length, availableCount, rentedCount
        ));
    }
}
