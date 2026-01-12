package com.financial.ui.views;

import com.financial.entries.*;
import com.financial.ui.Theme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Revenues view - displays budget revenues with Apple-like clean design.
 */
public class RevenuesView {

    private final ScrollPane scrollPane;
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
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

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

        // Wrap in scroll pane for consistency
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

        // Revenue icon
        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.SUCCESS, 0.15));

        Label iconText = new Label("+");
        iconText.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.SUCCESS_LIGHT + ";"
        );

        javafx.scene.layout.StackPane iconContainer = new javafx.scene.layout.StackPane(icon, iconText);

        Label title = new Label("Έσοδα Προϋπολογισμού");
        title.setStyle(Theme.pageTitle());

        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Διαχείριση και προβολή εσόδων κρατικού προϋπολογισμού");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private HBox createFilterSection() {
        HBox filterRow = new HBox(12);
        filterRow.setPadding(new Insets(0, 24, 16, 24));
        filterRow.setAlignment(Pos.CENTER_LEFT);

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Αναζήτηση κωδικού ή περιγραφής...");
        searchField.setPrefWidth(280);
        searchField.setStyle(Theme.textField());

        // Focus styling
        searchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (isFocused) {
                searchField.setStyle(Theme.textFieldFocused());
            } else {
                searchField.setStyle(Theme.textField());
            }
        });

        // Level filter
        VBox levelBox = new VBox(4);
        Label levelLabel = new Label("Επίπεδο");
        levelLabel.setStyle(Theme.mutedText());

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
        levelFilter.setStyle(Theme.comboBox());
        levelFilter.setPrefWidth(140);

        levelBox.getChildren().addAll(levelLabel, levelFilter);

        // Budget type filter
        VBox typeBox = new VBox(4);
        Label typeLabel = new Label("Τύπος");
        typeLabel.setStyle(Theme.mutedText());

        budgetTypeFilter = new ComboBox<>();
        budgetTypeFilter.getItems().addAll(
            "Κρατικός",
            "Τακτικός",
            "ΠΔΕ",
            "ΠΔΕ Εθνικό",
            "ΠΔΕ Συγχρ/νο"
        );
        budgetTypeFilter.setValue("Κρατικός");
        budgetTypeFilter.setStyle(Theme.comboBox());
        budgetTypeFilter.setPrefWidth(140);

        typeBox.getChildren().addAll(typeLabel, budgetTypeFilter);

        filterRow.getChildren().addAll(searchField, levelBox, typeBox);
        return filterRow;
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
            "-fx-text-fill: " + Theme.SUCCESS_LIGHT + ";"
        );

        statsBar.getChildren().addAll(countLabel, spacer, totalLabel);
        return statsBar;
    }

    private TableView<BudgetRevenue> createTable() {
        TableView<BudgetRevenue> tableView = new TableView<>();
        tableView.setStyle(Theme.table());

        TableColumn<BudgetRevenue, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        codeCol.setPrefWidth(100);
        codeCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<BudgetRevenue, String> levelCol = new TableColumn<>("Επ.");
        levelCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLevelOfHierarchy())));
        levelCol.setPrefWidth(50);
        levelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<BudgetRevenue, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        descCol.setPrefWidth(400);

        TableColumn<BudgetRevenue, String> regularCol = new TableColumn<>("Τακτικός");
        regularCol.setCellValueFactory(data -> {
            BudgetRevenue r = data.getValue();
            if (r instanceof RegularBudgetRevenue) {
                return new SimpleStringProperty(formatAmount(r.getAmount()));
            }
            return new SimpleStringProperty(formatAmount(r.getRegularAmount()));
        });
        regularCol.setPrefWidth(120);
        regularCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BudgetRevenue, String> investmentCol = new TableColumn<>("ΠΔΕ");
        investmentCol.setCellValueFactory(data -> {
            BudgetRevenue r = data.getValue();
            if (r instanceof PublicInvestmentBudgetRevenue || r instanceof PublicInvestmentBudgetNationalRevenue || r instanceof PublicInvestmentBudgetCoFundedRevenue) {
                return new SimpleStringProperty(formatAmount(r.getAmount()));
            }
            return new SimpleStringProperty(formatAmount(r.getPublicInvestmentAmount()));
        });
        investmentCol.setPrefWidth(120);
        investmentCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BudgetRevenue, String> totalCol = new TableColumn<>("Σύνολο");
        totalCol.setCellValueFactory(data -> new SimpleStringProperty(formatAmount(data.getValue().getAmount())));
        totalCol.setPrefWidth(130);
        totalCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().addAll(codeCol, levelCol, descCol, regularCol, investmentCol, totalCol);
        tableView.setItems(tableData);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Empty state placeholder
        Label placeholder = new Label("Δεν βρέθηκαν εγγραφές");
        placeholder.setStyle(
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";" +
            "-fx-font-size: 14px;"
        );
        tableView.setPlaceholder(placeholder);

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

        List<BudgetRevenue> filtered = baseList.stream().
                filter(r -> matchesSearchText(r, searchText)).
                filter(r -> matchesLevel(r, selectedLevel)).
                collect(Collectors.toList());

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
        return r.getCode().toLowerCase().contains(lowerSearch) ||
               r.getDescription().toLowerCase().contains(lowerSearch);
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
        String type = budgetTypeFilter.getValue();
        long total = BudgetRevenue.calculateSum();
        countLabel.setText(tableData.size() + " εγγραφές");
        totalLabel.setText("Σύνολο: " + Theme.formatAmount(total));
    }

    private String formatAmount(long amount) {
        return Theme.formatAmount(amount);
    }

    public Region getView() {
        return scrollPane;
    }
}
