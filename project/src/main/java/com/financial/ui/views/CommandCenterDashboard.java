package com.financial.ui.views;

import com.financial.entries.*;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

/**
 * Modern Command Center Dashboard - Greek State Budget.
 * Matches the dark banking dashboard reference design.
 */
public class CommandCenterDashboard {

    // Color constants
    private static final String BG_DARK = "#101216";
    private static final String BG_CARD = "#151519";
    private static final String TEXT_PRIMARY = "#f4f4f5";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String TEXT_MUTED = "#52525b";
    private static final String BORDER_COLOR = "#27272a";
    private static final String ACCENT_TEAL = "#14b8a6";
    private static final String ACCENT_GREEN = "#22c55e";
    private static final String ACCENT_BLUE = "#3b82f6";
    private static final String ACCENT_YELLOW = "#eab308";
    private static final String ACCENT_RED = "#ef4444";

    private final VBox view;
    private final StackPane rootStack;
    private Label toastLabel;

    public CommandCenterDashboard() {
        rootStack = new StackPane();
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_DARK + ";");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: " + BG_DARK + "; -fx-background: " + BG_DARK + ";");

        // Main content padding
        VBox contentWrapper = new VBox(24);
        contentWrapper.setPadding(new Insets(24));
        contentWrapper.setStyle("-fx-background-color: " + BG_DARK + ";");

        // Build sections
        HBox kpiRow = createKPICardsRow();
        HBox chartsRow = createChartsRow();
        HBox bottomRow = createBottomRow();

        contentWrapper.getChildren().addAll(kpiRow, chartsRow, bottomRow);
        view.getChildren().add(contentWrapper);

        rootStack.getChildren().add(scrollPane);

