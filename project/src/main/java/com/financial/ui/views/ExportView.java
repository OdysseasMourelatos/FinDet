package com.financial.ui.views;

import com.financial.pdf.BudgetRevenueConvertToPdf;
import com.financial.pdf.BudgetExpenseConvertToPdf;
import com.financial.ui.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * Export view - export budget data to PDF with clean Apple-like design.
 */
public class ExportView {

    private final ScrollPane scrollPane;
    private final VBox view;
    private final Label statusLabel;

    public ExportView() {
        view = new VBox(24);
        view.setPadding(new Insets(32, 24, 24, 24));
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Export cards container
        VBox cardsContainer = new VBox(16);
        cardsContainer.setPadding(new Insets(0, 0, 16, 0));

        // Export cards
        VBox revenueCard = createExportCard(
            "Έσοδα Προϋπολογισμού",
            "Εξαγωγή όλων των εσόδων κρατικού προϋπολογισμού σε μορφή PDF",
            Theme.SUCCESS_LIGHT,
            "+",
            this::exportRevenues
        );

        VBox expenseCard = createExportCard(
            "Έξοδα Προϋπολογισμού",
            "Εξαγωγή όλων των εξόδων κρατικού προϋπολογισμού σε μορφή PDF",
            Theme.ERROR_LIGHT,
            "-",
            this::exportExpenses
        );

        VBox fullCard = createExportCard(
            "Πλήρης Προϋπολογισμός",
            "Εξαγωγή εσόδων και εξόδων σε ξεχωριστά αρχεία PDF",
            Theme.ACCENT_BRIGHT,
            "=",
            this::exportFullBudget
        );

        cardsContainer.getChildren().addAll(revenueCard, expenseCard, fullCard);

        // Status section
        VBox statusSection = createStatusSection();

        // Info section
        VBox infoSection = createInfoSection();

        view.getChildren().addAll(header, cardsContainer, statusSection, infoSection);

        // Wrap in scroll pane
        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
            "-fx-background: " + Theme.BG_BASE + ";" +
            "-fx-background-color: " + Theme.BG_BASE + ";" +
            "-fx-border-color: transparent;"
        );

