package com.fakeproductdetectionsystem.qr;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class QRDisplayFrame extends JFrame {

    public QRDisplayFrame(String qrFilePath, String code) {

        setTitle("QR Code - " + code);
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon qrImage = new ImageIcon(qrFilePath);

        JLabel imageLabel = new JLabel(qrImage);
        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        JLabel codeLabel = new JLabel(
                "Verification Code: " + code,
                SwingConstants.CENTER
        );

        codeLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        JButton closeButton =
                new JButton("CLOSE");

        closeButton.addActionListener(
                e -> dispose()
        );

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(codeLabel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}