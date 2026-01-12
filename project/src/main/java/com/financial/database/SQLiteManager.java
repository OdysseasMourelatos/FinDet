package com.financial.database;

import com.financial.entries.*;

import java.sql.*;
import java.util.Set;
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

    public void loadAllRevenuesFromDb() {
        try (Statement stmt = connection.createStatement()) {
            // 1. Regular Revenues
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Regular_Budget_Revenues");
            while (rs1.next()) {
                new RegularBudgetRevenue(
                        rs1.getString("code"),
                        rs1.getString("description"),
                        rs1.getString("category"),
                        rs1.getLong("amount")
                );
            }

            // 2. PIB National Revenues
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM PIB_National_Revenues");
            while (rs2.next()) {
                new PublicInvestmentBudgetNationalRevenue(
                        rs2.getString("code"),
                        rs2.getString("description"),
                        rs2.getString("category"),
                        rs2.getString("type"),
                        rs2.getLong("amount")

                );
            }

            // 3. PIB CoFunded Revenues
            ResultSet rs3 = stmt.executeQuery("SELECT * FROM PIB_CoFunded_Revenues");
            while (rs3.next()) {
                new PublicInvestmentBudgetCoFundedRevenue(
                        rs3.getString("code"),
                        rs3.getString("description"),
                        rs3.getString("category"),
                        rs3.getString("type"),
                        rs3.getLong("amount")
                );
            }

        } catch (SQLException e) {
            LOGGER.severe("Error loading revenues from DB: " + e.getMessage());
        }
    }

    public void loadAllExpensesFromDb() {
        try (Statement stmt = connection.createStatement()) {
            // 1. Regular Expenses
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Regular_Budget_Expenses");
            while (rs1.next()) {
                new RegularBudgetExpense(
                        rs1.getString("entity_code"),
                        rs1.getString("entity_name"),
                        rs1.getString("service_code"),
                        rs1.getString("service_name"),
                        rs1.getString("expense_code"),
                        rs1.getString("description"),
                        rs1.getString("category"),
                        rs1.getLong("amount")
                );
            }

            // 2. PIB National Expenses
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM PIB_National_Expenses");
            while (rs2.next()) {
                new PublicInvestmentBudgetNationalExpense(
                        rs2.getString("entity_code"),
                        rs2.getString("entity_name"),
                        rs2.getString("service_code"),
                        rs2.getString("service_name"),
                        rs2.getString("expense_code"),
                        rs2.getString("description"),
                        rs2.getString("type"),
                        rs2.getString("category"),
                        rs2.getLong("amount")
                );
            }

            // 3. PIB CoFunded Expenses
            ResultSet rs3 = stmt.executeQuery("SELECT * FROM PIB_CoFunded_Expenses");
            while (rs3.next()) {
                new PublicInvestmentBudgetCoFundedExpense(
                        rs3.getString("entity_code"),
                        rs3.getString("entity_name"),
                        rs3.getString("service_code"),
                        rs3.getString("service_name"),
                        rs3.getString("expense_code"),
                        rs3.getString("description"),
                        rs3.getString("type"),
                        rs3.getString("category"),
                        rs3.getLong("amount")
                );
            }
            //BONUS: Entities
            ResultSet rs4 = stmt.executeQuery("SELECT * FROM Entities");
            while (rs4.next()) {
                new Entity(
                        rs4.getString("entity_code"),
                        rs4.getString("entity_name")
                );
            }
        } catch (SQLException e) {
            LOGGER.severe("Error loading expenses from DB: " + e.getMessage());
        }
    }

    public void updateRegularBudgetRevenues(RegularBudgetRevenue rbr) {
        String update = """ 
            Update Regular_Budget_Revenues
            Set amount = ?
            Where code = ?
            """;

        //Protection from SQL injection
        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, rbr.getAmount());
            ps.setString(2, rbr.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePublicInvestmentBudgetNationalRevenues(PublicInvestmentBudgetNationalRevenue pinbr) {
        String update = """ 
            Update PIB_National_Revenues
            Set amount = ?
            Where code = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, pinbr.getAmount());
            ps.setString(2, pinbr.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePublicInvestmentBudgetCoFundedRevenues(PublicInvestmentBudgetCoFundedRevenue pibcfr) {
        String update = """ 
            Update PIB_CoFunded_Revenue
            Set amount = ?
            Where code = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, pibcfr.getAmount());
            ps.setString(2, pibcfr.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateRegularBudgetExpenses(RegularBudgetExpense rbe) {
        String update = """ 
            Update Regular_Budget_Expenses
            Set amount = ?
            Where entity_code = ? and service_code = ? and expense_code = ?
            """;
        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, rbe.getAmount());
            ps.setString(2, rbe.getEntityCode());
            ps.setString(3, rbe.getServiceCode());
            ps.setString(4, rbe.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void updatePublicInvestmentBudgetNationalExpenses(PublicInvestmentBudgetNationalExpense pibne) {
        String update = """ 
            Update PIB_National_Expenses
            Set amount = ?
            Where entity_code = ? and service_code = ? and expense_code = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, pibne.getAmount());
            ps.setString(2, pibne.getEntityCode());
            ps.setString(3, pibne.getServiceCode());
            ps.setString(4, pibne.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePublicInvestmentBudgetCoFundedExpenses(PublicInvestmentBudgetCoFundedExpense pibcfe) {
        String update = """ 
            Update PIB_CoFunded_Expenses
            Set amount = ?
            Where entity_code = ? and service_code = ? and expense_code = ?
            """;

        try (PreparedStatement ps = connection.prepareStatement(update)) {
            ps.setLong(1, pibcfe.getAmount());
            ps.setString(2, pibcfe.getEntityCode());
            ps.setString(3, pibcfe.getServiceCode());
            ps.setString(4, pibcfe.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateTables(BudgetEntry be) {
        switch (be) {
            case RegularBudgetRevenue rbr -> updateRegularBudgetRevenues(rbr);
            case PublicInvestmentBudgetNationalRevenue pibnr -> updatePublicInvestmentBudgetNationalRevenues(pibnr);
            case PublicInvestmentBudgetCoFundedRevenue picfr -> updatePublicInvestmentBudgetCoFundedRevenues(picfr);
            case RegularBudgetExpense rbe -> updateRegularBudgetExpenses(rbe);
            case PublicInvestmentBudgetNationalExpense pibne -> updatePublicInvestmentBudgetNationalExpenses(pibne);
            case PublicInvestmentBudgetCoFundedExpense pibcfe -> updatePublicInvestmentBudgetCoFundedExpenses(pibcfe);
            default -> throw new IllegalArgumentException("Unknown budget type: " + be);
        }
    }

    public void saveChangesBatch(Set<BudgetEntry> changes) {
        if (changes == null || changes.isEmpty()) {
            return;
        }

        try {
            connection.setAutoCommit(false);

            for (BudgetEntry entry : changes) {
                updateTables(entry);
            }

            connection.commit();
            LOGGER.info("Batch update successful: " + changes.size() + " entries.");

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                LOGGER.severe("Batch update failed, transaction rolled back: " + e.getMessage());
            } catch (SQLException ex) {
                LOGGER.severe("Rollback failed: " + ex.getMessage());
            }
            throw new RuntimeException("Database error during batch save", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                LOGGER.severe("Could not reset autoCommit: " + e.getMessage());
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

    public boolean isDatabasePopulated() {
        String query = "SELECT COUNT(*) FROM Regular_Budget_Revenues";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public void resetDatabase() {
        String[] tables = { "Regular_Budget_Revenues", "PIB_National_Revenues", "PIB_CoFunded_Revenues", "Regular_Budget_Expenses", "PIB_National_Expenses", "PIB_CoFunded_Expenses", "Entities"};

        try (Statement stmt = connection.createStatement()) {
            connection.setAutoCommit(false);
            for (String table : tables) {
                stmt.execute("DELETE FROM " + table);
            }
            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Database wiped for fresh CSV import.");
        } catch (SQLException e) {
            LOGGER.severe("Error resetting database: " + e.getMessage());
        }
    }
}