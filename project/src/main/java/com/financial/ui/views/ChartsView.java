package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.RegularBudgetExpense;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Charts view - displays budget visualizations with clean Apple-style design.
 */
public class ChartsView {

    private final VBox view;
    private final VBox chartContainer;
    private final ScrollPane scrollPane;
    private Label totalAmountLabel;
    private Label totalDescLabel;

    private static final String[] PIE_COLORS = {
        "#34c759", "#ff3b30", "#007aff", "#ff9500", "#af52de",
        "#ffcc00", "#5ac8fa", "#ff2d55", "#5856d6", "#64d2ff",
        "#30d158", "#ff6961", "#a2845e", "#8e8e93", "#636366"
    };

    public ChartsView() {
        view = new VBox(24);
        view.setPadding(new Insets(32));
        view.setStyle("-fx-background-color: #000000;");

        Label title = new Label("Γραφήματα");
        title.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        FlowPane buttonPane = new FlowPane(12, 12);
        buttonPane.setAlignment(Pos.CENTER_LEFT);

        Button revenuesPieBtn = createChartButton("Έσοδα", "#34c759");
        Button expensesPieBtn = createChartButton("Έξοδα", "#ff3b30");
        Button comparisonBarBtn = createChartButton("Σύγκριση", "#007aff");
        Button ministryBarBtn = createChartButton("Υπουργεία", "#ff9500");

        buttonPane.getChildren().addAll(
            revenuesPieBtn, expensesPieBtn, comparisonBarBtn, ministryBarBtn);

        chartContainer = new VBox(24);
        chartContainer.setAlignment(Pos.TOP_CENTER);
        chartContainer.setStyle("-fx-background-color: #1c1c1e; -fx-background-radius: 16;");
        chartContainer.setPadding(new Insets(32));

        scrollPane = new ScrollPane(chartContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #000000; -fx-background-color: #000000; "
            + "-fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        showPlaceholder();

        revenuesPieBtn.setOnAction(e -> showRevenueBreakdown());
        expensesPieBtn.setOnAction(e -> showExpenseBreakdown());
        comparisonBarBtn.setOnAction(e -> showComparison());
        ministryBarBtn.setOnAction(e -> showMinistryExpenses());

        view.getChildren().addAll(title, buttonPane, scrollPane);
    }

    private Button createChartButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #2c2c2e; -fx-text-fill: " + color + "; "
            + "-fx-font-size: 15px; -fx-font-weight: 600; -fx-padding: 10 20; "
            + "-fx-background-radius: 20; -fx-cursor: hand;");

