package com.financial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; // ΝΕΟ
import java.sql.SQLException;

public class DatabaseManager {


    private static final String DB_URL = "jdbc:mysql://localhost:3306/budget_db";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
    }


    public static void insertRegularExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        String sql = "INSERT INTO RegularBudgetExpenses (entityCode, entityName, serviceCode, serviceName, code, description, category, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entityCode);
            pstmt.setString(2, entityName);
            pstmt.setString(3, serviceCode);
            pstmt.setString(4, serviceName);
            pstmt.setString(5, code);
            pstmt.setString(6, description);
            pstmt.setString(7, category);
            pstmt.setLong(8, amount);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Database INSERT error for RegularBudgetExpense: " + e.getMessage());
        }
    }
}