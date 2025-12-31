package com.financial.ui.views;

import com.financial.entries.BudgetExpense;
import com.financial.entries.Entity;
import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.PublicInvestmentBudgetNationalExpense;
import com.financial.entries.PublicInvestmentBudgetCoFundedExpense;
import com.financial.entries.RegularBudgetExpense;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Expenses view - displays budget expenses with filtering options.
 */
public class ExpensesView {

    // Design constants
    private static final String BG_PRIMARY = "#0a0a0f";
    private static final String BG_SECONDARY = "#12121a";
    private static final String TEXT_PRIMARY = "#e4e4e7";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String BORDER_COLOR = "#27272a";

    private final VBox view;
    private TabPane tabPane;

    // Regular budget components
    private TextField regularSearchField;
    private ComboBox<String> regularEntityFilter;
    private ComboBox<String> regularLevelFilter;
    private TableView<RegularBudgetExpense> regularTable;
    private ObservableList<RegularBudgetExpense> regularData;
    private Label regularCountLabel;
    private Label regularTotalLabel;

    // Public Investment components
    private TextField pibSearchField;
    private ComboBox<String> pibEntityFilter;
    private ComboBox<String> pibTypeFilter;
    private ComboBox<String> pibLevelFilter;
    private TableView<BudgetExpense> pibTable;
    private ObservableList<BudgetExpense> pibData;
    private Label pibCountLabel;
    private Label pibTotalLabel;

    public ExpensesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Header
        VBox header = createHeader();

        // Tab pane
        tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: " + BG_PRIMARY + ";");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Tab regularTab = new Tab("Τακτικός Προϋπολογισμός");
        regularTab.setClosable(false);
        regularTab.setContent(createRegularExpensesContent());

        Tab investmentTab = new Tab("Πρόγραμμα Δημοσίων Επενδύσεων");
        investmentTab.setClosable(false);
        investmentTab.setContent(createPublicInvestmentExpensesContent());

        tabPane.getTabs().addAll(regularTab, investmentTab);

