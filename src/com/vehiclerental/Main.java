package com.vehiclerental;

import com.vehiclerental.gui.MainFrame;

import javax.swing.*;

/**
 * Main application launcher for the Vehicle Rental Management System.
 * Case Study 12 - ITM Skills University, School of Future Tech.
 * B.Tech Computer Science Engineering (Semester III).
 */
public class Main {

    public static void main(String[] args) {
        // Set System Look and Feel for clean, native desktop integration
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set System Look and Feel: " + e.getMessage());
        }

        System.out.println("===============================================================");
        System.out.println("          VEHICLE RENTAL MANAGEMENT SYSTEM (CASE STUDY 12)     ");
        System.out.println("      ITM Skills University - School of Future Tech (CSE)      ");
        System.out.println("===============================================================");
        System.out.println("[*] Initializing In-Memory DataStore...");
        System.out.println("[*] Loaded Data Structures:");
        System.out.println("    - ArrayList<Vehicle>          : Dynamic Vehicle Fleet");
        System.out.println("    - ArrayList<Customer>         : Dynamic Customer Database");
        System.out.println("    - LinkedList<Rental>          : Chronological Rental History");
        System.out.println("    - HashMap<String, Vehicle>    : O(1) Fast Lookup by Reg No");
        System.out.println("    - TreeMap<String, Vehicle>    : Natural Sorted Order by Reg No");
        System.out.println("    - boolean[] availabilityStatus: Primitive Fleet Availability Array");
        System.out.println("[*] Launching Swing GUI MainFrame...");

        // Launch GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
            System.out.println("[+] Application GUI successfully opened and operational.");
            System.out.println("===============================================================");
        });
    }
}
