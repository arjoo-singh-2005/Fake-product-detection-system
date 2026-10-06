package com.fakeproductdetectionsystem.ui;

import com.fakeproductdetectionsystem.ui.VerifyProductFrame;
import com.fakeproductdetectionsystem.dao.ProductDAO;
import com.fakeproductdetectionsystem.model.Product;
import com.fakeproductdetectionsystem.qr.QRScannerFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class VerifyProductFrame extends JFrame {

    private JTextField codeField;
    private JLabel resultLabel;
    private JTextArea detailsArea;

    private ProductDAO productDAO;

    public VerifyProductFrame() {

        productDAO = new ProductDAO();

        setTitle("Verify Product");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    // Constructor used after QR scanning
    public VerifyProductFrame(String scannedCode) {

        this();

        codeField.setText(scannedCode);

        verifyProduct();
    }

    private void createGUI() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel(
                "FAKE PRODUCT DETECTION SYSTEM",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel(
                "Product Authentication & Verification",
                SwingConstants.CENTER
        );

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        panel.add(subtitleLabel, gbc);

        // Verification code label
        JLabel codeLabel =
                new JLabel("Verification Code:");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        panel.add(codeLabel, gbc);

        // Verification code field
        codeField = new JTextField(20);

        gbc.gridx = 1;
        gbc.gridy = 2;

        panel.add(codeField, gbc);

        // Verify button
        JButton verifyButton =
                new JButton("VERIFY PRODUCT");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;

        panel.add(verifyButton, gbc);

        // Scan QR button
        JButton scanButton =
                new JButton("SCAN QR CODE");

        gbc.gridx = 1;
        gbc.gridy = 3;

        panel.add(scanButton, gbc);

        // Result label
        resultLabel =
                new JLabel("Result: ", SwingConstants.CENTER);

        resultLabel.setFont(
                new Font("Arial", Font.BOLD, 18)
        );

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        panel.add(resultLabel, gbc);

        // Details area
        detailsArea =
                new JTextArea(12, 40);

        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        panel.add(
                new JScrollPane(detailsArea),
                gbc
        );

        // Clear button
        JButton clearButton =
                new JButton("CLEAR");

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(clearButton, gbc);

        add(panel);

        // Verify button action
        verifyButton.addActionListener(
                e -> verifyProduct()
        );

        // Scan QR button action
        scanButton.addActionListener(e -> {

            QRScannerFrame scanner =
                    new QRScannerFrame();

            scanner.setVisible(true);
        });

        // Clear button action
        clearButton.addActionListener(
                e -> clearFields()
        );
    }

    // =====================================================
    // VERIFY PRODUCT
    // =====================================================

    public void verifyProduct() {

        String code =
                codeField.getText().trim();

        if (code.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a verification code."
            );

            return;
        }

        Product product =
                productDAO.findProductByCode(code);

        // ---------------------------------------------
        // PRODUCT NOT FOUND
        // ---------------------------------------------

        if (product == null) {

            resultLabel.setText(
                    "Result: ❌ PRODUCT NOT VERIFIED"
            );

            detailsArea.setText(
                    "No product was found in the database.\n\n"
                            + "Verification Code: "
                            + code
                            + "\n\n"
                            + "The product could not be authenticated."
            );

            productDAO.saveVerificationHistory(
                    code,
                    "Not Verified"
            );

            return;
        }

        // ---------------------------------------------
        // DISPLAY PRODUCT DETAILS
        // ---------------------------------------------

        String details =
                "Product ID: "
                        + product.getProductId()
                        + "\n\n"

                        + "Product Name: "
                        + product.getProductName()
                        + "\n\n"

                        + "Manufacturer: "
                        + product.getManufacturer()
                        + "\n\n"

                        + "Batch Number: "
                        + product.getBatchNumber()
                        + "\n\n"

                        + "Manufacturing Date: "
                        + product.getManufacturingDate()
                        + "\n\n"

                        + "Expiry Date: "
                        + product.getExpiryDate()
                        + "\n\n"

                        + "Verification Code: "
                        + product.getVerificationCode();

        detailsArea.setText(details);

        // ---------------------------------------------
        // EXPIRY CHECK
        // ---------------------------------------------

        LocalDate today =
                LocalDate.now();

        LocalDate expiryDate =
                product.getExpiryDate();

        if (expiryDate != null
                && expiryDate.isBefore(today)) {

            resultLabel.setText(
                    "Result: ⚠️ PRODUCT IS EXPIRED"
            );

            productDAO.saveVerificationHistory(
                    code,
                    "Expired"
            );

        } else {

            resultLabel.setText(
                    "Result: ✅ PRODUCT IS GENUINE"
            );

            productDAO.saveVerificationHistory(
                    code,
                    "Genuine"
            );
        }
    }

    // =====================================================
    // CLEAR
    // =====================================================

    private void clearFields() {

        codeField.setText("");

        resultLabel.setText(
                "Result: "
        );

        detailsArea.setText("");
    }
}