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
            String regularRevenues = basePath + "/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv";
            if (new File(regularRevenues).exists()) {
                DataInput.advancedCSVReader(regularRevenues);
            }

            // Load Public Investment Revenues
            String publicRevenues = basePath + "/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΕΣΟΔΑ.csv";
            if (new File(publicRevenues).exists()) {
                DataInput.advancedCSVReader(publicRevenues);
            }

            // Load Regular Budget Expenses
            String regularExpenses = basePath
                + "/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
            if (new File(regularExpenses).exists()) {
                DataInput.advancedCSVReader(regularExpenses);
            }

            // Load Public Investment Expenses
            String publicExpenses = basePath
                + "/ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΔΗΜΟΣΙΩΝ_ΕΠΕΝΔΥΣΕΩΝ_ΠΙΣΤΩΣΕΙΣ_ΚΑΤΑ_ΕΙΔΙΚΟ_ΦΟΡΕΑ_ΚΑΙ_ΜΕΙΖΟΝΑ_ΚΑΤΗΓΟΡΙΑ_ΔΑΠΑΝΗΣ.csv";
            if (new File(publicExpenses).exists()) {
                DataInput.advancedCSVReader(publicExpenses);
            }

            // Create entities and process data
            DataInput.createEntityFromCSV();
            PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
            PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
            BudgetRevenue.sortBudgetRevenuesByCode();
            BudgetRevenue.filterBudgetRevenues();
            DataInput.mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues(2025);

            // Load historical data
            String historical = basePath + "/ΚΡΑΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ_ΠΕΝΤΑΕΤΙΑΣ.csv";
            if (new File(historical).exists()) {
                DataInput.advancedCSVReader(historical);
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
            File testFile = new File(path + "/ΤΑΚΤΙΚΟΣ_ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ_ΕΣΟΔΑ.csv");
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
