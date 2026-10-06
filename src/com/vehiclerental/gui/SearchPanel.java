package com.vehiclerental.gui;

import com.vehiclerental.model.Vehicle;
import com.vehiclerental.service.VehicleService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle Search and Sorting Panel demonstrating:
 * 1. HashMap<String, Vehicle>  -> O(1) lookup by Registration Number
 * 2. ArrayList search         -> Linear search by Model and Type
 * 3. TreeMap<String, Vehicle>  -> Automatic key sorting by Registration Number
 * 4. Comparator<Vehicle>      -> Flexible sorting by Model, Price, and Type
 */
public class SearchPanel extends JPanel {

    private final VehicleService vehicleService;

    // Search Controls
    private JComboBox<String> cmbSearchBy;
    private JTextField txtSearchQuery;
    private JButton btnSearch;
    private JButton btnReset;

    // Sorting Controls
    private JComboBox<String> cmbSortBy;
    private JButton btnSort;

    // Results Table
    private DefaultTableModel tableModel;
    private JTable tableResults;
    private JLabel lblResultCount;
    private JLabel lblAlgorithmInfo;

    public SearchPanel() {
        this.vehicleService = new VehicleService();

        setLayout(new BorderLayout(10, 10));
        setBackground(UIStyle.COLOR_BG);
        setBorder(new EmptyBorder(10, 15, 15, 15));

        initComponents();
        showAllVehicles();
    }

