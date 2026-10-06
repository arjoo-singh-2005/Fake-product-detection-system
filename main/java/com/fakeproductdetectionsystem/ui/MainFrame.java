package com.fakeproductdetectionsystem.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Fake Product Detection System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 30, 40
                )
        );

        JLabel titleLabel = new JLabel(
                "FAKE PRODUCT DETECTION SYSTEM",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        JLabel subtitleLabel = new JLabel(
                "Product Authentication & Verification",
                SwingConstants.CENTER
        );

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        JPanel titlePanel = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        mainPanel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        JPanel buttonPanel = new JPanel(
                new GridLayout(4, 1, 15, 15)
        );

        JButton verifyButton =
                new JButton("VERIFY PRODUCT");

        JButton adminButton =
                new JButton("ADMIN PANEL");

        JButton historyButton =
                new JButton("VIEW HISTORY");

        JButton exitButton =
                new JButton("EXIT");

        Font buttonFont =
                new Font("Arial", Font.BOLD, 16);

        verifyButton.setFont(buttonFont);
        adminButton.setFont(buttonFont);
        historyButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);

        buttonPanel.add(verifyButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(exitButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        JLabel footerLabel = new JLabel(
                "Java + JDBC + PostgreSQL",
                SwingConstants.CENTER
        );

        footerLabel.setFont(
                new Font("Arial", Font.ITALIC, 13)
        );

        mainPanel.add(
                footerLabel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // Verify Product
        verifyButton.addActionListener(e -> {

            VerifyProductFrame frame =
                    new VerifyProductFrame();

            frame.setVisible(true);
        });

        // Admin Login
        adminButton.addActionListener(e -> {

            LoginFrame frame =
                    new LoginFrame();

            frame.setVisible(true);
        });

        // View History
        historyButton.addActionListener(e -> {

            HistoryFrame frame =
                    new HistoryFrame();

            frame.setVisible(true);
        });

        // Exit
        exitButton.addActionListener(e -> {

            System.exit(0);
        });
    }
}