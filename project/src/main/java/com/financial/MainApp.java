package com.financial;

import atlantafx.base.theme.PrimerDark;
import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.services.data.DataInput;
import com.financial.ui.MainWindow;
import java.io.File;
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
        String basePath = findProjectBasePath();
        if (basePath == null) {
            System.out.println("Warning: Could not find CSV files directory");
            return;
        }

        try {
            // Load Regular Budget Revenues
            String regularRevenues = basePath + "/data/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv";
            if (new File(regularRevenues).exists()) {
                DataInput.advancedCSVReader(regularRevenues, null);
            }

            // Load Public Investment Revenues
            String publicRevenues = basePath + "/data/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΕΣΟΔΑ.csv";
            if (new File(publicRevenues).exists()) {
                DataInput.advancedCSVReader(publicRevenues, null);
            }

            // Load Regular Budget Expenses
            String regularExpenses = basePath
                + "/data/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
            if (new File(regularExpenses).exists()) {
                DataInput.advancedCSVReader(regularExpenses, null);
            }

            // Load Public Investment Expenses
            String publicExpenses = basePath
                + "/data/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
            if (new File(publicExpenses).exists()) {
                DataInput.advancedCSVReader(publicExpenses, null);
            }

            // Create entities and process data
            DataInput.createEntityFromCSV();
            PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
            PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
            BudgetRevenue.sortBudgetRevenuesByCode();
            BudgetRevenue.filterBudgetRevenues();

            // Load historical data
            String historicalRevenue = basePath + "/data/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ.csv";
            if (new File(historicalRevenue).exists()) {
                DataInput.mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues(2025);
                DataInput.advancedCSVReader(historicalRevenue, "HISTORICAL_BUDGET_REVENUES");
            }

            String historicalExpenses = basePath + "/data/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΞΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ_ΚΑΤΑ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
            if (new File(historicalExpenses).exists()) {
                DataInput.mergeBudgetExpensesOfBaseYearWithMultiYearBudgetExpenses(2025);
                DataInput.advancedCSVReader(historicalExpenses, "HISTORICAL_BUDGET_EXPENSES");
            }

            String historicalExpensesPerEntity = basePath + "/data/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΞΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ_ΚΑΤΑ_ΦΟΡΕΑ.csv";
            if (new File(historicalExpensesPerEntity).exists()) {
                DataInput.createMultiYearEntityFromCSV();
                DataInput.mergeBudgetExpensesPerEntityOfBaseYearWithMultiYearBudgetExpensesPerEntity(2025);
                DataInput.advancedCSVReader(historicalExpensesPerEntity, null);
            }

            System.out.println("Budget data loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error loading budget data: " + e.getMessage());
            e.printStackTrace();
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
            File testFile = new File(path + "/data/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv");
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
