package com.financial.ui.views;

import com.financial.entries.Entity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Entities view - displays government entities/ministries.
 */
public class EntitiesView {

    private final VBox view;
    private final TableView<Entity> table;
    private final ObservableList<Entity> tableData;

    public EntitiesView() {
        view = new VBox(20);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        Label title = new Label("Φορείς Κράτους");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        Label subtitle = new Label("Υπουργεία και Δημόσιοι Οργανισμοί");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c7086;");

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση φορέα...");
        searchField.setMaxWidth(400);
        searchField.setStyle("-fx-background-color: #1e1e2e; -fx-text-fill: #cdd6f4; "
            + "-fx-prompt-text-fill: #6c7086; -fx-background-radius: 8; -fx-padding: 10;");

        tableData = FXCollections.observableArrayList();
        table = createTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        loadData();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData(newVal));

        view.getChildren().addAll(title, subtitle, searchField, table);
    }

    private TableView<Entity> createTable() {
        TableView<Entity> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");

        TableColumn<Entity, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEntityCode()));
        codeCol.setPrefWidth(100);

        TableColumn<Entity, String> nameCol = new TableColumn<>("Ονομασία Φορέα");
        nameCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getEntityName()));
        nameCol.setPrefWidth(400);

        TableColumn<Entity, String> regularExpenseCol = new TableColumn<>("Τακτικά Έξοδα");
        regularExpenseCol.setCellValueFactory(data -> {
            long total = data.getValue().getRegularBudgetExpenses().stream().
                filter(e -> e.getCode().charAt(0) <= '3').mapToLong(e -> e.getAmount()).sum();
            return new SimpleStringProperty(String.format("%,d €", total));
        });
        regularExpenseCol.setPrefWidth(150);

        TableColumn<Entity, String> investmentExpenseCol = new TableColumn<>("Έξοδα ΠΔΕ");
        investmentExpenseCol.setCellValueFactory(data -> {
            long total = data.getValue().getPublicInvestmentExpenses().stream().
                filter(e -> e.getCode().charAt(0) <= '3').mapToLong(e -> e.getAmount()).sum();
            return new SimpleStringProperty(String.format("%,d €", total));
        });
        investmentExpenseCol.setPrefWidth(150);

        TableColumn<Entity, String> totalCol = new TableColumn<>("Σύνολο");
        totalCol.setCellValueFactory(data -> {
            long regular = data.getValue().getRegularBudgetExpenses().stream().
                filter(e -> e.getCode().charAt(0) <= '3').mapToLong(e -> e.getAmount()).sum();
            long investment = data.getValue().getPublicInvestmentExpenses().stream().
                filter(e -> e.getCode().charAt(0) <= '3').mapToLong(e -> e.getAmount()).sum();
            return new SimpleStringProperty(String.format("%,d €", regular + investment));
        });
        totalCol.setPrefWidth(150);

        tableView.getColumns().addAll(codeCol, nameCol, regularExpenseCol,
            investmentExpenseCol, totalCol);
        tableView.setItems(tableData);

        return tableView;
    }

    private void loadData() {
        tableData.clear();
        tableData.addAll(Entity.getEntities());
    }

    private void filterData(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableData.setAll(Entity.getEntities());
            return;
        }

        String lowerSearch = searchText.toLowerCase();
        tableData.setAll(Entity.getEntities().stream().
            filter(e -> e.getEntityCode().toLowerCase().contains(lowerSearch)
                || e.getEntityName().toLowerCase().contains(lowerSearch)).toList());
    }

    public Region getView() {
        return view;
    }
}
