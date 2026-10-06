package com.fakeproductdetectionsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fakeproductdetectionsystem.db.DatabaseConnection;
import com.fakeproductdetectionsystem.model.Product;

public class ProductDAO {

    // Find a product using verification code
    public Product findProductByCode(String code) {

        String sql = "SELECT * FROM products WHERE verification_code = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Product product = new Product();

                product.setProductId(result.getInt("product_id"));
                product.setProductName(result.getString("product_name"));
                product.setManufacturer(result.getString("manufacturer"));
                product.setBatchNumber(result.getString("batch_number"));

                if (result.getDate("manufacturing_date") != null) {
                    product.setManufacturingDate(
                            result.getDate("manufacturing_date").toLocalDate()
                    );
                }

                if (result.getDate("expiry_date") != null) {
                    product.setExpiryDate(
                            result.getDate("expiry_date").toLocalDate()
                    );
                }

                product.setVerificationCode(
                        result.getString("verification_code")
                );

                return product;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // Save verification history
    public void saveVerificationHistory(
            String verificationCode,
            String result) {

        String sql = """
                INSERT INTO verification_history
                (verification_code, verification_result)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, verificationCode);
            statement.setString(2, result);

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Add a new product
   public boolean addProduct(Product product) {

    String checkSql =
            "SELECT COUNT(*) FROM products WHERE verification_code = ?";

    String insertSql = """
            INSERT INTO products
            (product_name, manufacturer, batch_number,
             manufacturing_date, expiry_date, verification_code)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement checkStatement =
                 connection.prepareStatement(checkSql)) {

        checkStatement.setString(
                1,
                product.getVerificationCode()
        );

        ResultSet result = checkStatement.executeQuery();

        if (result.next() && result.getInt(1) > 0) {
            return false;
        }

        try (PreparedStatement insertStatement =
                     connection.prepareStatement(insertSql)) {

            insertStatement.setString(
                    1,
                    product.getProductName()
            );

            insertStatement.setString(
                    2,
                    product.getManufacturer()
            );

            insertStatement.setString(
                    3,
                    product.getBatchNumber()
            );

            insertStatement.setDate(
                    4,
                    java.sql.Date.valueOf(
                            product.getManufacturingDate()
                    )
            );

            insertStatement.setDate(
                    5,
                    java.sql.Date.valueOf(
                            product.getExpiryDate()
                    )
            );

            insertStatement.setString(
                    6,
                    product.getVerificationCode()
            );

            insertStatement.executeUpdate();

            return true;
        }

    } catch (Exception e) {

        e.printStackTrace();
        return false;
    }
}
    public boolean updateProduct(Product product) {

    String sql = """
            UPDATE products
            SET product_name = ?,
                manufacturer = ?,
                batch_number = ?,
                manufacturing_date = ?,
                expiry_date = ?
            WHERE verification_code = ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, product.getProductName());
        statement.setString(2, product.getManufacturer());
        statement.setString(3, product.getBatchNumber());

        statement.setDate(
                4,
                java.sql.Date.valueOf(product.getManufacturingDate())
        );

        statement.setDate(
                5,
                java.sql.Date.valueOf(product.getExpiryDate())
        );

        statement.setString(6, product.getVerificationCode());

        int rowsUpdated = statement.executeUpdate();

        return rowsUpdated > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public boolean deleteProduct(String verificationCode) {

    String sql = """
            DELETE FROM products
            WHERE verification_code = ?
            """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, verificationCode);

        int rowsDeleted = statement.executeUpdate();

        return rowsDeleted > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
public ResultSet getAllProducts() throws Exception {

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

    Connection connection = DatabaseConnection.getConnection();

    PreparedStatement statement =
            connection.prepareStatement(sql);

    return statement.executeQuery();
}
}
