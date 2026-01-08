package com.financial.database;

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    //each insert method will be called on the object's class respectively

    public void insertToRegularBudgetRevenues(RegularBudgetRevenue regularBR) {
        String insert = """
        Insert into Regular_Budget_Revenues(code,description,amount,category)
        Values(?,?,?,?)
        """;

        try(PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setString(1,regularBR.code);
            ps.setString(2,regularBR.description);
            ps.setLong(3,regularBR.amount);
            ps.setString(4,regularBR.category);
        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
        }
    }
    
    public void insertToPublicInvestmentBudgetRevenues(PublicInvestmentBudgetRevenue investmentBR) {
       String insert = """
       Insert into Public_Investment_Budget_Revenues(code,description,type,amount,category)
       Values(?,?,?,?,?)
       """;
    }

    public void insertToRegularBudgeteExpenses(RegularBudgetExpense regularBE) {
       String insert = """
       Insert into Regular_Budget_Expenses(entity_code, entity_name, service_code, service_name,
       expense_code, description, amount, category)
       Values(?,?,?,?,?,?,?,?)
       """;
    }

    public void insertToPublicInvestmentBudgeteExpenses(PublicInvestmentBudgetExpense investmentBE) {
       String insert = """
       Insert into Public_Investment_Budget_Expenses(entity_code, entity_name, service_code, service_name,
       expense_code,description,type,amount,category) 
       Values(?,?,?,?,?,?,?,?,?)
       """;
    }

     public void insertToEntities(Entity e) {
        String insert = """
        Insert into Entities(entity_code,entity_name)
        Values(?,?)
        """;
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
