package com.financial.ui.views;

import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.RegularBudgetExpense;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Expenses view - displays budget expenses in a table.
 */
public class ExpensesView {

    private final VBox view;

    public ExpensesView() {
        view = new VBox(20);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        Label title = new Label("Έξοδα Προϋπολογισμού");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #1e1e2e;");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Tab regularTab = new Tab("Τακτικός Προϋπολογισμός");
        regularTab.setClosable(false);
        regularTab.setContent(createRegularExpensesTable());

        Tab investmentTab = new Tab("Πρόγραμμα Δημοσίων Επενδύσεων");
        investmentTab.setClosable(false);
        investmentTab.setContent(createPublicInvestmentExpensesTable());

        tabPane.getTabs().addAll(regularTab, investmentTab);

        view.getChildren().addAll(title, tabPane);
    }

    private VBox createRegularExpensesTable() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        ObservableList<RegularBudgetExpense> data = FXCollections.observableArrayList(
            RegularBudgetExpense.getAllRegularBudgetExpenses());

        TableView<RegularBudgetExpense> table = new TableView<>(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<RegularBudgetExpense, String> entityCol = new TableColumn<>("Φορέας");
        entityCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getEntityName()));
        entityCol.setPrefWidth(200);

        TableColumn<RegularBudgetExpense, String> serviceCol = new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(200);

        TableColumn<RegularBudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(100);

        TableColumn<RegularBudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(250);

        TableColumn<RegularBudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d ->
            new SimpleStringProperty(String.format("%,d €", d.getValue().getAmount())));
        amountCol.setPrefWidth(120);

        table.getColumns().addAll(entityCol, serviceCol, codeCol, descCol, amountCol);

        Label countLabel = new Label("Σύνολο εγγραφών: " + data.size());
        countLabel.setStyle("-fx-text-fill: #6c7086;");

        container.getChildren().addAll(table, countLabel);
        return container;
    }

    private VBox createPublicInvestmentExpensesTable() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(15));

        ObservableList<PublicInvestmentBudgetExpense> data = FXCollections.observableArrayList(
            PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses());

        TableView<PublicInvestmentBudgetExpense> table = new TableView<>(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<PublicInvestmentBudgetExpense, String> entityCol = new TableColumn<>("Φορέας");
        entityCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getEntityName()));
        entityCol.setPrefWidth(200);

        TableColumn<PublicInvestmentBudgetExpense, String> serviceCol =
            new TableColumn<>("Υπηρεσία");
        serviceCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getServiceName()));
        serviceCol.setPrefWidth(200);

        TableColumn<PublicInvestmentBudgetExpense, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(100);

        TableColumn<PublicInvestmentBudgetExpense, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(250);

        TableColumn<PublicInvestmentBudgetExpense, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d ->
            new SimpleStringProperty(String.format("%,d €", d.getValue().getAmount())));
        amountCol.setPrefWidth(120);

        table.getColumns().addAll(entityCol, serviceCol, codeCol, descCol, amountCol);

        Label countLabel = new Label("Σύνολο εγγραφών: " + data.size());
        countLabel.setStyle("-fx-text-fill: #6c7086;");

        container.getChildren().addAll(table, countLabel);
        return container;
    }

    public Region getView() {
        return view;
    }
}
