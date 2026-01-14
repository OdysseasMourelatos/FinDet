package com.financial;

import atlantafx.base.theme.PrimerDark;
import com.financial.data.SQLiteManager;
import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.data.DataInput;
import com.financial.ui.MainWindow;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import java.util.Optional;
import java.util.Set;
import com.financial.entries.BudgetEntry;

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

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        boolean loadFromCsv = showStartupDialog();

        loadBudgetData(loadFromCsv);

        MainWindow mainWindow = new MainWindow();
        Scene scene = new Scene(mainWindow.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("FinDet - Διαχείριση Κρατικού Προϋπολογισμού");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {

            Set<BudgetEntry> changes = mainWindow.getBudgetChangesView().getPendingChanges();

            if (changes != null && !changes.isEmpty()) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Μη αποθηκευμένες αλλαγές");
                alert.setHeaderText("Προσοχή! Εκκρεμούν " + changes.size() + " αλλαγές στη βάση.");
                alert.setContentText("Θέλετε να τις αποθηκεύσετε πριν την έξοδο;");

                ButtonType buttonSave = new ButtonType("Αποθήκευση");
                ButtonType buttonDiscard = new ButtonType("Απόρριψη");
                ButtonType buttonCancel = new ButtonType("Άκυρο", ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonSave, buttonDiscard, buttonCancel);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent()) {
                    if (result.get() == buttonSave) {
                        SQLiteManager.getInstance().saveChangesBatch(changes);
                    } else if (result.get() == buttonCancel) {
                        event.consume();
                    }
                }
            }
        });

        primaryStage.show();
    }

    private boolean showStartupDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Εκκίνηση Εφαρμογής");
        alert.setHeaderText("Επιλογή Πηγής Δεδομένων");
        alert.setContentText("Θέλετε να ξεκινήσετε με τα δεδομένα από τη Βάση (τελευταία κατάσταση) ή να κάνετε νέα εισαγωγή από τα CSV (αρχική κατάσταση);");

        ButtonType btnDb = new ButtonType("Από Βάση (SQLite)");
        ButtonType btnCsv = new ButtonType("Από Αρχή (CSV)");

        alert.getButtonTypes().setAll(btnDb, btnCsv);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == btnCsv;
    }

    private void loadBudgetData(boolean forceCsv) {
        SQLiteManager dbManager = SQLiteManager.getInstance();
        String basePath = findProjectBasePath();

        try {
            if (forceCsv) {
                System.out.println("Επιλέχθηκε αρχικοποίηση από CSV...");
                dbManager.resetDatabase();
                loadPrimaryCSVFiles(basePath);
                DataInput.createEntityFromCSV();
                dbManager.insertIntoTables();
            } else {
                if (dbManager.isDatabasePopulated()) {
                    System.out.println("Φόρτωση από τη βάση δεδομένων...");
                    dbManager.loadAllRevenuesFromDb();
                    dbManager.loadAllExpensesFromDb();
                } else {
                    loadPrimaryCSVFiles(basePath);
                    DataInput.createEntityFromCSV();
                    dbManager.insertIntoTables();
                }
            }
            processInitialData();
            loadHistoricalData(basePath);
        } catch (Exception e) {
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
        String[] possiblePaths = {".", "./project", "../project",
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