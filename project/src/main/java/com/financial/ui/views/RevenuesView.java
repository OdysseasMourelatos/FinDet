package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
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
 * Revenues view - displays budget revenues in a table.
 */
public class RevenuesView {

    private final VBox view;
    private final TableView<BudgetRevenue> table;
    private final ObservableList<BudgetRevenue> tableData;

    public RevenuesView() {
        view = new VBox(20);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        Label title = new Label("Έσοδα Προϋπολογισμού");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        TextField searchField = new TextField();
        searchField.setPromptText("Αναζήτηση με κωδικό ή περιγραφή...");
        searchField.setMaxWidth(400);
        searchField.setStyle("-fx-background-color: #1e1e2e; -fx-text-fill: #cdd6f4; "
            + "-fx-prompt-text-fill: #6c7086; -fx-background-radius: 8; -fx-padding: 10;");

        tableData = FXCollections.observableArrayList();
        table = createTable();
        VBox.setVgrow(table, Priority.ALWAYS);

        loadData();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterData(newVal));

        view.getChildren().addAll(title, searchField, table);
    }

    private TableView<BudgetRevenue> createTable() {
        TableView<BudgetRevenue> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");

        TableColumn<BudgetRevenue, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        codeCol.setPrefWidth(100);

        TableColumn<BudgetRevenue, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getDescription()));
        descCol.setPrefWidth(400);

        TableColumn<BudgetRevenue, String> regularCol = new TableColumn<>("Τακτικός");
        regularCol.setCellValueFactory(data ->
            new SimpleStringProperty(formatAmount(data.getValue().getRegularAmount())));
        regularCol.setPrefWidth(150);

        TableColumn<BudgetRevenue, String> investmentCol = new TableColumn<>("ΠΔΕ");
        investmentCol.setCellValueFactory(data ->
            new SimpleStringProperty(formatAmount(data.getValue().getPublicInvestmentAmount())));
        investmentCol.setPrefWidth(150);

        TableColumn<BudgetRevenue, String> totalCol = new TableColumn<>("Σύνολο");
        totalCol.setCellValueFactory(data -> {
            long total = data.getValue().getRegularAmount()
                + data.getValue().getPublicInvestmentAmount();
            return new SimpleStringProperty(formatAmount(total));
        });
        totalCol.setPrefWidth(150);

        tableView.getColumns().addAll(codeCol, descCol, regularCol, investmentCol, totalCol);
        tableView.setItems(tableData);

        return tableView;
    }

    private void loadData() {
        tableData.clear();
        tableData.addAll(BudgetRevenue.getAllBudgetRevenues());
    }

    private void filterData(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            tableData.setAll(BudgetRevenue.getAllBudgetRevenues());
            return;
        }

        String lowerSearch = searchText.toLowerCase();
        tableData.setAll(BudgetRevenue.getAllBudgetRevenues().stream().
            filter(r -> r.getCode().toLowerCase().contains(lowerSearch)
                || r.getDescription().toLowerCase().contains(lowerSearch)).toList());
    }

    private String formatAmount(long amount) {
        return String.format("%,d €", amount);
    }

    public Region getView() {
        return view;
    }
}
