package com.financial.ui.views;

import com.financial.entries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.financial.statistical_analysis.StatisticalExpenses;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Charts view - displays budget visualizations.
 */
public class ChartsView {

    // Design constants
    private static final String BG_PRIMARY = "#0a0a0f";
    private static final String BG_SECONDARY = "#12121a";
    private static final String BG_TERTIARY = "#1a1a24";
    private static final String TEXT_PRIMARY = "#e4e4e7";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String TEXT_MUTED = "#52525b";
    private static final String BORDER_COLOR = "#27272a";
    private static final String ACCENT = "#3b82f6";

    // Refined monochromatic chart colors - blues and grays
    private static final String[] CHART_COLORS = {"#3b82f6", "#60a5fa", "#93c5fd", "#bfdbfe", "#dbeafe", "#6366f1", "#818cf8", "#a5b4fc", "#c7d2fe", "#e0e7ff"};

    private final VBox view;
    private VBox chartContainer;
    private ScrollPane scrollPane;
    private ComboBox<String> chartTypeCombo;
    private TextField searchField;
    private Button searchBtn;
    private Button activeButton;
    private Button toggleOutliersBtn;
    private String currentChartType = "Pie Chart";
    private String currentChartView = "placeholder";
    private boolean filterOutliers = false;

    public ChartsView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Header
        VBox header = createHeader();

        // Controls section
        HBox controls = createControls();

        // Chart container
        chartContainer = new VBox(24);
        chartContainer.setPadding(new Insets(24));
        chartContainer.setAlignment(Pos.TOP_CENTER);

        scrollPane = new ScrollPane(chartContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + BG_PRIMARY + "; -fx-background-color: " + BG_PRIMARY + "; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        showPlaceholder();

        view.getChildren().addAll(header, controls, scrollPane);
    }

