package com.financial.ui.views;

import com.financial.entries.*;
import com.financial.ui.Theme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Entities view - displays government entities/ministries with clean Apple-like design.
 */
public class EntitiesView {

    private final ScrollPane scrollPane;
    private final VBox view;
    private TableView<Entity> table;
    private ObservableList<Entity> tableData;
    private TextField searchField;
    private Label countLabel;
    private Label totalLabel;
    private ComboBox<Entity> entitySelector;
    private Button moreInfoButton;
    private VBox detailsSection;
    private ComboBox<String> budgetTypeFilter;
    private ComboBox<String> analysisLevelFilter;
    private TableView<AnalysisResult> analysisTable;
    private ObservableList<AnalysisResult> analysisData;

    public EntitiesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        detailsSection = createDetailsSection();

        VBox header = createHeader();
        HBox searchSection = createSearchSection();

        HBox statsBar = createStatsBar();
        tableData = FXCollections.observableArrayList();
        table = createTable();

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(table);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);


        loadData();
        updateStats();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData(newVal));

        view.getChildren().addAll(header, searchSection, detailsSection, statsBar, tableContainer);

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

        // Entity icon
        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.INFO, 0.15));

        Label iconText = new Label("*");
        iconText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.INFO + ";"
        );

        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Φορείς Κράτους");
        title.setStyle(Theme.pageTitle());

        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Υπουργεία και Δημόσιοι Οργανισμοί της Ελληνικής Δημοκρατίας");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private HBox createSearchSection() {
        HBox section = new HBox(15);
        section.setPadding(new Insets(0, 24, 16, 24));
        section.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Αναζήτηση φορέα...");
        searchField.setPrefWidth(220);
        searchField.setStyle(Theme.textField());

        entitySelector = new ComboBox<>(FXCollections.observableArrayList(Entity.getEntities()));
        entitySelector.setPromptText("Επιλογή Φορέα");
        entitySelector.setPrefWidth(280);
        entitySelector.setStyle(Theme.comboBox());

        entitySelector.setCellFactory(lv -> new ListCell<Entity>() {
            @Override
            protected void updateItem(Entity item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getEntityCode() + " - " + item.getEntityName());
            }
        });
        entitySelector.setButtonCell(entitySelector.getCellFactory().call(null));

        moreInfoButton = new Button("Περισσότερα");
        moreInfoButton.setStyle(Theme.buttonPrimary());
        moreInfoButton.setDisable(true);

        moreInfoButton.setOnAction(e -> {
            boolean isVisible = detailsSection.isVisible();
            detailsSection.setVisible(!isVisible);
            detailsSection.setManaged(!isVisible);
            moreInfoButton.setText(isVisible ? "Περισσότερα" : "Απόκρυψη");
        });

        entitySelector.setOnAction(e -> {
            Entity selected = entitySelector.getValue();
            if (selected != null) {
                filterData(selected.getEntityCode());
                moreInfoButton.setDisable(false);
            } else {
                moreInfoButton.setDisable(true);
            }
        });

        Button resetButton = new Button("✖");
        resetButton.setStyle(Theme.buttonSecondary());
        resetButton.setOnAction(e -> {
            entitySelector.setValue(null);
            searchField.clear();
            detailsSection.setVisible(false);
            detailsSection.setManaged(false);
            moreInfoButton.setText("Περισσότερα");
            moreInfoButton.setDisable(true);
            loadData();
            updateStats();
        });

        section.getChildren().addAll(searchField, new Label("ή"), entitySelector, resetButton, moreInfoButton);
        return section;
    }

    private HBox createStatsBar() {
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 1 0;"
        );

        countLabel = new Label();
        countLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        totalLabel = new Label();
        totalLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";"
        );

        statsBar.getChildren().addAll(countLabel, spacer, totalLabel);
        return statsBar;
    }

    private TableView<Entity> createTable() {
        TableView<Entity> tableView = new TableView<>();
        tableView.setStyle(Theme.table());

        TableColumn<Entity, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEntityCode()));
        codeCol.setPrefWidth(80);

        TableColumn<Entity, String> nameCol = new TableColumn<>("Ονομασία Φορέα");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEntityName()));
        nameCol.setPrefWidth(320);

        TableColumn<Entity, String> regularExpenseCol = new TableColumn<>("Τακτικά Έξοδα");
        regularExpenseCol.setCellValueFactory(data -> {
            long total = data.getValue().calculateRegularSum();
            return new SimpleStringProperty(Theme.formatAmount(total));
        });
        regularExpenseCol.setPrefWidth(130);
        regularExpenseCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<Entity, String> investmentExpenseCol = new TableColumn<>("Έξοδα ΠΔΕ");
        investmentExpenseCol.setCellValueFactory(data -> {
            long total = data.getValue().calculatePublicInvestmentSum();
            return new SimpleStringProperty(Theme.formatAmount(total));
        });
        investmentExpenseCol.setPrefWidth(130);
        investmentExpenseCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<Entity, String> totalCol = new TableColumn<>("Σύνολο");
        totalCol.setCellValueFactory(data -> {
            long total = data.getValue().calculateTotalSum();
            return new SimpleStringProperty(Theme.formatAmount(total));
        });
        totalCol.setPrefWidth(140);
        totalCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().addAll(codeCol, nameCol, regularExpenseCol, investmentExpenseCol, totalCol);
        tableView.setItems(tableData);

        Label placeholder = new Label("Δεν βρέθηκαν φορείς");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + "; -fx-font-size: 14px;");
        tableView.setPlaceholder(placeholder);

        return tableView;
    }

    private void loadData() {
        tableData.clear();
        tableData.addAll(Entity.getEntities());
    }

    private void filterData(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableData.setAll(Entity.getEntities());
        } else {
            String lowerSearch = searchText.toLowerCase();
            tableData.setAll(Entity.getEntities().stream().
                    filter(e -> e.getEntityCode().toLowerCase().contains(lowerSearch)
                            || e.getEntityName().toLowerCase().contains(lowerSearch)).
                    toList());
        }
        updateStats();
    }

    private void updateStats() {
        long total = tableData.stream().mapToLong(Entity::calculateTotalSum).sum();
        countLabel.setText(tableData.size() + " φορείς");
        totalLabel.setText("Συνολικά Έξοδα: " + Theme.formatAmount(total));
    }

    private VBox createDetailsSection() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(16, 24, 16, 24));
        container.setStyle("-fx-background-color: " + Theme.BG_SURFACE + "; -fx-border-color: " + Theme.BORDER_MUTED + "; -fx-border-width: 0 0 1 0;");
        container.setVisible(false);
        container.setManaged(false);

        Label title = new Label("Παράμετροι Προβολής");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");

        HBox filterRow = new HBox(20);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        budgetTypeFilter = new ComboBox<>();
        budgetTypeFilter.getItems().addAll("Κρατικός", "Τακτικός", "ΠΔΕ", "ΠΔΕ Εθνικό", "ΠΔΕ Συγχρηματοδοτούμενο");
        budgetTypeFilter.setValue("Κρατικός");
        budgetTypeFilter.setStyle(Theme.comboBox());

        analysisLevelFilter = new ComboBox<>();
        analysisLevelFilter.getItems().addAll(
                "Πιστώσεις κατά μείζονα κατηγορία δαπάνης",
                "Πιστώσεις κατά ειδικό φορέα",
                "Πιστώσεις κατά μείζονα κατηγορία δαπάνης και ειδικό φορέα"
        );
        analysisLevelFilter.getSelectionModel().selectFirst();
        analysisLevelFilter.setStyle(Theme.comboBox());
        analysisLevelFilter.setPrefWidth(350);

        Button confirmBtn = new Button("Προβολή");
        confirmBtn.setStyle(Theme.buttonPrimary());
        confirmBtn.setOnAction(e -> {
            Entity selectedEntity = entitySelector.getValue();
            String budgetType = budgetTypeFilter.getValue();
            String analysisLevel = analysisLevelFilter.getValue();

            if (selectedEntity != null && budgetType != null && analysisLevel != null) {
                processAnalysis(selectedEntity, budgetType, analysisLevel);
            } else {
                System.out.println("Παρακαλώ συμπληρώστε όλα τα φίλτρα.");
            }
        });

        analysisData = FXCollections.observableArrayList();
        analysisTable = new TableView<>(analysisData);
        analysisTable.setStyle(Theme.table());

        analysisTable.setMinHeight(450);
        VBox.setVgrow(analysisTable, Priority.ALWAYS);

        analysisTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<AnalysisResult, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setMaxWidth(1500);

        TableColumn<AnalysisResult, String> descCol = new TableColumn<>("Ονομασία");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setMaxWidth(6000);

        TableColumn<AnalysisResult, String> amountCol = new TableColumn<>("Ποσό (€)");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");
        amountCol.setMaxWidth(2500);

        analysisTable.getColumns().addAll(codeCol, descCol, amountCol);

        filterRow.getChildren().addAll(new Label("Τύπος:"), budgetTypeFilter, new Label("Επίπεδο:"), analysisLevelFilter, confirmBtn);
        container.getChildren().addAll(title, filterRow, analysisTable);
        return container;
    }

    private void processAnalysis(Entity entity, String budgetType, String level) {
        switch (level) {
            case "Πιστώσεις κατά μείζονα κατηγορία δαπάνης":
                handleMajorCategoryAnalysis(entity, budgetType);
                break;

            case "Πιστώσεις κατά ειδικό φορέα":
                handleSpecialServiceAnalysis(entity, budgetType);
                break;

            case "Πιστώσεις κατά μείζονα κατηγορία δαπάνης και ειδικό φορέα":
                handleCombinedAnalysis(entity, budgetType);
                break;
        }
    }

    private void handleMajorCategoryAnalysis(Entity entity, String budgetType) {
        analysisData.clear();
        Map<String, Long> results;

        results = switch (budgetType) {
            case "Κρατικός" -> entity.getTotalSumOfEveryExpenseCategory();
            case "Τακτικός" -> entity.getRegularSumOfEveryExpenseCategory();
            case "ΠΔΕ" -> entity.getPublicInvestmentSumOfEveryExpenseCategory();
            case "ΠΔΕ Εθνικό" -> entity.getPublicInvestmentNationalSumOfEveryExpenseCategory();
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> entity.getPublicInvestmentCoFundedSumOfEveryExpenseCategory();
            default -> new HashMap<>();
        };

        for (Map.Entry<String, Long> entry : results.entrySet()) {
            String code = entry.getKey();
            String description = BudgetExpense.getDescriptionWithCode(code);
            analysisData.add(new AnalysisResult(code, description, entry.getValue()));
        }
        analysisTable.getColumns().get(0).setText("Kωδικός Δαπάνης");
        analysisTable.getColumns().get(1).setText("Ονομασία Δαπάνης");
    }

    private void handleSpecialServiceAnalysis(Entity entity, String budgetType) {
        analysisData.clear();
        Map<String, Long> results;

        results = switch (budgetType) {
            case "Κρατικός" -> entity.getTotalSumOfEveryService();
            case "Τακτικός" -> entity.getRegularSumOfEveryService();
            case "ΠΔΕ" -> entity.getPublicInvestmentSumOfEveryService();
            case "ΠΔΕ Εθνικό" -> entity.getPublicInvestmentNationalSumOfEveryService();
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> entity.getPublicInvestmentCoFundedSumOfEveryService();
            default -> new HashMap<>();
        };

        for (Map.Entry<String, Long> entry : results.entrySet()) {
            String serviceCode = entry.getKey();
            String serviceName = entity.getServiceNameWithCode(serviceCode);
            analysisData.add(new AnalysisResult(serviceCode, serviceName, entry.getValue()));
        }
        analysisTable.getColumns().get(0).setText("Kωδικός Ειδικού Φορέα");
        analysisTable.getColumns().get(1).setText("Ονομασία Ειδικού Φορέα");
    }

    private void handleCombinedAnalysis(Entity entity, String budgetType) {
        analysisData.clear();

        analysisTable.getColumns().get(0).setText("Κωδικός");
        analysisTable.getColumns().get(1).setText("Ονομασία");

        switch (budgetType) {
            case "Τακτικός" -> {
                for (String sCode : entity.getAllRegularServiceCodes()) {
                    String sName = entity.getRegularServiceNameWithCode(sCode);
                    analysisData.add(new AnalysisResult(sCode, sName, entity.getRegularSumOfServiceWithCode(sCode)));

                    ArrayList<BudgetExpense> expenses = entity.getRegularExpensesOfServiceWithCode(sCode);
                    for (BudgetExpense exp : expenses) {
                        analysisData.add(new AnalysisResult(exp.getCode(), "   • " + exp.getDescription(), exp.getAmount()));
                    }
                }
            }
            case "ΠΔΕ Εθνικό" -> {
                for (String sCode : entity.getAllPublicInvestmentNationalServiceCodes()) {
                    String sName = entity.getPublicInvestmentNationalServiceNameWithCode(sCode);
                    analysisData.add(new AnalysisResult(sCode, sName, entity.getPublicInvestmentNationalSumOfServiceWithCode(sCode)));

                    ArrayList<BudgetExpense> expenses = entity.getPublicInvestmentNationalExpensesOfServiceWithCode(sCode);
                    for (BudgetExpense exp : expenses) {
                        analysisData.add(new AnalysisResult(exp.getCode(), "   • " + exp.getDescription(), exp.getAmount()));
                    }
                }
            }
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> {
                for (String sCode : entity.getAllPublicInvestmentCoFundedServiceCodes()) {
                    String sName = entity.getPublicInvestmentCoFundedServiceNameWithCode(sCode);
                    analysisData.add(new AnalysisResult(sCode, sName, entity.getPublicInvestmentCoFundedSumOfServiceWithCode(sCode)));

                    ArrayList<BudgetExpense> expenses = entity.getPublicInvestmentCoFundedExpensesOfServiceWithCode(sCode);
                    for (BudgetExpense exp : expenses) {
                        analysisData.add(new AnalysisResult(exp.getCode(), "   • " + exp.getDescription(), exp.getAmount()));
                    }
                }
            }
        }
    }

    public Region getView() {
        return scrollPane;
    }

    public static class AnalysisResult {
        private final String code;
        private final String description;
        private final long amount;

        public AnalysisResult(String code, String description, long amount) {
            this.code = code;
            this.description = description;
            this.amount = amount;
        }

        public String getCode() {
            return code;
        }
        public String getDescription() {
            return description;
        }
        public long getAmount() {
            return amount;
        }
    }
}
