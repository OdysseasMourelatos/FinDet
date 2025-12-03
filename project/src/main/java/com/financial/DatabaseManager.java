package com.financial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/budget_db";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";


    public static Connection getConnection() throws SQLException {
        try {
            // Φόρτωση του driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
    public static void insertPublicInvestmentExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        String sql = "INSERT INTO PublicInvestmentBudgetExpenses (entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entityCode);
            pstmt.setString(2, entityName);
            pstmt.setString(3, serviceCode);
            pstmt.setString(4, serviceName);
            pstmt.setString(5, code);
            pstmt.setString(6, description);
            pstmt.setString(7, type);
            pstmt.setString(8, category);
            pstmt.setLong(9, amount);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Database INSERT error for PublicInvestmentBudgetExpense: " + e.getMessage());
        }
    }
    public static ArrayList<RegularBudgetExpense> loadRegularExpensesFromDB() {
        ArrayList<RegularBudgetExpense> expenses = new ArrayList<>();
        String sql = "SELECT entityCode, entityName, serviceCode, serviceName, code, description, category, amount FROM RegularBudgetExpenses";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String entityCode = rs.getString("entityCode");
                String entityName = rs.getString("entityName");
                String serviceCode = rs.getString("serviceCode");
                String serviceName = rs.getString("serviceName");
                String code = rs.getString("code");
                String description = rs.getString("description");
                String category = rs.getString("category");
                long amount = rs.getLong("amount");

                // Δημιουργία αντικειμένου Java από τα δεδομένα της βάσης
                RegularBudgetExpense expense = new RegularBudgetExpense(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
                expenses.add(expense);
            }
        } catch (SQLException e) {
            System.err.println("SQL SELECT error for Regular Expenses: " + e.getMessage());
        }
        return expenses;
    }
}