        // Initialize status
        statusLabel = new Label("");
    }

    private VBox createHeader() {
        VBox header = new VBox(6);

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.ACCENT_LIGHT, 0.15));

        Label iconText = new Label(">");
        iconText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";"
        );

        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Εξαγωγή Δεδομένων");
        title.setStyle(Theme.pageTitle());

        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Δημιουργία εγγράφων PDF για τον κρατικό προϋπολογισμό");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private VBox createExportCard(String title, String description, String accentColor, String iconChar, Runnable action) {
        VBox card = new VBox(0);
        card.setStyle(Theme.card());

        HBox content = new HBox(16);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(20));

        // Icon
        StackPane iconBox = new StackPane();
        iconBox.setPrefSize(48, 48);
        iconBox.setMinSize(48, 48);

        Circle iconBg = new Circle(24);
        iconBg.setFill(javafx.scene.paint.Color.web(accentColor, 0.15));

        Label iconLabel = new Label(iconChar);
        iconLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + accentColor + ";"
        );

        iconBox.getChildren().addAll(iconBg, iconLabel);

        // Text
        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Label descLabel = new Label(description);
        descLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );
        descLabel.setWrapText(true);

        textBox.getChildren().addAll(titleLabel, descLabel);

        // Button
        Button exportBtn = new Button("Εξαγωγή PDF");
        exportBtn.setStyle(
            "-fx-background-color: " + accentColor + ";" +
            "-fx-text-fill: " + Theme.BG_BASE + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: " + Theme.RADIUS_SM + ";" +
            "-fx-cursor: hand;"
        );

        String hoverColor = accentColor.equals(Theme.SUCCESS_LIGHT) ? Theme.SUCCESS :
                           accentColor.equals(Theme.ERROR_LIGHT) ? Theme.ERROR : Theme.ACCENT_PRIMARY;

        exportBtn.setOnMouseEntered(e -> exportBtn.setStyle(
            "-fx-background-color: " + hoverColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: " + Theme.RADIUS_SM + ";" +
            "-fx-cursor: hand;"
        ));

        exportBtn.setOnMouseExited(e -> exportBtn.setStyle(
            "-fx-background-color: " + accentColor + ";" +
            "-fx-text-fill: " + Theme.BG_BASE + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: " + Theme.RADIUS_SM + ";" +
            "-fx-cursor: hand;"
        ));

        exportBtn.setOnAction(e -> action.run());

        content.getChildren().addAll(iconBox, textBox, exportBtn);
        card.getChildren().add(content);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(Theme.cardHover()));
        card.setOnMouseExited(e -> card.setStyle(Theme.card()));

        return card;
    }

    private VBox createStatusSection() {
        VBox section = new VBox(8);
        section.setId("status-section");

        return section;
    }

    private VBox createInfoSection() {
        VBox section = new VBox(12);
        section.setPadding(new Insets(20));
        section.setStyle(Theme.card());

        Label infoTitle = new Label("Πληροφορίες Εξαγωγής");
        infoTitle.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        VBox bullets = new VBox(8);
        bullets.setPadding(new Insets(8, 0, 0, 0));

        String[] infos = {
            "Τα αρχεία PDF αποθηκεύονται στον φάκελο output/",
            "Περιλαμβάνουν αναλυτικά στοιχεία ανά κατηγορία",
            "Μορφοποίηση κατάλληλη για επίσημη χρήση"
        };

        for (String info : infos) {
            HBox bullet = new HBox(8);
            bullet.setAlignment(Pos.CENTER_LEFT);

            Label dot = new Label("•");
            dot.setStyle("-fx-text-fill: " + Theme.ACCENT_BRIGHT + "; -fx-font-size: 14px;");

            Label text = new Label(info);
            text.setStyle("-fx-text-fill: " + Theme.TEXT_SECONDARY + "; -fx-font-size: 13px;");

            bullet.getChildren().addAll(dot, text);
            bullets.getChildren().add(bullet);
        }

        section.getChildren().addAll(infoTitle, bullets);
        return section;
    }

    private void exportRevenues() {
        try {
            BudgetRevenueConvertToPdf.createPdf("output/ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε επιτυχώς!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα κατά την εξαγωγή: " + e.getMessage(), false);
        }
    }

    private void exportExpenses() {
        try {
            BudgetExpenseConvertToPdf.createPdf("output/ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε επιτυχώς!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα κατά την εξαγωγή: " + e.getMessage(), false);
        }
    }

    private void exportFullBudget() {
        try {
            BudgetRevenueConvertToPdf.createPdf("output/ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            BudgetExpenseConvertToPdf.createPdf("output/ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Τα αρχεία PDF δημιουργήθηκαν επιτυχώς!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα κατά την εξαγωγή: " + e.getMessage(), false);
        }
    }

    private void showStatus(String message, boolean success) {
        // Find status section and update
        VBox statusSection = (VBox) view.lookup("#status-section");
        if (statusSection != null) {
            statusSection.getChildren().clear();

            HBox statusBox = new HBox(12);
            statusBox.setAlignment(Pos.CENTER_LEFT);
            statusBox.setPadding(new Insets(16));
            statusBox.setStyle(
                "-fx-background-color: " + (success ? Theme.SUCCESS_SUBTLE : Theme.ERROR_SUBTLE) + ";" +
                "-fx-background-radius: " + Theme.RADIUS_MD + ";"
            );

            Label icon = new Label(success ? "+" : "!");
            icon.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: " + (success ? Theme.SUCCESS_LIGHT : Theme.ERROR_LIGHT) + ";"
            );

            Label text = new Label(message);
            text.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + (success ? Theme.SUCCESS_LIGHT : Theme.ERROR_LIGHT) + ";"
            );

            statusBox.getChildren().addAll(icon, text);
            statusSection.getChildren().add(statusBox);
        }
    }

    public Region getView() {
        return scrollPane;
    }
}