    private void initComponents() {
        // 1. Header
        JPanel topHeader = UIStyle.createSectionHeader(
                "Search & Sort Vehicles (Demonstrating: HashMap, TreeMap & Comparator)",
                "Fast O(1) HashMap lookup, TreeMap natural ordering, and Comparator sorting algorithms."
        );
        add(topHeader, BorderLayout.NORTH);

        // 2. Center Content Card
        JPanel card = UIStyle.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        // Top Query & Sort Bar
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        controlPanel.setOpaque(false);

        // Row 1: Search Controls
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        searchRow.setOpaque(false);

        JLabel lblSearchBy = new JLabel("Search By:");
        lblSearchBy.setFont(UIStyle.FONT_BODY_BOLD);
        searchRow.add(lblSearchBy);

        String[] searchOptions = {
                "Registration Number (HashMap O(1))",
                "Model (ArrayList Iteration)",
                "Type (ArrayList Iteration)"
        };
        cmbSearchBy = new JComboBox<>(searchOptions);
        cmbSearchBy.setFont(UIStyle.FONT_BODY);
        searchRow.add(cmbSearchBy);

        txtSearchQuery = new JTextField(15);
        UIStyle.styleTextField(txtSearchQuery);
        txtSearchQuery.setToolTipText("Enter registration number, model, or type...");
        txtSearchQuery.addActionListener(e -> performSearch());
        searchRow.add(txtSearchQuery);

        btnSearch = new JButton("Search");
        UIStyle.stylePrimaryButton(btnSearch);
        btnSearch.addActionListener(e -> performSearch());
        searchRow.add(btnSearch);

        btnReset = new JButton("Show All / Reset");
        UIStyle.styleSecondaryButton(btnReset);
        btnReset.addActionListener(e -> {
            txtSearchQuery.setText("");
            showAllVehicles();
        });
        searchRow.add(btnReset);

        controlPanel.add(searchRow);

        // Row 2: Sorting Controls
        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        sortRow.setOpaque(false);

        JLabel lblSortBy = new JLabel("Sort Fleet By:");
        lblSortBy.setFont(UIStyle.FONT_BODY_BOLD);
        sortRow.add(lblSortBy);

        String[] sortOptions = {
                "Registration Number (TreeMap)",
                "Rental Price: Low to High",
                "Rental Price: High to Low",
                "Model",
                "Type",
                "Brand"
        };
        cmbSortBy = new JComboBox<>(sortOptions);
        cmbSortBy.setFont(UIStyle.FONT_BODY);
        sortRow.add(cmbSortBy);

        btnSort = new JButton("Apply Sort");
        UIStyle.stylePrimaryButton(btnSort);
        btnSort.addActionListener(e -> performSort());
        sortRow.add(btnSort);

        controlPanel.add(sortRow);
        card.add(controlPanel, BorderLayout.NORTH);

        // Results Table
        String[] cols = {"Vehicle ID", "Reg. Number", "Brand", "Model", "Type", "Price/Day (₹)", "Availability"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableResults = new JTable(tableModel);
        UIStyle.styleTable(tableResults);
        card.add(new JScrollPane(tableResults), BorderLayout.CENTER);

        // Bottom Info Bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(new Color(238, 242, 246));
        bottomBar.setBorder(new EmptyBorder(8, 12, 8, 12));

        lblResultCount = new JLabel("Total Records: 0");
        lblResultCount.setFont(UIStyle.FONT_BODY_BOLD);
        lblResultCount.setForeground(UIStyle.COLOR_PRIMARY);

        lblAlgorithmInfo = new JLabel("Displaying complete fleet");
        lblAlgorithmInfo.setFont(UIStyle.FONT_SMALL);
        lblAlgorithmInfo.setForeground(UIStyle.COLOR_TEXT_MUTED);

        bottomBar.add(lblResultCount, BorderLayout.WEST);
        bottomBar.add(lblAlgorithmInfo, BorderLayout.EAST);

        card.add(bottomBar, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = txtSearchQuery.getText().trim();
        if (query.isEmpty()) {
            showAllVehicles();
            return;
        }

        int searchIdx = cmbSearchBy.getSelectedIndex();
        List<Vehicle> results = new ArrayList<>();

        if (searchIdx == 0) {
            // 1. HashMap Lookup by Registration Number
            Vehicle v = vehicleService.searchByRegistrationNumber(query);
            if (v != null) {
                results.add(v);
            }
            lblAlgorithmInfo.setText("Executed: vehicleMap.get(\"" + query.toUpperCase() + "\") -> O(1) Fast HashMap Lookup");
        } else if (searchIdx == 1) {
            // 2. ArrayList Linear Search by Model
            results = vehicleService.searchByModel(query);
            lblAlgorithmInfo.setText("Executed: Linear search over ArrayList<Vehicle> for model matching '" + query + "'");
        } else {
            // 3. ArrayList Linear Search by Type
            results = vehicleService.searchByType(query);
            lblAlgorithmInfo.setText("Executed: Linear search over ArrayList<Vehicle> for type matching '" + query + "'");
        }

        populateTable(results);
    }

    private void performSort() {
        String criterion = (String) cmbSortBy.getSelectedItem();
        List<Vehicle> sorted = vehicleService.getVehiclesSorted(criterion);

        if ("Registration Number (TreeMap)".equals(criterion)) {
            lblAlgorithmInfo.setText("Executed: DataStore.getSortedVehicles().values() -> TreeMap natural ordering O(log N)");
        } else {
            lblAlgorithmInfo.setText("Executed: Collections.sort(list, Comparator.comparing(...)) for " + criterion);
        }

        populateTable(sorted);
    }

    public void showAllVehicles() {
        List<Vehicle> all = vehicleService.getAllVehicles();
        lblAlgorithmInfo.setText("Displaying all registered fleet vehicles from ArrayList<Vehicle>");
        populateTable(all);
    }

    private void populateTable(List<Vehicle> list) {
        tableModel.setRowCount(0);
        for (Vehicle v : list) {
            tableModel.addRow(new Object[]{
                    v.getVehicleId(),
                    v.getRegistrationNumber(),
                    v.getBrand(),
                    v.getModel(),
                    v.getType(),
                    String.format("%.2f", v.getRentalPricePerDay()),
                    v.isAvailable() ? "AVAILABLE" : "RENTED"
            });
        }
        lblResultCount.setText("Vehicles Displayed: " + list.size());
    }
}
