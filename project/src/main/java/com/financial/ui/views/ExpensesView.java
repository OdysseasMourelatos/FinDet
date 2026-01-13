package com.financial.ui.views;

import com.financial.entries.BudgetExpense;
import com.financial.entries.Entity;
import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.PublicInvestmentBudgetNationalExpense;
import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import com.financial.entries.RegularBudgetExpense;
import com.financial.ui.Theme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Expenses view - displays budget expenses with clean, modern tabs.
 */
public class ExpensesView {

    private final ScrollPane scrollPane;
    private final VBox view;
    private TabPane tabPane;

    // Regular budget components
    private TextField regularSearchField;
    private ComboBox<String> regularEntityFilter;
    private TableView<RegularBudgetExpense> regularTable;
    private ObservableList<RegularBudgetExpense> regularData;
    private Label regularCountLabel;
    private Label regularTotalLabel;
    private ComboBox<String> regularViewCombo;

    private TableView<EntitiesView.AnalysisResult> regularSummaryTable;
    private ObservableList<EntitiesView.AnalysisResult> regularSummaryData;

    // Public Investment components
    private TextField pibSearchField;
    private ComboBox<String> pibEntityFilter;
    private ComboBox<String> pibTypeFilter;
    private TableView<BudgetExpense> pibTable;
    private ObservableList<BudgetExpense> pibData;
    private Label pibCountLabel;
    private Label pibTotalLabel;
    private ComboBox<String> pibViewCombo;
    private TableView<EntitiesView.AnalysisResult> pibSummaryTable;
    private ObservableList<EntitiesView.AnalysisResult> pibSummaryData;

    public ExpensesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        VBox header = createHeader();

        tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Tab regularTab = new Tab("Τακτικός Προϋπολογισμός");
        regularTab.setClosable(false);
        regularTab.setContent(createRegularExpensesContent());

        Tab investmentTab = new Tab("Πρόγραμμα Δημοσίων Επενδύσεων");
        investmentTab.setClosable(false);
        investmentTab.setContent(createPublicInvestmentExpensesContent());

