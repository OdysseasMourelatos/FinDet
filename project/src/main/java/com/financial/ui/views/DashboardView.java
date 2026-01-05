package com.financial.ui.views;

import com.financial.entries.*;
import com.financial.ui.Theme;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Dashboard view - main overview screen with gamified Greek government budget stats.
 * "You are the President for One Day - Here's Your Kingdom's Treasury"
 */
public class DashboardView {

    private final ScrollPane scrollPane;
    private final VBox view;

    public DashboardView() {
        view = new VBox(24);
        view.setPadding(new Insets(32));
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Welcome header with gamified messaging
        VBox header = createHeader();

        // Budget overview - "Your Treasury"
        HBox statsRow = createStatsRow();

        // Quick actions / insights section
        HBox quickInsights = createQuickInsights();

        // Budget breakdown section
        VBox breakdownSection = createBreakdownSection();

        // Info section
        VBox infoSection = createInfoSection();

        view.getChildren().addAll(header, statsRow, quickInsights, breakdownSection, infoSection);

        // Wrap in scroll pane
        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: " + Theme.BG_BASE + ";" +
            "-fx-background-color: " + Theme.BG_BASE + ";" +
            "-fx-border-color: transparent;"
        );

        // Animate entrance
        animateEntrance();
    }

    private VBox createHeader() {
        VBox header = new VBox(8);

        // Greeting with gamification
        Label greeting = new Label("Καλώς ήρθατε, Πρόεδρε");
        greeting.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";" +
            "-fx-font-weight: 500;"
        );

        Label title = new Label("Επισκόπηση Κρατικού Προϋπολογισμού");
        title.setStyle(Theme.pageTitle());

        Label subtitle = new Label("Ελληνική Δημοκρατία - Οικονομικό Έτος 2025");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(greeting, title, subtitle);
        return header;
    }

    private HBox createStatsRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        // Calculate totals
        long totalRegularRevenue = RegularBudgetRevenue.calculateSum();
        long totalPublicInvestmentRevenue = PublicInvestmentBudgetRevenue.calculateSum();
        long totalRevenue = BudgetRevenue.calculateSum();

        long totalRegularExpense = RegularBudgetExpense.calculateSum();
        long totalPublicInvestmentExpense = PublicInvestmentBudgetExpense.calculateSum();
        long totalExpense = totalRegularExpense + totalPublicInvestmentExpense;

        long balance = totalRevenue - totalExpense;

        // Create stat cards with gamified design
        VBox revenueCard = createStatCard(
            "Συνολικά Έσοδα",
            Theme.formatAmount(totalRevenue),
            Theme.SUCCESS_LIGHT,
            "Τακτικός: " + Theme.formatAmount(totalRegularRevenue),
            "revenues"
        );

        VBox expenseCard = createStatCard(
            "Συνολικά Έξοδα",
            Theme.formatAmount(totalExpense),
            Theme.ERROR_LIGHT,
            "Τακτικός: " + Theme.formatAmount(totalRegularExpense),
            "expenses"
        );

        VBox balanceCard = createStatCard(
            "Δημοσιονομικό Ισοζύγιο",
            Theme.formatAmount(balance),
            balance >= 0 ? Theme.SUCCESS_LIGHT : Theme.ERROR_LIGHT,
            balance >= 0 ? "Πλεόνασμα" : "Έλλειμμα",
            "balance"
        );

        VBox investmentCard = createStatCard(
            "Δημόσιες Επενδύσεις",
            Theme.formatAmount(totalPublicInvestmentRevenue),
            Theme.INFO,
            "Πρόγραμμα Δημοσίων Επενδύσεων",
            "investments"
        );

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);
        HBox.setHgrow(balanceCard, Priority.ALWAYS);
        HBox.setHgrow(investmentCard, Priority.ALWAYS);

        row.getChildren().addAll(revenueCard, expenseCard, balanceCard, investmentCard);
        return row;
    }

    private VBox createStatCard(String title, String value, String valueColor, String subtitle, String type) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle(Theme.card());

        // Top row with icon
        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Icon indicator based on type
        StackPane iconContainer = createCardIcon(type, valueColor);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        topRow.getChildren().addAll(iconContainer, titleLabel);

        // Value
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + valueColor + ";"
        );

        // Subtitle with badge style
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        card.getChildren().addAll(topRow, valueLabel, subtitleLabel);

        // Hover effect using Theme methods
        card.setOnMouseEntered(e -> card.setStyle(Theme.cardHover()));
        card.setOnMouseExited(e -> card.setStyle(Theme.card()));

        return card;
    }

    private StackPane createCardIcon(String type, String color) {
        StackPane container = new StackPane();
        container.setPrefSize(32, 32);
        container.setMinSize(32, 32);

        Circle bg = new Circle(16);
        bg.setFill(javafx.scene.paint.Color.web(color, 0.15));

        // Simple icon representation
        String iconChar = switch (type) {
            case "revenues" -> "+";
            case "expenses" -> "-";
            case "balance" -> "=";
            case "investments" -> "*";
            default -> "?";
        };
        Label icon = new Label(iconChar);
        icon.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";"
        );

        container.getChildren().addAll(bg, icon);
        return container;
    }

    private HBox createQuickInsights() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);

        // Calculate some quick insights
        int revenueCount = BudgetRevenue.getAllBudgetRevenues().size();
        int entityCount = Entity.getEntities().size();

        // Budget health indicator
        VBox healthCard = createInsightCard(
            "Κατάσταση Προϋπολογισμού",
            "Ισορροπημένος",
            Theme.SUCCESS_LIGHT,
            Theme.SUCCESS_SUBTLE
        );

        // Data loaded indicator
        VBox dataCard = createInsightCard(
            "Δεδομένα Φορτώθηκαν",
            revenueCount + " εγγραφές",
            Theme.ACCENT_BRIGHT,
            Theme.ACCENT_SUBTLE
        );

        // Entities indicator
        VBox entitiesCard = createInsightCard(
            "Ενεργοί Φορείς",
            entityCount + " υπουργεία",
            Theme.INFO,
            Theme.INFO_SUBTLE
        );

        // Achievement-style card (gamification)
        VBox achievementCard = createAchievementCard();

        HBox.setHgrow(healthCard, Priority.ALWAYS);
        HBox.setHgrow(dataCard, Priority.ALWAYS);
        HBox.setHgrow(entitiesCard, Priority.ALWAYS);
        HBox.setHgrow(achievementCard, Priority.ALWAYS);

        row.getChildren().addAll(healthCard, dataCard, entitiesCard, achievementCard);
        return row;
    }

    private VBox createInsightCard(String title, String value, String accentColor, String bgColor) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: " + Theme.RADIUS_MD + ";" +
            "-fx-border-color: transparent;" +
            "-fx-border-radius: " + Theme.RADIUS_MD + ";"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + accentColor + ";"
        );

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createAchievementCard() {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: " + Theme.GOLD_SUBTLE + ";" +
            "-fx-background-radius: " + Theme.RADIUS_MD + ";" +
            "-fx-border-color: transparent;" +
            "-fx-border-radius: " + Theme.RADIUS_MD + ";"
        );

        HBox titleRow = new HBox(6);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label trophy = new Label("*");
        trophy.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.GOLD + ";"
        );

        Label titleLabel = new Label("Επίτευγμα");
        titleLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        titleRow.getChildren().addAll(trophy, titleLabel);

        Label valueLabel = new Label("Πρώτη Ημέρα!");
        valueLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.GOLD + ";"
        );

        card.getChildren().addAll(titleRow, valueLabel);
        return card;
    }

    private VBox createBreakdownSection() {
        VBox section = new VBox(16);

        Label sectionTitle = new Label("Ανάλυση Προϋπολογισμού");
        sectionTitle.setStyle(Theme.sectionHeader());

        HBox breakdownRow = new HBox(16);

        // Revenue breakdown
        VBox revenueBreakdown = createBreakdownCard(
            "Κατανομή Εσόδων",
            new String[]{"Τακτικός Προϋπολογισμός", "ΠΔΕ Εθνικό", "ΠΔΕ Συγχρηματοδοτούμενο"},
            new long[]{
                RegularBudgetRevenue.calculateSum(),
                PublicInvestmentBudgetNationalRevenue.calculateSum(),
                PublicInvestmentBudgetCoFundedRevenue.calculateSum()
            },
            Theme.SUCCESS_LIGHT
        );

        // Expense breakdown
        VBox expenseBreakdown = createBreakdownCard(
            "Κατανομή Εξόδων",
            new String[]{"Τακτικός Προϋπολογισμός", "Δημόσιες Επενδύσεις"},
            new long[]{
                RegularBudgetExpense.calculateSum(),
                PublicInvestmentBudgetExpense.calculateSum()
            },
            Theme.ERROR_LIGHT
        );

        HBox.setHgrow(revenueBreakdown, Priority.ALWAYS);
        HBox.setHgrow(expenseBreakdown, Priority.ALWAYS);

        breakdownRow.getChildren().addAll(revenueBreakdown, expenseBreakdown);

        section.getChildren().addAll(sectionTitle, breakdownRow);
        return section;
    }

    private VBox createBreakdownCard(String title, String[] labels, long[] values, String accentColor) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(20));
        card.setStyle(Theme.card());

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        VBox items = new VBox(12);

        long total = 0;
        for (long v : values) {
            total += v;
        }

        for (int i = 0; i < labels.length; i++) {
            HBox item = createBreakdownItem(labels[i], values[i], total, accentColor, i);
            items.getChildren().add(item);
        }

        card.getChildren().addAll(titleLabel, items);
        return card;
    }

    private HBox createBreakdownItem(String label, long value, long total, String accentColor, int index) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        // Color indicator
        Rectangle colorBar = new Rectangle(4, 24);
        colorBar.setFill(javafx.scene.paint.Color.web(accentColor, 1.0 - (index * 0.25)));
        colorBar.setArcWidth(2);
        colorBar.setArcHeight(2);

        // Label
        VBox labelBox = new VBox(2);
        HBox.setHgrow(labelBox, Priority.ALWAYS);

        Label nameLabel = new Label(label);
        nameLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        // Progress bar
        double percentage = total > 0 ? (value * 100.0 / total) : 0;
        HBox progressContainer = new HBox();
        progressContainer.setPrefHeight(4);
        progressContainer.setMaxWidth(200);
        progressContainer.setStyle(
            "-fx-background-color: " + Theme.BG_MUTED + ";" +
            "-fx-background-radius: 2;"
        );

        Region progressBar = new Region();
        progressBar.setPrefWidth(percentage * 2);
        progressBar.setPrefHeight(4);
        progressBar.setStyle(
            "-fx-background-color: " + accentColor + ";" +
            "-fx-background-radius: 2;"
        );
        progressContainer.getChildren().add(progressBar);

        labelBox.getChildren().addAll(nameLabel, progressContainer);

        // Value
        VBox valueBox = new VBox(2);
        valueBox.setAlignment(Pos.CENTER_RIGHT);

        Label valueLabel = new Label(Theme.formatAmount(value));
        valueLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Label percentLabel = new Label(String.format("%.1f%%", percentage));
        percentLabel.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        valueBox.getChildren().addAll(valueLabel, percentLabel);

        item.getChildren().addAll(colorBar, labelBox, valueBox);
        return item;
    }

    private VBox createInfoSection() {
        VBox section = new VBox(16);
        section.setPadding(new Insets(20));
        section.setStyle(Theme.card());

        HBox headerRow = new HBox(12);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Circle infoIcon = new Circle(12);
        infoIcon.setFill(javafx.scene.paint.Color.web(Theme.ACCENT_PRIMARY));

        Label sectionTitle = new Label("Πληροφορίες Συστήματος");
        sectionTitle.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        headerRow.getChildren().addAll(infoIcon, sectionTitle);

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(12);
        grid.setPadding(new Insets(12, 0, 0, 0));

        int revenueCount = BudgetRevenue.getAllBudgetRevenues().size();
        int regularRevenueCount = RegularBudgetRevenue.getAllRegularBudgetRevenues().size();
        int publicInvestmentRevenueCount = PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues().size();
        int expenseCount = RegularBudgetExpense.getAllRegularBudgetExpenses().size() +
                          PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses().size();

        addInfoRow(grid, 0, "Συνολικές Εγγραφές Εσόδων", String.valueOf(revenueCount));
        addInfoRow(grid, 1, "Εγγραφές Τακτικού Προϋπολογισμού", String.valueOf(regularRevenueCount));
        addInfoRow(grid, 2, "Εγγραφές ΠΔΕ", String.valueOf(publicInvestmentRevenueCount));
        addInfoRow(grid, 3, "Συνολικές Εγγραφές Εξόδων", String.valueOf(expenseCount));

        // Hint
        Label hint = new Label("Χρησιμοποιήστε την πλοήγηση για να εξερευνήσετε τα δεδομένα του προϋπολογισμού");
        hint.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";" +
            "-fx-padding: 12 0 0 0;"
        );

        section.getChildren().addAll(headerRow, grid, hint);
        return section;
    }

    private void addInfoRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle(
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";" +
            "-fx-font-size: 13px;"
        );

        Label valueNode = new Label(value);
        valueNode.setStyle(
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;"
        );

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private void animateEntrance() {
        view.setOpacity(0);

        FadeTransition fade = new FadeTransition(Duration.millis(300), view);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(100));
        fade.play();
    }

    public Region getView() {
        return scrollPane;
    }
}
