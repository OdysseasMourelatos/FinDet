package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetExpense;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Charts view - displays budget visualizations.
 */
public class ChartsView {

    private final VBox view;
    private final VBox chartContainer;

    public ChartsView() {
        view = new VBox(20);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        Label title = new Label("Γραφήματα & Οπτικοποιήσεις");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        FlowPane buttonPane = new FlowPane(15, 15);
        buttonPane.setAlignment(Pos.CENTER_LEFT);

        Button revenuesPieBtn = createChartButton("Κατανομή Εσόδων", "#a6e3a1");
        Button expensesPieBtn = createChartButton("Κατανομή Εξόδων", "#f38ba8");
        Button comparisonBarBtn = createChartButton("Σύγκριση Εσόδων-Εξόδων", "#89b4fa");
        Button ministryBarBtn = createChartButton("Έξοδα ανά Υπουργείο", "#fab387");

        buttonPane.getChildren().addAll(
            revenuesPieBtn, expensesPieBtn, comparisonBarBtn, ministryBarBtn);

        chartContainer = new VBox();
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");
        chartContainer.setPadding(new Insets(40));
        VBox.setVgrow(chartContainer, Priority.ALWAYS);

        showPlaceholder();

        revenuesPieBtn.setOnAction(e -> showRevenueBreakdown());
        expensesPieBtn.setOnAction(e -> showExpenseBreakdown());
        comparisonBarBtn.setOnAction(e -> showComparison());
        ministryBarBtn.setOnAction(e -> showMinistryExpenses());

        view.getChildren().addAll(title, buttonPane, chartContainer);
    }