    private VBox createHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(32, 24, 16, 24));

        Label title = new Label("Γραφήματα");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label subtitle = new Label("Οπτικοποίηση δεδομένων προϋπολογισμού");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox createControls() {
        HBox controls = new HBox(8);
        controls.setPadding(new Insets(0, 24, 16, 24));
        controls.setAlignment(Pos.BOTTOM_LEFT);

        // Chart type selector
        chartTypeCombo = new ComboBox<>();
        chartTypeCombo.getItems().addAll("Pie Chart", "Bar Chart");
        chartTypeCombo.setValue("Pie Chart");
        styleComboBox(chartTypeCombo);
        chartTypeCombo.setPrefWidth(120);
        chartTypeCombo.setOnAction(e -> {
            currentChartType = chartTypeCombo.getValue();
            refreshCurrentChart();
        });

        // Separator
        Region sep1 = new Region();
        sep1.setPrefWidth(8);

        // Chart buttons
        Button revenuesBtn = createChartButton("Έσοδα");
        Button expensesBtn = createChartButton("Έξοδα");
        Button comparisonBtn = createChartButton("Σύγκριση");
        Button ministryBtn = createChartButton("Υπουργεία");

        revenuesBtn.setOnAction(e -> {
            setActiveButton(revenuesBtn);
            showRevenueBreakdown();
        });
        expensesBtn.setOnAction(e -> {
            setActiveButton(expensesBtn);
            showExpenseBreakdown();
        });
        comparisonBtn.setOnAction(e -> {
            setActiveButton(comparisonBtn);
            showComparison();
        });
        ministryBtn.setOnAction(e -> {
            setActiveButton(ministryBtn);
            showMinistryExpenses();
        });

        // Separator
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label customTitle = new Label("CUSTOM ΔΙΑΓΡΑΜΜΑ");
        customTitle.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-weight: bold; -fx-letter-spacing: 1px; -fx-padding: 0 0 0 30;");


        // Search
        searchField = new TextField();
        searchField.setPromptText("Κωδικός...");
        searchField.setPrefWidth(180);
        styleTextField(searchField);

        searchBtn = createChartButton("Κατανομή");
        searchBtn.setOnAction(e -> handleContextualSearch());
        searchField.setOnAction(e -> handleContextualSearch());

        HBox searchRow = new HBox(8, searchField, searchBtn);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        VBox searchContainer = new VBox(4, customTitle, searchRow);
        searchContainer.setAlignment(Pos.BOTTOM_LEFT);

        toggleOutliersBtn = new Button("Εξομάλυνση");
        toggleOutliersBtn.setStyle(getOutlierButtonStyle(false));

        toggleOutliersBtn.setOnMouseEntered(e -> {
            if (!filterOutliers) {
                toggleOutliersBtn.setStyle(getOutlierButtonHoverStyle());
            }
        });
        toggleOutliersBtn.setOnMouseExited(e -> {
            if (!filterOutliers) {
                toggleOutliersBtn.setStyle(getOutlierButtonStyle(false));
            }
        });

        toggleOutliersBtn.setOnAction(e -> {
            filterOutliers = !filterOutliers;
            toggleOutliersBtn.setStyle(getOutlierButtonStyle(filterOutliers));
            refreshCurrentChart();
        });

        controls.getChildren().addAll(chartTypeCombo, sep1, revenuesBtn, expensesBtn, comparisonBtn, ministryBtn, spacer, searchContainer);
        controls.getChildren().add(6, toggleOutliersBtn);

        return controls;
    }

    private void handleContextualSearch() {
        String code = searchField.getText().trim();
        if (code.isEmpty()) {
            return;
        }

        switch (currentChartView) {
            case "revenues":
            case "revenue_hierarchy":
                showRevenueHierarchyChart();
                break;

            case "expenses":
            case "ministry_category_hierarchy":
                showMinistryExpenseCategoryHierarchyChart();
                break;

            case "ministry":
            case "ministry_service_hierarchy":
                showMinistryServiceHierarchyChart();
                break;
        }
    }

    private Button createChartButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(getButtonStyle(false));
        btn.setOnMouseEntered(e -> {
            if (btn != activeButton) {
                btn.setStyle(getButtonHoverStyle());
            }
        });
        btn.setOnMouseExited(e -> {
            if (btn != activeButton) {
                btn.setStyle(getButtonStyle(false));
            }
        });
        return btn;
    }

    private String getButtonStyle(boolean active) {
        if (active) {
            return "-fx-background-color: " + ACCENT + ";" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-border-color: transparent;" +
                    "-fx-border-radius: 6;" +
                    "-fx-padding: 8 14;" +
                    "-fx-font-size: 12px;" +
                    "-fx-cursor: hand;";
        }
        return "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                "-fx-background-radius: 6;" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 6;" +
                "-fx-padding: 8 14;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;";
    }

    private String getButtonHoverStyle() {
        return "-fx-background-color: " + BG_TERTIARY + ";" +
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-background-radius: 6;" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 6;" +
                "-fx-padding: 8 14;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;";
    }

    private void setActiveButton(Button btn) {
        filterOutliers = false;
        toggleOutliersBtn.setStyle(getOutlierButtonStyle(false));

        if (searchField != null) {
            searchField.clear();
        }

        if (activeButton != null) {
            activeButton.setStyle(getButtonStyle(false));
        }
        activeButton = btn;
        btn.setStyle(getButtonStyle(true));
    }

    private void styleComboBox(ComboBox<String> combo) {
        combo.setStyle(
                "-fx-background-color: " + BG_SECONDARY + ";" +
                        "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-font-size: 12px;"
        );
    }

    private void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: " + BG_SECONDARY + ";" +
                        "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                        "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-background-radius: 6;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-padding: 8 12;" +
                        "-fx-font-size: 12px;"
        );
    }

    private void refreshCurrentChart() {
        switch (currentChartView) {
            case "revenues" -> showRevenueBreakdown();
            case "expenses" -> showExpenseBreakdown();
            case "comparison" -> showComparison();
            case "ministry" -> showMinistryExpenses();
            case "revenue_hierarchy" -> showRevenueHierarchyChart();
            case "ministry_category_hierarchy" -> showMinistryExpenseCategoryHierarchyChart();
            case "ministry_service_hierarchy" -> showMinistryServiceHierarchyChart();
            default -> showPlaceholder();
        }
    }

    private void showPlaceholder() {
        currentChartView = "placeholder";
        chartContainer.getChildren().clear();

        VBox placeholder = new VBox(12);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setPadding(new Insets(100));

        Label icon = new Label("○");
        icon.setStyle("-fx-font-size: 48px; -fx-text-fill: " + BORDER_COLOR + ";");

        Label text = new Label("Επιλέξτε γράφημα για προβολή");
        text.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_MUTED + ";");

        placeholder.getChildren().addAll(icon, text);
        chartContainer.getChildren().add(placeholder);
    }

    private void showRevenueBreakdown() {
        currentChartView = "revenues";
        searchField.setDisable(false);
        searchField.setPromptText("Κωδικός Εσόδων...");
        searchBtn.setText("Κατανομή Εσόδων");
        chartContainer.getChildren().clear();

        List<BudgetRevenue> revenues = BudgetRevenue.getMainBudgetRevenues();
        if (revenues.isEmpty()) {
            showNoData();
            return;
        }

        long total = BudgetRevenue.calculateSum();
        List<ChartItem> items = revenues.stream().map(r -> new ChartItem(r.getDescription(), r.getAmount())).collect(Collectors.toList());

        VBox content = createChartContent("Κατανομή Εσόδων", "Ανάλυση κατηγοριών εσόδων", items, total);
        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private void showExpenseBreakdown() {
        currentChartView = "expenses";
        searchField.setDisable(false);
        searchField.setPromptText("Κωδικός Υπουργείου...");
        searchBtn.setText("Κατανομή Εξόδων");
        chartContainer.getChildren().clear();

        Map<String, Long> expenses = new HashMap<>();

        for (Map.Entry<String, Long> entry : BudgetExpense.getSumOfEveryBudgetExpenseCategory().entrySet()) {
            expenses.put(BudgetExpense.getDescriptionWithCode(entry.getKey()), entry.getValue());
        }

        List<Map.Entry<String, Long>> sorted = expenses.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).collect(Collectors.toList());

        if (sorted.isEmpty()) {
            showNoData();
            return;
        }

        long total = sorted.stream().mapToLong(Map.Entry::getValue).sum();
        List<ChartItem> items = sorted.stream().map(e -> new ChartItem(e.getKey(), e.getValue())).collect(Collectors.toList());

        VBox content = createChartContent("Κατανομή Εξόδων", "Ανάλυση κατηγοριών εξόδων", items, total);
        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private void showComparison() {
        currentChartView = "comparison";
        searchField.setDisable(true);
        searchBtn.setText("Μη Διαθέσιμο");
        searchField.setPromptText("Μη διαθέσιμο στη σύγκριση");
        chartContainer.getChildren().clear();

        long totalRevenue = BudgetRevenue.calculateSum();
        long totalExpense = BudgetExpense.calculateSum();

        if (totalRevenue == 0 && totalExpense == 0) {
            showNoData();
            return;
        }

        List<ChartItem> items = new ArrayList<>();
        items.add(new ChartItem("Έσοδα", totalRevenue));
        items.add(new ChartItem("Έξοδα", totalExpense));

        long total = totalRevenue + totalExpense;
        VBox content = createChartContent("Σύγκριση Εσόδων - Εξόδων", "Συνολική εικόνα προϋπολογισμού", items, total);

        // Add balance card
        long balance = totalRevenue - totalExpense;
        VBox balanceCard = createBalanceCard(balance);
        content.getChildren().add(balanceCard);

        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private VBox createBalanceCard(long balance) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(300);

        boolean positive = balance >= 0;
        String bgColor = positive ? "rgba(34, 197, 94, 0.1)" : "rgba(239, 68, 68, 0.1)";
        String textColor = positive ? "#22c55e" : "#ef4444";
        String borderColor = positive ? "rgba(34, 197, 94, 0.3)" : "rgba(239, 68, 68, 0.3)";

        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 8;"
        );

        Label titleLbl = new Label(positive ? "Πλεόνασμα" : "Έλλειμμα");
        titleLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textColor + "; -fx-font-weight: 500;");

        Label valueLbl = new Label(formatAmount(Math.abs(balance)));
        valueLbl.setStyle("-fx-font-size: 20px; -fx-text-fill: " + textColor + "; -fx-font-weight: 600;");

        card.getChildren().addAll(titleLbl, valueLbl);
        return card;
    }

    private void showMinistryExpenses() {
        currentChartView = "ministry";
        searchField.setDisable(false);
        searchField.setPromptText("Κωδικός Υπουργείου...");
        searchBtn.setText("Κατανομή Ειδικών Φορέων");
        chartContainer.getChildren().clear();

        Map<String, Long> expensesByMinistry = new HashMap<>();
        for (Entity entity : Entity.getEntities()) {
            expensesByMinistry.put(entity.getEntityName(), entity.calculateTotalSum());
        }

        List<Map.Entry<String, Long>> sorted = expensesByMinistry.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).limit(10).collect(Collectors.toList());

        if (sorted.isEmpty()) {
            showNoData();
            return;
        }

        long total = sorted.stream().mapToLong(Map.Entry::getValue).sum();
        List<ChartItem> items = sorted.stream().map(e -> new ChartItem(e.getKey(), e.getValue())).collect(Collectors.toList());

        VBox content = createChartContent("Έξοδα ανά Υπουργείο", "Κατανομή δαπανών", items, total);
        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private void showRevenueHierarchyChart() {
        currentChartView = "revenue_hierarchy";
        String code = searchField.getText().trim();
        if (code.isEmpty()) {
            return;
        }


        BudgetRevenue revenue = BudgetRevenue.findBudgetRevenueWithCode(code);
        if (revenue == null) {
            chartContainer.getChildren().clear();
            VBox errorBox = new VBox(8);
            errorBox.setAlignment(Pos.CENTER);
            errorBox.setPadding(new Insets(40));

            Label errorIcon = new Label("!");
            errorIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #ef4444; -fx-font-weight: bold;");

            Label error = new Label("Δεν βρέθηκε λογαριασμός με κωδικό: " + code);
            error.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

            errorBox.getChildren().addAll(errorIcon, error);
            chartContainer.getChildren().add(errorBox);
            return;
        }

        chartContainer.getChildren().clear();

        // Build hierarchy items
        List<ChartItem> items = new ArrayList<>();

        ArrayList<BudgetRevenue> superCats = revenue.getAllSuperCategories();
        if (superCats != null) {
            for (int i = superCats.size() - 1; i >= 0; i--) {
                BudgetRevenue sup = superCats.get(i);
            }
        }

        ArrayList<BudgetRevenue> nextLevel = revenue.getNextLevelSubCategories();
        if (nextLevel != null) {
            for (BudgetRevenue sub : nextLevel) {
                items.add(new ChartItem(truncate(sub.getDescription(), 60) + " (" + sub.getCode() + ")", sub.getAmount()));
            }
        }

        long total = items.stream().mapToLong(i -> i.amount).sum();

        VBox content = createChartContent(revenue.getDescription(), "Κατανομή Λογαριασμού Εσόδων", items, total);

        // Add hierarchy info card
        ArrayList<BudgetRevenue> subCats = revenue.getNextLevelSubCategories();
        int nextLevelSubs = (subCats != null) ? subCats.size() : 0;
        int totalSupers = (superCats != null) ? superCats.size() : 0;

        HBox infoCards = new HBox(12);
        infoCards.setAlignment(Pos.CENTER);
        infoCards.setPadding(new Insets(8, 0, 0, 0));

        infoCards.getChildren().addAll(
                createInfoChip("Επίπεδο " + revenue.getLevelOfHierarchy()),
                createInfoChip("Ανώτερες: " + totalSupers),
                createInfoChip("Υποκατηγορίες: " + nextLevelSubs)
        );

        content.getChildren().add(infoCards);
        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private void showMinistryExpenseCategoryHierarchyChart() {
        currentChartView = "ministry_category_hierarchy";
        String code = searchField.getText().trim();
        if (code.isEmpty()) {
            return;
        }

        Entity entity = Entity.findEntityWithEntityCode(code);
        if (entity == null) {
            chartContainer.getChildren().clear();
            VBox errorBox = new VBox(8);
            errorBox.setAlignment(Pos.CENTER);
            errorBox.setPadding(new Insets(40));

            Label errorIcon = new Label("!");
            errorIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #ef4444; -fx-font-weight: bold;");

            Label error = new Label("Δεν βρέθηκε φορέας με κωδικό: " + code);
            error.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

            errorBox.getChildren().addAll(errorIcon, error);
            chartContainer.getChildren().add(errorBox);
            return;
        }

        chartContainer.getChildren().clear();

        // Build items
        List<ChartItem> items = new ArrayList<>();

        Map<String, Long> expensesByMinistry = entity.getTotalSumOfEveryExpenseCategory();
        if (!expensesByMinistry.isEmpty()) {
            for (Map.Entry<String, Long> entry : expensesByMinistry.entrySet()) {
                items.add(new ChartItem(truncate(BudgetExpense.getDescriptionWithCode(entry.getKey()), 150) + " (" + entry.getKey() + ")", entry.getValue()));
            }
        }

        long total = items.stream().mapToLong(i -> i.amount).sum();

        VBox content = createChartContent(entity.getEntityName(), "Κατανομή Εξόδων", items, total);

        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private void showMinistryServiceHierarchyChart() {
        currentChartView = "ministry_service_hierarchy";
        String code = searchField.getText().trim();
        if (code.isEmpty()) {
            return;
        }

        Entity entity = Entity.findEntityWithEntityCode(code);
        if (entity == null) {
            chartContainer.getChildren().clear();
            VBox errorBox = new VBox(8);
            errorBox.setAlignment(Pos.CENTER);
            errorBox.setPadding(new Insets(40));

            Label errorIcon = new Label("!");
            errorIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: #ef4444; -fx-font-weight: bold;");

            Label error = new Label("Δεν βρέθηκε φορέας με κωδικό: " + code);
            error.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

            errorBox.getChildren().addAll(errorIcon, error);
            chartContainer.getChildren().add(errorBox);
            return;
        }

        chartContainer.getChildren().clear();

        // Build items
        List<ChartItem> items = new ArrayList<>();

        Map<String, Long> expensesByService = entity.getTotalSumOfEveryService();
        if (!expensesByService.isEmpty()) {
            for (Map.Entry<String, Long> entry : expensesByService.entrySet()) {
                items.add(new ChartItem(truncate(entity.getServiceNameWithCode(entry.getKey()), 300) + " (" + entry.getKey() + ")", entry.getValue()));
            }
        }

        long total = items.stream().mapToLong(i -> i.amount).sum();

        VBox content = createChartContent(entity.getEntityName(), "Κατανομή Ειδικών Φορέων", items, total);

        animateIn(content);
        chartContainer.getChildren().add(content);
    }

    private Label createInfoChip(String text) {
        Label chip = new Label(text);
        chip.setStyle(
                "-fx-background-color: " + BG_TERTIARY + ";" +
                        "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                        "-fx-padding: 6 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 11px;"
        );
        return chip;
    }

    private VBox createChartContent(String title, String subtitle, List<ChartItem> originalItems, long originalTotal) {
        List<ChartItem> displayItems = getFilteredItems(originalItems);

        long currentDisplayTotal = displayItems.stream().mapToLong(i -> i.amount).sum();
        int removedCount = originalItems.size() - displayItems.size();

        VBox content = new VBox(0);
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(content, Priority.ALWAYS);

        // Header section
        VBox headerSection = new VBox(2);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setPadding(new Insets(20, 20, 16, 20));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");

        String dynamicSubtitle = subtitle;
        if (filterOutliers && removedCount > 0) {
            if (removedCount == 1) {
                dynamicSubtitle += " (Εξαιρέθηκε 1 ακραία υψηλή τιμή)";
            } else {
                dynamicSubtitle += " (Εξαιρέθηκαν " + removedCount + " ακραία υψηλές τιμές)";
            }
        }
        Label subtitleLabel = new Label(dynamicSubtitle);
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + (removedCount > 0 && filterOutliers ? ACCENT : TEXT_MUTED) + ";");

        headerSection.getChildren().addAll(titleLabel, subtitleLabel);

        VBox chartSection = new VBox(16);
        chartSection.setAlignment(Pos.CENTER);
        chartSection.setPadding(new Insets(0, 20, 20, 20));
        VBox.setVgrow(chartSection, Priority.ALWAYS);

        if (currentChartType.equals("Bar Chart")) {
            chartSection.getChildren().add(createBarChart(displayItems));
        } else {
            chartSection.getChildren().add(createPieChart(displayItems, currentDisplayTotal));
        }

        HBox totalsContainer = new HBox(40);
        totalsContainer.setAlignment(Pos.CENTER);
        totalsContainer.setPadding(new Insets(10, 0, 20, 0));

        VBox mainTotalBox = new VBox(2);
        mainTotalBox.setAlignment(Pos.CENTER);

        boolean hasActualChanges = filterOutliers && removedCount > 0;

        Label totalTitle = new Label(hasActualChanges ? "ΠΡΟΣΑΡΜΟΣΜΕΝΟ ΣΥΝΟΛΟ" : "ΣΥΝΟΛΟ");
        totalTitle.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_MUTED + "; -fx-letter-spacing: 1px;");

        Label totalValue = new Label(formatAmount(currentDisplayTotal));
        totalValue.setStyle("-fx-font-size: 18px; -fx-font-weight: 700; -fx-text-fill: " +
                (hasActualChanges ? "#ef4444" : TEXT_PRIMARY) + ";");

        mainTotalBox.getChildren().addAll(totalTitle, totalValue);
        totalsContainer.getChildren().add(mainTotalBox);

        if (filterOutliers && removedCount > 0) {
            VBox refTotalBox = new VBox(2);
            refTotalBox.setAlignment(Pos.CENTER);
            Label refTitle = new Label("ΠΡΑΓΜΑΤΙΚΟ ΣΥΝΟΛΟ");
            refTitle.setStyle("-fx-font-size: 9px; -fx-text-fill: " + TEXT_MUTED + ";");
            Label refValue = new Label(formatAmount(originalTotal));
            refValue.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_SECONDARY + "; -fx-strikethrough: false;");
            refTotalBox.getChildren().addAll(refTitle, refValue);
            totalsContainer.getChildren().add(refTotalBox);
        }

        chartSection.getChildren().add(totalsContainer);

        // Data rows section (χρησιμοποιεί τα φιλτραρισμένα items)
        VBox dataSection = createDataRows(displayItems, currentDisplayTotal);

        content.getChildren().addAll(headerSection, chartSection, dataSection);
        content.setStyle(
                "-fx-background-color: " + BG_SECONDARY + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-radius: 12;"
        );

        return content;
    }

    private PieChart createPieChart(List<ChartItem> items, long total) {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (ChartItem item : items) {
            pieData.add(new PieChart.Data(item.name, item.amount));
        }

        PieChart chart = new PieChart(pieData);
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setMinHeight(550);
        chart.setPrefHeight(550);
        VBox.setVgrow(chart, Priority.ALWAYS);
        chart.setStyle("-fx-background-color: transparent;");

        // Apply monochromatic colors and tooltips
        int colorIndex = 0;
        for (PieChart.Data data : chart.getData()) {
            String color = CHART_COLORS[colorIndex % CHART_COLORS.length];
            data.getNode().setStyle("-fx-pie-color: " + color + ";");

            double pct = (data.getPieValue() * 100.0) / total;
            Tooltip tooltip = new Tooltip(data.getName() + "\n" + formatAmount((long) data.getPieValue()) + " (" + String.format("%.1f%%", pct) + ")");
            tooltip.setShowDelay(Duration.millis(100));
            tooltip.setStyle("-fx-font-size: 11px; -fx-background-color: " + BG_PRIMARY + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-background-radius: 4;");
            Tooltip.install(data.getNode(), tooltip);

            // Hover effect
            final String originalColor = color;
            data.getNode().setOnMouseEntered(e -> {
                data.getNode().setStyle("-fx-pie-color: " + originalColor + "; -fx-opacity: 0.8;");
            });
            data.getNode().setOnMouseExited(e -> {
                data.getNode().setStyle("-fx-pie-color: " + originalColor + ";");
            });

            colorIndex++;
        }

        return chart;
    }

    private BarChart<String, Number> createBarChart(List<ChartItem> items) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        int maxLabelLen = (items.size() <= 5) ? 30 : (items.size() <= 10) ? 20 : 15;

        xAxis.setTickLabelRotation(0);
        xAxis.setTickLabelFont(javafx.scene.text.Font.font("System", 10));
        xAxis.setTickLabelGap(5);
        xAxis.setAnimated(false);

        yAxis.setForceZeroInRange(true);
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web(TEXT_MUTED));
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web(TEXT_MUTED));

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setMinHeight(500);
        chart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (int i = 0; i < items.size(); i++) {
            ChartItem item = items.get(i);

            String shortName = truncate(item.name, maxLabelLen) + "\u200B".repeat(i);

            series.getData().add(new XYChart.Data<>(shortName, item.amount));
        }

        chart.getData().add(series);

        chart.applyCss();
        chart.layout();

        for (int i = 0; i < series.getData().size(); i++) {
            XYChart.Data<String, Number> data = series.getData().get(i);
            if (data.getNode() != null) {
                String color = CHART_COLORS[i % CHART_COLORS.length];
                data.getNode().setStyle("-fx-bar-fill: " + color + "; -fx-background-radius: 4 4 0 0;");

                Tooltip tooltip = new Tooltip(items.get(i).name + "\n" + formatAmount(items.get(i).amount));
                tooltip.setShowDelay(Duration.millis(100));
                Tooltip.install(data.getNode(), tooltip);
            }
        }

        return chart;
    }

    private VBox createDataRows(List<ChartItem> items, long total) {
        VBox dataSection = new VBox(0);
        dataSection.setStyle("-fx-background-color: " + BG_TERTIARY + "; -fx-background-radius: 0 0 12 12;");

        int colorIndex = 0;
        for (int i = 0; i < items.size(); i++) {
            ChartItem item = items.get(i);
            String color = CHART_COLORS[colorIndex % CHART_COLORS.length];
            double pct = (item.amount * 100.0) / total;
            final boolean isLastRow = (i == items.size() - 1);

            HBox row = new HBox(0);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(14, 16, 14, 16));
            row.setMinHeight(Region.USE_PREF_SIZE);

            String baseStyle = isLastRow ? "" : "-fx-border-color: transparent transparent " + BORDER_COLOR + " transparent; -fx-border-width: 0 0 1 0;";
            row.setStyle(baseStyle);

            // Color indicator
            Region colorDot = new Region();
            colorDot.setMinSize(6, 6);
            colorDot.setMaxSize(6, 6);
            colorDot.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 3;");

            // Name
            Label nameLabel = new Label(item.name);
            nameLabel.setWrapText(true);
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-line-spacing: 2px;");
            nameLabel.setPadding(new Insets(0, 15, 0, 12));
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            // Percentage bar fill
            Region barFill = new Region();
            barFill.setPrefWidth(Math.max(2, pct * 0.8));
            barFill.setPrefHeight(4);
            barFill.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 2;");

            VBox barContainer = new VBox();
            barContainer.getChildren().addAll(barFill);
            barContainer.setStyle("-fx-background-color: " + BORDER_COLOR + "; -fx-background-radius: 2;");
            barContainer.setPrefWidth(80);
            barContainer.setMaxWidth(80);
            HBox.setMargin(barContainer, new Insets(0, 0, 4, 0));

            // Percentage text
            Label pctLabel = new Label(String.format("%.1f%%", pct));
            pctLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_MUTED + ";");
            pctLabel.setMinWidth(45);
            pctLabel.setPadding(new Insets(0, 0, 0, 8));

            // Amount
            Label amtLabel = new Label(formatAmount(item.amount));
            amtLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-weight: 500;");
            amtLabel.setMinWidth(80);
            amtLabel.setAlignment(Pos.CENTER_RIGHT);

            amtLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-weight: 500;");
            amtLabel.setMinWidth(80);
            amtLabel.setAlignment(Pos.CENTER_RIGHT);

            row.getChildren().addAll(colorDot, nameLabel, barContainer, pctLabel, amtLabel);

            // Hover effect
            final String rowBaseStyle = baseStyle;
            row.setOnMouseEntered(e -> row.setStyle(rowBaseStyle + "-fx-background-color: rgba(255,255,255,0.02);"));
            row.setOnMouseExited(e -> row.setStyle(rowBaseStyle));

            dataSection.getChildren().add(row);
            colorIndex++;
        }

        return dataSection;
    }

    private void animateIn(Node node) {
        node.setOpacity(0);
        node.setTranslateY(10);

        FadeTransition fade = new FadeTransition(Duration.millis(200), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition translate = new TranslateTransition(Duration.millis(200), node);
        translate.setFromY(10);
        translate.setToY(0);

        ParallelTransition parallel = new ParallelTransition(fade, translate);
        parallel.play();
    }

    private void showNoData() {
        VBox noData = new VBox(12);
        noData.setAlignment(Pos.CENTER);
        noData.setPadding(new Insets(80));

        Label icon = new Label("∅");
        icon.setStyle("-fx-font-size: 32px; -fx-text-fill: " + BORDER_COLOR + ";");

        Label text = new Label("Δεν υπάρχουν δεδομένα");
        text.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED + ";");

        noData.getChildren().addAll(icon, text);
        chartContainer.getChildren().add(noData);
    }

    private String truncate(String text, int maxLen) {
        if (text == null) {
            return "";
        }
        return text.length() > maxLen ? text.substring(0, maxLen - 2) + "..." : text;
    }

    private String formatAmount(long amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.2f δισ.", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.2f εκ.", amount / 1_000_000.0);
        }
        return String.format("%,d €", amount);
    }

    private List<ChartItem> getFilteredItems(List<ChartItem> items) {
        if (!filterOutliers || items.size() < 4) {
            return items;
        }

        Map<String, Long> dataMap = new HashMap<>();
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (ChartItem item : items) {
            dataMap.put(item.name, item.amount);
            stats.addValue(item.amount);
        }

        Map<String, Long> outliers = StatisticalExpenses.findOutliersIQR(dataMap, stats);

        return items.stream().filter(item -> !outliers.containsKey(item.name)).collect(Collectors.toList());
    }

    private String getOutlierButtonStyle(boolean active) {
        if (active) {
            return "-fx-background-color: #a855f7;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 6;" +
                    "-fx-border-color: transparent;" +
                    "-fx-border-radius: 6;" +
                    "-fx-padding: 8 14;" +
                    "-fx-font-size: 12px;" +
                    "-fx-cursor: hand;";
        }
        return "-fx-background-color: transparent;" +
                "-fx-text-fill: #a855f7;" +
                "-fx-background-radius: 6;" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 6;" +
                "-fx-padding: 8 14;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;";
    }

    private String getOutlierButtonHoverStyle() {
        return "-fx-background-color: " + BG_TERTIARY + ";" +
                "-fx-text-fill: #c084fc;" +
                "-fx-background-radius: 6;" +
                "-fx-border-color: " + BORDER_COLOR + ";" +
                "-fx-border-radius: 6;" +
                "-fx-padding: 8 14;" +
                "-fx-font-size: 12px;" +
                "-fx-cursor: hand;";
    }

    public Region getView() {
        return view;
    }

    private static class ChartItem {
        final String name;
        final long amount;

        ChartItem(String name, long amount) {
            this.name = name;
            this.amount = amount;
        }
    }
}