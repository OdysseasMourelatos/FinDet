package com.financial.services;

import java.sql.*;
import java.util.logging.Logger;

public class SQLiteManager {
    private static final Logger LOGGER = Logger.getLogger(SQLiteManager.class.getName());
    private static final String DB_URL = "jdbc:sqlite:budget_database.db";
    
    private static SQLiteManager instance;
    private Connection connection;
    
    private SQLiteManager() {
        initializeDatabase();
    }
    
    public static synchronized SQLiteManager getInstance() {
        if (instance == null) {
            instance = new SQLiteManager();
        }
        return instance;
    }
    
    private void initializeDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            LOGGER.info("Database initialized successfully");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.severe("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    private void createTables() throws SQLException {
        String[] createTableQueries = {
            // Πίνακας για ΕΣΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
            """
            CREATE TABLE IF NOT EXISTS regular_revenues (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                code TEXT NOT NULL,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(code, description)
            )
            """,
            
            // Πίνακας για ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
            """
            CREATE TABLE IF NOT EXISTS public_investment_revenues (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(type IN ('ΕΘΝΙΚΟ', 'ΕΘΝΙΚΟ ΣΚΕΛΟΣ', 'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ', 'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ')),
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(code, description, type)
            )
            """,
            
            // Πίνακας για ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
            """
            CREATE TABLE IF NOT EXISTS regular_expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                entity_code TEXT NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(entity_code, service_code, expense_code, description)
            )
            """,
            
            // Πίνακας για ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
            """
            CREATE TABLE IF NOT EXISTS public_investment_expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                entity_code TEXT NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(entity_code, service_code, expense_code, description, type)
            )
            """,
            
            // Πίνακας για ΟΝΤΟΤΗΤΕΣ (Entities)
            """
            CREATE TABLE IF NOT EXISTS entities (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                entity_code TEXT UNIQUE NOT NULL,
                entity_name TEXT NOT NULL,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """
        };
        
        try (Statement stmt = connection.createStatement()) {
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
        }
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.warning("Error closing connection: " + e.getMessage());
        }
    }
}
