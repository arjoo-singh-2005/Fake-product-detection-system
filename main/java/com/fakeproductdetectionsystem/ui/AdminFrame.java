package com.fakeproductdetectionsystem.ui;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fakeproductdetectionsystem.dao.ProductDAO;
import com.fakeproductdetectionsystem.model.Product;
import com.fakeproductdetectionsystem.qr.QRCodeGenerator;
import com.fakeproductdetectionsystem.qr.QRDisplayFrame;

public class AdminFrame extends JFrame {

    private JTextField nameField;
    private JTextField manufacturerField;
    private JTextField batchField;
    private JTextField manufacturingDateField;
    private JTextField expiryDateField;
    private JTextField codeField;

    private ProductDAO productDAO;

    public AdminFrame() {

        productDAO = new ProductDAO();

        setTitle("Admin - Fake Product Detection System");
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        JPanel panel = new JPanel(new GridLayout(12, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Product Name
        panel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        // Manufacturer
        panel.add(new JLabel("Manufacturer:"));
        manufacturerField = new JTextField();
        panel.add(manufacturerField);

        // Batch Number
        panel.add(new JLabel("Batch Number:"));
        batchField = new JTextField();
        panel.add(batchField);

        // Manufacturing Date
        panel.add(new JLabel("Manufacturing Date (YYYY-MM-DD):"));
        manufacturingDateField = new JTextField();
        panel.add(manufacturingDateField);

        // Expiry Date
        panel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        expiryDateField = new JTextField();
        panel.add(expiryDateField);

        // Verification Code
        panel.add(new JLabel("Verification Code:"));
        codeField = new JTextField();
        panel.add(codeField);

        // Buttons
        JButton addButton = new JButton("ADD PRODUCT");
        JButton updateButton = new JButton("UPDATE PRODUCT");

        JButton deleteButton = new JButton("DELETE PRODUCT");
        JButton viewButton = new JButton("VIEW PRODUCTS");

        JButton searchButton = new JButton("SEARCH PRODUCT");
        JButton clearButton = new JButton("CLEAR");

        JButton closeButton = new JButton("CLOSE");

        panel.add(addButton);
        panel.add(updateButton);

        panel.add(deleteButton);
        panel.add(viewButton);

        panel.add(searchButton);
        panel.add(clearButton);

        panel.add(closeButton);
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        add(panel);

        // ADD PRODUCT
        addButton.addActionListener(e -> addProduct());

        // UPDATE PRODUCT
        updateButton.addActionListener(e -> updateProduct());

        // DELETE PRODUCT
        deleteButton.addActionListener(e -> deleteProduct());

        // VIEW PRODUCTS
        viewButton.addActionListener(e -> {

            ProductListFrame frame =
                    new ProductListFrame();

            frame.setVisible(true);
        });

        // SEARCH PRODUCT
        searchButton.addActionListener(e -> searchProduct());

        // CLEAR
        clearButton.addActionListener(e -> clearFields());

        // CLOSE
        closeButton.addActionListener(e -> dispose());
    }

    // =====================================================
    // ADD PRODUCT + QR GENERATION
    // =====================================================

    private void addProduct() {

        try {

            String name =
                    nameField.getText().trim();

            String manufacturer =
                    manufacturerField.getText().trim();

            String batch =
                    batchField.getText().trim();

            String manufacturingDate =
                    manufacturingDateField.getText().trim();

            String expiryDate =
                    expiryDateField.getText().trim();

            String code =
                    codeField.getText().trim();

            // Check empty fields
            if (name.isEmpty()
                    || manufacturer.isEmpty()
                    || batch.isEmpty()
                    || manufacturingDate.isEmpty()
                    || expiryDate.isEmpty()
                    || code.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields."
                );

                return;
            }

            // Convert dates
            LocalDate mfgDate =
                    LocalDate.parse(manufacturingDate);

            LocalDate expDate =
                    LocalDate.parse(expiryDate);

            // Validate dates
            if (expDate.isBefore(mfgDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Expiry date cannot be before manufacturing date."
                );

                return;
            }

            // Create Product object
            Product product = new Product(
                    name,
                    manufacturer,
                    batch,
                    mfgDate,
                    expDate,
                    code
            );

            // Add product to PostgreSQL
            boolean success =
                    productDAO.addProduct(product);

            if (success) {

                try {

                    // QR file name
                    String qrFilePath =
                            "QR_" + code + ".png";

                    // Generate QR
                    QRCodeGenerator.generateQRCode(
                            code,
                            qrFilePath
                    );

                    // Product added message
                    JOptionPane.showMessageDialog(
                            this,
                            "Product added successfully!"
                    );

                    // Display QR
                    QRDisplayFrame qrFrame =
                            new QRDisplayFrame(
                                    qrFilePath,
                                    code
                            );

                    qrFrame.setVisible(true);

                } catch (Exception qrError) {

                    qrError.printStackTrace();

                    JOptionPane.showMessageDialog(
                            this,
                            "Product added successfully,\n"
                                    + "but QR code could not be generated."
                    );
                }

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Product could not be added.\n"
                                + "Verification code may already exist."
                );
            }

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n"
                            + "Please use YYYY-MM-DD"
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while adding the product."
            );
        }
    }

    // =====================================================
    // UPDATE PRODUCT
    // =====================================================

    private void updateProduct() {

        try {

            String name =
                    nameField.getText().trim();

            String manufacturer =
                    manufacturerField.getText().trim();

            String batch =
                    batchField.getText().trim();

            String manufacturingDate =
                    manufacturingDateField.getText().trim();

            String expiryDate =
                    expiryDateField.getText().trim();

            String code =
                    codeField.getText().trim();

            if (name.isEmpty()
                    || manufacturer.isEmpty()
                    || batch.isEmpty()
                    || manufacturingDate.isEmpty()
                    || expiryDate.isEmpty()
                    || code.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields."
                );

                return;
            }

            LocalDate mfgDate =
                    LocalDate.parse(manufacturingDate);

            LocalDate expDate =
                    LocalDate.parse(expiryDate);

            if (expDate.isBefore(mfgDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Expiry date cannot be before manufacturing date."
                );

                return;
            }

            Product product = new Product(
                    name,
                    manufacturer,
                    batch,
                    mfgDate,
                    expDate,
                    code
            );

            boolean success =
                    productDAO.updateProduct(product);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product updated successfully!"
                );

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No product found with this verification code."
                );
            }

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format.\n"
                            + "Please use YYYY-MM-DD"
            );

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while updating the product."
            );
        }
    }

    // =====================================================
    // DELETE PRODUCT
    // =====================================================

    private void deleteProduct() {

        String code =
                codeField.getText().trim();

        if (code.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter the verification code of the product to delete."
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this product?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                productDAO.deleteProduct(code);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Product deleted successfully!"
            );

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No product found with this verification code."
            );
        }
    }

    // =====================================================
    // SEARCH PRODUCT
    // =====================================================

    private void searchProduct() {

        String code =
                codeField.getText().trim();

        if (code.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a verification code."
            );

            return;
        }

        Product product =
                productDAO.findProductByCode(code);

        if (product != null) {

            nameField.setText(
                    product.getProductName()
            );

            manufacturerField.setText(
                    product.getManufacturer()
            );

            batchField.setText(
                    product.getBatchNumber()
            );

            if (product.getManufacturingDate() != null) {

                manufacturingDateField.setText(
                        product.getManufacturingDate().toString()
                );
            }

            if (product.getExpiryDate() != null) {

                expiryDateField.setText(
                        product.getExpiryDate().toString()
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Product found successfully!"
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No product found with this verification code."
            );
        }
    }

    // =====================================================
    // CLEAR FIELDS
    // =====================================================

    private void clearFields() {

        nameField.setText("");
        manufacturerField.setText("");
        batchField.setText("");
        manufacturingDateField.setText("");
        expiryDateField.setText("");
        codeField.setText("");
    }
}