        tabPane.getTabs().addAll(regularTab, investmentTab);
        view.getChildren().addAll(header, tabPane);

        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: " + Theme.BG_BASE + "; -fx-border-color: transparent;");
    }

    private VBox createHeader() {
        VBox header = new VBox(6);
        header.setPadding(new Insets(32, 24, 20, 24));
        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.ERROR, 0.15));
        Label iconText = new Label("-");
        iconText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Theme.ERROR_LIGHT + ";");
        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Έξοδα Προϋπολογισμού");
        title.setStyle(Theme.pageTitle());
        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Διαχείριση και προβολή εξόδων κρατικού προϋπολογισμού");
        subtitle.setStyle(Theme.subtitle());
        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private VBox createRegularExpensesContent() {
        VBox container = new VBox(0);

        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        regularSearchField = new TextField();
        regularSearchField.setPromptText("Αναζήτηση...");
        regularSearchField.setStyle(Theme.textField());
        regularSearchField.setPrefWidth(220);

        VBox entityBox = new VBox(4);
        Label entityLabel = new Label("Φορέας");
        entityLabel.setStyle(Theme.mutedText());
        regularEntityFilter = new ComboBox<>();
        regularEntityFilter.getItems().add("Όλοι οι φορείς");
        Entity.getEntities().forEach(e -> regularEntityFilter.getItems().add(e.getEntityCode() + " - " + e.getEntityName()));
        regularEntityFilter.setValue("Όλοι οι φορείς");
        regularEntityFilter.setStyle(Theme.comboBox());
        regularEntityFilter.setPrefWidth(160);
        entityBox.getChildren().addAll(entityLabel, regularEntityFilter);

        VBox viewBox = new VBox(4);
        Label viewLabel = new Label("Τύπος Προβολής");
        viewLabel.setStyle(Theme.mutedText());
        regularViewCombo = new ComboBox<>();
        regularViewCombo.getItems().addAll("Αναλυτική Προβολή", "Συγκεντρωτική Προβολή");
        regularViewCombo.setValue("Αναλυτική Προβολή");
        regularViewCombo.setStyle(Theme.comboBox());
        viewBox.getChildren().addAll(viewLabel, regularViewCombo);

        filterRow.getChildren().addAll(regularSearchField, entityBox, viewBox);

        HBox statsBar = createStatsBar(true);

        regularData = FXCollections.observableArrayList(RegularBudgetExpense.getAllRegularBudgetExpenses());
        regularTable = createRegularTable();

        regularSummaryData = FXCollections.observableArrayList();
        regularSummaryTable = createRegularSummaryTable();
        regularSummaryTable.setVisible(false);

        StackPane tableStack = new StackPane(regularTable, regularSummaryTable);
        VBox.setVgrow(tableStack, Priority.ALWAYS);

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(tableStack);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        // Listeners
        regularViewCombo.setOnAction(e -> {
            boolean isSummary = regularViewCombo.getValue().equals("Συγκεντρωτική Προβολή");

            regularTable.setVisible(!isSummary);
            regularSummaryTable.setVisible(isSummary);

            regularSearchField.setDisable(isSummary);
            regularEntityFilter.setDisable(isSummary);

            if (isSummary) {
                regularSearchField.clear();
                regularEntityFilter.setValue("Όλοι οι φορείς");
                loadRegularSummaryData();
            } else {
                applyRegularFilters();
            }
        });
        regularSearchField.textProperty().addListener((obs, old, val) -> applyRegularFilters());
        regularEntityFilter.setOnAction(e -> applyRegularFilters());

        container.getChildren().addAll(filterRow, statsBar, tableContainer);
        VBox.setVgrow(container, Priority.ALWAYS);
        return container;
    }

    private TableView<RegularBudgetExpense> createRegularTable() {
        TableView<RegularBudgetExpense> table = new TableView<>(regularData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RegularBudgetExpense, String> entCodeCol = new TableColumn<>("Κωδ. Φορ.");
        TableColumn<RegularBudgetExpense, String> entNameCol = new TableColumn<>("Φορέας");
        TableColumn<RegularBudgetExpense, String> serCodeCol = new TableColumn<>("Κωδ. Υπηρ.");
        TableColumn<RegularBudgetExpense, String> serNameCol = new TableColumn<>("Υπηρεσία");
        TableColumn<RegularBudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        TableColumn<RegularBudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        TableColumn<RegularBudgetExpense, String> amountCol = new TableColumn<>("Ποσό");

        entCodeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.07));
        entNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        serCodeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
        serNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.28));
        codeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
        descCol.prefWidthProperty().bind(table.widthProperty().multiply(0.18));
        amountCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        entCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityName()));
        serCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceCode()));
        serNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().addAll(entCodeCol, entNameCol, serCodeCol, serNameCol, codeCol, descCol, amountCol);
        return table;
    }
    private TableView<EntitiesView.AnalysisResult> createRegularSummaryTable() {
        TableView<EntitiesView.AnalysisResult> table = new TableView<>(regularSummaryData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<EntitiesView.AnalysisResult, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));

        TableColumn<EntitiesView.AnalysisResult, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));

        TableColumn<EntitiesView.AnalysisResult, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().addAll(codeCol, descCol, amountCol);
        return table;
    }

    private VBox createPublicInvestmentExpensesContent() {
        VBox container = new VBox(0);

        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        pibSearchField = new TextField();
        pibSearchField.setPromptText("Αναζήτηση...");
        pibSearchField.setStyle(Theme.textField());

        VBox entityBox = new VBox(4);
        pibEntityFilter = new ComboBox<>();
        pibEntityFilter.getItems().add("Όλοι οι φορείς");
        Entity.getEntities().forEach(e -> pibEntityFilter.getItems().add(e.getEntityCode() + " - " + e.getEntityName()));
        pibEntityFilter.setValue("Όλοι οι φορείς");
        pibEntityFilter.setStyle(Theme.comboBox());
        pibEntityFilter.setPrefWidth(160);
        entityBox.getChildren().addAll(new Label("Φορέας"), pibEntityFilter);

        VBox typeBox = new VBox(4);
        pibTypeFilter = new ComboBox<>();
        pibTypeFilter.getItems().addAll("ΠΔΕ (Εθνικό + Συγχρ.)", "Εθνικό", "Συγχρηματοδοτούμενο");
        pibTypeFilter.setValue("ΠΔΕ (Εθνικό + Συγχρ.)");
        pibTypeFilter.setStyle(Theme.comboBox());
        typeBox.getChildren().addAll(new Label("Τύπος"), pibTypeFilter);

        VBox viewBox = new VBox(4);
        pibViewCombo = new ComboBox<>();
        pibViewCombo.getItems().addAll("Αναλυτική Προβολή", "Συγκεντρωτική Προβολή");
        pibViewCombo.setValue("Αναλυτική Προβολή");
        pibViewCombo.setStyle(Theme.comboBox());
        viewBox.getChildren().addAll(new Label("Προβολή"), pibViewCombo);

        filterRow.getChildren().addAll(pibSearchField, entityBox, typeBox, viewBox);

        HBox statsBar = createStatsBar(false);

        pibData = FXCollections.observableArrayList();
        pibTable = createPibTable();
        loadPibData();

        pibSummaryData = FXCollections.observableArrayList();
        pibSummaryTable = createPibSummaryTable();
        pibSummaryTable.setVisible(false);

        StackPane tableStack = new StackPane(pibTable, pibSummaryTable);
        VBox.setVgrow(tableStack, Priority.ALWAYS);

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(tableStack);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        // Listeners
        pibViewCombo.setOnAction(e -> {
            boolean isSummary = pibViewCombo.getValue().equals("Συγκεντρωτική Προβολή");

            pibTable.setVisible(!isSummary);
            pibSummaryTable.setVisible(isSummary);

            pibSearchField.setDisable(isSummary);
            pibEntityFilter.setDisable(isSummary);

            if (isSummary) {
                pibSearchField.clear();
                pibEntityFilter.setValue("Όλοι οι φορείς");
                loadPibSummaryData();
            } else {
                applyPibFilters();
            }
        });
        pibSearchField.textProperty().addListener((obs, old, val) -> applyPibFilters());
        pibEntityFilter.setOnAction(e -> applyPibFilters());
        pibTypeFilter.setOnAction(e -> {
            if (pibViewCombo.getValue().equals("Συγκεντρωτική Προβολή")) {
                loadPibSummaryData();
            } else {
                applyPibFilters();
            }
        });

        container.getChildren().addAll(filterRow, statsBar, tableContainer);
        return container;
    }

    private TableView<BudgetExpense> createPibTable() {
        TableView<BudgetExpense> table = new TableView<>(pibData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BudgetExpense, String> entCodeCol = new TableColumn<>("Κωδ. Φορ.");
        TableColumn<BudgetExpense, String> entNameCol = new TableColumn<>("Φορέας");
        TableColumn<BudgetExpense, String> serCodeCol = new TableColumn<>("Κωδ. Υπηρ.");
        TableColumn<BudgetExpense, String> serNameCol = new TableColumn<>("Υπηρεσία");
        TableColumn<BudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        TableColumn<BudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        TableColumn<BudgetExpense, String> typeCol = new TableColumn<>("Τύπος");
        TableColumn<BudgetExpense, String> amountCol = new TableColumn<>("Ποσό");

        entCodeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.06));
        entNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.14));
        serCodeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.07));
        serNameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        codeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.09));
        descCol.prefWidthProperty().bind(table.widthProperty().multiply(0.16));
        typeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.08));
        amountCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        entCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityName()));
        serCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceCode()));
        serNameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        typeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue() instanceof PublicInvestmentBudgetExpense pib ? pib.getType() : "-"));
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().addAll(entCodeCol, entNameCol, serCodeCol, serNameCol, codeCol, descCol, typeCol, amountCol);
        return table;
    }

    private TableView<EntitiesView.AnalysisResult> createPibSummaryTable() {
        TableView<EntitiesView.AnalysisResult> table = new TableView<>(pibSummaryData);
        table.setStyle(Theme.table());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<EntitiesView.AnalysisResult, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));

        TableColumn<EntitiesView.AnalysisResult, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));

        TableColumn<EntitiesView.AnalysisResult, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().addAll(codeCol, descCol, amountCol);
        return table;
    }

    private HBox createStatsBar(boolean regular) {
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle("-fx-background-color: " + Theme.BG_SURFACE + "; -fx-border-color: " + Theme.BORDER_MUTED + "; -fx-border-width: 1 0;");

        Label countLbl = new Label();
        Label totalLbl = new Label();
        totalLbl.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + Theme.ERROR_LIGHT + ";");

        if (regular) {
            regularCountLabel = countLbl;
            regularTotalLabel = totalLbl;
        } else {
            pibCountLabel = countLbl;
            pibTotalLabel = totalLbl;
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        statsBar.getChildren().addAll(countLbl, spacer, totalLbl);
        return statsBar;
    }

    private void loadPibData() {
        pibData.setAll(PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses());
    }

    private void loadRegularSummaryData() {
        regularSummaryData.clear();
        Map<String, Long> summary = RegularBudgetExpense.getSumOfEveryRegularExpenseCategory();
        summary.forEach((code, amount) -> {
            String desc = RegularBudgetExpense.getDescriptionWithCode(code);
            regularSummaryData.add(new EntitiesView.AnalysisResult(code, desc, amount));
        });
        updateRegularStats();
    }

    private void loadPibSummaryData() {
        pibSummaryData.clear();
        String type = pibTypeFilter.getValue();
        Map<String, Long> summary = switch (type) {
            case "Εθνικό" -> PublicInvestmentBudgetNationalExpense.getSumOfEveryPublicInvestmentNationalExpenseCategory();
            case "Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedExpense.getSumOfEveryPublicInvestmentCoFundedExpenseCategory();
            default -> PublicInvestmentBudgetExpense.getSumOfEveryPublicInvestmentExpenseCategory();
        };
        summary.forEach((code, amount) -> {
            String desc = switch (type) {
                case "Εθνικό" -> PublicInvestmentBudgetNationalExpense.getDescriptionWithCode(code);
                case "Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedExpense.getDescriptionWithCode(code);
                default -> PublicInvestmentBudgetExpense.getDescriptionWithCode(code);
            };
            pibSummaryData.add(new EntitiesView.AnalysisResult(code, desc, amount));
        });
        updatePibStats();
    }

    private void applyRegularFilters() {
        String text = regularSearchField.getText().toLowerCase();
        String entity = regularEntityFilter.getValue();
        List<RegularBudgetExpense> filtered = RegularBudgetExpense.getAllRegularBudgetExpenses().stream().filter(e -> e.getCode().toLowerCase().contains(text) || e.getDescription().toLowerCase().contains(text)).filter(e -> entity.equals("Όλοι οι φορείς") || e.getEntityCode().equals(entity.split(" - ")[0])).collect(Collectors.toList());
        regularData.setAll(filtered);
        updateRegularStats();
    }

    private void applyPibFilters() {
        String text = pibSearchField.getText().toLowerCase();
        String entity = pibEntityFilter.getValue();
        List<BudgetExpense> base = getBasePibList(pibTypeFilter.getValue());
        List<BudgetExpense> filtered = base.stream().filter(e -> e.getCode().toLowerCase().contains(text) || e.getDescription().toLowerCase().contains(text)).filter(e -> entity.equals("Όλοι οι φορείς") || e.getEntityCode().equals(entity.split(" - ")[0])).collect(Collectors.toList());
        pibData.setAll(filtered);
        updatePibStats();
    }

    private List<BudgetExpense> getBasePibList(String type) {
        return switch (type) {
            case "Εθνικό" -> PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().stream().map(e -> (BudgetExpense)e).toList();
            case "Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().stream().map(e -> (BudgetExpense)e).toList();
            default -> PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().stream().map(e -> (BudgetExpense)e).toList();
        };
    }

    private void updateRegularStats() {
        long total = (regularViewCombo.getValue().equals("Συγκεντρωτική Προβολή") ? regularSummaryData.stream().mapToLong(EntitiesView.AnalysisResult::getAmount) : regularData.stream().mapToLong(RegularBudgetExpense::getAmount)).sum();
        regularCountLabel.setText((regularViewCombo.getValue().equals("Συγκεντρωτική Προβολή") ? regularSummaryData.size() : regularData.size()) + " εγγραφές");
        regularTotalLabel.setText("Σύνολο: " + Theme.formatAmount(total));
    }

    private void updatePibStats() {
        long total = (pibViewCombo.getValue().equals("Συγκεντρωτική Προβολή") ? pibSummaryData.stream().mapToLong(EntitiesView.AnalysisResult::getAmount) : pibData.stream().mapToLong(BudgetExpense::getAmount)).sum();
        pibCountLabel.setText((pibViewCombo.getValue().equals("Συγκεντρωτική Προβολή") ? pibSummaryData.size() : pibData.size()) + " εγγραφές");
        pibTotalLabel.setText("Σύνολο: " + Theme.formatAmount(total));
    }

    public Region getView() {
        return scrollPane;
    }
}