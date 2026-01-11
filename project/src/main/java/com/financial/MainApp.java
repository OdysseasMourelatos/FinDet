package com.financial;

import atlantafx.base.theme.PrimerDark;
import com.financial.database.SQLiteManager;
import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.services.data.DataInput;
import com.financial.ui.MainWindow;
import java.io.File;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application entry point.
 */
public class MainApp extends Application {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        // Load CSV data on startup
        loadBudgetData();

        // Apply modern dark theme
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // Create main window
        MainWindow mainWindow = new MainWindow();

        // Setup scene
        Scene scene = new Scene(mainWindow.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        // Configure stage
        primaryStage.setTitle("FinDet - Διαχείριση Κρατικού Προϋπολογισμού");
        primaryStage.setScene(scene);

        primaryStage.setResizable(true);

        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    private void loadBudgetData() {
        SQLiteManager dbManager = SQLiteManager.getInstance();
        String basePath = findProjectBasePath();
        if (basePath == null) {
            System.out.println("Warning: Could not find CSV files directory");
            return;
        }

        try {
            // --- 1. ΦΟΡΤΩΣΗ ΚΥΡΙΩΝ ΔΕΔΟΜΕΝΩΝ ---
            if (dbManager.isDatabasePopulated()) {
                System.out.println("Loading data from SQLite database...");
                dbManager.loadAllRevenuesFromDb();
                dbManager.loadAllExpensesFromDb();
            } else {
                System.out.println("Database empty. Loading from CSV files...");
                loadPrimaryCSVFiles(basePath);

                // Αποθήκευση στη βάση για πρώτη φορά
                DataInput.createEntityFromCSV();
                dbManager.insertIntoTables();
            }

            // --- 2. ΕΠΕΞΕΡΓΑΣΙΑ (Sorting/Filtering) ---
            processInitialData();

            // --- 3. ΙΣΤΟΡΙΚΑ ΔΕΔΟΜΕΝΑ ---
            loadHistoricalData(basePath);

            System.out.println("Budget data loaded successfully!");

        } catch (SQLException se) {
            System.err.println("Database Error: " + se.getMessage());
        } catch (Exception e) {
            System.out.println("Error loading budget data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPrimaryCSVFiles(String basePath) throws Exception {
        String regularRevenues = basePath + "/data/input/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv";
        if (new File(regularRevenues).exists()) {
            DataInput.advancedCSVReader(regularRevenues, null);
        }

        String publicRevenues = basePath + "/data/input/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΕΣΟΔΑ.csv";
        if (new File(publicRevenues).exists()) {
            DataInput.advancedCSVReader(publicRevenues, null);
        }

        String regularExpenses = basePath + "/data/input/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
        if (new File(regularExpenses).exists()) {
            DataInput.advancedCSVReader(regularExpenses, null);
        }

        String publicExpenses = basePath + "/data/input/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
        if (new File(publicExpenses).exists()) {
            DataInput.advancedCSVReader(publicExpenses, null);
        }
    }

    private void processInitialData() {
        PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
        PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
        BudgetRevenue.sortBudgetRevenuesByCode();
        BudgetRevenue.filterBudgetRevenues();
    }

    private void loadHistoricalData(String basePath) throws Exception {
        String historicalRevenue = basePath + "/data/input/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ.csv";
        if (new File(historicalRevenue).exists()) {
            DataInput.mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues(2025);
            DataInput.advancedCSVReader(historicalRevenue, "HISTORICAL_BUDGET_REVENUES");
        }

        String historicalExpenses = basePath + "/data/input/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΞΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ_ΚΑΤΑ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
        if (new File(historicalExpenses).exists()) {
            DataInput.mergeBudgetExpensesOfBaseYearWithMultiYearBudgetExpenses(2025);
            DataInput.advancedCSVReader(historicalExpenses, "HISTORICAL_BUDGET_EXPENSES");
        }

        String historicalExpensesPerEntity = basePath + "/data/input/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΞΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ_ΚΑΤΑ_ΦΟΡΕΑ.csv";
        if (new File(historicalExpensesPerEntity).exists()) {
            DataInput.mergeBudgetExpensesPerEntityOfBaseYearWithMultiYearBudgetExpensesPerEntity(2025);
            DataInput.advancedCSVReader(historicalExpensesPerEntity, null);
            DataInput.createMultiYearEntityFromCSV();
        }
    }

    private String findProjectBasePath() {
        // Try current directory first
        String[] possiblePaths = {
            ".",
            "./project",
            "../project",
            System.getProperty("user.dir"),
            System.getProperty("user.dir") + "/project"
        };

        for (String path : possiblePaths) {
            File testFile = new File(path + "/data/input/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv");
            if (testFile.exists()) {
                return path;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}