        view.getChildren().addAll(header, tabPane);
    }

    private VBox createHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(32, 24, 24, 24));

        Label title = new Label("Έξοδα Προϋπολογισμού");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label subtitle = new Label("Διαχείριση και προβολή εξόδων κρατικού προϋπολογισμού");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private VBox createRegularExpensesContent() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Filter section
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        regularSearchField = new TextField();
        regularSearchField.setPromptText("Αναζήτηση...");
        regularSearchField.setPrefWidth(200);
        styleTextField(regularSearchField);

        regularEntityFilter = new ComboBox<>();
        regularEntityFilter.getItems().add("Όλοι οι φορείς");
        for (Entity entity : Entity.getEntities()) {
            regularEntityFilter.getItems().add(entity.getEntityCode() + " - " + entity.getEntityName());
        }
        regularEntityFilter.setValue("Όλοι οι φορείς");
        styleComboBox(regularEntityFilter);
        regularEntityFilter.setPrefWidth(220);

        regularLevelFilter = new ComboBox<>();
        regularLevelFilter.getItems().addAll("Όλα τα επίπεδα", "Επίπεδο 1", "Επίπεδο 2", "Επίπεδο 3", "Επίπεδο 4", "Επίπεδο 5");
        regularLevelFilter.setValue("Όλα τα επίπεδα");
        styleComboBox(regularLevelFilter);
        regularLevelFilter.setPrefWidth(140);

        filterRow.getChildren().addAll(regularSearchField, regularEntityFilter, regularLevelFilter);

        // Stats bar
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1 0;");

        regularCountLabel = new Label();
        regularCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        regularTotalLabel = new Label();
        regularTotalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        statsBar.getChildren().addAll(regularCountLabel, regularTotalLabel);

        // Table
        regularData = FXCollections.observableArrayList(RegularBudgetExpense.getAllRegularBudgetExpenses());
        regularTable = createRegularTable();

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(regularTable);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        VBox.setVgrow(regularTable, Priority.ALWAYS);

        updateRegularStats();

        // Listeners
        regularSearchField.textProperty().addListener((obs, oldVal, newVal) -> applyRegularFilters());
        regularEntityFilter.setOnAction(e -> applyRegularFilters());
        regularLevelFilter.setOnAction(e -> applyRegularFilters());

        container.getChildren().addAll(filterRow, statsBar, tableContainer);
        VBox.setVgrow(container, Priority.ALWAYS);
        return container;
    }

    private TableView<RegularBudgetExpense> createRegularTable() {
        TableView<RegularBudgetExpense> table = new TableView<>(regularData);
        table.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");

        TableColumn<RegularBudgetExpense, String> entityCodeCol = new TableColumn<>("Φορέας");
        entityCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entityCodeCol.setPrefWidth(70);

        TableColumn<RegularBudgetExpense, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(160);

        TableColumn<RegularBudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(80);

        TableColumn<RegularBudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(280);

        TableColumn<RegularBudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(100);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(entityCodeCol, serviceCol, codeCol, descCol, amountCol);

        return table;
    }

    private void applyRegularFilters() {
        String searchText = regularSearchField.getText();
        String selectedEntity = regularEntityFilter.getValue();
        String selectedLevel = regularLevelFilter.getValue();

        List<RegularBudgetExpense> filtered = RegularBudgetExpense.getAllRegularBudgetExpenses().stream().filter(e -> matchesSearch(e, searchText)).filter(e -> matchesEntity(e, selectedEntity)).filter(e -> matchesLevel(e, selectedLevel)).collect(Collectors.toList());

        regularData.setAll(filtered);
        updateRegularStats();
    }

    private void updateRegularStats() {
        long total = regularData.stream().mapToLong(RegularBudgetExpense::getAmount).sum();
        regularCountLabel.setText(regularData.size() + " εγγραφές");
        regularTotalLabel.setText("Σύνολο: " + formatAmount(total));
    }

    private VBox createPublicInvestmentExpensesContent() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Filter section
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        pibSearchField = new TextField();
        pibSearchField.setPromptText("Αναζήτηση...");
        pibSearchField.setPrefWidth(200);
        styleTextField(pibSearchField);

        pibEntityFilter = new ComboBox<>();
        pibEntityFilter.getItems().add("Όλοι οι φορείς");
        for (Entity entity : Entity.getEntities()) {
            pibEntityFilter.getItems().add(entity.getEntityCode() + " - " + entity.getEntityName());
        }
        pibEntityFilter.setValue("Όλοι οι φορείς");
        styleComboBox(pibEntityFilter);
        pibEntityFilter.setPrefWidth(220);

        pibTypeFilter = new ComboBox<>();
        pibTypeFilter.getItems().addAll("Όλοι οι τύποι", "Εθνικό", "Συγχρηματοδοτούμενο");
        pibTypeFilter.setValue("Όλοι οι τύποι");
        styleComboBox(pibTypeFilter);
        pibTypeFilter.setPrefWidth(160);

        pibLevelFilter = new ComboBox<>();
        pibLevelFilter.getItems().addAll("Όλα τα επίπεδα", "Επίπεδο 1", "Επίπεδο 2", "Επίπεδο 3", "Επίπεδο 4", "Επίπεδο 5");
        pibLevelFilter.setValue("Όλα τα επίπεδα");
        styleComboBox(pibLevelFilter);
        pibLevelFilter.setPrefWidth(140);

        filterRow.getChildren().addAll(pibSearchField, pibEntityFilter, pibTypeFilter, pibLevelFilter);

        // Stats bar
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1 0;");

        pibCountLabel = new Label();
        pibCountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        pibTotalLabel = new Label();
        pibTotalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        statsBar.getChildren().addAll(pibCountLabel, pibTotalLabel);

        // Table
        pibData = FXCollections.observableArrayList();
        loadPibData();
        pibTable = createPibTable();

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(pibTable);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        VBox.setVgrow(pibTable, Priority.ALWAYS);

        updatePibStats();

        // Listeners
        pibSearchField.textProperty().addListener((obs, oldVal, newVal) -> applyPibFilters());
        pibEntityFilter.setOnAction(e -> applyPibFilters());
        pibTypeFilter.setOnAction(e -> applyPibFilters());
        pibLevelFilter.setOnAction(e -> applyPibFilters());

        container.getChildren().addAll(filterRow, statsBar, tableContainer);
        VBox.setVgrow(container, Priority.ALWAYS);
        return container;
    }

    private void loadPibData() {
        pibData.clear();
        pibData.addAll(PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses());
    }

    private TableView<BudgetExpense> createPibTable() {
        TableView<BudgetExpense> table = new TableView<>(pibData);
        table.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8;");

        TableColumn<BudgetExpense, String> typeCol = new TableColumn<>("Τύπος");
        typeCol.setCellValueFactory(d -> {
            if (d.getValue() instanceof PublicInvestmentBudgetExpense pib) {
                return new SimpleStringProperty(pib.getType());
            }
            return new SimpleStringProperty("-");
        });
        typeCol.setPrefWidth(80);

        TableColumn<BudgetExpense, String> entityCodeCol = new TableColumn<>("Φορέας");
        entityCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entityCodeCol.setPrefWidth(70);

        TableColumn<BudgetExpense, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(140);

        TableColumn<BudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(80);

        TableColumn<BudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(240);

        TableColumn<BudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(100);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(typeCol, entityCodeCol, serviceCol, codeCol, descCol, amountCol);

        return table;
    }

    private void applyPibFilters() {
        String searchText = pibSearchField.getText();
        String selectedEntity = pibEntityFilter.getValue();
        String selectedType = pibTypeFilter.getValue();
        String selectedLevel = pibLevelFilter.getValue();

        List<BudgetExpense> baseList = getBasePibList(selectedType);

        List<BudgetExpense> filtered = baseList.stream().filter(e -> matchesSearch(e, searchText)).filter(e -> matchesEntity(e, selectedEntity)).filter(e -> matchesLevel(e, selectedLevel)).collect(Collectors.toList());

        pibData.setAll(filtered);
        updatePibStats();
    }

    private List<BudgetExpense> getBasePibList(String type) {
        return switch (type) {
            case "Εθνικό" -> PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
            case "Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedExpense.getAllPublicInvestmentBudgetCoFundedExpenses().stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
            default -> PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
        };
    }

    private void updatePibStats() {
        long total = pibData.stream().mapToLong(BudgetExpense::getAmount).sum();
        pibCountLabel.setText(pibData.size() + " εγγραφές");
        pibTotalLabel.setText("Σύνολο: " + formatAmount(total));
    }

    private void styleTextField(TextField field) {
        field.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 6;" +
            "-fx-padding: 8 12;"
        );
    }

    private void styleComboBox(ComboBox<String> combo) {
        combo.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 6;"
        );
    }

    private boolean matchesSearch(BudgetExpense e, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        String lower = searchText.toLowerCase();
        return e.getCode().toLowerCase().contains(lower) || e.getDescription().toLowerCase().contains(lower) || e.getEntityName().toLowerCase().contains(lower) || e.getServiceName().toLowerCase().contains(lower);
    }

    private boolean matchesEntity(BudgetExpense e, String selectedEntity) {
        if (selectedEntity == null || selectedEntity.equals("Όλοι οι φορείς")) {
            return true;
        }
        String entityCode = selectedEntity.split(" - ")[0];
        return e.getEntityCode().equals(entityCode);
    }

    private boolean matchesLevel(BudgetExpense e, String selectedLevel) {
        if (selectedLevel == null || selectedLevel.equals("Όλα τα επίπεδα")) {
            return true;
        }
        int level = getLevelFromCode(e.getCode());
        return switch (selectedLevel) {
            case "Επίπεδο 1" -> level == 1;
            case "Επίπεδο 2" -> level == 2;
            case "Επίπεδο 3" -> level == 3;
            case "Επίπεδο 4" -> level == 4;
            case "Επίπεδο 5" -> level == 5;
            default -> true;
        };
    }

    private int getLevelFromCode(String code) {
        return switch (code.length()) {
            case 2 -> 1;
            case 3 -> 2;
            case 5 -> 3;
            case 7 -> 4;
            case 10 -> 5;
            default -> 0;
        };
    }

    private String formatAmount(long amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.2f δισ.", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.2f εκ.", amount / 1_000_000.0);
        }
        return String.format("%,d €", amount);
    }

    public Region getView() {
        return view;
    }
}
