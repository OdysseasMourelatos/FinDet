package com.financial.ui.views;

import com.financial.entries.Entity;
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

    public EntitiesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Search section
        HBox searchSection = createSearchSection();

        // Stats bar
        HBox statsBar = createStatsBar();

        // Table
        tableData = FXCollections.observableArrayList();
        table = createTable();

        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 24, 24, 24));
        tableContainer.getChildren().add(table);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        VBox.setVgrow(table, Priority.ALWAYS);

        loadData();
        updateStats();

        // Listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData(newVal));

        view.getChildren().addAll(header, searchSection, statsBar, tableContainer);

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
        HBox section = new HBox(12);
        section.setPadding(new Insets(0, 24, 16, 24));
        section.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Αναζήτηση φορέα με κωδικό ή όνομα...");
        searchField.setPrefWidth(360);
        searchField.setStyle(Theme.textField());

        searchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            searchField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });

        section.getChildren().add(searchField);
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

    public Region getView() {
        return scrollPane;
    }
}
