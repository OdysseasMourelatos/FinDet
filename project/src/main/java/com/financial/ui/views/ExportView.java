package com.financial.ui.views;

import com.financial.pdf.BudgetRevenueConvertToPdf;
import com.financial.pdf.BudgetExpenseConvertToPdf;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Export view - export budget data to PDF.
 */
public class ExportView {

    private final VBox view;
    private final Label statusLabel;

    public ExportView() {
        view = new VBox(30);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #11111b;");

        // Title
        Label title = new Label("Εξαγωγή Δεδομένων");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        Label subtitle = new Label("Εξαγωγή προϋπολογισμού σε PDF");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c7086;");

        // Export options
        VBox optionsContainer = new VBox(20);
        optionsContainer.setPadding(new Insets(30));
        optionsContainer.setStyle("-fx-background-color: #1e1e2e; -fx-background-radius: 12;");

        // Revenue export
        HBox revenueExport = createExportOption(
            "Έσοδα Προϋπολογισμού",
            "Εξαγωγή όλων των εσόδων σε PDF",
            "#a6e3a1",
            this::exportRevenues
        );

        // Expense export
        HBox expenseExport = createExportOption(
            "Έξοδα Προϋπολογισμού",
            "Εξαγωγή όλων των εξόδων σε PDF",
            "#f38ba8",
            this::exportExpenses
        );

        // Full budget export
        HBox fullExport = createExportOption(
            "Πλήρης Προϋπολογισμός",
            "Εξαγωγή εσόδων και εξόδων",
            "#89b4fa",
            this::exportFullBudget
        );

        optionsContainer.getChildren().addAll(revenueExport, expenseExport, fullExport);

        // Status label
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #a6e3a1;");

        view.getChildren().addAll(title, subtitle, optionsContainer, statusLabel);
    }

    private HBox createExportOption(String title, String description,
                                    String color, Runnable action) {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(15));
        container.setStyle("-fx-background-color: #11111b; -fx-background-radius: 8;");

        // Icon placeholder
        Region icon = new Region();
        icon.setMinSize(40, 40);
        icon.setMaxSize(40, 40);
        icon.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");

        // Text
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #6c7086;");
        textBox.getChildren().addAll(titleLabel, descLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Button
        Button exportBtn = new Button("Εξαγωγή PDF");
        exportBtn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: #1e1e2e; "
            + "-fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 10 20; "
            + "-fx-background-radius: 6; -fx-cursor: hand;");
        exportBtn.setOnAction(e -> action.run());

        container.getChildren().addAll(icon, textBox, spacer, exportBtn);
        return container;
    }

    private void exportRevenues() {
        try {
            BudgetRevenueConvertToPdf.createPdf("output/ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε!", true);
        } catch (Exception e) {
            showStatus("Σφάλμα κατά την εξαγωγή: " + e.getMessage(), false);
        }
    }

    private void exportExpenses() {
        try {
            BudgetExpenseConvertToPdf.createPdf("output/ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
            showStatus("Το αρχείο ΕΞΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf δημιουργήθηκε!", true);
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
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: "
            + (success ? "#a6e3a1" : "#f38ba8") + ";");
    }

    public Region getView() {
        return view;
    }
}
