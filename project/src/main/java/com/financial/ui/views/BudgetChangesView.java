package com.financial.ui.views;

import com.financial.database.SQLiteManager;
import com.financial.entries.*;
import com.financial.services.BudgetType;
import com.financial.services.expenses.ExpensesHistory;
import com.financial.services.revenues.RevenuesHistory;
import com.financial.ui.Theme;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;

import java.util.*;

/**
 * Budget Changes view - allows users to make changes to revenue and expense accounts
 * with gamified feedback showing before/after values.
 */
public class BudgetChangesView {

    private final ScrollPane scrollPane;
    private final VBox view;
    private TabPane tabPane;

    // Revenue form components
    private ComboBox<String> budgetTypeCombo;
    private TextField accountCodeField;
    private TextField changeValueField;
    private ComboBox<String> changeTypeCombo;
    private ComboBox<String> distributionCombo;
    private Button executeButton;
    private Label statusLabel;
    private ComboBox<String> revenueViewScopeCombo;

    // Revenue results area
    private VBox resultsContainer;
    private TableView<ChangeResult> resultsTable;
    private ObservableList<ChangeResult> resultsData;
    private Map<String, Long> lastBeforeValues;
    private String lastSelectedCode;

    // Expense form components
    private ComboBox<String> expenseBudgetTypeCombo;
    private ComboBox<String> expenseScopeCombo;
    private ComboBox<String> expenseEntityCombo;
    private ComboBox<String> expenseServiceCombo;
    private ComboBox<String> expenseCategoryCombo;
    private TextField expenseChangeValueField;
    private ComboBox<String> expenseChangeTypeCombo;
    private Button expenseExecuteButton;
    private Label expenseStatusLabel;
    private boolean isUpdatingScope = false;
    private ComboBox<String> expenseCalculationModeCombo;
    private Map<String, Long> lastExpenseBeforeValues;

    // Expense results area
    private VBox expenseResultsContainer;
    private TableView<ExpenseChangeResult> expenseResultsTable;
    private ObservableList<ExpenseChangeResult> expenseResultsData;
    private ComboBox<String> expenseDisplayModeCombo;
    private ComboBox<String> expenseViewLevelCombo;

    //Saving Changes
    private final Set<BudgetEntry> pendingChanges = new HashSet<>();
    private Button saveToDbButton;
    private Label statsLabel;

    private Button globalUndoButton;
    private Spinner<Integer> undoStepsSpinner;
    private Label globalStatusLabel;

    public BudgetChangesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Create TabPane for Revenues and Expenses
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Revenue Tab
        Tab revenueTab = new Tab("Έσοδα");
        VBox revenueContent = createRevenueTabContent();
        revenueTab.setContent(revenueContent);

        // Expense Tab
        Tab expenseTab = new Tab("Έξοδα");
        VBox expenseContent = createExpenseTabContent();
        expenseTab.setContent(expenseContent);

        tabPane.getTabs().addAll(revenueTab, expenseTab);

        view.getChildren().addAll(header, tabPane);

