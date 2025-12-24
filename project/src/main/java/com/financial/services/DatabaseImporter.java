package com.financial.services;

import java.sql.*;
import java.util.logging.Logger;

public class DatabaseImporter {
    private static final Logger LOGGER = Logger.getLogger(DatabaseImporter.class.getName());
    private final SQLiteManager dbManager;
    
    public DatabaseImporter() {
        this.dbManager = SQLiteManager.getInstance();
    }
    
    // Μέθοδος για εισαγωγή ΕΣΟΔΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
    public void importRegularRevenue(String code, String description, long amount) {
        String sql = "INSERT OR REPLACE INTO regular_revenues (code, description, amount) VALUES (?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, code);
            pstmt.setString(2, description);
            pstmt.setLong(3, amount);
            pstmt.executeUpdate();
            
            LOGGER.fine("Inserted regular revenue: " + code + " - " + description);
            
        } catch (SQLException e) {
            LOGGER.severe("Error importing regular revenue: " + e.getMessage());
        }
    }
    
    // Μέθοδος για εισαγωγή ΕΣΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
    public void importPublicInvestmentRevenue(String code, String description, String type, long amount) {
        String sql = "INSERT OR REPLACE INTO public_investment_revenues (code, description, type, amount) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, code);
            pstmt.setString(2, description);
            pstmt.setString(3, type);
            pstmt.setLong(4, amount);
            pstmt.executeUpdate();
            
            LOGGER.fine("Inserted public investment revenue: " + code + " - " + description);
            
        } catch (SQLException e) {
            LOGGER.severe("Error importing public investment revenue: " + e.getMessage());
        }
    }
    
    // Μέθοδος για εισαγωγή ΕΞΟΔΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
    public void importRegularExpense(String entityCode, String entityName, String serviceCode, 
                                    String serviceName, String expenseCode, String description, long amount) {
        String sql = """
            INSERT OR REPLACE INTO regular_expenses 
            (entity_code, entity_name, service_code, service_name, expense_code, description, amount) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entityCode);
            pstmt.setString(2, entityName);
            pstmt.setString(3, serviceCode);
            pstmt.setString(4, serviceName);
            pstmt.setString(5, expenseCode);
            pstmt.setString(6, description);
            pstmt.setLong(7, amount);
            pstmt.executeUpdate();
            
            // Εισαγωγή οντότητας (αν δεν υπάρχει)
            importEntity(entityCode, entityName);
            
            LOGGER.fine("Inserted regular expense: " + expenseCode + " - " + description);
            
        } catch (SQLException e) {
            LOGGER.severe("Error importing regular expense: " + e.getMessage());
        }
    }
    
    // Μέθοδος για εισαγωγή ΕΞΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
    public void importPublicInvestmentExpense(String entityCode, String entityName, String serviceCode,
        String serviceName, String expenseCode, String description, String type, long amount) {
        String sql = """
            INSERT OR REPLACE INTO public_investment_expenses 
            (entity_code, entity_name, service_code, service_name, expense_code, description, type, amount) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entityCode);
            pstmt.setString(2, entityName);
            pstmt.setString(3, serviceCode);
            pstmt.setString(4, serviceName);
            pstmt.setString(5, expenseCode);
            pstmt.setString(6, description);
            pstmt.setString(7, type);
            pstmt.setLong(8, amount);
            pstmt.executeUpdate();
            
            // Εισαγωγή οντότητας (αν δεν υπάρχει)
            importEntity(entityCode, entityName);
            
            LOGGER.fine("Inserted public investment expense: " + expenseCode + " - " + description);
            
        } catch (SQLException e) {
            LOGGER.severe("Error importing public investment expense: " + e.getMessage());
        }
    }
    
    // Μέθοδος για εισαγωγή ΟΝΤΟΤΗΤΑΣ
    private void importEntity(String entityCode, String entityName) {
        String sql = "INSERT OR IGNORE INTO entities (entity_code, entity_name) VALUES (?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entityCode);
            pstmt.setString(2, entityName);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.warning("Error importing entity: " + e.getMessage());
        }
    }
    
    // Μέθοδος για στατιστικά
    public void printDatabaseStatistics() {
        String[] queries = {
            "SELECT COUNT(*) as count FROM regular_revenues",
            "SELECT COUNT(*) as count FROM public_investment_revenues",
            "SELECT COUNT(*) as count FROM regular_expenses",
            "SELECT COUNT(*) as count FROM public_investment_expenses",
            "SELECT COUNT(*) as count FROM entities"
        };
        
        String[] tableNames = {
            "ΕΣΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ",
            "ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ",
            "ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ",
            "ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ",
            "ΟΝΤΟΤΗΤΕΣ"
        };
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("\n=== ΣΤΑΤΙΣΤΙΚΑ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ ===");
            
            for (int i = 0; i < queries.length; i++) {
                try (ResultSet rs = stmt.executeQuery(queries[i])) {
                    if (rs.next()) {
                        System.out.printf("%-40s: %d εγγραφές%n", tableNames[i], rs.getInt("count"));
                    }
                }
            }
            
            // Υπολογισμός συνολικού ποσού
            String totalAmountQuery = """
                SELECT 
                    SUM(amount) as total_amount,
                    (SELECT SUM(amount) FROM regular_expenses) as total_expenses_regular,
                    (SELECT SUM(amount) FROM public_investment_expenses) as total_expenses_investment
                FROM (
                    SELECT amount FROM regular_revenues
                    UNION ALL
                    SELECT amount FROM public_investment_revenues
                )
                """;
            
            try (ResultSet rs = stmt.executeQuery(totalAmountQuery)) {
                if (rs.next()) {
                    System.out.printf("%-40s: %,.2f €%n", "ΣΥΝΟΛΟ ΕΣΟΔΩΝ", rs.getDouble("total_amount"));
                    System.out.printf("%-40s: %,.2f €%n", "ΣΥΝΟΛΟ ΕΞΟΔΩΝ ΤΑΚΤΙΚΟΥ", rs.getDouble("total_expenses_regular"));
                    System.out.printf("%-40s: %,.2f €%n", "ΣΥΝΟΛΟ ΕΞΟΔΩΝ ΕΠΕΝΔΥΣΕΩΝ", rs.getDouble("total_expenses_investment"));
                }
            }
            
        } catch (SQLException e) {
            LOGGER.severe("Error getting statistics: " + e.getMessage());
        }
    }
}