    private Button createChartButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1e1e2e; "
            + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 20; "
            + "-fx-background-radius: 8; -fx-cursor: hand;");

        button.setOnMouseEntered(e ->
            button.setStyle("-fx-background-color: derive(" + color + ", -20%); "
                + "-fx-text-fill: #1e1e2e; -fx-font-size: 14px; -fx-font-weight: bold; "
                + "-fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        button.setOnMouseExited(e ->
            button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1e1e2e; "
                + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 12 20; "
                + "-fx-background-radius: 8; -fx-cursor: hand;"));

        return button;
    }

    private void showPlaceholder() {
        chartContainer.getChildren().clear();

        Label placeholder = new Label("Επιλέξτε ένα γράφημα για προβολή");
        placeholder.setStyle("-fx-font-size: 18px; -fx-text-fill: #6c7086;");

        Label hint = new Label("Τα γραφήματα θα εμφανιστούν εδώ");
        hint.setStyle("-fx-font-size: 14px; -fx-text-fill: #45475a;");

        chartContainer.getChildren().addAll(placeholder, hint);
    }

    private void showRevenueBreakdown() {
        chartContainer.getChildren().clear();

        Label title = new Label("Κατανομή Εσόδων Προϋπολογισμού");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        VBox dataDisplay = new VBox(10);
        dataDisplay.setAlignment(Pos.CENTER_LEFT);
        dataDisplay.setPadding(new Insets(20));

        BudgetRevenue.getAllBudgetRevenues().stream().filter(r -> r.getCode().length() == 2).
            limit(10).forEach(r -> {
                HBox row = createDataRow(r.getDescription(), r.getAmount(), "#a6e3a1");
                dataDisplay.getChildren().add(row);
            });

        if (dataDisplay.getChildren().isEmpty()) {
            Label noData = new Label("Δεν υπάρχουν δεδομένα. Φορτώστε CSV αρχεία.");
            noData.setStyle("-fx-text-fill: #6c7086;");
            dataDisplay.getChildren().add(noData);
        }

        chartContainer.getChildren().addAll(title, dataDisplay);
    }

    private void showExpenseBreakdown() {
        chartContainer.getChildren().clear();

        Label title = new Label("Κατανομή Εξόδων Προϋπολογισμού");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        VBox dataDisplay = new VBox(10);
        dataDisplay.setAlignment(Pos.CENTER_LEFT);
        dataDisplay.setPadding(new Insets(20));

        RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
            collect(Collectors.groupingBy(RegularBudgetExpense::getEntityName,
                Collectors.summingLong(RegularBudgetExpense::getAmount))).
            entrySet().stream().
            sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).
            limit(10).forEach(entry -> {
                HBox row = createDataRow(entry.getKey(), entry.getValue(), "#f38ba8");
                dataDisplay.getChildren().add(row);
            });

        if (dataDisplay.getChildren().isEmpty()) {
            Label noData = new Label("Δεν υπάρχουν δεδομένα. Φορτώστε CSV αρχεία.");
            noData.setStyle("-fx-text-fill: #6c7086;");
            dataDisplay.getChildren().add(noData);
        }

        chartContainer.getChildren().addAll(title, dataDisplay);
    }

    private void showComparison() {
        chartContainer.getChildren().clear();

        Label title = new Label("Σύγκριση Εσόδων - Εξόδων");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        long totalRevenue = BudgetRevenue.getAllBudgetRevenues().stream().
            filter(r -> r.getCode().length() == 2).
            mapToLong(BudgetRevenue::getAmount).sum();

        long totalExpense = RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
            mapToLong(RegularBudgetExpense::getAmount).sum();

        VBox dataDisplay = new VBox(15);
        dataDisplay.setAlignment(Pos.CENTER);
        dataDisplay.setPadding(new Insets(30));

        HBox revenueRow = createDataRow("Συνολικά Έσοδα", totalRevenue, "#a6e3a1");
        HBox expenseRow = createDataRow("Συνολικά Έξοδα", totalExpense, "#f38ba8");

        long balance = totalRevenue - totalExpense;
        String balanceColor = balance >= 0 ? "#a6e3a1" : "#f38ba8";
        HBox balanceRow = createDataRow(balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα",
            Math.abs(balance), balanceColor);

        dataDisplay.getChildren().addAll(revenueRow, expenseRow, balanceRow);
        chartContainer.getChildren().addAll(title, dataDisplay);
    }

    private void showMinistryExpenses() {
        chartContainer.getChildren().clear();

        Label title = new Label("Έξοδα ανά Υπουργείο");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        VBox dataDisplay = new VBox(10);
        dataDisplay.setAlignment(Pos.CENTER_LEFT);
        dataDisplay.setPadding(new Insets(20));

        RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
            collect(Collectors.groupingBy(RegularBudgetExpense::getEntityName,
                Collectors.summingLong(RegularBudgetExpense::getAmount))).
            entrySet().stream().
            sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).
            limit(15).forEach(entry -> {
                HBox row = createDataRow(entry.getKey(), entry.getValue(), "#fab387");
                dataDisplay.getChildren().add(row);
            });

        if (dataDisplay.getChildren().isEmpty()) {
            Label noData = new Label("Δεν υπάρχουν δεδομένα. Φορτώστε CSV αρχεία.");
            noData.setStyle("-fx-text-fill: #6c7086;");
            dataDisplay.getChildren().add(noData);
        }

        chartContainer.getChildren().addAll(title, dataDisplay);
    }

    private HBox createDataRow(String label, long amount, String color) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Region indicator = new Region();
        indicator.setMinSize(12, 12);
        indicator.setMaxSize(12, 12);
        indicator.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 3;");

        String displayLabel = label.length() > 50 ? label.substring(0, 47) + "..." : label;
        Label nameLabel = new Label(displayLabel);
        nameLabel.setStyle("-fx-text-fill: #cdd6f4; -fx-font-size: 14px;");
        nameLabel.setMinWidth(350);

        Label amountLabel = new Label(String.format("%,d €", amount));
        amountLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px; "
            + "-fx-font-weight: bold;");

        row.getChildren().addAll(indicator, nameLabel, amountLabel);
        return row;
    }

    public Region getView() {
        return view;
    }
}