        // Setup toast container
        setupToast();
    }

    /**
     * Creates the row of 4 KPI cards.
     */
    private HBox createKPICardsRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        // Get real data from the budget entries
        long totalRevenue = BudgetRevenue.calculateSum();
        long totalRegularExpense = RegularBudgetExpense.calculateSum();
        long totalPublicInvestmentExpense = PublicInvestmentBudgetExpense.calculateSum();
        long totalExpense = totalRegularExpense + totalPublicInvestmentExpense;
        long balance = totalRevenue - totalExpense;

        // Calculate total budget (revenue + expenses combined as total movement)
        long totalBudget = totalRevenue;

        VBox totalCard = createKPICard(
            "Total Budget",
            formatCurrency(totalBudget),
            "+2.5%",
            true,
            "teal",
            "\u20AC" // Euro symbol as icon placeholder
        );

        VBox revenueCard = createKPICard(
            "Revenues",
            formatCurrency(totalRevenue),
            "+4.2%",
            true,
            "green",
            "\u2191" // Up arrow
        );

        VBox expenseCard = createKPICard(
            "Expenses",
            formatCurrency(totalExpense),
            "+1.8%",
            false,
            "yellow",
            "\u2193" // Down arrow
        );

        VBox deficitCard = createKPICard(
            balance >= 0 ? "Surplus" : "Deficit",
            formatCurrency(Math.abs(balance)),
            balance >= 0 ? "+12.3%" : "-8.5%",
            balance >= 0,
            balance >= 0 ? "blue" : "red",
            "\u2248" // Approximately equal
        );

        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);
        HBox.setHgrow(deficitCard, Priority.ALWAYS);

        row.getChildren().addAll(totalCard, revenueCard, expenseCard, deficitCard);
        return row;
    }

    /**
     * Creates a single KPI card with icon, value, and trend indicator.
     */
    private VBox createKPICard(String label, String value, String trend, boolean positive,
                               String colorScheme, String iconText) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setMinWidth(200);
        card.getStyleClass().addAll("kpi-card", colorScheme);

        // Apply gradient background based on color scheme
        String gradientEnd = switch (colorScheme) {
            case "teal" -> "#0d2d2a";
            case "green" -> "#14532d";
            case "yellow" -> "#422006";
            case "blue" -> "#1e3a5f";
            case "red" -> "#450a0a";
            default -> BG_CARD;
        };

        String borderColor = switch (colorScheme) {
            case "teal" -> "#134e4a";
            case "green" -> "#166534";
            case "yellow" -> "#854d0e";
            case "blue" -> "#1e40af";
            case "red" -> "#991b1b";
            default -> BORDER_COLOR;
        };

        card.setStyle(String.format(
            "-fx-background-color: linear-gradient(to bottom right, %s 0%%, %s 100%%); " +
            "-fx-background-radius: 16; -fx-border-color: %s; -fx-border-radius: 16; " +
            "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);",
            BG_CARD, gradientEnd, borderColor
        ));

        // Hover effect
        String baseStyle = card.getStyle();
        card.setOnMouseEntered(e -> card.setStyle(baseStyle +
            "-fx-translate-y: -2; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 4);"));
        card.setOnMouseExited(e -> card.setStyle(baseStyle));

        // Top row: icon + trend
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Icon container
        String iconBg = switch (colorScheme) {
            case "teal" -> ACCENT_TEAL;
            case "green" -> ACCENT_GREEN;
            case "yellow" -> ACCENT_YELLOW;
            case "blue" -> ACCENT_BLUE;
            case "red" -> ACCENT_RED;
            default -> ACCENT_BLUE;
        };

        StackPane iconContainer = new StackPane();
        iconContainer.setMinSize(48, 48);
        iconContainer.setMaxSize(48, 48);
        iconContainer.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 12;", iconBg
        ));

        Label iconLabel = new Label(iconText);
        iconLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
        iconContainer.getChildren().add(iconLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Trend indicator
        Label trendLabel = new Label(trend + " \u2197");
        trendLabel.setStyle(String.format(
            "-fx-font-size: 12px; -fx-text-fill: %s; -fx-padding: 4 8; " +
            "-fx-background-color: rgba(%s, 0.15); -fx-background-radius: 4;",
            positive ? ACCENT_GREEN : ACCENT_RED,
            positive ? "34, 197, 94" : "239, 68, 68"
        ));

        topRow.getChildren().addAll(iconContainer, spacer, trendLabel);

        // Label
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        // Value
        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

        card.getChildren().addAll(topRow, labelNode, valueNode);
        return card;
    }

    /**
     * Creates the middle row with donut chart and line chart.
     */
    private HBox createChartsRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.TOP_LEFT);

        VBox donutCard = createDonutChartCard();
        VBox lineChartCard = createLineChartCard();

        donutCard.setMinWidth(380);
        donutCard.setMaxWidth(420);

        HBox.setHgrow(lineChartCard, Priority.ALWAYS);

        row.getChildren().addAll(donutCard, lineChartCard);
        return row;
    }

    /**
     * Creates the donut chart card for spending breakdown.
     */
    private VBox createDonutChartCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 16; " +
            "-fx-border-color: %s; -fx-border-radius: 16; -fx-border-width: 1;",
            BG_CARD, BORDER_COLOR
        ));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label("Spending Breakdown");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
        Label subtitle = new Label("By category");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("Monthly", "Quarterly", "Yearly");
        dropdown.setValue("Monthly");
        dropdown.setStyle(
            "-fx-background-color: #1a1a1f; -fx-text-fill: #a1a1aa; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-border-color: #27272a; -fx-border-radius: 6;"
        );

        header.getChildren().addAll(titleBox, spacer, dropdown);

        // Donut chart with center label
        StackPane chartContainer = new StackPane();
        chartContainer.setMinHeight(280);

        // Create pie chart data (sample data matching categories)
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Healthcare", 35),
            new PieChart.Data("Education", 25),
            new PieChart.Data("Defense", 18),
            new PieChart.Data("Infrastructure", 12),
            new PieChart.Data("Social", 10)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setLabelsVisible(false);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(90);
        pieChart.setStyle("-fx-background-color: transparent;");
        pieChart.setMinSize(260, 260);
        pieChart.setMaxSize(260, 260);

        // Center hole for donut effect
        VBox centerLabel = new VBox(2);
        centerLabel.setAlignment(Pos.CENTER);
        centerLabel.setMaxSize(100, 60);
        centerLabel.setStyle("-fx-background-color: " + BG_CARD + "; -fx-background-radius: 50;");

        long totalExpense = RegularBudgetExpense.calculateSum() + PublicInvestmentBudgetExpense.calculateSum();
        Label valueLabel = new Label(formatCurrencyShort(totalExpense));
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label totalLabel = new Label("Total");
        totalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        centerLabel.getChildren().addAll(valueLabel, totalLabel);

        // Create circle mask for donut hole
        Circle innerCircle = new Circle(50);
        innerCircle.setStyle("-fx-fill: " + BG_CARD + ";");

        chartContainer.getChildren().addAll(pieChart, centerLabel);

        card.getChildren().addAll(header, chartContainer);
        return card;
    }

    /**
     * Creates the line chart card for revenue vs expense trend.
     */
    private VBox createLineChartCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 16; " +
            "-fx-border-color: %s; -fx-border-radius: 16; -fx-border-width: 1;",
            BG_CARD, BORDER_COLOR
        ));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label("Revenue vs Expense Trend");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

        // Legend
        HBox legend = new HBox(16);
        legend.setAlignment(Pos.CENTER_LEFT);

        HBox revLegend = new HBox(6);
        revLegend.setAlignment(Pos.CENTER_LEFT);
        Circle revDot = new Circle(5);
        revDot.setStyle("-fx-fill: " + ACCENT_GREEN + ";");
        Label revLabel = new Label("Revenue");
        revLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        revLegend.getChildren().addAll(revDot, revLabel);

        HBox expLegend = new HBox(6);
        expLegend.setAlignment(Pos.CENTER_LEFT);
        Circle expDot = new Circle(5);
        expDot.setStyle("-fx-fill: " + ACCENT_YELLOW + ";");
        Label expLabel = new Label("Expenses");
        expLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        expLegend.getChildren().addAll(expDot, expLabel);

        legend.getChildren().addAll(revLegend, expLegend);
        titleBox.getChildren().addAll(title, legend);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("2023", "2024", "2025");
        dropdown.setValue("2025");
        dropdown.setStyle(
            "-fx-background-color: #1a1a1f; -fx-text-fill: #a1a1aa; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-border-color: #27272a; -fx-border-radius: 6;"
        );

        header.getChildren().addAll(titleBox, spacer, dropdown);

        // Line Chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setStyle("-fx-tick-label-fill: " + TEXT_SECONDARY + ";");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setStyle("-fx-tick-label-fill: " + TEXT_SECONDARY + ";");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return "\u20AC" + (object.intValue() / 1000) + "K";
            }
        });

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.setMinHeight(250);
        VBox.setVgrow(lineChart, Priority.ALWAYS);

        // Revenue series (sample data)
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int[] revenueData = {2100, 1800, 2400, 2200, 2600, 2800,
                             2500, 3000, 2900, 3100, 2800, 3200};
        int[] expenseData = {1800, 1600, 2000, 1900, 2200, 2400,
                             2100, 2500, 2300, 2600, 2400, 2700};

        for (int i = 0; i < months.length; i++) {
            revenueSeries.getData().add(new XYChart.Data<>(months[i], revenueData[i]));
        }

        // Expense series
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        for (int i = 0; i < months.length; i++) {
            expenseSeries.getData().add(new XYChart.Data<>(months[i], expenseData[i]));
        }

        lineChart.getData().add(revenueSeries);
        lineChart.getData().add(expenseSeries);

        card.getChildren().addAll(header, lineChart);
        return card;
    }

    /**
     * Creates the bottom row with table and quick allocation.
     */
    private HBox createBottomRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.TOP_LEFT);

        VBox tableCard = createTableCard();
        VBox quickAllocationCard = createQuickAllocationCard();

        HBox.setHgrow(tableCard, Priority.ALWAYS);
        quickAllocationCard.setMinWidth(320);
        quickAllocationCard.setMaxWidth(360);

        row.getChildren().addAll(tableCard, quickAllocationCard);
        return row;
    }

    /**
     * Creates the Recent Decisions table card.
     */
    private VBox createTableCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 16; " +
            "-fx-border-color: %s; -fx-border-radius: 16; -fx-border-width: 1;",
            BG_CARD, BORDER_COLOR
        ));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Recent Decisions");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.getItems().addAll("Recent", "This Week", "This Month");
        dropdown.setValue("Recent");
        dropdown.setStyle(
            "-fx-background-color: #1a1a1f; -fx-text-fill: #a1a1aa; -fx-font-size: 12px; " +
            "-fx-background-radius: 6; -fx-border-color: #27272a; -fx-border-radius: 6;"
        );

        header.getChildren().addAll(title, spacer, dropdown);

        // Table
        TableView<DecisionItem> table = new TableView<>();
        table.setStyle("-fx-background-color: transparent;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setMinHeight(280);

        // Columns
        TableColumn<DecisionItem, String> nameCol = new TableColumn<>("Item");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        nameCol.setMinWidth(180);
        styleTableColumn(nameCol);

        TableColumn<DecisionItem, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().date()));
        dateCol.setMinWidth(100);
        styleTableColumn(dateCol);

        TableColumn<DecisionItem, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().amount()));
        amountCol.setMinWidth(120);
        styleTableColumn(amountCol);

        TableColumn<DecisionItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().status()));
        statusCol.setMinWidth(100);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    String bgColor;
                    String textColor;
                    switch (item.toLowerCase()) {
                        case "completed" -> {
                            bgColor = "rgba(34, 197, 94, 0.15)";
                            textColor = ACCENT_GREEN;
                        }
                        case "pending" -> {
                            bgColor = "rgba(234, 179, 8, 0.15)";
                            textColor = ACCENT_YELLOW;
                        }
                        default -> {
                            bgColor = "rgba(239, 68, 68, 0.15)";
                            textColor = ACCENT_RED;
                        }
                    }
                    badge.setStyle(String.format(
                        "-fx-background-color: %s; -fx-text-fill: %s; " +
                        "-fx-padding: 4 10; -fx-background-radius: 4; -fx-font-size: 11px; -fx-font-weight: bold;",
                        bgColor, textColor
                    ));
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        table.getColumns().addAll(List.of(nameCol, dateCol, amountCol, statusCol));

        // Sample data
        ObservableList<DecisionItem> items = FXCollections.observableArrayList(
            new DecisionItem("Healthcare Fund Allocation", "12/04/25", "\u20AC2.4M", "Completed"),
            new DecisionItem("Education Budget Review", "12/04/25", "\u20AC1.8M", "Completed"),
            new DecisionItem("Defense Equipment Purchase", "13/04/25", "\u20AC5.2M", "Pending"),
            new DecisionItem("Infrastructure Grant", "15/04/25", "\u20AC3.1M", "Completed"),
            new DecisionItem("Social Welfare Increase", "16/04/25", "\u20AC890K", "Pending"),
            new DecisionItem("Research & Development", "17/04/25", "\u20AC1.2M", "Completed"),
            new DecisionItem("Emergency Response Fund", "18/04/25", "\u20AC2.0M", "Rejected"),
            new DecisionItem("Digital Transformation", "19/04/25", "\u20AC4.5M", "Completed")
        );

        table.setItems(items);

        card.getChildren().addAll(header, table);
        return card;
    }

    /**
     * Styles a table column for dark theme.
     */
    private void styleTableColumn(TableColumn<DecisionItem, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 13px; -fx-padding: 12 16;");
                }
            }
        });
    }

    /**
     * Creates the Quick Allocation card.
     */
    private VBox createQuickAllocationCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(20));
        card.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 16; " +
            "-fx-border-color: %s; -fx-border-radius: 16; -fx-border-width: 1;",
            BG_CARD, BORDER_COLOR
        ));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        Label title = new Label("Quick Allocation");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
        Label subtitle = new Label("Category selection");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        titleBox.getChildren().addAll(title, subtitle);

        header.getChildren().add(titleBox);

        // Category chips
        HBox chipsRow = new HBox(12);
        chipsRow.setAlignment(Pos.CENTER);

        String[][] categories = {
            {"\uD83C\uDFE5", "Health"},      // Hospital emoji
            {"\uD83C\uDF93", "Education"},   // Graduation cap
            {"\uD83D\uDEE1", "Defense"},     // Shield
            {"\uD83D\uDEA7", "Infra"},       // Construction
            {"\u2795", "More"}               // Plus
        };

        for (String[] cat : categories) {
            VBox chipContainer = new VBox(4);
            chipContainer.setAlignment(Pos.CENTER);

            StackPane chip = new StackPane();
            chip.setMinSize(48, 48);
            chip.setMaxSize(48, 48);
            chip.setStyle(
                "-fx-background-color: #1f1f23; -fx-background-radius: 50; -fx-cursor: hand;"
            );

            Label icon = new Label(cat[0]);
            icon.setStyle("-fx-font-size: 18px;");
            chip.getChildren().add(icon);

            // Hover effect
            chip.setOnMouseEntered(e -> chip.setStyle(
                "-fx-background-color: #27272a; -fx-background-radius: 50; -fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(59, 130, 246, 0.3), 8, 0, 0, 0);"
            ));
            chip.setOnMouseExited(e -> chip.setStyle(
                "-fx-background-color: #1f1f23; -fx-background-radius: 50; -fx-cursor: hand;"
            ));

            Label name = new Label(cat[1]);
            name.setStyle("-fx-font-size: 10px; -fx-text-fill: " + TEXT_SECONDARY + ";");

            chipContainer.getChildren().addAll(chip, name);
            chipsRow.getChildren().add(chipContainer);
        }

        // Amount input
        VBox inputSection = new VBox(8);

        Label amountLabel = new Label("Allocation Amount");
        amountLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        TextField amountField = new TextField();
        amountField.setPromptText("\u20AC 0.00");
        amountField.setStyle(
            "-fx-background-color: #1a1a1f; -fx-text-fill: " + TEXT_PRIMARY + "; " +
            "-fx-prompt-text-fill: " + TEXT_MUTED + "; -fx-padding: 12 16; " +
            "-fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; " +
            "-fx-font-size: 14px;"
        );
        amountField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                amountField.setStyle(amountField.getStyle().replace(BORDER_COLOR, ACCENT_BLUE));
            } else {
                amountField.setStyle(amountField.getStyle().replace(ACCENT_BLUE, BORDER_COLOR));
            }
        });

        inputSection.getChildren().addAll(amountLabel, amountField);

        // Buttons
        HBox buttonsRow = new HBox(12);
        buttonsRow.setAlignment(Pos.CENTER_RIGHT);

        Button draftBtn = new Button("Save as draft");
        draftBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #a1a1aa; -fx-font-size: 13px; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; " +
            "-fx-border-radius: 8; -fx-cursor: hand;"
        );
        draftBtn.setOnMouseEntered(e -> draftBtn.setStyle(
            "-fx-background-color: #1f1f23; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 13px; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-border-color: #3f3f46; " +
            "-fx-border-radius: 8; -fx-cursor: hand;"
        ));
        draftBtn.setOnMouseExited(e -> draftBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: #a1a1aa; -fx-font-size: 13px; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; " +
            "-fx-border-radius: 8; -fx-cursor: hand;"
        ));

        Button ratifyBtn = new Button("Ratify Change");
        ratifyBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #eab308, #f59e0b); " +
            "-fx-text-fill: #0a0a0f; -fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"
        );
        ratifyBtn.setOnMouseEntered(e -> ratifyBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #fbbf24, #f59e0b); " +
            "-fx-text-fill: #0a0a0f; -fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand; " +
            "-fx-effect: dropshadow(gaussian, rgba(234, 179, 8, 0.4), 10, 0, 0, 2);"
        ));
        ratifyBtn.setOnMouseExited(e -> ratifyBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #eab308, #f59e0b); " +
            "-fx-text-fill: #0a0a0f; -fx-font-size: 13px; -fx-font-weight: bold; " +
            "-fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"
        ));

        // Toast action
        ratifyBtn.setOnAction(e -> showToast("Allocation ratified successfully!"));

        buttonsRow.getChildren().addAll(draftBtn, ratifyBtn);

        card.getChildren().addAll(header, chipsRow, inputSection, buttonsRow);
        return card;
    }

    /**
     * Sets up the toast notification overlay.
     */
    private void setupToast() {
        HBox toast = new HBox(10);
        toast.setAlignment(Pos.CENTER);
        toast.setMaxWidth(300);
        toast.setMaxHeight(50);
        toast.setStyle(
            "-fx-background-color: " + ACCENT_GREEN + "; -fx-background-radius: 8; " +
            "-fx-padding: 12 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 4);"
        );
        toast.setVisible(false);
        toast.setTranslateY(50);

        Label checkIcon = new Label("\u2713");
        checkIcon.setStyle("-fx-text-fill: #0a0a0f; -fx-font-size: 16px; -fx-font-weight: bold;");

        toastLabel = new Label();
        toastLabel.setStyle("-fx-text-fill: #0a0a0f; -fx-font-size: 13px; -fx-font-weight: bold;");

        toast.getChildren().addAll(checkIcon, toastLabel);

        StackPane.setAlignment(toast, Pos.BOTTOM_CENTER);
        StackPane.setMargin(toast, new Insets(0, 0, 30, 0));

        rootStack.getChildren().add(toast);
    }

    /**
     * Shows a toast notification.
     */
    private void showToast(String message) {
        HBox toast = (HBox) rootStack.getChildren().get(1);
        toastLabel.setText(message);
        toast.setVisible(true);

        // Slide up animation
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(300), toast);
        slideUp.setFromY(50);
        slideUp.setToY(0);

        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        slideUp.play();
        fadeIn.play();

        // Auto hide after 3 seconds
        TranslateTransition slideDown = new TranslateTransition(Duration.millis(300), toast);
        slideDown.setDelay(Duration.seconds(3));
        slideDown.setFromY(0);
        slideDown.setToY(50);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
        fadeOut.setDelay(Duration.seconds(3));
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> toast.setVisible(false));

        slideDown.play();
        fadeOut.play();
    }

    /**
     * Formats a currency value with appropriate suffix.
     */
    private String formatCurrency(long amount) {
        if (Math.abs(amount) >= 1_000_000_000) {
            return String.format("\u20AC %.2fB", amount / 1_000_000_000.0);
        } else if (Math.abs(amount) >= 1_000_000) {
            return String.format("\u20AC %.2fM", amount / 1_000_000.0);
        } else if (Math.abs(amount) >= 1_000) {
            return String.format("\u20AC %.1fK", amount / 1_000.0);
        }
        return String.format("\u20AC %,d", amount);
    }

    /**
     * Formats currency in short form for charts.
     */
    private String formatCurrencyShort(long amount) {
        if (Math.abs(amount) >= 1_000_000_000) {
            return String.format("\u20AC%.1fB", amount / 1_000_000_000.0);
        } else if (Math.abs(amount) >= 1_000_000) {
            return String.format("\u20AC%.1fM", amount / 1_000_000.0);
        }
        return String.format("\u20AC%.0fK", amount / 1_000.0);
    }

    /**
     * Returns the root view.
     */
    public Region getView() {
        return rootStack;
    }

    /**
     * Record for table items.
     */
    private record DecisionItem(String name, String date, String amount, String status) {}
}
