package com.vehiclerental.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Utility class providing a consistent, clean, and professional student-project styling
 * for standard Java Swing components.
 * Strictly avoids futuristic, neon, or AI-themed styling.
 */
public class UIStyle {

    // Palette: Clean Classic College Project Theme
    public static final Color COLOR_PRIMARY = new Color(30, 58, 95);      // Classic Deep Navy
    public static final Color COLOR_PRIMARY_LIGHT = new Color(44, 82, 130);
    public static final Color COLOR_SIDEBAR = new Color(40, 53, 69);      // Slate Charcoal
    public static final Color COLOR_BG = new Color(245, 247, 250);        // Soft Off-White
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    public static final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    public static final Color COLOR_BORDER = new Color(206, 212, 218);
    public static final Color COLOR_SUCCESS = new Color(40, 167, 69);     // Forest Green
    public static final Color COLOR_DANGER = new Color(220, 53, 69);      // Crimson

    // Standard Fonts
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("SansSerif", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Monospaced", Font.PLAIN, 12);

    /**
     * Styles a standard JButton with a clean professional appearance.
     */
    public static void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(FONT_BODY_BOLD);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    /**
     * Styles a primary action button.
     */
    public static void stylePrimaryButton(JButton button) {
        styleButton(button, COLOR_PRIMARY, Color.WHITE);
    }

    /**
     * Styles a secondary / neutral action button.
     */
    public static void styleSecondaryButton(JButton button) {
        styleButton(button, new Color(108, 117, 125), Color.WHITE);
    }

    /**
     * Styles a danger action button (e.g. Delete).
     */
    public static void styleDangerButton(JButton button) {
        styleButton(button, COLOR_DANGER, Color.WHITE);
    }

    /**
     * Styles a success action button (e.g. Book, Return).
     */
    public static void styleSuccessButton(JButton button) {
        styleButton(button, COLOR_SUCCESS, Color.WHITE);
    }

    /**
     * Styles a standard JTable for clean alignment and readable row heights.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(26);
        table.setGridColor(COLOR_BORDER);
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(214, 230, 247));
        table.setSelectionForeground(COLOR_TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BODY_BOLD);
        header.setBackground(new Color(230, 235, 240));
        header.setForeground(COLOR_TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 30));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Styles a standard JTextField with clean padding and border.
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(5, 8, 5, 8)
        ));
    }

    /**
     * Creates a standard panel title with an underline border.
     */
    public static JPanel createSectionHeader(String titleText, String subtitleText) {
        JPanel panel = new JPanel(new BorderLayout(5, 2));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(FONT_HEADER);
        titleLabel.setForeground(COLOR_PRIMARY);

        panel.add(titleLabel, BorderLayout.NORTH);

        if (subtitleText != null && !subtitleText.isEmpty()) {
            JLabel subLabel = new JLabel(subtitleText);
            subLabel.setFont(FONT_SMALL);
            subLabel.setForeground(COLOR_TEXT_MUTED);
            panel.add(subLabel, BorderLayout.SOUTH);
        }

        return panel;
    }

    /**
     * Creates a clean bordered card/panel.
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return card;
    }
}