        // Wrap in scroll pane
        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
                "-fx-background: " + Theme.BG_BASE + ";" +
                        "-fx-background-color: " + Theme.BG_BASE + ";" +
                        "-fx-border-color: transparent;"
        );
    }

    private VBox createRevenueTabContent() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Form
        VBox formSection = createFormSection();

        // Results
        resultsContainer = createResultsSection();
        VBox.setVgrow(resultsContainer, Priority.ALWAYS);

        content.getChildren().addAll(formSection, resultsContainer);
        return content;
    }

    private VBox createHeader() {
        VBox header = new VBox(6);
        header.setPadding(new Insets(32, 24, 24, 24));

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.WARNING, 0.15));

        Label iconText = new Label("~");
        iconText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + Theme.WARNING_LIGHT + ";"
        );

        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Αλλαγές Προϋπολογισμού");
        title.setStyle(Theme.pageTitle());

        globalStatusLabel = new Label("");
        globalStatusLabel.setPadding(new Insets(5, 0, 0, 0));
        globalStatusLabel.setVisible(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // --- Stats Section ---
        statsLabel = new Label("Εκκρεμούν: 0 εγγραφές | 0 αλλαγές");
        statsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_MUTED + "; -fx-padding: 0 15 0 0;");

        saveToDbButton = new Button("Αποθήκευση Αλλαγών");
        saveToDbButton.setStyle(Theme.buttonPrimary() + "-fx-background-color: " + Theme.SUCCESS + ";");
        saveToDbButton.setDisable(true);
        saveToDbButton.setOnAction(e -> saveAllToDatabase());

        undoStepsSpinner = new Spinner<>(1, 100, 1);
        undoStepsSpinner.setEditable(true);
        undoStepsSpinner.setPrefWidth(80);
        undoStepsSpinner.getEditor().focusedProperty().addListener((s, ov, nv) -> {
            if (!nv) {
                undoStepsSpinner.increment(0);
            }
        });

        undoStepsSpinner.getEditor().setOnAction(e -> {
            try {
                undoStepsSpinner.increment(0); // commit edit
            } catch (Exception ex) { /* ignore */ }
        });

        globalUndoButton = new Button("Ακύρωση");
        globalUndoButton.setStyle("-fx-background-color: " + Theme.ERROR + "; -fx-text-fill: white; -fx-font-weight: bold;");
        globalUndoButton.setDisable(true);
        globalUndoButton.setOnAction(e -> handleGlobalUndo());

        // Grouping: Stats | Save | Steps | Undo
        HBox buttonGroup = new HBox(12, statsLabel, saveToDbButton, new Label("Βήματα:"), undoStepsSpinner, globalUndoButton);
        buttonGroup.setAlignment(Pos.CENTER_RIGHT);

        titleRow.getChildren().addAll(iconContainer, title, spacer, buttonGroup);

        Label subtitle = new Label("Εφαρμογή αλλαγών σε λογαριασμούς με αυτόματη ενημέρωση ιεραρχίας");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);

        header.getChildren().add(globalStatusLabel);
        return header;
    }

    private VBox createFormSection() {
        // Διάταξη από το 2ο snippet (πιο συμπαγές spacing)
        VBox section = new VBox(10);
        section.setPadding(new Insets(15, 24, 10, 24));

        VBox formCard = new VBox(12);
        formCard.setPadding(new Insets(15));
        formCard.setStyle(Theme.card());

        // Τίτλος
        Label formTitle = new Label("Παράμετροι Αλλαγής Εσόδων");
        formTitle.setStyle(Theme.sectionHeader());

        // Η κύρια σειρά των φίλτρων (One-row layout)
        HBox filtersRow = new HBox(12);
        filtersRow.setAlignment(Pos.BOTTOM_LEFT);

        // 1. Τύπος Προϋπολογισμού (Label από 1ο snippet)
        VBox budgetTypeBox = createFormField("Τύπος Προϋπολογισμού");
        budgetTypeCombo = new ComboBox<>();
        budgetTypeCombo.getItems().addAll(
                "Τακτικός Προϋπολογισμός",
                "ΠΔΕ Εθνικό",
                "ΠΔΕ Συγχρηματοδοτούμενο"
        );
        budgetTypeCombo.setValue("Τακτικός Προϋπολογισμός");
        budgetTypeCombo.setPrefWidth(200);
        budgetTypeCombo.setStyle(Theme.comboBox());
        budgetTypeBox.getChildren().add(budgetTypeCombo);

        // 2. Κωδικός Λογαριασμού (Label & Prompt από 1ο snippet)
        VBox codeBox = createFormField("Κωδικός Λογαριασμού");
        accountCodeField = new TextField();
        accountCodeField.setPromptText("π.χ. 11, 111, 11101");
        accountCodeField.setPrefWidth(160);
        accountCodeField.setStyle(Theme.textField());
        accountCodeField.focusedProperty().addListener((obs, old, nv) ->
                accountCodeField.setStyle(nv ? Theme.textFieldFocused() : Theme.textField()));
        codeBox.getChildren().add(accountCodeField);

        // 3. Τιμή Αλλαγής (Label & Prompt από 1ο snippet)
        VBox changeBox = createFormField("Τιμή Αλλαγής");
        changeValueField = new TextField();
        changeValueField.setPromptText("π.χ. 10000, 10%");
        changeValueField.setPrefWidth(140);
        changeValueField.setStyle(Theme.textField());
        changeValueField.focusedProperty().addListener((obs, old, nv) ->
                changeValueField.setStyle(nv ? Theme.textFieldFocused() : Theme.textField()));
        changeBox.getChildren().add(changeValueField);

        // 4. Τύπος Αλλαγής (Label από 1ο snippet)
        VBox changeTypeBox = createFormField("Τύπος Αλλαγής");
        changeTypeCombo = new ComboBox<>();
        changeTypeCombo.getItems().addAll(
                "Μεταβολή (+/-)",
                "Ποσοστό (%)",
                "Τελικό Υπόλοιπο"
        );
        changeTypeCombo.setValue("Μεταβολή (+/-)");
        changeTypeCombo.setPrefWidth(150);
        changeTypeCombo.setStyle(Theme.comboBox());
        changeTypeBox.getChildren().add(changeTypeCombo);

        // 5. Κατανομή (Label από 1ο snippet)
        VBox distBox = createFormField("Κατανομή σε Υποκατηγορίες");
        distributionCombo = new ComboBox<>();
        distributionCombo.getItems().addAll("Ισόποσα", "Ποσοστιαία");
        distributionCombo.setValue("Ποσοστιαία");
        distributionCombo.setPrefWidth(160);
        distributionCombo.setStyle(Theme.comboBox());
        distBox.getChildren().add(distributionCombo);

        // 6. Το Κουμπί (Κείμενο από 1ο snippet)
        executeButton = new Button("Εκτέλεση Αλλαγής");
        executeButton.setStyle(Theme.buttonPrimary());
        executeButton.setPrefHeight(35);
        executeButton.setOnAction(e -> executeChange());
        executeButton.setOnMouseEntered(e -> executeButton.setStyle(Theme.buttonPrimaryHover()));
        executeButton.setOnMouseExited(e -> executeButton.setStyle(Theme.buttonPrimary()));

        // Προσθήκη όλων στη σειρά
        filtersRow.getChildren().addAll(
                budgetTypeBox,
                codeBox,
                changeBox,
                changeTypeBox,
                distBox,
                executeButton
        );

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");

        formCard.getChildren().addAll(formTitle, filtersRow, statusLabel);
        section.getChildren().add(formCard);

        return section;
    }

    private VBox createFormField(String labelText) {
        VBox field = new VBox(6);
        Label label = new Label(labelText);
        label.setStyle(Theme.mutedText());
        field.getChildren().add(label);
        return field;
    }

    private VBox createResultsSection() {
        VBox section = new VBox(0);
        section.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(section, Priority.ALWAYS);

        VBox resultsCard = new VBox(16);
        resultsCard.setPadding(new Insets(20));
        resultsCard.setStyle(Theme.card());
        VBox.setVgrow(resultsCard, Priority.ALWAYS);

        HBox resultsHeader = new HBox(12);
        resultsHeader.setAlignment(Pos.CENTER_LEFT);

        Label resultsTitle = new Label("Αποτελέσματα Αλλαγών Εσόδων");
        resultsTitle.setStyle(Theme.sectionHeader());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label scopeLabel = new Label("Προβολή:");
        scopeLabel.setStyle(Theme.mutedText());

        revenueViewScopeCombo = new ComboBox<>();
        revenueViewScopeCombo.setStyle(Theme.comboBox());
        revenueViewScopeCombo.setPrefWidth(180);
        revenueViewScopeCombo.setOnAction(this::handleScopeChange);


        resultsHeader.getChildren().addAll(resultsTitle, spacer, scopeLabel, revenueViewScopeCombo);

        resultsData = FXCollections.observableArrayList();
        resultsTable = createResultsTable();
        VBox.setVgrow(resultsTable, Priority.ALWAYS);

        resultsCard.getChildren().addAll(resultsHeader, resultsTable);
        section.getChildren().add(resultsCard);
        return section;
    }

    private void handleScopeChange(javafx.event.ActionEvent event) {
        if (!isUpdatingScope) {
            refreshRevenueResults();
        }
    }

    private TableView<ChangeResult> createResultsTable() {
        TableView<ChangeResult> table = new TableView<>(resultsData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label placeholder = new Label("Εκτελέστε μια αλλαγή για να δείτε αποτελέσματα");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        table.setPlaceholder(placeholder);

        TableColumn<ChangeResult, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().code));
        codeCol.setPrefWidth(90);

        TableColumn<ChangeResult, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().description));
        descCol.setPrefWidth(220);

        TableColumn<ChangeResult, String> levelCol = new TableColumn<>("Επ.");
        levelCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().level));
        levelCol.setPrefWidth(40);
        levelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<ChangeResult, String> beforeCol = new TableColumn<>("Πριν");
        beforeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().before));
        beforeCol.setPrefWidth(100);
        beforeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> afterCol = new TableColumn<>("Μετά");
        afterCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().after));
        afterCol.setPrefWidth(100);
        afterCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> changeCol = new TableColumn<>("Μεταβολή");
        changeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().change));
        changeCol.setPrefWidth(130);
        changeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> roleCol = new TableColumn<>("Ρόλος");
        roleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().role));
        roleCol.setPrefWidth(110);

        codeCol.setMaxWidth(1f * Integer.MAX_VALUE * 7);
        descCol.setMaxWidth(1f * Integer.MAX_VALUE * 38);
        levelCol.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        beforeCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        afterCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        changeCol.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        roleCol.setMaxWidth(1f * Integer.MAX_VALUE * 10);

        table.getColumns().add(codeCol);
        table.getColumns().add(descCol);
        table.getColumns().add(levelCol);
        table.getColumns().add(beforeCol);
        table.getColumns().add(afterCol);
        table.getColumns().add(changeCol);
        table.getColumns().add(roleCol);

        return table;
    }

    private void executeChange() {
        String code = accountCodeField.getText().trim();
        String changeValue = changeValueField.getText().trim();
        String budgetType = budgetTypeCombo.getValue();
        String changeType = changeTypeCombo.getValue();
        String distribution = distributionCombo.getValue();

        if (code.isEmpty()) {
            showRevenueError("Παρακαλώ εισάγετε κωδικό λογαριασμού");
            return;
        }
        if (changeValue.isEmpty()) {
            showRevenueError("Παρακαλώ εισάγετε τιμή αλλαγής");
            return;
        }

        BudgetRevenue targetRevenue = findRevenue(code, budgetType);
        if (targetRevenue == null) {
            showRevenueError("Δεν βρέθηκε λογαριασμός με κωδικό: " + code);
            return;
        }

        try {
            lastSelectedCode = code;

            lastBeforeValues = new HashMap<>();
            lastBeforeValues.putAll(captureAllLevels(code));

            applyChange(targetRevenue, changeValue, changeType, distribution, budgetType);

            updateRevenueViewScopeOptions(budgetType);
            refreshRevenueResults();

            showRevenueSuccess("Η αλλαγή εφαρμόστηκε επιτυχώς!");

            pendingChanges.add(targetRevenue);
            pendingChanges.addAll(targetRevenue.getAllSuperCategories());
            pendingChanges.addAll(targetRevenue.getAllSubCategories());
            updateSaveButtonState();
        } catch (NumberFormatException e) {
            showRevenueError("Μη έγκυρη τιμή. Χρησιμοποιήστε αριθμό ή ποσοστό (π.χ. 10000 ή 10%)");
        } catch (IllegalArgumentException e) {
            showRevenueError("Η αλλαγή απέτυχε, καθώς ένας ή παραπάνω λογαριασμοί θα είχαν αρνητικό υπόλοιπο");
        } catch (Exception e) {
            showRevenueError("Σφάλμα: " + e.getMessage());
        }
    }

    private Map<String, Long> captureAllLevels(String code) {
        Map<String, Long> allValues = new HashMap<>();

        captureScopeToMap(allValues, "Τακτικός Προϋπολογισμός", RegularBudgetRevenue.findRegularBudgetRevenueWithCode(code));
        captureScopeToMap(allValues, "ΠΔΕ Εθνικό", PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(code));
        captureScopeToMap(allValues, "ΠΔΕ Συγχρηματοδοτούμενο", PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(code));
        captureScopeToMap(allValues, "ΠΔΕ (Σύνολο)", PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(code));
        captureScopeToMap(allValues, "Κρατικός Προϋπολογισμός", BudgetRevenue.findBudgetRevenueWithCode(code));

        return allValues;
    }

    private void captureScopeToMap(Map<String, Long> map, String scope, BudgetRevenue revenue) {
        if (revenue == null) {
            return;
        }

        map.put(scope + "|" + revenue.getCode(), revenue.getAmount());

        for (BudgetRevenue sup : revenue.getAllSuperCategories()) {
            map.put(scope + "|" + sup.getCode(), sup.getAmount());
        }
        for (BudgetRevenue sub : revenue.getAllSubCategories()) {
            map.put(scope + "|" + sub.getCode(), sub.getAmount());
        }
    }

    private BudgetRevenue findRevenue(String code, String budgetType) {
        return switch (budgetType) {
            case "Τακτικός Προϋπολογισμός" -> RegularBudgetRevenue.findRegularBudgetRevenueWithCode(code);
            case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(code);
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(code);
            default -> null;
        };
    }

    private Map<String, Long> captureValues(BudgetRevenue revenue, String budgetType) {
        Map<String, Long> values = new HashMap<>();
        values.put(revenue.getCode(), revenue.getAmount());

        ArrayList<BudgetRevenue> superCats = revenue.getAllSuperCategories();
        if (superCats != null) {
            for (BudgetRevenue sup : superCats) {
                values.put(sup.getCode(), sup.getAmount());
            }
        }

        ArrayList<BudgetRevenue> subCats = revenue.getAllSubCategories();
        if (subCats != null) {
            for (BudgetRevenue sub : subCats) {
                values.put(sub.getCode(), sub.getAmount());
            }
        }

        return values;
    }

    private void applyChange(BudgetRevenue revenue, String changeValue, String changeType, String distribution, String budgetType) {
        double numericValue;
        boolean isPercentage = changeValue.contains("%");

        String cleanValue = changeValue.replaceAll("[^0-9.\\-]", "");
        numericValue = Double.parseDouble(cleanValue);

        long changeAmount;
        double percentage = 0;

        if (changeType.equals("Ποσοστό (%)") || isPercentage) {
            percentage = numericValue / 100.0;
            changeAmount = (long) (revenue.getAmount() * percentage);
        } else if (changeType.equals("Τελικό Υπόλοιπο")) {
            changeAmount = (long) numericValue - revenue.getAmount();
            if (revenue.getAmount() != 0) {
                percentage = (double) changeAmount / revenue.getAmount();
            }
        } else {
            changeAmount = (long) numericValue;
            if (revenue.getAmount() != 0) {
                percentage = (double) changeAmount / revenue.getAmount();
            }
        }

        if (distribution.equals("Ισόποσα")) {
            applyEqualDistribution(revenue, changeAmount, budgetType);
        } else {
            applyPercentageDistribution(revenue, percentage, budgetType);
        }
    }

    private void applyEqualDistribution(BudgetRevenue revenue, long changeAmount, String budgetType) {
        if (revenue instanceof RegularBudgetRevenue rbr) {
            rbr.implementChangesOfEqualDistribution(changeAmount);
        } else if (revenue instanceof PublicInvestmentBudgetNationalRevenue pibnr) {
            pibnr.implementChangesOfEqualDistribution(changeAmount);
        } else if (revenue instanceof PublicInvestmentBudgetCoFundedRevenue pibcr) {
            pibcr.implementChangesOfEqualDistribution(changeAmount);
        }
    }

    private void applyPercentageDistribution(BudgetRevenue revenue, double percentage, String budgetType) {
        if (revenue instanceof RegularBudgetRevenue rbr) {
            rbr.implementChangesOfPercentageAdjustment(percentage);
        } else if (revenue instanceof PublicInvestmentBudgetNationalRevenue pibnr) {
            pibnr.implementChangesOfPercentageAdjustment(percentage);
        } else if (revenue instanceof PublicInvestmentBudgetCoFundedRevenue pibcr) {
            pibcr.implementChangesOfPercentageAdjustment(percentage);
        }
    }

    private void displayResults(BudgetRevenue targetRevenue, Map<String, Long> before, Map<String, Long> after, String scope) {
        resultsData.clear();

        ArrayList<BudgetRevenue> superCats = targetRevenue.getAllSuperCategories();
        if (superCats != null) {
            for (int i = superCats.size() - 1; i >= 0; i--) {
                BudgetRevenue sup = superCats.get(i);
                addResultRow(sup, before, after, "Ανώτερη", scope);
            }
        }

        addResultRow(targetRevenue, before, after, "* Στόχος", scope);

        ArrayList<BudgetRevenue> subCats = targetRevenue.getAllSubCategories();
        if (subCats != null) {
            for (BudgetRevenue sub : subCats) {
                addResultRow(sub, before, after, "Υποκατηγορία", scope);
            }
        }
        animateResults();
    }

    private void updateRevenueViewScopeOptions(String budgetType) {
        isUpdatingScope = true;

        revenueViewScopeCombo.setOnAction(null);

        String currentSelection = revenueViewScopeCombo.getValue();
        revenueViewScopeCombo.getItems().clear();

        if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
            revenueViewScopeCombo.getItems().addAll("Τακτικός Προϋπολογισμός", "Κρατικός Προϋπολογισμός");
        } else {
            revenueViewScopeCombo.getItems().addAll(budgetType, "ΠΔΕ (Σύνολο)", "Κρατικός Προϋπολογισμός");
        }

        if (currentSelection != null && revenueViewScopeCombo.getItems().contains(currentSelection)) {
            revenueViewScopeCombo.setValue(currentSelection);
        } else {
            revenueViewScopeCombo.setValue(budgetType);
        }

        revenueViewScopeCombo.setOnAction(this::handleScopeChange);
        isUpdatingScope = false;
    }

    private void refreshRevenueResults() {
        if (isUpdatingScope) {
            return;
        }

        String viewScope = revenueViewScopeCombo.getValue();
        if (lastSelectedCode == null || viewScope == null) {
            return;
        }

        BudgetRevenue currentTarget = switch (viewScope) {
            case "Τακτικός Προϋπολογισμός" -> RegularBudgetRevenue.findRegularBudgetRevenueWithCode(lastSelectedCode);
            case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(lastSelectedCode);
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(lastSelectedCode);
            case "ΠΔΕ (Σύνολο)" -> PublicInvestmentBudgetRevenue.findPublicInvestmentBudgetRevenueWithCode(lastSelectedCode);
            case "Κρατικός Προϋπολογισμός" -> BudgetRevenue.findBudgetRevenueWithCode(lastSelectedCode);
            default -> null;
        };

        if (currentTarget != null) {
            Map<String, Long> currentAfterValues = captureValues(currentTarget, viewScope);
            displayResults(currentTarget, lastBeforeValues, currentAfterValues, viewScope);
        }
    }

    private void addResultRow(BudgetRevenue revenue, Map<String, Long> before, Map<String, Long> after, String role, String scope) {
        String code = revenue.getCode();
        String key = scope + "|" + code;

        long beforeVal = before.getOrDefault(key, revenue.getAmount());
        long afterVal = revenue.getAmount();

        long change = afterVal - beforeVal;
        double percentChange = (beforeVal != 0) ? ((double) change / beforeVal) * 100 : 0;

        String changeStr = (change == 0) ? "0 (0.0%)" :
                String.format("%s%,d (%.1f%%)", (change > 0 ? "+" : ""), change, percentChange);

        resultsData.add(new ChangeResult(
                code,
                truncateDescription(revenue.getDescription(), 50),
                String.valueOf(revenue.getLevelOfHierarchy()),
                Theme.formatAmount(beforeVal),
                Theme.formatAmount(afterVal),
                changeStr,
                role
        ));
    }

    private String truncateDescription(String desc, int maxLength) {
        if (desc == null) {
            return "";
        }
        if (desc.length() <= maxLength) {
            return desc;
        }
        return desc.substring(0, maxLength - 3) + "...";
    }

    private void animateResults() {
        resultsTable.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), resultsTable);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), resultsTable);
        scale.setFromX(0.98);
        scale.setFromY(0.98);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition parallel = new ParallelTransition(fadeIn, scale);
        parallel.play();
    }

    private void showRevenueError(String message) {
        statusLabel.setText("! " + message);
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.ERROR_LIGHT + "; -fx-font-weight: 500;");
    }

    private void showRevenueSuccess(String message) {
        statusLabel.setText("+ " + message);
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.SUCCESS_LIGHT + "; -fx-font-weight: 500;");
    }

    public Region getView() {
        return scrollPane;
    }

    public static class ChangeResult {
        public final String code;
        public final String description;
        public final String level;
        public final String before;
        public final String after;
        public final String change;
        public final String role;

        public ChangeResult(String code, String description, String level, String before, String after, String change, String role) {
            this.code = code;
            this.description = description;
            this.level = level;
            this.before = before;
            this.after = after;
            this.change = change;
            this.role = role;
        }
    }

    // ==================== EXPENSE TAB METHODS ====================

    private VBox createExpenseTabContent() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Form
        VBox formSection = createExpenseFormSection();

        // Results
        expenseResultsContainer = createExpenseResultsSection();
        VBox.setVgrow(expenseResultsContainer, Priority.ALWAYS);

        content.getChildren().addAll(formSection, expenseResultsContainer);
        return content;
    }

    private VBox createExpenseFormSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15, 24, 10, 24));

        VBox formCard = new VBox(12);
        formCard.setPadding(new Insets(15));
        formCard.setStyle(Theme.card());

        Label formTitle = new Label("Παράμετροι Αλλαγής Δαπανών");
        formTitle.setStyle(Theme.sectionHeader());

        HBox row1 = new HBox(12);
        row1.setAlignment(Pos.BOTTOM_LEFT);

        VBox budgetTypeBox = createFormField("Τύπος Προϋπολογισμού");
        expenseBudgetTypeCombo = new ComboBox<>();
        expenseBudgetTypeCombo.getItems().addAll(
                "Τακτικός Προϋπολογισμός",
                "ΠΔΕ Εθνικό",
                "ΠΔΕ Συγχρηματοδοτούμενο"
        );
        expenseBudgetTypeCombo.setValue("Τακτικός Προϋπολογισμός");
        expenseBudgetTypeCombo.setPrefWidth(220); // Προσαρμοσμένο πλάτος για το κείμενο
        expenseBudgetTypeCombo.setStyle(Theme.comboBox());
        expenseBudgetTypeCombo.setOnAction(e -> updateExpenseEntityCombo());
        budgetTypeBox.getChildren().add(expenseBudgetTypeCombo);

        VBox scopeBox = createFormField("Εύρος Αλλαγής");
        expenseScopeCombo = new ComboBox<>();
        expenseScopeCombo.getItems().addAll(
                "Καθολική (Όλοι οι Φορείς)",
                "Ανά Φορέα",
                "Ανά Υπηρεσία"
        );
        expenseScopeCombo.setValue("Καθολική (Όλοι οι Φορείς)");
        expenseScopeCombo.setPrefWidth(200);
        expenseScopeCombo.setStyle(Theme.comboBox());
        expenseScopeCombo.setOnAction(e -> updateScopeVisibility());
        scopeBox.getChildren().add(expenseScopeCombo);

        VBox entityBox = createFormField("Φορέας");
        expenseEntityCombo = new ComboBox<>();
        expenseEntityCombo.setPrefWidth(250);
        expenseEntityCombo.setStyle(Theme.comboBox());
        expenseEntityCombo.setDisable(true);
        expenseEntityCombo.setOnAction(e -> updateExpenseServiceCombo());
        entityBox.getChildren().add(expenseEntityCombo);

        VBox serviceBox = createFormField("Υπηρεσία");
        expenseServiceCombo = new ComboBox<>();
        expenseServiceCombo.setPrefWidth(250);
        expenseServiceCombo.setStyle(Theme.comboBox());
        expenseServiceCombo.setDisable(true);
        serviceBox.getChildren().add(expenseServiceCombo);

        row1.getChildren().addAll(budgetTypeBox, scopeBox, entityBox, serviceBox);

        HBox row2 = new HBox(12);
        row2.setAlignment(Pos.BOTTOM_LEFT);

        VBox categoryBox = createFormField("Κατηγορία Δαπάνης (προαιρετικό)");
        expenseCategoryCombo = new ComboBox<>();
        expenseCategoryCombo.getItems().add("Όλες οι κατηγορίες");
        expenseCategoryCombo.getItems().addAll(getExpenseCategoryOptions());
        expenseCategoryCombo.setValue("Όλες οι κατηγορίες");
        expenseCategoryCombo.setPrefWidth(250);
        expenseCategoryCombo.setStyle(Theme.comboBox());
        categoryBox.getChildren().add(expenseCategoryCombo);

        VBox valueBox = createFormField("Τιμή Αλλαγής");
        expenseChangeValueField = new TextField();
        expenseChangeValueField.setPromptText("π.χ. 10000, 10%"); // Prompt από 2ο snippet
        expenseChangeValueField.setPrefWidth(140);
        expenseChangeValueField.setStyle(Theme.textField());
        expenseChangeValueField.focusedProperty().addListener((obs, old, nv) ->
                expenseChangeValueField.setStyle(nv ? Theme.textFieldFocused() : Theme.textField()));
        valueBox.getChildren().add(expenseChangeValueField);

        VBox changeTypeBox = createFormField("Τύπος Αλλαγής");
        expenseChangeTypeCombo = new ComboBox<>();
        expenseChangeTypeCombo.getItems().addAll("Ποσοστό (%)", "Σταθερό Ποσό");
        expenseChangeTypeCombo.setValue("Ποσοστό (%)");
        expenseChangeTypeCombo.setPrefWidth(140);
        expenseChangeTypeCombo.setStyle(Theme.comboBox());
        changeTypeBox.getChildren().add(expenseChangeTypeCombo);

        VBox calcModeBox = createFormField("Τρόπος Εφαρμογής");
        expenseCalculationModeCombo = new ComboBox<>();
        expenseCalculationModeCombo.getItems().addAll(
                "Επηρεασμός κάθε λογαριασμού (Οριζόντια)",
                "Επηρεασμός συνολικού αθροίσματος (Επιμερισμός)"
        );
        expenseCalculationModeCombo.setValue("Επηρεασμός κάθε λογαριασμού (Οριζόντια)");
        expenseCalculationModeCombo.setPrefWidth(280);
        expenseCalculationModeCombo.setStyle(Theme.comboBox());
        calcModeBox.getChildren().add(expenseCalculationModeCombo);

        expenseExecuteButton = new Button("Εκτέλεση Αλλαγής");
        expenseExecuteButton.setStyle(Theme.buttonPrimary());
        expenseExecuteButton.setPrefHeight(35);
        expenseExecuteButton.setOnAction(e -> executeExpenseChange());
        expenseExecuteButton.setOnMouseEntered(e -> expenseExecuteButton.setStyle(Theme.buttonPrimaryHover()));
        expenseExecuteButton.setOnMouseExited(e -> expenseExecuteButton.setStyle(Theme.buttonPrimary()));

        row2.getChildren().addAll(categoryBox, valueBox, changeTypeBox, calcModeBox, expenseExecuteButton);

        expenseStatusLabel = new Label("");
        expenseStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");

        formCard.getChildren().addAll(formTitle, row1, row2, expenseStatusLabel);
        section.getChildren().add(formCard);

        updateExpenseEntityCombo();

        return section;
    }

    private List<String> getExpenseCategoryOptions() {
        List<String> categories = new ArrayList<>();
        categories.add("21 - Παροχές σε εργαζομένους");
        categories.add("22 - Κοινωνικές παροχές");
        categories.add("23 - Μεταβιβάσεις");
        categories.add("24 - Αγορές αγαθών και υπηρεσιών");
        categories.add("25 - Επιδοτήσεις");
        categories.add("26 - Τόκοι");
        categories.add("27 - Λοιπές δαπάνες");
        categories.add("29 - Πιστώσεις υπό κατανομή");
        categories.add("31 - Πάγια περιουσιακά στοιχεία");
        categories.add("33 - Τιμαλφή");
        categories.add("44 - Δάνεια");
        categories.add("45 - Συμμετοχικοί τίτλοι");
        categories.add("53 - Χρεωστικοί τίτλοι");
        categories.add("54 - Δάνεια (αποπληρωμή)");
        return categories;
    }

    private void updateScopeVisibility() {
        String scope = expenseScopeCombo.getValue();
        if (scope == null) {
            return;
        }

        if (scope.equals("Καθολική (Όλοι οι Φορείς)")) {
            expenseEntityCombo.setDisable(true);
            expenseServiceCombo.setDisable(true);
            expenseEntityCombo.setValue(null);
            expenseServiceCombo.setValue(null);
        } else if (scope.equals("Ανά Φορέα")) {
            expenseEntityCombo.setDisable(false);
            expenseServiceCombo.setDisable(true);
            expenseServiceCombo.setValue(null);
        } else if (scope.equals("Ανά Υπηρεσία")) {
            expenseEntityCombo.setDisable(false);
            expenseServiceCombo.setDisable(false);
        }
    }

    private void updateExpenseEntityCombo() {
        expenseEntityCombo.getItems().clear();
        ArrayList<Entity> entities = Entity.getEntities();
        for (Entity entity : entities) {
            expenseEntityCombo.getItems().add(entity.getEntityCode() + " - " + entity.getEntityName());
        }
        if (!expenseEntityCombo.getItems().isEmpty()) {
            expenseEntityCombo.setValue(expenseEntityCombo.getItems().get(0));
            updateExpenseServiceCombo();
        }
    }

    private void updateExpenseServiceCombo() {
        expenseServiceCombo.getItems().clear();
        String entitySelection = expenseEntityCombo.getValue();
        if (entitySelection == null || entitySelection.isEmpty()) {
            return;
        }

        String entityCode = entitySelection.split(" - ")[0];
        Entity entity = Entity.findEntityWithEntityCode(entityCode);
        if (entity == null) {
            return;
        }

        String budgetType = expenseBudgetTypeCombo.getValue();
        List<String> serviceCodes;

        if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
            serviceCodes = entity.getAllRegularServiceCodes();
        } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
            serviceCodes = entity.getAllPublicInvestmentNationalServiceCodes();
        } else {
            serviceCodes = entity.getAllPublicInvestmentCoFundedServiceCodes();
        }

        for (String serviceCode : serviceCodes) {
            String serviceName = getServiceName(entity, serviceCode, budgetType);
            expenseServiceCombo.getItems().add(serviceCode + " - " + truncateDescription(serviceName, 70));
        }

        if (!expenseServiceCombo.getItems().isEmpty()) {
            expenseServiceCombo.setValue(expenseServiceCombo.getItems().get(0));
        }
    }

    private String getServiceName(Entity entity, String serviceCode, String budgetType) {
        if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
            return entity.getRegularServiceNameWithCode(serviceCode);
        } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
            return entity.getPublicInvestmentNationalServiceNameWithCode(serviceCode);
        } else {
            return entity.getPublicInvestmentCoFundedServiceNameWithCode(serviceCode);
        }
    }

    private VBox createExpenseResultsSection() {
        VBox section = new VBox(0);
        section.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(section, Priority.ALWAYS);

        VBox resultsCard = new VBox(16);
        resultsCard.setPadding(new Insets(20));
        resultsCard.setStyle(Theme.card());
        VBox.setVgrow(resultsCard, Priority.ALWAYS);

        Label resultsTitle = new Label("Αποτελέσματα Αλλαγών Δαπανών");
        resultsTitle.setStyle(Theme.sectionHeader());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        expenseResultsData = FXCollections.observableArrayList();
        expenseResultsTable = createExpenseResultsTable();
        VBox.setVgrow(expenseResultsTable, Priority.ALWAYS);

        expenseDisplayModeCombo = new ComboBox<>();
        expenseDisplayModeCombo.getItems().addAll("Αναλυτική Προβολή", "Συγκεντρωτική Προβολή");
        expenseDisplayModeCombo.setValue("Αναλυτική Προβολή");
        expenseDisplayModeCombo.setStyle(Theme.comboBox());
        expenseDisplayModeCombo.setPrefWidth(180);
        expenseDisplayModeCombo.setOnAction(e -> refreshExpenseResultsView());

        expenseViewLevelCombo = new ComboBox<>();
        expenseViewLevelCombo.setStyle(Theme.comboBox());
        expenseViewLevelCombo.setPrefWidth(180);
        expenseViewLevelCombo.setDisable(true);
        expenseViewLevelCombo.setOnAction(e -> refreshExpenseResultsView());

        HBox resultsHeader = new HBox(12);
        resultsHeader.setAlignment(Pos.CENTER_LEFT);
        resultsHeader.getChildren().addAll(resultsTitle, spacer, new Label("Τύπος:"), expenseDisplayModeCombo, new Label("Επίπεδο:"), expenseViewLevelCombo);

        expenseResultsData = FXCollections.observableArrayList();
        expenseResultsTable = createExpenseResultsTable();
        VBox.setVgrow(expenseResultsTable, Priority.ALWAYS);

        resultsCard.getChildren().addAll(resultsHeader, expenseResultsTable);
        section.getChildren().add(resultsCard);
        return section;
    }

    private TableView<ExpenseChangeResult> createExpenseResultsTable() {
        TableView<ExpenseChangeResult> table = new TableView<>(expenseResultsData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label placeholder = new Label("Εκτελέστε μια αλλαγή για να δείτε αποτελέσματα");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        table.setPlaceholder(placeholder);

        TableColumn<ExpenseChangeResult, String> entityCol = new TableColumn<>("Φορέας");
        entityCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().entityName));
        entityCol.setPrefWidth(150);

        TableColumn<ExpenseChangeResult, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().serviceName));
        serviceCol.setPrefWidth(200);

        TableColumn<ExpenseChangeResult, String> categoryCol = new TableColumn<>("Κατηγορία");
        categoryCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().categoryCode));
        categoryCol.setPrefWidth(70);

        TableColumn<ExpenseChangeResult, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().description));
        descCol.setPrefWidth(180);

        TableColumn<ExpenseChangeResult, String> beforeCol = new TableColumn<>("Πριν");
        beforeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().before));
        beforeCol.setPrefWidth(100);
        beforeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ExpenseChangeResult, String> afterCol = new TableColumn<>("Μετά");
        afterCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().after));
        afterCol.setPrefWidth(100);
        afterCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ExpenseChangeResult, String> changeCol = new TableColumn<>("Μεταβολή");
        changeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().change));
        changeCol.setPrefWidth(180);
        changeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        entityCol.setMaxWidth(1f * Integer.MAX_VALUE * 6);
        serviceCol.setMaxWidth(1f * Integer.MAX_VALUE * 40);
        categoryCol.setMaxWidth(1f * Integer.MAX_VALUE * 5);
        descCol.setMaxWidth(1f * Integer.MAX_VALUE * 15);
        beforeCol.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        afterCol.setMaxWidth(1f * Integer.MAX_VALUE * 8);
        changeCol.setMaxWidth(1f * Integer.MAX_VALUE * 18);
        table.getColumns().add(entityCol);
        table.getColumns().add(serviceCol);
        table.getColumns().add(categoryCol);
        table.getColumns().add(descCol);
        table.getColumns().add(beforeCol);
        table.getColumns().add(afterCol);
        table.getColumns().add(changeCol);

        return table;
    }

    private void executeExpenseChange() {
        String changeValue = expenseChangeValueField.getText().trim();
        String budgetType = expenseBudgetTypeCombo.getValue();
        String scope = expenseScopeCombo.getValue();
        String changeType = expenseChangeTypeCombo.getValue();
        String categorySelection = expenseCategoryCombo.getValue();

        if (changeValue.isEmpty()) {
            showExpenseError("Παρακαλώ εισάγετε τιμή αλλαγής");
            return;
        }

        try {
            double percentage = 0;
            long fixedAmount = 0;

            String cleanValue = changeValue.replaceAll("[^0-9.\\-]", "");
            double numericValue = Double.parseDouble(cleanValue);

            if (changeType.equals("Ποσοστό (%)") || changeValue.contains("%")) {
                percentage = numericValue / 100.0;
            } else {
                fixedAmount = (long) numericValue;
            }

            // Get category code if specific category selected
            String categoryCode = null;
            if (categorySelection != null && !categorySelection.equals("Όλες οι κατηγορίες")) {
                categoryCode = categorySelection.split(" - ")[0];
            }

            ArrayList<? extends BudgetExpense> affectedExpenses = getExpensesForScope(budgetType, scope, categoryCode);

            if (affectedExpenses.isEmpty()) {
                showExpenseError("Δεν βρέθηκαν λογαριασμοί για τις παραμέτρους που ορίσατε.");
                return;
            }

            // Capture before values and apply changes
            Map<String, Long> beforeValues = captureExpenseValues(budgetType, scope, categoryCode);
            lastExpenseBeforeValues = captureExpenseValues(budgetType, scope, categoryCode);
            applyExpenseChange(budgetType, scope, categoryCode, percentage, fixedAmount);
            Map<String, Long> afterValues = captureExpenseValues(budgetType, scope, categoryCode);

            if (beforeValues.equals(afterValues)) {
                showExpenseError("Η αλλαγή απέτυχε, καθώς ένας ή παραπάνω λογαριασμοί θα είχαν αρνητικό υπόλοιπο");
                return;
            }
            // Display results
            displayExpenseResults(budgetType, scope, categoryCode, beforeValues, afterValues);
            updateConsolidationOptions(budgetType);

            showExpenseSuccess("Η αλλαγή εφαρμόστηκε επιτυχώς!");

            pendingChanges.addAll(affectedExpenses);
            updateSaveButtonState();

        } catch (NumberFormatException e) {
            showExpenseError("Μη έγκυρη τιμή. Χρησιμοποιήστε αριθμό ή ποσοστό");
        } catch (Exception e) {
            showExpenseError("Σφάλμα: " + e.getMessage());
        }
    }

    private Map<String, Long> captureExpenseValues(String budgetType, String scope, String categoryCode) {
        Map<String, Long> values = new HashMap<>();
        ArrayList<? extends BudgetExpense> expenses = getExpensesForScope(budgetType, scope, categoryCode);

        for (BudgetExpense expense : expenses) {
            String key = expense.getEntityCode() + "|" + expense.getServiceCode() + "|" + expense.getCode();
            values.put(key, expense.getAmount());
        }
        return values;
    }

    private ArrayList<? extends BudgetExpense> getExpensesForScope(String budgetType, String scope, String categoryCode) {
        ArrayList<BudgetExpense> result = new ArrayList<>();

        if (scope.equals("Καθολική (Όλοι οι Φορείς)")) {
            if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
                if (categoryCode != null) {
                    result.addAll(RegularBudgetExpense.getRegularBudgetExpensesOfCategoryWithCode(categoryCode));
                } else {
                    result.addAll(RegularBudgetExpense.getAllRegularBudgetExpenses());
                }
            } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
                if (categoryCode != null) {
                    result.addAll(PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(categoryCode));
                } else {
                    result.addAll(PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses());
                }
            } else {
                if (categoryCode != null) {
                    result.addAll(PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(categoryCode));
                } else {
                    result.addAll(PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses());
                }
            }
        } else {
            // Get selected entity
            String entitySelection = expenseEntityCombo.getValue();
            if (entitySelection == null) {
                return result;
            }
            String entityCode = entitySelection.split(" - ")[0];
            Entity entity = Entity.findEntityWithEntityCode(entityCode);
            if (entity == null) {
                return result;
            }

            if (scope.equals("Ανά Φορέα")) {
                if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
                    if (categoryCode != null) {
                        for (RegularBudgetExpense exp : entity.getRegularBudgetExpenses()) {
                            if (exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    } else {
                        result.addAll(entity.getRegularBudgetExpenses());
                    }
                } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
                    if (categoryCode != null) {
                        for (PublicInvestmentBudgetNationalExpense exp : entity.getPublicInvestmentBudgetNationalExpenses()) {
                            if (exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    } else {
                        result.addAll(entity.getPublicInvestmentBudgetNationalExpenses());
                    }
                } else {
                    if (categoryCode != null) {
                        for (PublicInvestmentBudgetCoFundedExpense exp : entity.getPublicInvestmentBudgetCoFundedExpenses()) {
                            if (exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    } else {
                        result.addAll(entity.getPublicInvestmentBudgetCoFundedExpenses());
                    }
                }
            } else if (scope.equals("Ανά Υπηρεσία")) {
                String serviceSelection = expenseServiceCombo.getValue();
                if (serviceSelection == null) {
                    return result;
                }
                String serviceCode = serviceSelection.split(" - ")[0];

                if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
                    for (RegularBudgetExpense exp : entity.getRegularBudgetExpenses()) {
                        if (exp.getServiceCode().equals(serviceCode)) {
                            if (categoryCode == null || exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    }
                } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
                    for (PublicInvestmentBudgetNationalExpense exp : entity.getPublicInvestmentBudgetNationalExpenses()) {
                        if (exp.getServiceCode().equals(serviceCode)) {
                            if (categoryCode == null || exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    }
                } else {
                    for (PublicInvestmentBudgetCoFundedExpense exp : entity.getPublicInvestmentBudgetCoFundedExpenses()) {
                        if (exp.getServiceCode().equals(serviceCode)) {
                            if (categoryCode == null || exp.getCode().equals(categoryCode)) {
                                result.add(exp);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void applyExpenseChange(String budgetType, String scope, String categoryCode, double percentage, long fixedAmount) {
        BudgetType type = getBudgetTypeEnum(budgetType);
        String mode = expenseCalculationModeCombo.getValue();
        boolean isTotalSum = mode.equals("Επηρεασμός συνολικού αθροίσματος (Επιμερισμός)");

        if (scope.equals("Καθολική (Όλοι οι Φορείς)")) {
            // Global change
            if (isTotalSum && fixedAmount != 0) {
                int count = findTotalAccountsOfBudgetType(budgetType, categoryCode);
                if (count > 0) {
                    fixedAmount = fixedAmount / count;
                } else {
                    showExpenseError("Δεν βρέθηκαν λογαριασμοί για επιμερισμό.");
                    return;
                }
            }
            if (categoryCode != null) {
                if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
                    RegularBudgetExpense.implementGlobalChangesInCertainRegularExpenseCategoryWithPercentageAllocation(categoryCode, percentage, fixedAmount);
                } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
                    PublicInvestmentBudgetNationalExpense.implementGlobalChangesInCertainPublicInvestmentBudgetNationalCategory(categoryCode, percentage, fixedAmount);
                } else {
                    PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInCertainPublicInvestmentBudgetCoFundedCategory(categoryCode, percentage, fixedAmount);
                }
            } else {
                if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
                    RegularBudgetExpense.implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(percentage, fixedAmount);
                } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
                    PublicInvestmentBudgetNationalExpense.implementGlobalChangesInAllPublicInvestmentBudgetNationalCategories(percentage, fixedAmount);
                } else {
                    PublicInvestmentBudgetCoFundedExpense.implementGlobalChangesInAllPublicInvestmentBudgetCoFundedCategories(percentage, fixedAmount);
                }
            }
        } else {
            // Entity or Service level change
            String entitySelection = expenseEntityCombo.getValue();
            if (entitySelection == null) {
                return;
            }
            String entityCode = entitySelection.split(" - ")[0];
            Entity entity = Entity.findEntityWithEntityCode(entityCode);
            if (entity == null) {
                return;
            }
            String serviceSelection = expenseServiceCombo.getValue();
            String serviceCodeOnly = (serviceSelection != null) ? serviceSelection.split(" - ")[0] : null;
            if (isTotalSum && fixedAmount != 0) {
                int count = findTotalAccountsOfEntity(budgetType, scope, categoryCode, serviceCodeOnly, entity);
                if (count > 0) {
                    fixedAmount = fixedAmount / count;
                } else {
                    showExpenseError("Δεν βρέθηκαν λογαριασμοί για επιμερισμό.");
                    return;
                }
            }

            if (scope.equals("Ανά Φορέα")) {
                if (categoryCode != null) {
                    entity.implementChangesInSpecificExpenseCategoryOfAllServices(categoryCode, percentage, fixedAmount, type);
                } else {
                    entity.implementChangesInAllExpenseCategoriesOfAllServices(percentage, fixedAmount, type);
                }
            } else if (scope.equals("Ανά Υπηρεσία")) {
                if (serviceSelection == null) {
                    return;
                }
                String serviceCode = serviceSelection.split(" - ")[0];
                if (categoryCode != null) {
                    entity.implementChangesInSpecificExpenseCategoryOfSpecificService(serviceCode, categoryCode, percentage, fixedAmount, type);
                } else {
                    entity.implementChangesInAllExpenseCategoriesOfSpecificService(serviceCode, percentage, fixedAmount, type);
                }
            }
        }
    }

    private int findTotalAccountsOfEntity(String budgetType, String scope, String categoryCode, String serviceCode, Entity entity) {
        if (entity == null) {
            return 0;
        }

        List<? extends BudgetExpense> expenses = switch (budgetType) {
            case "Τακτικός Προϋπολογισμός" -> entity.getRegularBudgetExpenses();
            case "ΠΔΕ Εθνικό" -> entity.getPublicInvestmentBudgetNationalExpenses();
            default -> entity.getPublicInvestmentBudgetCoFundedExpenses();
        };

        if (scope.equals("Ανά Φορέα")) {
            return (int) expenses.stream().filter(exp -> categoryCode == null || exp.getCode().equals(categoryCode)).count();
        } else if (scope.equals("Ανά Υπηρεσία") && serviceCode != null) {
            return (int) expenses.stream().filter(exp -> exp.getServiceCode().equals(serviceCode)).filter(exp -> categoryCode == null || exp.getCode().equals(categoryCode)).count();
        }

        return 0;
    }

    private int findTotalAccountsOfBudgetType(String budgetType, String categoryCode) {
        List<? extends BudgetExpense> expenses;
        if (categoryCode == null) {
            expenses = switch (budgetType) {
                case "Τακτικός Προϋπολογισμός" -> RegularBudgetExpense.getAllRegularBudgetExpenses();
                case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses();
                default -> PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses();
            };
        } else {
            expenses = switch (budgetType) {
                case "Τακτικός Προϋπολογισμός" -> RegularBudgetExpense.getRegularBudgetExpensesOfCategoryWithCode(categoryCode);
                case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(categoryCode);
                default -> PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(categoryCode);
            };
        }
        return expenses.size();
    }

    private BudgetType getBudgetTypeEnum(String budgetType) {
        return switch (budgetType) {
            case "Τακτικός Προϋπολογισμός" -> BudgetType.REGULAR_BUDGET;
            case "ΠΔΕ Εθνικό" -> BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL;
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED;
            default -> BudgetType.REGULAR_BUDGET;
        };
    }

    private void displayExpenseResults(String budgetType, String scope, String categoryCode, Map<String, Long> before, Map<String, Long> after) {
        expenseResultsData.clear();

        if (expenseDisplayModeCombo.getValue().equals("Αναλυτική Προβολή")) {
            ArrayList<? extends BudgetExpense> expenses = getExpensesForScope(budgetType, scope, categoryCode);
            for (BudgetExpense expense : expenses) {
                String key = expense.getEntityCode() + "|" + expense.getServiceCode() + "|" + expense.getCode();
                Long beforeVal = before.getOrDefault(key, 0L);
                Long afterVal = after.getOrDefault(key, expense.getAmount());

                long change = afterVal - beforeVal;
                if (change == 0) {
                    continue;
                }

                expenseResultsData.add(createResultRow(expense.getEntityName(), expense.getServiceName(),
                        expense.getCode(), expense.getDescription(), beforeVal, afterVal));
            }
        } else {
            String viewLevel = expenseViewLevelCombo.getValue(); // "Κρατικός Προϋπολογισμός", κτλ.
            String levelPrefix = (viewLevel == null || viewLevel.equals(budgetType)) ? budgetType : viewLevel;

            String entitySelection = expenseEntityCombo.getValue();
            String entityCode = (entitySelection != null) ? entitySelection.split(" - ")[0] : null;
            String entityName = (entitySelection != null) ? entitySelection.split(" - ")[1] : "ΕΠΙΚΡΑΤΕΙΑ";

            String serviceSelection = expenseServiceCombo.getValue();
            String serviceCode = (serviceSelection != null) ? serviceSelection.split(" - ")[0] : null;
            String serviceName = (serviceSelection != null) ? serviceSelection.split(" - ")[1] : "";

            long bSum = before.values().stream().mapToLong(Long::longValue).sum();
            long aSum = after.values().stream().mapToLong(Long::longValue).sum();
            long delta = aSum - bSum;

            if (scope.equals("Ανά Υπηρεσία") && serviceCode != null) {

                long aServ = getConsolidatedSum(viewLevel, budgetType, scope, entityCode, serviceCode, null);
                long bServ = aServ - delta;
                addSummaryRow("ΥΠΗΡΕΣΙΑ", entityName, serviceName, "ΟΛΕΣ", "Σύνολο Υπηρεσίας", bServ, aServ);


                if (categoryCode != null && !categoryCode.equals("Όλες οι κατηγορίες")) {
                    long aCat = getConsolidatedSum(viewLevel, budgetType, "Ανά Φορέα", entityCode, null, categoryCode);
                    long bCat = aCat - delta;
                    addSummaryRow("ΚΑΤΗΓΟΡΙΑ", entityName, "", categoryCode, BudgetExpense.getDescriptionWithCode(categoryCode), bCat, aCat);
                }

                long aEnt = getConsolidatedSum(viewLevel, budgetType, "Ανά Φορέα", entityCode, null, null);
                long bEnt = aEnt - delta;
                addSummaryRow("ΦΟΡΕΑΣ", entityName, "", "ΟΛΕΣ", "Σύνολο Φορέα", bEnt, aEnt);

            } else if (scope.equals("Ανά Φορέα") && entityCode != null) {
                if (categoryCode != null && !categoryCode.equals("Όλες οι κατηγορίες")) {
                    long aCat = getConsolidatedSum(viewLevel, budgetType, "Ανά Φορέα", entityCode, null, categoryCode);
                    long bCat = aCat - delta;
                    addSummaryRow("ΚΑΤΗΓΟΡΙΑ", entityName, "", categoryCode, BudgetExpense.getDescriptionWithCode(categoryCode), bCat, aCat);
                }
                long aEnt = getConsolidatedSum(viewLevel, budgetType, "Ανά Φορέα", entityCode, null, null);
                long bEnt = aEnt - delta;
                addSummaryRow("ΦΟΡΕΑΣ", entityName, "", "ΟΛΕΣ", "Σύνολο Φορέα", bEnt, aEnt);

            } else {
                if (categoryCode != null && !categoryCode.equals("Όλες οι κατηγορίες")) {
                    long aCatGlobal = getConsolidatedSum(viewLevel, budgetType, "Καθολική (Όλοι οι Φορείς)", null, null, categoryCode);
                    long bCatGlobal = aCatGlobal - delta;
                    addSummaryRow("ΚΑΤΗΓΟΡΙΑ", "ΕΠΙΚΡΑΤΕΙΑ", "", categoryCode, BudgetExpense.getDescriptionWithCode(categoryCode), bCatGlobal, aCatGlobal);
                }
                long aTypeGlobal = getConsolidatedSum(viewLevel, budgetType, "Καθολική (Όλοι οι Φορείς)", null, null, null);
                long bTypeGlobal = aTypeGlobal - delta;
                addSummaryRow("ΣΥΝΟΛΟ", "ΕΠΙΚΡΑΤΕΙΑ", "", "ΟΛΕΣ", "Γενικό Σύνολο Εξόδων", bTypeGlobal, aTypeGlobal);

            }
        }
        animateExpenseResults();
    }

    private void updateConsolidationOptions(String budgetType) {
        expenseViewLevelCombo.getItems().clear();
        expenseViewLevelCombo.setDisable(false);

        if (budgetType.equals("Τακτικός Προϋπολογισμός")) {
            expenseViewLevelCombo.getItems().addAll("Τακτικός Προϋπολογισμός", "Κρατικός Προϋπολογισμός");
        } else if (budgetType.equals("ΠΔΕ Εθνικό")) {
            expenseViewLevelCombo.getItems().addAll("ΠΔΕ Εθνικό", "ΠΔΕ (Σύνολο)", "Κρατικός Προϋπολογισμός");
        } else {
            expenseViewLevelCombo.getItems().addAll("ΠΔΕ Συγχρηματοδοτούμενο", "ΠΔΕ (Σύνολο)", "Κρατικός Προϋπολογισμός");
        }
        expenseViewLevelCombo.setValue(budgetType);
    }

    private ExpenseChangeResult createResultRow(String ent, String ser, String cat, String desc, long b, long a) {
        long change = a - b;
        double pct = (b != 0) ? ((double) change / b) * 100 : 0;

        String changeStr = String.format("%s%s (%.1f%%)", (change > 0 ? "+" : ""), Theme.formatAmount(change), pct);

        return new ExpenseChangeResult(ent, truncateDescription(ser, 25), cat, truncateDescription(desc, 30),
                Theme.formatAmount(b), Theme.formatAmount(a), changeStr);
    }

    private void addSummaryRow(String levelTag, String entityName, String serviceName, String catCode, String desc, long b, long a) {
        long change = a - b;
        double pct = (b != 0) ? ((double) change / b) * 100 : 0;

        String changeStr = String.format("%s%s (%.1f%%)",
                (change > 0 ? "+" : ""),
                Theme.formatAmount(change),
                pct);

        expenseResultsData.add(new ExpenseChangeResult(
                entityName,
                serviceName,
                catCode,
                "[" + levelTag + "] " + desc,
                Theme.formatAmount(b),
                Theme.formatAmount(a),
                changeStr
        ));
    }

    private ArrayList<? extends BudgetExpense> getExpensesForCustomScope(String bType, String scope, String entityCode, String serviceCode, String categoryCode) {
        ArrayList<BudgetExpense> result = new ArrayList<>();

        if (scope.equals("Καθολική (Όλοι οι Φορείς)")) {
            if (bType.equals("Τακτικός Προϋπολογισμός")) {
                if (categoryCode != null) {
                    result.addAll(RegularBudgetExpense.getRegularBudgetExpensesOfCategoryWithCode(categoryCode));
                } else {
                    result.addAll(RegularBudgetExpense.getAllRegularBudgetExpenses());
                }
            } else if (bType.equals("ΠΔΕ Εθνικό")) {
                var list = (categoryCode != null) ?
                        PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfCategoryWithCode(categoryCode) :
                        PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses();
                if (list != null) {
                    result.addAll(list);
                }
            } else {
                var list = (categoryCode != null) ?
                        PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfCategoryWithCode(categoryCode) :
                        PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses();
                if (list != null) {
                    result.addAll(list);
                }
            }
        } else {
            Entity entity = Entity.findEntityWithEntityCode(entityCode);
            if (entity == null) {
                return result;
            }

            List<? extends BudgetExpense> allEntityExpenses = switch (bType) {
                case "Τακτικός Προϋπολογισμός" -> entity.getRegularBudgetExpenses();
                case "ΠΔΕ Εθνικό" -> entity.getPublicInvestmentBudgetNationalExpenses();
                default -> entity.getPublicInvestmentBudgetCoFundedExpenses();
            };

            if (allEntityExpenses != null) {
                for (BudgetExpense exp : allEntityExpenses) {
                    boolean matchesService = (serviceCode == null) || exp.getServiceCode().equals(serviceCode);
                    boolean matchesCategory = (categoryCode == null) || exp.getCode().equals(categoryCode);

                    if (matchesService && matchesCategory) {
                        result.add(exp);
                    }
                }
            }
        }
        return result;
    }

    private long getConsolidatedSum(String viewLevel, String currentBudgetType, String scope, String entityCode, String serviceCode, String categoryCode) {
        if ("Κρατικός Προϋπολογισμός".equals(viewLevel)) {
            return calculateSum("Τακτικός Προϋπολογισμός", scope, entityCode, serviceCode, categoryCode) +
                    calculateSum("ΠΔΕ Εθνικό", scope, entityCode, serviceCode, categoryCode) +
                    calculateSum("ΠΔΕ Συγχρηματοδοτούμενο", scope, entityCode, serviceCode, categoryCode);
        } else if ("ΠΔΕ (Σύνολο)".equals(viewLevel)) {
            return calculateSum("ΠΔΕ Εθνικό", scope, entityCode, serviceCode, categoryCode) +
                    calculateSum("ΠΔΕ Συγχρηματοδοτούμενο", scope, entityCode, serviceCode, categoryCode);
        } else {
            return calculateSum(currentBudgetType, scope, entityCode, serviceCode, categoryCode);
        }
    }

    private long calculateSum(String bType, String scope, String entityCode, String serviceCode, String categoryCode) {
        return getExpensesForCustomScope(bType, scope, entityCode, serviceCode, categoryCode).stream().mapToLong(BudgetExpense::getAmount).sum();
    }

    private void refreshExpenseResultsView() {
        if (lastExpenseBeforeValues == null) {
            return;
        }

        boolean isSummary = "Συγκεντρωτική Προβολή".equals(expenseDisplayModeCombo.getValue());
        expenseViewLevelCombo.setDisable(!isSummary);

        String budgetType = expenseBudgetTypeCombo.getValue();
        String scope = expenseScopeCombo.getValue();
        String categorySelection = expenseCategoryCombo.getValue();
        String categoryCode = (categorySelection != null && !categorySelection.equals("Όλες οι κατηγορίες")) ? categorySelection.split(" - ")[0] : null;

        Map<String, Long> currentAfterValues = captureExpenseValues(budgetType, scope, categoryCode);

        displayExpenseResults(budgetType, scope, categoryCode, lastExpenseBeforeValues, currentAfterValues);
    }

    private void saveAllToDatabase() {
        if (pendingChanges.isEmpty()) {
            return;
        }

        int totalSaved = pendingChanges.size();

        try {
            SQLiteManager sqLiteManager = SQLiteManager.getInstance();
            sqLiteManager.saveChangesBatch(pendingChanges);

            ExpensesHistory.getHistoryDeque().clear();
            RevenuesHistory.getHistoryDeque().clear();

            pendingChanges.clear();

            updateSaveButtonState();

            showGlobalNotification("Επιτυχής αποθήκευση " + totalSaved + " εγγραφών στη βάση δεδομένων!", false);

        } catch (Exception e) {
            showGlobalNotification("Σφάλμα κατά την αποθήκευση: " + e.getMessage(), true);
        }
    }

    private void updateSaveButtonState() {
        if (saveToDbButton == null || globalUndoButton == null) {
            return;
        }

        int records = pendingChanges.size();
        int batchExp = ExpensesHistory.getHistoryDeque().size();
        int batchRev = RevenuesHistory.getHistoryDeque().size();
        int totalBatches = batchExp + batchRev;

        saveToDbButton.setDisable(records == 0);
        saveToDbButton.setText("Αποθήκευση (" + records + ")");

        // Ενεργοποίηση/Απενεργοποίηση
        globalUndoButton.setDisable(totalBatches == 0);
        undoStepsSpinner.setDisable(totalBatches == 0);

        // Ενημέρωση του Max μόνο αν υπάρχουν αλλαγές
        if (totalBatches > 0) {
            SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                    (SpinnerValueFactory.IntegerSpinnerValueFactory) undoStepsSpinner.getValueFactory();
            factory.setMax(totalBatches);
        }

        statsLabel.setText(String.format("Εκκρεμούν: %d εγγραφές | %d αλλαγές", records, totalBatches));
    }

    private void handleGlobalUndo() {
        undoStepsSpinner.increment(0);

        int steps = undoStepsSpinner.getValue();
        int batchExpenses = ExpensesHistory.getHistoryDeque().size();
        int batchRevenues = RevenuesHistory.getHistoryDeque().size();
        int available = batchExpenses + batchRevenues;

        if (available == 0) {
            return;
        }
        int actualSteps = Math.min(steps, available);

        int selectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();

        for (int i = 0; i < actualSteps; i++) {
            if (selectedTabIndex == 1 && !ExpensesHistory.getHistoryDeque().isEmpty()) {
                ExpensesHistory.returnToPreviousState();
            } else if (selectedTabIndex == 0 && !RevenuesHistory.getHistoryDeque().isEmpty()) {
                RevenuesHistory.returnToPreviousState();
            } else {
                if (!ExpensesHistory.getHistoryDeque().isEmpty()) {
                    ExpensesHistory.returnToPreviousState();
                } else if (!RevenuesHistory.getHistoryDeque().isEmpty()) {
                    RevenuesHistory.returnToPreviousState();
                }
            }
        }

        resultsData.clear();
        expenseResultsData.clear();
        statusLabel.setText("");
        expenseStatusLabel.setText("");
        lastSelectedCode = null;
        lastExpenseBeforeValues = null;
        lastBeforeValues = null;

        if (ExpensesHistory.getHistoryDeque().isEmpty() && RevenuesHistory.getHistoryDeque().isEmpty()) {
            pendingChanges.clear();
        }

        undoStepsSpinner.getValueFactory().setValue(1);
        updateSaveButtonState();

        showGlobalNotification("Αναιρέθηκαν " + actualSteps + " αλλαγές. Οι πίνακες καθαρίστηκαν.", false);
    }

    private void animateExpenseResults() {
        expenseResultsTable.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), expenseResultsTable);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), expenseResultsTable);
        scale.setFromX(0.98);
        scale.setFromY(0.98);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition parallel = new ParallelTransition(fadeIn, scale);
        parallel.play();
    }

    private void showExpenseError(String message) {
        expenseStatusLabel.setText("! " + message);
        expenseStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.ERROR_LIGHT + "; -fx-font-weight: 500;");
    }

    private void showExpenseSuccess(String message) {
        expenseStatusLabel.setText("+ " + message);
        expenseStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.SUCCESS_LIGHT + "; -fx-font-weight: 500;");
    }

    private void showGlobalNotification(String message, boolean isError) {
        globalStatusLabel.setText(message);
        globalStatusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " +
                (isError ? Theme.ERROR_LIGHT : Theme.SUCCESS_LIGHT) + ";");
        globalStatusLabel.setVisible(true);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), globalStatusLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), globalStatusLabel);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> globalStatusLabel.setVisible(false));
            fadeOut.play();
        });
        delay.play();
    }

    public Set<BudgetEntry> getPendingChanges() {
        return pendingChanges;
    }

    public static class ExpenseChangeResult {
        public final String entityName;
        public final String serviceName;
        public final String categoryCode;
        public final String description;
        public final String before;
        public final String after;
        public final String change;

        public ExpenseChangeResult(String entityName, String serviceName, String categoryCode,
                                   String description, String before, String after, String change) {
            this.entityName = entityName;
            this.serviceName = serviceName;
            this.categoryCode = categoryCode;
            this.description = description;
            this.before = before;
            this.after = after;
            this.change = change;
        }
    }
}