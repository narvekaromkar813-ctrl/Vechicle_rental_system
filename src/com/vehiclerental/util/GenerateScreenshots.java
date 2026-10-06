package com.vehiclerental.util;

import com.vehiclerental.gui.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenerateScreenshots {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        MainFrame frame = new MainFrame();
        frame.setSize(1180, 750);
        frame.setLocation(0, 0);
        frame.setVisible(true);
        Thread.sleep(500);

        String[] cards = {
            "DASHBOARD", "VEHICLES", "CUSTOMERS", "AVAILABILITY",
            "SEARCH", "BOOKING", "RETURN", "HISTORY", "BILLING", "REPORTS"
        };

        File outDir = new File("docs/screenshots");
        if (!outDir.exists()) outDir.mkdirs();

        for (String card : cards) {
            frame.navigateTo(card);
            Thread.sleep(200);

            // Capture the content pane cleanly
            Container contentPane = frame.getContentPane();
            BufferedImage image = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            contentPane.paintAll(g2);
            g2.dispose();

            File file = new File(outDir, card.toLowerCase() + ".png");
            ImageIO.write(image, "png", file);
            System.out.println("Generated: " + file.getAbsolutePath() + " (" + file.length() + " bytes)");
        }
        frame.dispose();
        System.exit(0);
    }
}
