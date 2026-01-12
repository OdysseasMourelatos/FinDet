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
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Tab pane
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

    private VBox createHeader() {
        VBox header = new VBox(6);
        header.setPadding(new Insets(32, 24, 20, 24));

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        // Expense icon
        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.ERROR, 0.15));

        Label iconText = new Label("-");
        iconText.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.ERROR_LIGHT + ";"
        );

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
        container.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Filter section
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        regularSearchField = new TextField();
        regularSearchField.setPromptText("Αναζήτηση...");
        regularSearchField.setPrefWidth(220);
        regularSearchField.setStyle(Theme.textField());

        regularSearchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            regularSearchField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });

        VBox entityBox = new VBox(4);
        Label entityLabel = new Label("Φορέας");
        entityLabel.setStyle(Theme.mutedText());

        regularEntityFilter = new ComboBox<>();
        regularEntityFilter.getItems().add("Όλοι οι φορείς");
        for (Entity entity : Entity.getEntities()) {
            regularEntityFilter.getItems().add(entity.getEntityCode() + " - " + entity.getEntityName());
        }
        regularEntityFilter.setValue("Όλοι οι φορείς");
        regularEntityFilter.setStyle(Theme.comboBox());
        regularEntityFilter.setPrefWidth(240);

        entityBox.getChildren().addAll(entityLabel, regularEntityFilter);

        VBox levelBox = new VBox(4);
        Label levelLabel = new Label("Επίπεδο");
        levelLabel.setStyle(Theme.mutedText());

        regularLevelFilter = new ComboBox<>();
        regularLevelFilter.getItems().addAll("Όλες οι πιστώσεις", "Πιστώσεις κατά κατηγορία δαπάνης");
        regularLevelFilter.setValue("Πιστώσεις κατά κατηγορία δαπάνης");
        regularLevelFilter.setStyle(Theme.comboBox());
        regularLevelFilter.setPrefWidth(140);

        levelBox.getChildren().addAll(levelLabel, regularLevelFilter);

        filterRow.getChildren().addAll(regularSearchField, entityBox, levelBox);

        // Stats bar
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 1 0;"
        );

        regularCountLabel = new Label();
        regularCountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        regularTotalLabel = new Label();
        regularTotalLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + Theme.ERROR_LIGHT + ";");

        statsBar.getChildren().addAll(regularCountLabel, spacer, regularTotalLabel);

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
        table.setStyle(Theme.table());

        TableColumn<RegularBudgetExpense, String> entityCodeCol = new TableColumn<>("Φορέας");
        entityCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entityCodeCol.setPrefWidth(80);

        TableColumn<RegularBudgetExpense, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(180);

        TableColumn<RegularBudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(90);

        TableColumn<RegularBudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(300);

        TableColumn<RegularBudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(120);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(entityCodeCol, serviceCol, codeCol, descCol, amountCol);

        Label placeholder = new Label("Δεν βρέθηκαν εγγραφές");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + "; -fx-font-size: 14px;");
        table.setPlaceholder(placeholder);

        return table;
    }

    private void applyRegularFilters() {
        String searchText = regularSearchField.getText();
        String selectedEntity = regularEntityFilter.getValue();
        String selectedLevel = regularLevelFilter.getValue();

        List<RegularBudgetExpense> filtered = RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
                filter(e -> matchesSearch(e, searchText)).
                filter(e -> matchesEntity(e, selectedEntity)).
                filter(e -> matchesLevel(e, selectedLevel)).
                collect(Collectors.toList());

        regularData.setAll(filtered);
        updateRegularStats();
    }

    private void updateRegularStats() {
        long total = regularData.stream().mapToLong(RegularBudgetExpense::getAmount).sum();
        regularCountLabel.setText(regularData.size() + " εγγραφές");
        regularTotalLabel.setText("Σύνολο: " + Theme.formatAmount(total));
    }

    private VBox createPublicInvestmentExpensesContent() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Filter section
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(16, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        pibSearchField = new TextField();
        pibSearchField.setPromptText("Αναζήτηση...");
        pibSearchField.setPrefWidth(200);
        pibSearchField.setStyle(Theme.textField());

        pibSearchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            pibSearchField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });

        VBox entityBox = new VBox(4);
        Label entityLabel = new Label("Φορέας");
        entityLabel.setStyle(Theme.mutedText());

        pibEntityFilter = new ComboBox<>();
        pibEntityFilter.getItems().add("Όλοι οι φορείς");
        for (Entity entity : Entity.getEntities()) {
            pibEntityFilter.getItems().add(entity.getEntityCode() + " - " + entity.getEntityName());
        }
        pibEntityFilter.setValue("Όλοι οι φορείς");
        pibEntityFilter.setStyle(Theme.comboBox());
        pibEntityFilter.setPrefWidth(220);

        entityBox.getChildren().addAll(entityLabel, pibEntityFilter);

        VBox typeBox = new VBox(4);
        Label typeLabel = new Label("Τύπος");
        typeLabel.setStyle(Theme.mutedText());

        pibTypeFilter = new ComboBox<>();
        pibTypeFilter.getItems().addAll("Όλοι οι τύποι", "Εθνικό", "Συγχρηματοδοτούμενο");
        pibTypeFilter.setValue("Όλοι οι τύποι");
        pibTypeFilter.setStyle(Theme.comboBox());
        pibTypeFilter.setPrefWidth(160);

        typeBox.getChildren().addAll(typeLabel, pibTypeFilter);

        VBox levelBox = new VBox(4);
        Label levelLabel = new Label("Επίπεδο");
        levelLabel.setStyle(Theme.mutedText());

        pibLevelFilter = new ComboBox<>();
        pibLevelFilter.getItems().addAll("Όλα τα επίπεδα", "Επίπεδο 1", "Επίπεδο 2", "Επίπεδο 3", "Επίπεδο 4", "Επίπεδο 5");
        pibLevelFilter.setValue("Όλα τα επίπεδα");
        pibLevelFilter.setStyle(Theme.comboBox());
        pibLevelFilter.setPrefWidth(140);

        levelBox.getChildren().addAll(levelLabel, pibLevelFilter);

        filterRow.getChildren().addAll(pibSearchField, entityBox, typeBox, levelBox);

        // Stats bar
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 1 0;"
        );

        pibCountLabel = new Label();
        pibCountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        pibTotalLabel = new Label();
        pibTotalLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + Theme.ERROR_LIGHT + ";");

        statsBar.getChildren().addAll(pibCountLabel, spacer, pibTotalLabel);

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
        table.setStyle(Theme.table());

        TableColumn<BudgetExpense, String> typeCol = new TableColumn<>("Τύπος");
        typeCol.setCellValueFactory(d -> {
            if (d.getValue() instanceof PublicInvestmentBudgetExpense pib) {
                return new SimpleStringProperty(pib.getType());
            }
            return new SimpleStringProperty("-");
        });
        typeCol.setPrefWidth(90);

        TableColumn<BudgetExpense, String> entityCodeCol = new TableColumn<>("Φορέας");
        entityCodeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getEntityCode()));
        entityCodeCol.setPrefWidth(80);

        TableColumn<BudgetExpense, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(160);

        TableColumn<BudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(90);

        TableColumn<BudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(260);

        TableColumn<BudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(120);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(typeCol, entityCodeCol, serviceCol, codeCol, descCol, amountCol);

        Label placeholder = new Label("Δεν βρέθηκαν εγγραφές");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + "; -fx-font-size: 14px;");
        table.setPlaceholder(placeholder);

        return table;
    }

    private void applyPibFilters() {
        String searchText = pibSearchField.getText();
        String selectedEntity = pibEntityFilter.getValue();
        String selectedType = pibTypeFilter.getValue();
        String selectedLevel = pibLevelFilter.getValue();

        List<BudgetExpense> baseList = getBasePibList(selectedType);

        List<BudgetExpense> filtered = baseList.stream().
                filter(e -> matchesSearch(e, searchText)).
                filter(e -> matchesEntity(e, selectedEntity)).
                filter(e -> matchesLevel(e, selectedLevel)).
                collect(Collectors.toList());

        pibData.setAll(filtered);
        updatePibStats();
    }

    private List<BudgetExpense> getBasePibList(String type) {
        return switch (type) {
            case "Εθνικό" -> PublicInvestmentBudgetNationalExpense.getAllPublicInvestmentBudgetNationalExpenses().
                    stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
            case "Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedExpense.
                    getAllPublicInvestmentBudgetCoFundedExpenses().
                    stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
            default -> PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().
                    stream().map(e -> (BudgetExpense) e).collect(Collectors.toList());
        };
    }

    private void updatePibStats() {
        long total = pibData.stream().mapToLong(BudgetExpense::getAmount).sum();
        pibCountLabel.setText(pibData.size() + " εγγραφές");
        pibTotalLabel.setText("Σύνολο: " + Theme.formatAmount(total));
    }

    private boolean matchesSearch(BudgetExpense e, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        String lower = searchText.toLowerCase();
        return e.getCode().toLowerCase().contains(lower) ||
               e.getDescription().toLowerCase().contains(lower) ||
               e.getEntityName().toLowerCase().contains(lower) ||
               e.getServiceName().toLowerCase().contains(lower);
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

    public Region getView() {
        return scrollPane;
    }
}
