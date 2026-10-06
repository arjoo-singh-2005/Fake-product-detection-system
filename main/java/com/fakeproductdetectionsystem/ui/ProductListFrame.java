package com.fakeproductdetectionsystem.ui;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fakeproductdetectionsystem.db.DatabaseConnection;

public class ProductListFrame extends JFrame {

    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductListFrame() {

        setTitle("All Products");
        setSize(950, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        loadProducts();
    }

    private void createGUI() {

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Manufacturer");
        tableModel.addColumn("Batch Number");
        tableModel.addColumn("Manufacturing Date");
        tableModel.addColumn("Expiry Date");
        tableModel.addColumn("Verification Code");

        productTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(productTable);

        JButton refreshButton =
                new JButton("REFRESH");

        JButton closeButton =
                new JButton("CLOSE");

        refreshButton.addActionListener(e -> loadProducts());

        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();

        bottomPanel.add(refreshButton);
        bottomPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {

        tableModel.setRowCount(0);

        String sql = """
                SELECT product_id,
                       product_name,
                       manufacturer,
                       batch_number,
                       manufacturing_date,
                       expiry_date,
                       verification_code
                FROM products
                ORDER BY product_id
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                tableModel.addRow(new Object[] {
                        result.getInt("product_id"),
                        result.getString("product_name"),
                        result.getString("manufacturer"),
                        result.getString("batch_number"),
                        result.getDate("manufacturing_date"),
                        result.getDate("expiry_date"),
                        result.getString("verification_code")
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load products."
            );
        }
    }
}