        button.setOnMouseEntered(e ->
            button.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: " + color + "; "
                + "-fx-font-size: 15px; -fx-font-weight: 600; -fx-padding: 10 20; "
                + "-fx-background-radius: 20; -fx-cursor: hand;"));
        button.setOnMouseExited(e ->
            button.setStyle("-fx-background-color: #2c2c2e; -fx-text-fill: " + color + "; "
                + "-fx-font-size: 15px; -fx-font-weight: 600; -fx-padding: 10 20; "
                + "-fx-background-radius: 20; -fx-cursor: hand;"));

        return button;
    }

    private void showPlaceholder() {
        chartContainer.getChildren().clear();

        VBox placeholderBox = new VBox(12);
        placeholderBox.setAlignment(Pos.CENTER);
        placeholderBox.setPadding(new Insets(80));

        Label placeholder = new Label("Επιλέξτε γράφημα");
        placeholder.setStyle("-fx-font-size: 20px; -fx-font-weight: 500; -fx-text-fill: #8e8e93;");

        placeholderBox.getChildren().add(placeholder);

        FadeTransition fade = new FadeTransition(Duration.millis(400), placeholderBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        chartContainer.getChildren().add(placeholderBox);
    }

    private VBox createChartWithTotal(ObservableList<PieChart.Data> data, long total,
                                      String accentColor, String totalLabel) {
        PieChart pieChart = new PieChart(data);
        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(false);
        pieChart.setStartAngle(90);
        pieChart.setMinSize(320, 320);
        pieChart.setPrefSize(320, 320);
        pieChart.setMaxSize(320, 320);
        pieChart.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        VBox totalBox = new VBox(4);
        totalBox.setAlignment(Pos.CENTER);
        totalBox.setPadding(new Insets(20, 0, 0, 0));

        totalAmountLabel = new Label(formatAmount(total));
        totalAmountLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; "
            + "-fx-text-fill: #ffffff;");

        totalDescLabel = new Label(totalLabel);
        totalDescLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 500; "
            + "-fx-text-fill: #8e8e93;");

        totalBox.getChildren().addAll(totalAmountLabel, totalDescLabel);

        VBox chartBox = new VBox(0);
        chartBox.setAlignment(Pos.CENTER);
        chartBox.getChildren().addAll(pieChart, totalBox);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), chartBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        return chartBox;
    }

    private String formatAmount(long amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.1f δισ. €", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.1f εκ. €", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("%.1f χιλ. €", amount / 1_000.0);
        }
        return String.format("%,d €", amount);
    }

    private void applyInteractiveEffects(PieChart pieChart, List<LegendItem> legendItems,
                                         long total, String defaultLabel) {
        int colorIndex = 0;
        for (PieChart.Data data : pieChart.getData()) {
            String color = PIE_COLORS[colorIndex % PIE_COLORS.length];
            String itemName = legendItems.get(colorIndex).name;
            long itemAmount = legendItems.get(colorIndex).amount;
            double percentage = (itemAmount * 100.0) / total;

            data.getNode().setStyle("-fx-pie-color: " + color + ";");

            DropShadow glow = new DropShadow();
            glow.setColor(Color.web(color, 0.6));
            glow.setRadius(15);
            glow.setSpread(0.2);

            String tooltipText = String.format("%s\n%s  •  %.1f%%", itemName,
                formatAmount(itemAmount), percentage);
            Tooltip tooltip = new Tooltip(tooltipText);
            tooltip.setShowDelay(Duration.millis(100));
            tooltip.setStyle("-fx-font-size: 13px; -fx-font-family: 'SF Pro Display'; "
                + "-fx-background-color: rgba(60,60,60,0.95); "
                + "-fx-text-fill: #ffffff; -fx-padding: 10; -fx-background-radius: 8;");
            Tooltip.install(data.getNode(), tooltip);

            final int idx = colorIndex;
            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setEffect(glow);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), data.getNode());
                scale.setToX(1.03);
                scale.setToY(1.03);
                scale.play();
                totalAmountLabel.setText(formatAmount(legendItems.get(idx).amount));
                totalDescLabel.setText(truncate(legendItems.get(idx).name, 30));
            });

            data.getNode().setOnMouseExited(e -> {
                data.getNode().setEffect(null);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), data.getNode());
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
                totalAmountLabel.setText(formatAmount(total));
                totalDescLabel.setText(defaultLabel);
            });

            colorIndex++;
        }
    }

    private String truncate(String text, int maxLength) {
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }

    private VBox createLegend(List<LegendItem> items, long total, PieChart pieChart) {
        VBox legend = new VBox(2);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(8, 0, 0, 0));
        legend.setMaxWidth(480);

        for (int i = 0; i < items.size(); i++) {
            LegendItem item = items.get(i);
            String color = PIE_COLORS[i % PIE_COLORS.length];
            double percentage = (item.amount * 100.0) / total;

            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;");

            Region colorDot = new Region();
            colorDot.setMinSize(10, 10);
            colorDot.setMaxSize(10, 10);
            colorDot.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

            String displayName = truncate(item.name, 32);
            Label nameLabel = new Label(displayName);
            nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 15px;");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            nameLabel.setMaxWidth(Double.MAX_VALUE);

            Label percentLabel = new Label(String.format("%.1f%%", percentage));
            percentLabel.setStyle("-fx-text-fill: #8e8e93; -fx-font-size: 15px;");
            percentLabel.setMinWidth(55);

            Label amountLabel = new Label(formatAmount(item.amount));
            amountLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 15px; "
                + "-fx-font-weight: 600;");
            amountLabel.setMinWidth(100);
            amountLabel.setAlignment(Pos.CENTER_RIGHT);

            final int idx = i;
            row.setOnMouseEntered(e -> {
                row.setStyle("-fx-background-color: #2c2c2e; -fx-background-radius: 10;");
                if (pieChart != null && idx < pieChart.getData().size()) {
                    PieChart.Data slice = pieChart.getData().get(idx);
                    DropShadow glow = new DropShadow();
                    glow.setColor(Color.web(PIE_COLORS[idx % PIE_COLORS.length], 0.6));
                    glow.setRadius(15);
                    slice.getNode().setEffect(glow);
                }
            });
            row.setOnMouseExited(e -> {
                row.setStyle("-fx-background-color: transparent; -fx-background-radius: 10;");
                if (pieChart != null && idx < pieChart.getData().size()) {
                    pieChart.getData().get(idx).getNode().setEffect(null);
                }
            });

            row.getChildren().addAll(colorDot, nameLabel, percentLabel, amountLabel);
            legend.getChildren().add(row);
        }

        return legend;
    }

    private void showRevenueBreakdown() {
        chartContainer.getChildren().clear();

        Label title = createSectionTitle("Κατανομή Εσόδων");

        List<BudgetRevenue> revenues = BudgetRevenue.getAllBudgetRevenues().stream().
            filter(r -> r.getCode().length() == 2 && r.getCode().charAt(0) <= '3').
            collect(Collectors.toList());

        if (revenues.isEmpty()) {
            showNoDataMessage();
            return;
        }

        long total = revenues.stream().mapToLong(BudgetRevenue::getAmount).sum();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        List<LegendItem> legendItems = new ArrayList<>();

        for (BudgetRevenue r : revenues) {
            pieData.add(new PieChart.Data(r.getDescription(), r.getAmount()));
            legendItems.add(new LegendItem(r.getDescription(), r.getAmount()));
        }

        VBox chartBox = createChartWithTotal(pieData, total, "#34c759", "Συνολικά Έσοδα");
        PieChart pieChart = (PieChart) chartBox.getChildren().get(0);
        applyInteractiveEffects(pieChart, legendItems, total, "Συνολικά Έσοδα");

        VBox legendBox = createLegend(legendItems, total, pieChart);

        chartContainer.getChildren().addAll(title, chartBox, legendBox);
    }

    private void showExpenseBreakdown() {
        chartContainer.getChildren().clear();

        Label title = createSectionTitle("Κατανομή Εξόδων");

        Map<String, Long> expensesByEntity = new java.util.HashMap<>();

        for (RegularBudgetExpense e : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
            if (e.getCode().charAt(0) <= '3') {
                expensesByEntity.merge(e.getEntityName(), e.getAmount(), Long::sum);
            }
        }
        for (PublicInvestmentBudgetExpense e : PublicInvestmentBudgetExpense.
                getAllPublicInvestmentBudgetExpenses()) {
            if (e.getCode().charAt(0) <= '3') {
                expensesByEntity.merge(e.getEntityName(), e.getAmount(), Long::sum);
            }
        }

        List<Map.Entry<String, Long>> sortedExpenses = expensesByEntity.entrySet().stream().
            sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).limit(8).
            collect(Collectors.toList());

        if (sortedExpenses.isEmpty()) {
            showNoDataMessage();
            return;
        }

        long total = sortedExpenses.stream().mapToLong(Map.Entry::getValue).sum();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        List<LegendItem> legendItems = new ArrayList<>();

        for (Map.Entry<String, Long> entry : sortedExpenses) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            legendItems.add(new LegendItem(entry.getKey(), entry.getValue()));
        }

        VBox chartBox = createChartWithTotal(pieData, total, "#ff3b30", "Συνολικά Έξοδα");
        PieChart pieChart = (PieChart) chartBox.getChildren().get(0);
        applyInteractiveEffects(pieChart, legendItems, total, "Συνολικά Έξοδα");

        VBox legendBox = createLegend(legendItems, total, pieChart);

        chartContainer.getChildren().addAll(title, chartBox, legendBox);
    }

    private void showComparison() {
        chartContainer.getChildren().clear();

        Label title = createSectionTitle("Έσοδα vs Έξοδα");

        long totalRevenue = BudgetRevenue.getAllBudgetRevenues().stream().
            filter(r -> r.getCode().length() == 2 && r.getCode().charAt(0) <= '3').
            mapToLong(BudgetRevenue::getAmount).sum();

        long totalExpense = RegularBudgetExpense.getAllRegularBudgetExpenses().stream().
            filter(e -> e.getCode().charAt(0) <= '3').mapToLong(RegularBudgetExpense::getAmount).sum()
            + PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().stream().
            filter(e -> e.getCode().charAt(0) <= '3').
            mapToLong(PublicInvestmentBudgetExpense::getAmount).sum();

        if (totalRevenue == 0 && totalExpense == 0) {
            showNoDataMessage();
            return;
        }

        long total = totalRevenue + totalExpense;

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Έσοδα", totalRevenue),
            new PieChart.Data("Έξοδα", totalExpense)
        );

        List<LegendItem> legendItems = new ArrayList<>();
        legendItems.add(new LegendItem("Έσοδα", totalRevenue));
        legendItems.add(new LegendItem("Έξοδα", totalExpense));

        VBox chartBox = createChartWithTotal(pieData, total, "#007aff", "Σύνολο");
        PieChart pieChart = (PieChart) chartBox.getChildren().get(0);

        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #34c759;");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #ff3b30;");

        applyComparisonEffects(pieChart, legendItems, total);

        VBox statsBox = createComparisonStats(totalRevenue, totalExpense);

        chartContainer.getChildren().addAll(title, chartBox, statsBox);
    }

    private void applyComparisonEffects(PieChart pieChart, List<LegendItem> legendItems,
                                        long total) {
        String[] colors = {"#34c759", "#ff3b30"};
        for (int i = 0; i < pieChart.getData().size(); i++) {
            PieChart.Data data = pieChart.getData().get(i);
            String color = colors[i];
            LegendItem item = legendItems.get(i);
            double percentage = (item.amount * 100.0) / total;

            DropShadow glow = new DropShadow();
            glow.setColor(Color.web(color, 0.6));
            glow.setRadius(15);
            glow.setSpread(0.2);

            String tooltipText = String.format("%s\n%s  •  %.1f%%", item.name,
                formatAmount(item.amount), percentage);
            Tooltip tooltip = new Tooltip(tooltipText);
            tooltip.setShowDelay(Duration.millis(100));
            tooltip.setStyle("-fx-font-size: 13px; -fx-background-color: rgba(60,60,60,0.95); "
                + "-fx-text-fill: #ffffff; -fx-padding: 10; -fx-background-radius: 8;");
            Tooltip.install(data.getNode(), tooltip);

            final int idx = i;
            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setEffect(glow);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), data.getNode());
                scale.setToX(1.03);
                scale.setToY(1.03);
                scale.play();
                totalAmountLabel.setText(formatAmount(legendItems.get(idx).amount));
                totalDescLabel.setText(legendItems.get(idx).name);
            });

            data.getNode().setOnMouseExited(e -> {
                data.getNode().setEffect(null);
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), data.getNode());
                scale.setToX(1.0);
                scale.setToY(1.0);
                scale.play();
                totalAmountLabel.setText(formatAmount(total));
                totalDescLabel.setText("Σύνολο");
            });
        }
    }

    private VBox createComparisonStats(long totalRevenue, long totalExpense) {
        VBox statsBox = new VBox(16);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(24));
        statsBox.setStyle("-fx-background-color: #2c2c2e; -fx-background-radius: 12;");
        statsBox.setMaxWidth(400);

        HBox revenueRow = createStatRow("Έσοδα", totalRevenue, "#34c759");
        HBox expenseRow = createStatRow("Έξοδα", totalExpense, "#ff3b30");

        Region divider = new Region();
        divider.setMinHeight(1);
        divider.setStyle("-fx-background-color: #3a3a3c;");

        long balance = totalRevenue - totalExpense;
        String balanceColor = balance >= 0 ? "#34c759" : "#ff3b30";
        String balanceText = balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα";

        HBox balanceRow = new HBox(12);
        balanceRow.setAlignment(Pos.CENTER);

        Label balanceLabel = new Label(balanceText);
        balanceLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: #8e8e93;");

        Label balanceAmount = new Label(formatAmount(Math.abs(balance)));
        balanceAmount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: "
            + balanceColor + ";");

        balanceRow.getChildren().addAll(balanceLabel, balanceAmount);

        statsBox.getChildren().addAll(revenueRow, expenseRow, divider, balanceRow);
        return statsBox;
    }

    private HBox createStatRow(String label, long amount, String color) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Region colorDot = new Region();
        colorDot.setMinSize(10, 10);
        colorDot.setMaxSize(10, 10);
        colorDot.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 17px;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label amountLabel = new Label(formatAmount(amount));
        amountLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 17px; "
            + "-fx-font-weight: 600;");

        row.getChildren().addAll(colorDot, nameLabel, amountLabel);
        return row;
    }

    private void showMinistryExpenses() {
        chartContainer.getChildren().clear();

        Label title = createSectionTitle("Έξοδα ανά Υπουργείο");

        Map<String, Long> expensesByMinistry = new java.util.HashMap<>();

        for (RegularBudgetExpense e : RegularBudgetExpense.getAllRegularBudgetExpenses()) {
            if (e.getCode().charAt(0) <= '3') {
                expensesByMinistry.merge(e.getEntityName(), e.getAmount(), Long::sum);
            }
        }
        for (PublicInvestmentBudgetExpense e : PublicInvestmentBudgetExpense.
                getAllPublicInvestmentBudgetExpenses()) {
            if (e.getCode().charAt(0) <= '3') {
                expensesByMinistry.merge(e.getEntityName(), e.getAmount(), Long::sum);
            }
        }

        List<Map.Entry<String, Long>> sortedExpenses = expensesByMinistry.entrySet().stream().
            sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).limit(10).
            collect(Collectors.toList());

        if (sortedExpenses.isEmpty()) {
            showNoDataMessage();
            return;
        }

        long total = sortedExpenses.stream().mapToLong(Map.Entry::getValue).sum();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        List<LegendItem> legendItems = new ArrayList<>();

        for (Map.Entry<String, Long> entry : sortedExpenses) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            legendItems.add(new LegendItem(entry.getKey(), entry.getValue()));
        }

        VBox chartBox = createChartWithTotal(pieData, total, "#ff9500", "Συνολικά Έξοδα");
        PieChart pieChart = (PieChart) chartBox.getChildren().get(0);
        applyInteractiveEffects(pieChart, legendItems, total, "Συνολικά Έξοδα");

        VBox legendBox = createLegend(legendItems, total, pieChart);

        chartContainer.getChildren().addAll(title, chartBox, legendBox);
    }

    private Label createSectionTitle(String text) {
        Label title = new Label(text);
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: 600; -fx-text-fill: #ffffff;");
        return title;
    }

    private void showNoDataMessage() {
        VBox noDataBox = new VBox(8);
        noDataBox.setAlignment(Pos.CENTER);
        noDataBox.setPadding(new Insets(60));

        Label noData = new Label("Δεν υπάρχουν δεδομένα");
        noData.setStyle("-fx-font-size: 17px; -fx-text-fill: #8e8e93;");

        Label hint = new Label("Φορτώστε CSV αρχεία");
        hint.setStyle("-fx-font-size: 15px; -fx-text-fill: #636366;");

        noDataBox.getChildren().addAll(noData, hint);
        chartContainer.getChildren().add(noDataBox);
    }

    public Region getView() {
        return view;
    }

    private static class LegendItem {
        final String name;
        final long amount;

        LegendItem(String name, long amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}
