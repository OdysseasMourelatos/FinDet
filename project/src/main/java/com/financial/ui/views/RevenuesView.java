package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
 * Revenues view - displays budget revenues in a table with filtering options.
 */
public class RevenuesView {

    // Design constants
    private static final String BG_PRIMARY = "#0a0a0f";
    private static final String BG_SECONDARY = "#12121a";
    private static final String TEXT_PRIMARY = "#e4e4e7";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String BORDER_COLOR = "#27272a";

    private final VBox view;
    private TableView<BudgetRevenue> table;
    private ObservableList<BudgetRevenue> tableData;
    private TextField searchField;
    private ComboBox<String> levelFilter;
    private ComboBox<String> budgetTypeFilter;
    private Label countLabel;
    private Label totalLabel;

    public RevenuesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Header section
        VBox header = createHeader();

        // Filter section
        HBox filterSection = createFilterSection();

        // Stats bar
        HBox statsBar = createStatsBar();

        // Table
        tableData = FXCollections.observableArrayList();
        table = createTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(table);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        loadData();
        updateStats();

        // Add listeners
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        levelFilter.setOnAction(e -> applyFilters());
        budgetTypeFilter.setOnAction(e -> applyFilters());

        view.getChildren().addAll(header, filterSection, statsBar, tableContainer);
    }

    private VBox createHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(32, 24, 24, 24));

        Label title = new Label("Έσοδα Προϋπολογισμού");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label subtitle = new Label("Διαχείριση και προβολή εσόδων κρατικού προϋπολογισμού");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox createFilterSection() {
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(0, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Αναζήτηση...");
        searchField.setPrefWidth(240);
        searchField.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 6;" +
            "-fx-padding: 8 12;"
        );

        // Level filter
        levelFilter = new ComboBox<>();
        levelFilter.getItems().addAll(
            "Όλα τα επίπεδα",
            "Επίπεδο 1",
            "Επίπεδο 2",
            "Επίπεδο 3",
            "Επίπεδο 4",
            "Επίπεδο 5"
        );
        levelFilter.setValue("Όλα τα επίπεδα");
        styleComboBox(levelFilter);
        levelFilter.setPrefWidth(160);

        // Budget type filter
        budgetTypeFilter = new ComboBox<>();
        budgetTypeFilter.getItems().addAll(
            "Όλοι οι τύποι",
            "Τακτικός",
            "ΠΔΕ",
            "ΠΔΕ Εθνικό",
            "ΠΔΕ Συγχρ/νο"
        );
        budgetTypeFilter.setValue("Όλοι οι τύποι");
        styleComboBox(budgetTypeFilter);
        budgetTypeFilter.setPrefWidth(160);

        filterRow.getChildren().addAll(searchField, levelFilter, budgetTypeFilter);
        return filterRow;
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

    private HBox createStatsBar() {
        HBox statsBar = new HBox(24);
        statsBar.setPadding(new Insets(12, 24, 12, 24));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1 0;");

        countLabel = new Label();
        countLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        totalLabel = new Label();
        totalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        statsBar.getChildren().addAll(countLabel, totalLabel);
        return statsBar;
    }

    private TableView<BudgetRevenue> createTable() {
        TableView<BudgetRevenue> tableView = new TableView<>();
        tableView.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 8;"
        );

        TableColumn<BudgetRevenue, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        codeCol.setPrefWidth(90);
        codeCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<BudgetRevenue, String> levelCol = new TableColumn<>("Επ.");
        levelCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLevelOfHierarchy())));
        levelCol.setPrefWidth(45);
        levelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<BudgetRevenue, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        descCol.setPrefWidth(380);

        TableColumn<BudgetRevenue, String> regularCol = new TableColumn<>("Τακτικός");
        regularCol.setCellValueFactory(data -> {
            BudgetRevenue r = data.getValue();
            if (r instanceof RegularBudgetRevenue) {
                return new SimpleStringProperty(formatAmount(r.getAmount()));
            }
            return new SimpleStringProperty(formatAmount(r.getRegularAmount()));
        });
        regularCol.setPrefWidth(110);
        regularCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BudgetRevenue, String> investmentCol = new TableColumn<>("ΠΔΕ");
        investmentCol.setCellValueFactory(data -> {
            BudgetRevenue r = data.getValue();
            if (r instanceof PublicInvestmentBudgetNationalRevenue || r instanceof PublicInvestmentBudgetCoFundedRevenue) {
                return new SimpleStringProperty(formatAmount(r.getAmount()));
            }
            return new SimpleStringProperty(formatAmount(r.getPublicInvestmentAmount()));
        });
        investmentCol.setPrefWidth(110);
        investmentCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BudgetRevenue, String> totalCol = new TableColumn<>("Σύνολο");
        totalCol.setCellValueFactory(data -> new SimpleStringProperty(formatAmount(data.getValue().getAmount())));
        totalCol.setPrefWidth(120);
        totalCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().addAll(codeCol, levelCol, descCol, regularCol, investmentCol, totalCol);
        tableView.setItems(tableData);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
    }

    private void loadData() {
        tableData.clear();
        tableData.addAll(BudgetRevenue.getAllBudgetRevenues());
    }

    private void applyFilters() {
        String searchText = searchField.getText();
        String selectedLevel = levelFilter.getValue();
        String selectedType = budgetTypeFilter.getValue();

        List<? extends BudgetRevenue> baseList = getBaseListByType(selectedType);

        List<BudgetRevenue> filtered = baseList.stream().filter(r -> matchesSearchText(r, searchText)).filter(r -> matchesLevel(r, selectedLevel)).collect(Collectors.toList());

        tableData.setAll(filtered);
        updateStats();
    }

    private List<? extends BudgetRevenue> getBaseListByType(String type) {
        return switch (type) {
            case "Τακτικός" -> RegularBudgetRevenue.getAllRegularBudgetRevenues();
            case "ΠΔΕ" -> PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues();
            case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalRevenue.getAllPublicInvestmentBudgetNationalRevenues();
            case "ΠΔΕ Συγχρ/νο" -> PublicInvestmentBudgetCoFundedRevenue.getAllPublicInvestmentBudgetCoFundedRevenues();
            default -> BudgetRevenue.getAllBudgetRevenues();
        };
    }

    private boolean matchesSearchText(BudgetRevenue r, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        String lowerSearch = searchText.toLowerCase();
        return r.getCode().toLowerCase().contains(lowerSearch) || r.getDescription().toLowerCase().contains(lowerSearch);
    }

    private boolean matchesLevel(BudgetRevenue r, String selectedLevel) {
        if (selectedLevel == null || selectedLevel.equals("Όλα τα επίπεδα")) {
            return true;
        }
        int level = r.getLevelOfHierarchy();
        return switch (selectedLevel) {
            case "Επίπεδο 1" -> level == 1;
            case "Επίπεδο 2" -> level == 2;
            case "Επίπεδο 3" -> level == 3;
            case "Επίπεδο 4" -> level == 4;
            case "Επίπεδο 5" -> level == 5;
            default -> true;
        };
    }

    private void updateStats() {
        long total = tableData.stream().mapToLong(BudgetRevenue::getAmount).sum();
        countLabel.setText(tableData.size() + " εγγραφές");
        totalLabel.setText("Σύνολο: " + formatAmount(total));
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
