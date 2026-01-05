package com.financial.ui.views;

import com.financial.pdf.BudgetRevenueConvertToPdf;
import com.financial.pdf.BudgetExpenseConvertToPdf;
import com.financial.ui.Theme;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
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
 * Export view - Apple-like immersive design for PDF export.
 * Clean, crisp layout with subtle animations.
 */
public class ExportView {

    private final ScrollPane scrollPane;
    private final VBox view;
    private VBox statusSection;

    public ExportView() {
        view = new VBox(40);
        view.setPadding(new Insets(48, 56, 56, 56));
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Export options as clean cards
        VBox exportOptions = createExportOptions();

        // Status section (initially empty)
        statusSection = createStatusSection();

        // Info section
        VBox infoSection = createInfoSection();

        view.getChildren().addAll(header, exportOptions, statusSection, infoSection);

        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
            "-fx-background: " + Theme.BG_BASE + ";" +
            "-fx-background-color: " + Theme.BG_BASE + ";" +
            "-fx-border-color: transparent;"
        );

        // Entrance animation
        animateEntrance();
    }

    private VBox createHeader() {
        VBox header = new VBox(6);

        Label title = new Label("Εξαγωγή PDF");
        title.setStyle(
            "-fx-font-size: 34px;" +
            "-fx-font-weight: 700;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Label subtitle = new Label("Δημιουργία εγγράφων για τον κρατικό προϋπολογισμό");
        subtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private VBox createExportOptions() {
        VBox container = new VBox(20);

        // Three export cards with staggered animation
        HBox cardsRow = new HBox(20);

        VBox revenueCard = createExportCard(
            "Έσοδα",
            "Εξαγωγή εσόδων κρατικού προϋπολογισμού",
            Theme.SUCCESS_LIGHT,
            "↓",
            this::exportRevenues,
            0
        );

        VBox expenseCard = createExportCard(
            "Έξοδα",
            "Εξαγωγή εξόδων κρατικού προϋπολογισμού",
            Theme.ERROR_LIGHT,
            "↓",
            this::exportExpenses,
            100
        );

        VBox fullCard = createExportCard(
            "Πλήρης",
            "Εξαγωγή εσόδων και εξόδων",
            Theme.ACCENT_BRIGHT,
            "⇊",
            this::exportFullBudget,
            200
        );

        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(expenseCard, Priority.ALWAYS);
        HBox.setHgrow(fullCard, Priority.ALWAYS);

        cardsRow.getChildren().addAll(revenueCard, expenseCard, fullCard);
        container.getChildren().add(cardsRow);

        return container;
    }

    private VBox createExportCard(String title, String description, String accentColor,
                                   String iconChar, Runnable action, int delayMs) {
        VBox card = new VBox(16);
        card.setPadding(new Insets(28, 32, 28, 32));
        card.setAlignment(Pos.CENTER);
        card.setCursor(Cursor.HAND);

        String baseStyle = "-fx-background-color: rgba(255,255,255,0.02);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-radius: 18;";
        card.setStyle(baseStyle);

        // Initial state for animation
        card.setOpacity(0);
        card.setTranslateY(20);

        // Icon container with accent background
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(56, 56);
        iconContainer.setMinSize(56, 56);

        Rectangle iconBg = new Rectangle(56, 56);
        iconBg.setFill(Color.web(accentColor, 0.12));
        iconBg.setArcWidth(16);
        iconBg.setArcHeight(16);

        Label iconLabel = new Label(iconChar);
        iconLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + accentColor + ";"
        );

        iconContainer.getChildren().addAll(iconBg, iconLabel);

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);

        // Export button - minimal pill style
        HBox button = createExportButton(accentColor);

        card.getChildren().addAll(iconContainer, titleLabel, descLabel, button);

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

        // Interactive hover effects
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

        // Click effect
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

        // Click action
        card.setOnMouseClicked(e -> action.run());

        return card;
    }

    private HBox createExportButton(String accentColor) {
        HBox button = new HBox(8);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(10, 20, 10, 20));
        button.setStyle(
            "-fx-background-color: " + accentColor + ";" +
            "-fx-background-radius: 20;"
        );

        Label label = new Label("Εξαγωγή");
        label.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.BG_BASE + ";"
        );

        button.getChildren().add(label);
        return button;
    }

    private VBox createStatusSection() {
        VBox section = new VBox(8);
        section.setId("status-section");
        return section;
    }

    private VBox createInfoSection() {
        VBox section = new VBox(16);
        section.setPadding(new Insets(28, 32, 28, 32));
        section.setCursor(Cursor.HAND);

        String baseStyle = "-fx-background-color: rgba(255,255,255,0.02);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-radius: 18;";
        section.setStyle(baseStyle);

        // Initial state for animation
        section.setOpacity(0);
        section.setTranslateY(20);

        // Animate entrance
        FadeTransition fade = new FadeTransition(Duration.millis(500), section);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis(400));

        TranslateTransition slide = new TranslateTransition(Duration.millis(500), section);
        slide.setFromY(20);
        slide.setToY(0);
        slide.setDelay(Duration.millis(400));
        slide.setInterpolator(Interpolator.EASE_OUT);

        fade.play();
        slide.play();

        // Header
        Label infoTitle = new Label("Πληροφορίες");
        infoTitle.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        // Info items
        VBox items = new VBox(12);

        items.getChildren().addAll(
            createInfoItem("Αποθήκευση", "Τα αρχεία αποθηκεύονται στο output/", Theme.ACCENT_BRIGHT),
            createInfoItem("Μορφή", "Αναλυτικά στοιχεία ανά κατηγορία", Theme.INFO),
            createInfoItem("Χρήση", "Κατάλληλα για επίσημη χρήση", Theme.SUCCESS_LIGHT)
        );

        section.getChildren().addAll(infoTitle, items);

        // Hover effect
        String hoverStyle = "-fx-background-color: rgba(255,255,255,0.04);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-border-radius: 18;";

        section.setOnMouseEntered(e -> {
            section.setStyle(hoverStyle);
            TranslateTransition lift = new TranslateTransition(Duration.millis(200), section);
            lift.setToY(-2);
            lift.play();
        });

        section.setOnMouseExited(e -> {
            section.setStyle(baseStyle);
            TranslateTransition lift = new TranslateTransition(Duration.millis(200), section);
            lift.setToY(0);
            lift.play();
        });

        return section;
    }

    private HBox createInfoItem(String label, String value, String accentColor) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        // Accent dot
        Rectangle dot = new Rectangle(6, 6);
        dot.setFill(Color.web(accentColor));
        dot.setArcWidth(6);
        dot.setArcHeight(6);

        // Label
        Label labelNode = new Label(label);
        labelNode.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );
        labelNode.setMinWidth(80);

        // Value
        Label valueNode = new Label(value);
        valueNode.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        item.getChildren().addAll(dot, labelNode, valueNode);

        // Hover highlight
        item.setOnMouseEntered(e -> {
            labelNode.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
            );
            valueNode.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + accentColor + ";"
            );
        });

        item.setOnMouseExited(e -> {
            labelNode.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: 500;" +
                "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
            );
            valueNode.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
            );
        });

        return item;
    }

    private void exportRevenues() {
        try {
            BudgetRevenueConvertToPdf.createPdf("data/output/ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα: " + e.getMessage(), false);
        }
    }

    private void exportExpenses() {
        try {
            BudgetExpenseConvertToPdf.createPdf("data/output/ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα: " + e.getMessage(), false);
        }
    }

    private void exportFullBudget() {
        try {
            BudgetRevenueConvertToPdf.createPdf("data/output/ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            BudgetExpenseConvertToPdf.createPdf("data/output/ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Τα αρχεία PDF δημιουργήθηκαν επιτυχώς!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα: " + e.getMessage(), false);
        }
    }

    private void showStatus(String message, boolean success) {
        statusSection.getChildren().clear();

        HBox statusBox = new HBox(12);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(16, 20, 16, 20));

        String bgColor = success ? "rgba(63, 185, 80, 0.1)" : "rgba(248, 81, 73, 0.1)";
        String borderColor = success ? "rgba(63, 185, 80, 0.2)" : "rgba(248, 81, 73, 0.2)";
        String textColor = success ? Theme.SUCCESS_LIGHT : Theme.ERROR_LIGHT;

        statusBox.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-radius: 12;"
        );

        // Icon
        Label icon = new Label(success ? "✓" : "!");
        icon.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + textColor + ";"
        );

        // Message
        Label text = new Label(message);
        text.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-font-weight: 500;" +
            "-fx-text-fill: " + textColor + ";"
        );

        statusBox.getChildren().addAll(icon, text);

        // Animate in
        statusBox.setOpacity(0);
        statusBox.setTranslateY(-10);

        statusSection.getChildren().add(statusBox);

        FadeTransition fade = new FadeTransition(Duration.millis(300), statusBox);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(300), statusBox);
        slide.setFromY(-10);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        fade.play();
        slide.play();
    }

    private void animateEntrance() {
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
