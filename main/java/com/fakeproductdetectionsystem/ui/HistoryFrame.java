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

public class HistoryFrame extends JFrame {

    private JTable historyTable;
    private DefaultTableModel tableModel;

    public HistoryFrame() {

        setTitle("Verification History");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
        loadHistory();
    }

    private void createGUI() {

        tableModel = new DefaultTableModel();

        tableModel.addColumn("ID");
        tableModel.addColumn("Verification Code");
        tableModel.addColumn("Result");
        tableModel.addColumn("Date & Time");

        historyTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(historyTable);

        JButton refreshButton =
                new JButton("REFRESH");

        refreshButton.addActionListener(e -> loadHistory());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton);

        setLayout(new BorderLayout());

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadHistory() {

        tableModel.setRowCount(0);

        String sql = """
                SELECT history_id,
                       verification_code,
                       verification_result,
                       verification_time
                FROM verification_history
                ORDER BY verification_time DESC
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result =
                     statement.executeQuery()) {

            while (result.next()) {

                tableModel.addRow(new Object[] {
                        result.getInt("history_id"),
                        result.getString("verification_code"),
                        result.getString("verification_result"),
                        result.getTimestamp("verification_time")
                });
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load verification history."
            );
        }
    }
}