package com.financial.database;

import com.financial.entries.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Manages the SQLite database operations for the budget application.
 */
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
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
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
                code TEXT PRIMARY KEY NOT NULL,
                description TEXT NOT NULL,
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) """,

            """
            CREATE TABLE IF NOT EXISTS PIB_National_Revenues (
                code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(
                    type IN (
                        'ΕΘΝΙΚΟ',
                        'ΕΘΝΙΚΟ ΣΚΕΛΟΣ',
                        'Εθνικό',
                        'Εθνικό Σκέλος'
                    )
                ),
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY(code)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS PIB_CoFunded_Revenues (
                code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(
                    type IN (
                        'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ',
                        'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ', 
                        'Συγχρηματοδοτούμενο', 
                        'Συγχρηματοδοτούμενο σκέλος'
                    )
                ),
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΣΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY(code)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS Entities (
                entity_code TEXT PRIMARY KEY NOT NULL,
                entity_name TEXT NOT NULL,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) """,

            """
            CREATE TABLE IF NOT EXISTS Regular_Budget_Expenses (
                entity_code TEXT NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY(entity_code, service_code, expense_code),
                FOREIGN KEY(entity_code) REFERENCES Entities(entity_code)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS PIB_National_Expenses (
                entity_code TEXT NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(
                    type IN ('ΕΘΝΙΚΟ',
                             'ΕΘΝΙΚΟ ΣΚΕΛΟΣ',
                             'Εθνικό',
                             'Εθνικό Σκέλος')
                ),
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY(entity_code, service_code, expense_code),
                FOREIGN KEY(entity_code) REFERENCES Entities(entity_code)
            ) """,

            """
            CREATE TABLE IF NOT EXISTS PIB_CoFunded_Expenses (
                entity_code TEXT NOT NULL,
                entity_name TEXT NOT NULL,
                service_code TEXT NOT NULL,
                service_name TEXT NOT NULL,
                expense_code TEXT NOT NULL,
                description TEXT NOT NULL,
                type TEXT NOT NULL CHECK(
                    type IN ('ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ',
                             'ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ',
                             'Συγχρηματοδοτούμενο', 
                             'Συγχρηματοδοτούμενο σκέλος')
                ),
                amount INTEGER NOT NULL,
                category TEXT DEFAULT 'ΕΞΟΔΑ ΠΔΕ-ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ',
                import_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY(entity_code, service_code, expense_code),
                FOREIGN KEY(entity_code) REFERENCES Entities(entity_code)
            ) """,
        };

        try (Statement stmt = connection.createStatement()) {
            for (String query : createTableQueries) {
                stmt.execute(query);
            }
        }
    }

    public void insertToRegularBudgetRevenues(RegularBudgetRevenue regularBR) {
        String insert = """
            INSERT OR IGNORE INTO Regular_Budget_Revenues(code, description, amount, category)
            VALUES(?, ?, ?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setString(1, regularBR.getCode());
            ps.setString(2, regularBR.getDescription());
            ps.setLong(3, regularBR.getAmount());
            ps.setString(4, regularBR.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting revenue: " + e.getMessage());
        }
    }

    public void insertToPublicInvestmentBudgetNationalRevenues(PublicInvestmentBudgetNationalRevenue r) {
        String sql = "INSERT OR IGNORE INTO PIB_National_Revenues(code, description, type, amount, category) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getCode());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getType());
            ps.setLong(4, r.getAmount());
            ps.setString(5, r.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting National Revenue: " + e.getMessage());
        }
    }

    public void insertToPublicInvestmentBudgetCoFundedRevenues(PublicInvestmentBudgetCoFundedRevenue r) {
        String sql = "INSERT OR IGNORE INTO PIB_CoFunded_Revenues(code, description, type, amount, category) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getCode());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getType());
            ps.setLong(4, r.getAmount());
            ps.setString(5, r.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting CoFunded Revenue: " + e.getMessage());
        }
    }

    public void insertToRegularBudgetExpenses(RegularBudgetExpense regularBE) {
        String insert = """
            INSERT OR IGNORE INTO Regular_Budget_Expenses(entity_code, entity_name, service_code,
            service_name, expense_code, description, amount, category)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setString(1, regularBE.getEntityCode());
            ps.setString(2, regularBE.getEntityName());
            ps.setString(3, regularBE.getServiceCode());
            ps.setString(4, regularBE.getServiceName());
            ps.setString(5, regularBE.getCode());
            ps.setString(6, regularBE.getDescription());
            ps.setLong(7, regularBE.getAmount());
            ps.setString(8, regularBE.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting expense: " + e.getMessage());
        }
    }

    public void insertToPublicInvestmentBudgetNationalExpenses(PublicInvestmentBudgetNationalExpense e) {
        String sql = "INSERT OR IGNORE INTO PIB_National_Expenses(entity_code, entity_name, service_code, service_name, expense_code, description, type, amount, category) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getEntityCode());
            ps.setString(2, e.getEntityName());
            ps.setString(3, e.getServiceCode());
            ps.setString(4, e.getServiceName());
            ps.setString(5, e.getCode());
            ps.setString(6, e.getDescription());
            ps.setString(7, e.getType());
            ps.setLong(8, e.getAmount());
            ps.setString(9, e.getCategory());
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.severe("Error inserting National Expense: " + ex.getMessage());
        }
    }

    public void insertToPublicInvestmentBudgetCoFundedExpenses(PublicInvestmentBudgetCoFundedExpense e) {
        String sql = "INSERT OR IGNORE INTO PIB_CoFunded_Expenses(entity_code, entity_name, service_code, service_name, expense_code, description, type, amount, category) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getEntityCode());
            ps.setString(2, e.getEntityName());
            ps.setString(3, e.getServiceCode());
            ps.setString(4, e.getServiceName());
            ps.setString(5, e.getCode());
            ps.setString(6, e.getDescription());
            ps.setString(7, e.getType());
            ps.setLong(8, e.getAmount());
            ps.setString(9, e.getCategory());
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.severe("Error inserting CoFunded Expense: " + ex.getMessage());
        }
    }

    public void insertToEntities(Entity entity) {
        String insert = """
            INSERT OR IGNORE INTO Entities(entity_code, entity_name)
            VALUES(?, ?)
            """;

        try (PreparedStatement ps = connection.prepareStatement(insert)) {
            ps.setString(1, entity.getEntityCode());
            ps.setString(2, entity.getEntityName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting entity: " + e.getMessage());
        }
    }

    public void insertIntoTables() {
        try {
            connection.setAutoCommit(false);

            for (RegularBudgetRevenue revenue : RegularBudgetRevenue.getAllRegularBudgetRevenues()) {
                insertToRegularBudgetRevenues(revenue);
            }

            for (PublicInvestmentBudgetNationalRevenue revenue : PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues()) {
                insertToPublicInvestmentBudgetNationalRevenues(revenue);
            }

            for (PublicInvestmentBudgetCoFundedRevenue revenue : PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues()) {
                insertToPublicInvestmentBudgetCoFundedRevenues(revenue);
            }

            for (Entity entity : Entity.getEntities()) {
                insertToEntities(entity);
            }

            for (RegularBudgetExpense expense : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
                insertToRegularBudgetExpenses(expense);
            }

            for (PublicInvestmentBudgetNationalExpense e : PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses()) {
                insertToPublicInvestmentBudgetNationalExpenses(e);
            }

            for (PublicInvestmentBudgetCoFundedExpense e : PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses()) {
                insertToPublicInvestmentBudgetCoFundedExpenses(e);
            }

            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Bulk insert completed successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback();
                LOGGER.severe("Transaction rolled back due to error: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                LOGGER.severe("Error during rollback: " + rollbackEx.getMessage());
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