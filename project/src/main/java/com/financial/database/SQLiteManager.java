package com.financial.database;

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

            """
            CREATE TABLE IF NOT EXISTS Regular_Budget_Revenues (
                code VARCHAR PRIMARY KEY NOT NULL,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(description)
            ) """,
                                                    
            """
            CREATE TABLE IF NOT EXISTS Public_Investment_Budget_Revenues (
                code VARCHAR PRIMARY KEY NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(
                    type IN (
                        'ΕΘΝΙΚΟ',
                        'ΕΘΝΙΚΟ ΣΚΕΛΟΣ',
                        'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ',
                        'ΣΥΓΧΡΗΜΑΤΟΔΟΥΤΟΜΕΝΟ ΣΚΕΛΟΣ'
                    )
                ),
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(description, type)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS Regular_Budget_Expenses (
                entity_code VARCHAR PRIMARY KEY NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(service_code, expense_code, description)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS Public_Investment_Budget_Expenses (
                entity_code VARCHAR PRIMARY KEY NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(service_code, expense_code, description, type)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS Entities (
                entity_code VARCHAR PRIMARY KEY NOT NULL,
                entity_name TEXT NOT NULL,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) """
        };

        try (Statement stmt = connection.createStatement()) {
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
        }
    }

    public void insertToRegularBudgetRevenues(RegularBudgetRevenue regularBR) {
        String insert = "Insert into Regular_Budget_Revenues(code,description,amount,category)
        Values(" + regularBR.code + "," + regularBR.description + "," + regularBR.amount + "," + regularBR.category + ")";
    }
    
    public void insertToPublicInvestmentBudgetRevenues(PublicInvestmentBudgetRevenue investmentBR) {
       String insert = "Insert into Public_Investment_Budget_Revenues(code,description,type,amount,category)
       Values(" + investmentBR.code + "," + investmentBR.description + "," + investmentBR.type + "," + investmentBR.amount +
       "," + investmentBR.category + ")";
    }

    public void insertToRegularBudgeteExpenses(RegularBudgetExpense regularBE) {
       String insert = "Insert into Regular_Budget_Revenues(entity_code, entity_name, service_code, service_name,
       expense_code,description,amount,category)
       Values(" + regularBE.entity_code + "," + regularBE.entity_name + "," + regularBE.service_code + "," + regularBE.service_name + ","
       + regularBE.expense_code + "," + regularBE.description + "," +  regularBE.amount + "," + regularBE.category ")";
    }

    public void insertToPublicInvestmentBudgeteExpenses(PublicInvestmentBudgetExpense investmentBE) {
       String insert = "Insert into Regular_Budget_Revenues(entity_code, entity_name, service_code, service_name,
       expense_code,description,type,amount,category) Values(" + investmentBE.entity_code + "," + investmentBE.entity_name + "," 
       + investmentBE.service_code + "," + investmentBE.service_name + "," + investmentBE.expense_code + "," 
       + investmentBE.description + "," + investmentBE.type + "," investmentBE.amount + "," 
       + investmentBE.category ")";
    }

     public void insertToEntities(Entity e) {
        String insert = "Insert into Entities(entity_code,entity_name)
        Values(" + e.entity_code + "," + e.entity_name + ")";
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
