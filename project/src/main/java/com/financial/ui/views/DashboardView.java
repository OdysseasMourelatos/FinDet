package com.financial.ui.views;

import com.financial.entries.*;
import com.financial.ui.Theme;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Dashboard view - Dynamic, immersive Apple-inspired design.
 * Animated counters, breathing effects, responsive interactions.
 */
public class DashboardView {

    private final ScrollPane scrollPane;
    private final VBox view;

    public DashboardView() {
        view = new VBox(40);
        view.setPadding(new Insets(48, 56, 56, 56));
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        HBox header = createHeader();

        // Animated primary metrics
        HBox primaryMetrics = createPrimaryMetrics();

        // Live secondary stats
        HBox secondaryMetrics = createSecondaryMetrics();

        // Interactive distribution
        VBox distribution = createDistribution();

        view.getChildren().addAll(header, primaryMetrics, secondaryMetrics, distribution);

        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: " + Theme.BG_BASE + ";" +
            "-fx-background-color: " + Theme.BG_BASE + ";" +
            "-fx-border-color: transparent;"
        );

        // Staggered entrance animation
        animateEntrance();
    }

    private HBox createHeader() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(6);

        Label title = new Label("Επισκόπηση");
        title.setStyle(
            "-fx-font-size: 34px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Label subtitle = new Label("Κρατικός Προϋπολογισμός 2025");
        subtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        titleBox.getChildren().addAll(title, subtitle);

        header.getChildren().add(titleBox);
        return header;
    }

    private HBox createPrimaryMetrics() {
        HBox row = new HBox(24);
        row.setAlignment(Pos.TOP_LEFT);

        long totalRevenue = BudgetRevenue.calculateSum();
        long totalExpense = RegularBudgetExpense.calculateSum() + PublicInvestmentBudgetExpense.calculateSum();
        long balance = totalRevenue - totalExpense;

        VBox revenue = createAnimatedMetricCard(
            "Έσοδα",
            totalRevenue,
            Theme.SUCCESS_LIGHT,
            0
        );

        VBox expense = createAnimatedMetricCard(
            "Έξοδα",
            totalExpense,
            Theme.ERROR_LIGHT,
            100
        );

        VBox balanceCard = createAnimatedMetricCard(
            "Ισοζύγιο",
            balance,
            balance >= 0 ? Theme.SUCCESS_LIGHT : Theme.ERROR_LIGHT,
            200
        );

        HBox.setHgrow(revenue, Priority.ALWAYS);
        HBox.setHgrow(expense, Priority.ALWAYS);
        HBox.setHgrow(balanceCard, Priority.ALWAYS);

        row.getChildren().addAll(revenue, expense, balanceCard);
        return row;
    }

    private VBox createAnimatedMetricCard(String label, long targetValue, String accentColor, int delayMs) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(28, 32, 28, 32));
        card.setCursor(Cursor.HAND);

        String baseStyle = "-fx-background-color: rgba(255,255,255,0.02);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-radius: 18;";
        card.setStyle(baseStyle);

        // Label
        Label labelNode = new Label(label);
        labelNode.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        // Animated value counter
        Label valueNode = new Label("0 €");
        valueNode.setStyle(
            "-fx-font-size: 32px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        // Animate counting up
        animateCounter(valueNode, targetValue, delayMs);

        // Accent bar with width animation
        StackPane accentContainer = new StackPane();
        accentContainer.setAlignment(Pos.CENTER_LEFT);

        Rectangle accentBg = new Rectangle(60, 4);
        accentBg.setFill(Color.web("rgba(255,255,255,0.05)"));
        accentBg.setArcWidth(4);
        accentBg.setArcHeight(4);

        Rectangle accent = new Rectangle(0, 4);
        accent.setFill(Color.web(accentColor));
        accent.setArcWidth(4);
        accent.setArcHeight(4);

        // Animate accent bar
        Timeline accentAnim = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(accent.widthProperty(), 0)),
            new KeyFrame(Duration.millis(800),
                new KeyValue(accent.widthProperty(), 40, Interpolator.EASE_OUT))
        );
        accentAnim.setDelay(Duration.millis(delayMs + 300));
        accentAnim.play();

        accentContainer.getChildren().addAll(accentBg, accent);

        card.getChildren().addAll(labelNode, valueNode, accentContainer);

        // Interactive hover with scale and glow
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.04);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: " + accentColor + "40;" +
            "-fx-border-radius: 18;";

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(accentColor, 0.15));
        shadow.setRadius(20);
        shadow.setOffsetY(4);

        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setEffect(shadow);

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();

            TranslateTransition lift = new TranslateTransition(Duration.millis(200), card);
            lift.setToY(-4);
            lift.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(baseStyle);
            card.setEffect(null);

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            TranslateTransition lift = new TranslateTransition(Duration.millis(200), card);
            lift.setToY(0);
            lift.play();
        });

        // Click ripple effect
        card.setOnMousePressed(e -> {
            ScaleTransition press = new ScaleTransition(Duration.millis(100), card);
            press.setToX(0.98);
            press.setToY(0.98);
            press.play();
        });

        card.setOnMouseReleased(e -> {
            ScaleTransition release = new ScaleTransition(Duration.millis(100), card);
            release.setToX(1.02);
            release.setToY(1.02);
            release.play();
        });

        return card;
    }

    private void animateCounter(Label label, long targetValue, int delayMs) {
        Timeline timeline = new Timeline();
        int frames = 40;
        long duration = 1200;

        for (int i = 0; i <= frames; i++) {
            double progress = (double) i / frames;
            // Ease out cubic
            double eased = 1 - Math.pow(1 - progress, 3);
            long currentValue = (long) (targetValue * eased);

            KeyFrame keyFrame = new KeyFrame(
                Duration.millis(delayMs + (duration * i / frames)),
                event -> label.setText(Theme.formatAmount(currentValue))
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    private HBox createSecondaryMetrics() {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));

        int records = BudgetRevenue.getAllBudgetRevenues().size();
        int entities = Entity.getEntities().size();
        long pde = PublicInvestmentBudgetRevenue.calculateSum();

        row.getChildren().addAll(
            createInteractiveChip("Εγγραφές", String.valueOf(records), Theme.ACCENT_BRIGHT, 300),
            createInteractiveChip("Φορείς", String.valueOf(entities), Theme.INFO, 400),
            createInteractiveChip("ΠΔΕ", Theme.formatAmount(pde), Theme.WARNING_LIGHT, 500)
        );

        return row;
    }

    private HBox createInteractiveChip(String label, String value, String accentColor, int delayMs) {
        HBox chip = new HBox(10);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setPadding(new Insets(12, 18, 12, 18));
        chip.setCursor(Cursor.HAND);

        String baseStyle = "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-radius: 12;";
        chip.setStyle(baseStyle);

        // Initial state - hidden
        chip.setOpacity(0);
        chip.setTranslateY(10);

        Label labelNode = new Label(label);
        labelNode.setStyle(
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";" +
            "-fx-font-weight: 500;"
        );

        Label valueNode = new Label(value);
        valueNode.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + accentColor + ";" +
            "-fx-font-weight: 600;"
        );

        chip.getChildren().addAll(labelNode, valueNode);

        // Entrance animation
        FadeTransition fade = new FadeTransition(Duration.millis(400), chip);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delayMs));

        TranslateTransition slide = new TranslateTransition(Duration.millis(400), chip);
        slide.setFromY(10);
        slide.setToY(0);
        slide.setDelay(Duration.millis(delayMs));
        slide.setInterpolator(Interpolator.EASE_OUT);

        fade.play();
        slide.play();

        // Hover effect
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.06);" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + accentColor + "30;" +
            "-fx-border-radius: 12;";

        chip.setOnMouseEntered(e -> {
            chip.setStyle(hoverStyle);
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), chip);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        chip.setOnMouseExited(e -> {
            chip.setStyle(baseStyle);
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), chip);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return chip;
    }

    private VBox createDistribution() {
        VBox section = new VBox(20);

        Label sectionLabel = new Label("Κατανομή Προϋπολογισμού");
        sectionLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        HBox cards = new HBox(20);

        VBox revenueCard = createDistributionCard(
            "Έσοδα",
            new String[]{"Τακτικός", "ΠΔΕ Εθνικό", "ΠΔΕ Συγχρ."},
            new long[]{
                RegularBudgetRevenue.calculateSum(),
                PublicInvestmentBudgetNationalRevenue.calculateSum(),
                PublicInvestmentBudgetCoFundedRevenue.calculateSum()
            },
            Theme.SUCCESS_LIGHT,
            700
        );

        VBox expenseCard = createDistributionCard(
            "Έξοδα",
            new String[]{"Τακτικός", "Δημ. Επενδύσεις"},
            new long[]{
                RegularBudgetExpense.calculateSum(),
                PublicInvestmentBudgetExpense.calculateSum()
            },
            Theme.ERROR_LIGHT,
            800
        );

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);

        cards.getChildren().addAll(revenueCard, expenseCard);
        section.getChildren().addAll(sectionLabel, cards);
        return section;
    }

    private VBox createDistributionCard(String title, String[] labels, long[] values, String accent, int delayMs) {
        VBox card = new VBox(18);
        card.setPadding(new Insets(24, 28, 24, 28));
        card.setCursor(Cursor.HAND);

        String baseStyle = "-fx-background-color: rgba(255,255,255,0.02);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-radius: 18;";
        card.setStyle(baseStyle);

        // Initial state
        card.setOpacity(0);
        card.setTranslateY(20);

        // Entrance animation
        FadeTransition fade = new FadeTransition(Duration.millis(500), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(delayMs));

        TranslateTransition slide = new TranslateTransition(Duration.millis(500), card);
        slide.setFromY(20);
        slide.setToY(0);
        slide.setDelay(Duration.millis(delayMs));
        slide.setInterpolator(Interpolator.EASE_OUT);

        fade.play();
        slide.play();

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        long total = 0;
        for (long v : values) {
            total += v;
        }

        Label totalLabel = new Label(Theme.formatAmount(total));
        totalLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: " + accent + ";"
        );

        header.getChildren().addAll(titleLabel, spacer, totalLabel);

        // Items with animated bars
        VBox items = new VBox(14);
        for (int i = 0; i < labels.length; i++) {
            items.getChildren().add(createAnimatedDistributionRow(
                labels[i], values[i], total, accent, i, delayMs + 200 + (i * 100)
            ));
        }

        card.getChildren().addAll(header, items);

        // Hover
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.04);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: " + accent + "30;" +
            "-fx-border-radius: 18;";

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(accent, 0.1));
        shadow.setRadius(16);
        shadow.setOffsetY(4);

        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setEffect(shadow);
            TranslateTransition lift = new TranslateTransition(Duration.millis(200), card);
            lift.setToY(-3);
            lift.play();
        });

        card.setOnMouseExited(e -> {
            card.setStyle(baseStyle);
            card.setEffect(null);
            TranslateTransition lift = new TranslateTransition(Duration.millis(200), card);
            lift.setToY(0);
            lift.play();
        });

        return card;
    }

    private HBox createAnimatedDistributionRow(String label, long value, long total, String accent, int idx, int delayMs) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(label);
        nameLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );
        nameLabel.setMinWidth(120);

        // Animated progress bar
        StackPane progressContainer = new StackPane();
        progressContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(progressContainer, Priority.ALWAYS);

        Rectangle bg = new Rectangle(200, 6);
        bg.setFill(Color.web("rgba(255,255,255,0.04)"));
        bg.setArcWidth(6);
        bg.setArcHeight(6);
        bg.widthProperty().bind(progressContainer.widthProperty());

        double pct = total > 0 ? (value * 100.0 / total) : 0;
        Rectangle bar = new Rectangle(0, 6);
        double opacity = 1.0 - (idx * 0.2);
        bar.setFill(Color.web(accent, opacity));
        bar.setArcWidth(6);
        bar.setArcHeight(6);

        // Animate bar width
        Timeline barAnim = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(bar.widthProperty(), 0)),
            new KeyFrame(Duration.millis(600),
                new KeyValue(bar.widthProperty(), Math.max(pct * 1.8, 8), Interpolator.EASE_OUT))
        );
        barAnim.setDelay(Duration.millis(delayMs));
        barAnim.play();

        progressContainer.getChildren().addAll(bg, bar);

        // Percentage label
        Label pctLabel = new Label(String.format("%.0f%%", pct));
        pctLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );
        pctLabel.setMinWidth(40);
        pctLabel.setAlignment(Pos.CENTER_RIGHT);

        row.getChildren().addAll(nameLabel, progressContainer, pctLabel);

        // Row hover highlight
        row.setOnMouseEntered(e -> {
            nameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");
            pctLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: " + accent + ";");
        });
        row.setOnMouseExited(e -> {
            nameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");
            pctLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: " + Theme.TEXT_MUTED + ";");
        });

        return row;
    }

    private void animateEntrance() {
        // Staggered fade in for the whole view
        view.setOpacity(0);

        FadeTransition viewFade = new FadeTransition(Duration.millis(600), view);
        viewFade.setFromValue(0);
        viewFade.setToValue(1);
        viewFade.setDelay(Duration.millis(50));
        viewFade.play();
    }

    public Region getView() {
        return scrollPane;
    }
}
