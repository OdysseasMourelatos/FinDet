package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetNationalRevenue;
import com.financial.entries.PublicInvestmentBudgetCoFundedRevenue;
import com.financial.ui.Theme;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Budget Changes view - allows users to make changes to revenue accounts
 * with gamified feedback showing before/after values.
 */
public class BudgetChangesView {

    private final ScrollPane scrollPane;
    private final VBox view;

    // Form components
    private ComboBox<String> budgetTypeCombo;
    private TextField accountCodeField;
    private TextField changeValueField;
    private ComboBox<String> changeTypeCombo;
    private ComboBox<String> distributionCombo;
    private Button executeButton;
    private Label statusLabel;

    // Results area
    private VBox resultsContainer;
    private TableView<ChangeResult> resultsTable;
    private ObservableList<ChangeResult> resultsData;

    public BudgetChangesView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Form
        VBox formSection = createFormSection();

        // Results
        resultsContainer = createResultsSection();
        VBox.setVgrow(resultsContainer, Priority.ALWAYS);

        view.getChildren().addAll(header, formSection, resultsContainer);

        // Wrap in scroll pane
        scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
            "-fx-background: " + Theme.BG_BASE + ";" +
            "-fx-background-color: " + Theme.BG_BASE + ";" +
            "-fx-border-color: transparent;"
        );
    }

    private VBox createHeader() {
        VBox header = new VBox(6);
        header.setPadding(new Insets(32, 24, 24, 24));

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.WARNING, 0.15));

        Label iconText = new Label("~");
        iconText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.WARNING_LIGHT + ";"
        );

        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Αλλαγές Προϋπολογισμού");
        title.setStyle(Theme.pageTitle());

        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Εφαρμογή αλλαγών σε λογαριασμούς με αυτόματη ενημέρωση ιεραρχίας");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private VBox createFormSection() {
        VBox section = new VBox(16);
        section.setPadding(new Insets(0, 24, 24, 24));

        VBox formCard = new VBox(20);
        formCard.setPadding(new Insets(20));
        formCard.setStyle(Theme.card());

        // Form title
        Label formTitle = new Label("Παράμετροι Αλλαγής");
        formTitle.setStyle(Theme.sectionHeader());

        // Row 1: Budget type and Account code
        HBox row1 = new HBox(16);
        row1.setAlignment(Pos.CENTER_LEFT);

        VBox budgetTypeBox = createFormField("Τύπος Προϋπολογισμού");
        budgetTypeCombo = new ComboBox<>();
        budgetTypeCombo.getItems().addAll(
            "Τακτικός Προϋπολογισμός",
            "ΠΔΕ Εθνικό",
            "ΠΔΕ Συγχρηματοδοτούμενο"
        );
        budgetTypeCombo.setValue("Τακτικός Προϋπολογισμός");
        budgetTypeCombo.setPrefWidth(220);
        budgetTypeCombo.setStyle(Theme.comboBox());
        budgetTypeBox.getChildren().add(budgetTypeCombo);

        VBox codeBox = createFormField("Κωδικός Λογαριασμού");
        accountCodeField = new TextField();
        accountCodeField.setPromptText("π.χ. 11, 111, 11101");
        accountCodeField.setPrefWidth(180);
        accountCodeField.setStyle(Theme.textField());
        accountCodeField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            accountCodeField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });
        codeBox.getChildren().add(accountCodeField);

        row1.getChildren().addAll(budgetTypeBox, codeBox);

        // Row 2: Change value, type, and distribution
        HBox row2 = new HBox(16);
        row2.setAlignment(Pos.CENTER_LEFT);

        VBox changeBox = createFormField("Τιμή Αλλαγής");
        changeValueField = new TextField();
        changeValueField.setPromptText("π.χ. 10000, 10%");
        changeValueField.setPrefWidth(140);
        changeValueField.setStyle(Theme.textField());
        changeValueField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            changeValueField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });
        changeBox.getChildren().add(changeValueField);

        VBox changeTypeBox = createFormField("Τύπος Αλλαγής");
        changeTypeCombo = new ComboBox<>();
        changeTypeCombo.getItems().addAll(
            "Μεταβολή (+/-)",
            "Ποσοστό (%)",
            "Τελικό Υπόλοιπο"
        );
        changeTypeCombo.setValue("Μεταβολή (+/-)");
        changeTypeCombo.setPrefWidth(160);
        changeTypeCombo.setStyle(Theme.comboBox());
        changeTypeBox.getChildren().add(changeTypeCombo);

        VBox distBox = createFormField("Κατανομή σε Υποκατηγορίες");
        distributionCombo = new ComboBox<>();
        distributionCombo.getItems().addAll(
            "Ισόποσα",
            "Ποσοστιαία"
        );
        distributionCombo.setValue("Ποσοστιαία");
        distributionCombo.setPrefWidth(140);
        distributionCombo.setStyle(Theme.comboBox());
        distBox.getChildren().add(distributionCombo);

        row2.getChildren().addAll(changeBox, changeTypeBox, distBox);

        // Button row
        HBox buttonRow = new HBox(16);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
        buttonRow.setPadding(new Insets(8, 0, 0, 0));

        executeButton = new Button("Εκτέλεση Αλλαγής");
        executeButton.setStyle(Theme.buttonPrimary());
        executeButton.setOnMouseEntered(e -> executeButton.setStyle(Theme.buttonPrimaryHover()));
        executeButton.setOnMouseExited(e -> executeButton.setStyle(Theme.buttonPrimary()));
        executeButton.setOnAction(e -> executeChange());

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.TEXT_SECONDARY + ";");

        buttonRow.getChildren().addAll(executeButton, statusLabel);

        formCard.getChildren().addAll(formTitle, row1, row2, buttonRow);
        section.getChildren().add(formCard);
        return section;
    }

    private VBox createFormField(String labelText) {
        VBox field = new VBox(6);
        Label label = new Label(labelText);
        label.setStyle(Theme.mutedText());
        field.getChildren().add(label);
        return field;
    }

    private VBox createResultsSection() {
        VBox section = new VBox(0);
        section.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(section, Priority.ALWAYS);

        VBox resultsCard = new VBox(16);
        resultsCard.setPadding(new Insets(20));
        resultsCard.setStyle(Theme.card());
        VBox.setVgrow(resultsCard, Priority.ALWAYS);

        Label resultsTitle = new Label("Αποτελέσματα Αλλαγών");
        resultsTitle.setStyle(Theme.sectionHeader());

        resultsData = FXCollections.observableArrayList();
        resultsTable = createResultsTable();
        VBox.setVgrow(resultsTable, Priority.ALWAYS);

        resultsCard.getChildren().addAll(resultsTitle, resultsTable);
        section.getChildren().add(resultsCard);
        return section;
    }

    private TableView<ChangeResult> createResultsTable() {
        TableView<ChangeResult> table = new TableView<>(resultsData);
        table.setStyle(Theme.table());

        Label placeholder = new Label("Εκτελέστε μια αλλαγή για να δείτε αποτελέσματα");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        table.setPlaceholder(placeholder);

        TableColumn<ChangeResult, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().code));
        codeCol.setPrefWidth(90);

        TableColumn<ChangeResult, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().description));
        descCol.setPrefWidth(220);

        TableColumn<ChangeResult, String> levelCol = new TableColumn<>("Επ.");
        levelCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().level));
        levelCol.setPrefWidth(40);
        levelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<ChangeResult, String> beforeCol = new TableColumn<>("Πριν");
        beforeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().before));
        beforeCol.setPrefWidth(100);
        beforeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> afterCol = new TableColumn<>("Μετά");
        afterCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().after));
        afterCol.setPrefWidth(100);
        afterCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> changeCol = new TableColumn<>("Μεταβολή");
        changeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().change));
        changeCol.setPrefWidth(130);
        changeCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<ChangeResult, String> roleCol = new TableColumn<>("Ρόλος");
        roleCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().role));
        roleCol.setPrefWidth(110);

        table.getColumns().add(codeCol);
        table.getColumns().add(descCol);
        table.getColumns().add(levelCol);
        table.getColumns().add(beforeCol);
        table.getColumns().add(afterCol);
        table.getColumns().add(changeCol);
        table.getColumns().add(roleCol);

        return table;
    }

    private void executeChange() {
        String code = accountCodeField.getText().trim();
        String changeValue = changeValueField.getText().trim();
        String budgetType = budgetTypeCombo.getValue();
        String changeType = changeTypeCombo.getValue();
        String distribution = distributionCombo.getValue();

        if (code.isEmpty()) {
            showError("Παρακαλώ εισάγετε κωδικό λογαριασμού");
            return;
        }
        if (changeValue.isEmpty()) {
            showError("Παρακαλώ εισάγετε τιμή αλλαγής");
            return;
        }

        BudgetRevenue targetRevenue = findRevenue(code, budgetType);
        if (targetRevenue == null) {
            showError("Δεν βρέθηκε λογαριασμός με κωδικό: " + code);
            return;
        }

        try {
            Map<String, Long> beforeValues = captureValues(targetRevenue, budgetType);
            applyChange(targetRevenue, changeValue, changeType, distribution, budgetType);
            Map<String, Long> afterValues = captureValues(targetRevenue, budgetType);

            for (Map.Entry<String, Long> beforeEntry : beforeValues.entrySet()) {
                for (Map.Entry<String, Long> afterEntry : afterValues.entrySet()) {
                    if (beforeEntry.getKey().equals(afterEntry.getKey()) && (beforeEntry.getValue() - afterEntry.getValue()) == 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            // Display results with animation
            displayResults(targetRevenue, beforeValues, afterValues, budgetType);
            showSuccess("Η αλλαγή εφαρμόστηκε επιτυχώς!");
        } catch (NumberFormatException e) {
            showError("Μη έγκυρη τιμή. Χρησιμοποιήστε αριθμό ή ποσοστό (π.χ. 10000 ή 10%)");
        } catch (IllegalArgumentException e) {
            showError("Η αλλαγή απέτυχε, καθώς ένας ή παραπάνω λογαριασμοί θα είχαν αρνητικό υπόλοιπο");
        } catch (Exception e) {
            showError("Σφάλμα: " + e.getMessage());
        }
    }

    private BudgetRevenue findRevenue(String code, String budgetType) {
        return switch (budgetType) {
            case "Τακτικός Προϋπολογισμός" -> RegularBudgetRevenue.findRegularBudgetRevenueWithCode(code);
            case "ΠΔΕ Εθνικό" -> PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(code);
            case "ΠΔΕ Συγχρηματοδοτούμενο" -> PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(code);
            default -> null;
        };
    }

    private Map<String, Long> captureValues(BudgetRevenue revenue, String budgetType) {
        Map<String, Long> values = new HashMap<>();
        values.put(revenue.getCode(), revenue.getAmount());

        ArrayList<BudgetRevenue> superCats = revenue.getAllSuperCategories();
        if (superCats != null) {
            for (BudgetRevenue sup : superCats) {
                values.put(sup.getCode(), sup.getAmount());
            }
        }

        ArrayList<BudgetRevenue> subCats = revenue.getAllSubCategories();
        if (subCats != null) {
            for (BudgetRevenue sub : subCats) {
                values.put(sub.getCode(), sub.getAmount());
            }
        }

        return values;
    }

    private void applyChange(BudgetRevenue revenue, String changeValue, String changeType, String distribution, String budgetType) {
        double numericValue;
        boolean isPercentage = changeValue.contains("%");

        String cleanValue = changeValue.replaceAll("[^0-9.\\-]", "");
        numericValue = Double.parseDouble(cleanValue);

        long changeAmount;
        double percentage = 0;

        if (changeType.equals("Ποσοστό (%)") || isPercentage) {
            percentage = numericValue / 100.0;
            changeAmount = (long) (revenue.getAmount() * percentage);
        } else if (changeType.equals("Τελικό Υπόλοιπο")) {
            changeAmount = (long) numericValue - revenue.getAmount();
            if (revenue.getAmount() != 0) {
                percentage = (double) changeAmount / revenue.getAmount();
            }
        } else {
            changeAmount = (long) numericValue;
            if (revenue.getAmount() != 0) {
                percentage = (double) changeAmount / revenue.getAmount();
            }
        }

        if (distribution.equals("Ισόποσα")) {
            applyEqualDistribution(revenue, changeAmount, budgetType);
        } else {
            applyPercentageDistribution(revenue, percentage, budgetType);
        }
    }

    private void applyEqualDistribution(BudgetRevenue revenue, long changeAmount, String budgetType) {
        if (revenue instanceof RegularBudgetRevenue rbr) {
            rbr.implementChangesOfEqualDistribution(changeAmount);
        } else if (revenue instanceof PublicInvestmentBudgetNationalRevenue pibnr) {
            pibnr.implementChangesOfEqualDistribution(changeAmount);
        } else if (revenue instanceof PublicInvestmentBudgetCoFundedRevenue pibcr) {
            pibcr.implementChangesOfEqualDistribution(changeAmount);
        }
    }

    private void applyPercentageDistribution(BudgetRevenue revenue, double percentage, String budgetType) {
        if (revenue instanceof RegularBudgetRevenue rbr) {
            rbr.implementChangesOfPercentageAdjustment(percentage);
        } else if (revenue instanceof PublicInvestmentBudgetNationalRevenue pibnr) {
            pibnr.implementChangesOfPercentageAdjustment(percentage);
        } else if (revenue instanceof PublicInvestmentBudgetCoFundedRevenue pibcr) {
            pibcr.implementChangesOfPercentageAdjustment(percentage);
        }
    }

    private void displayResults(BudgetRevenue targetRevenue, Map<String, Long> before, Map<String, Long> after, String budgetType) {
        resultsData.clear();

        ArrayList<BudgetRevenue> superCats = targetRevenue.getAllSuperCategories();
        if (superCats != null) {
            for (int i = superCats.size() - 1; i >= 0; i--) {
                BudgetRevenue sup = superCats.get(i);
                addResultRow(sup, before, after, "Ανώτερη");
            }
        }

        addResultRow(targetRevenue, before, after, "* Στόχος");

        ArrayList<BudgetRevenue> subCats = targetRevenue.getAllSubCategories();
        if (subCats != null) {
            for (BudgetRevenue sub : subCats) {
                addResultRow(sub, before, after, "Υποκατηγορία");
            }
        }

        animateResults();
    }

    private void addResultRow(BudgetRevenue revenue, Map<String, Long> before, Map<String, Long> after, String role) {
        Long beforeVal = before.get(revenue.getCode());
        Long afterVal = after.get(revenue.getCode());

        if (beforeVal == null) {
            beforeVal = 0L;
        }
        if (afterVal == null) {
            afterVal = revenue.getAmount();
        }

        long change = afterVal - beforeVal;
        double percentChange = beforeVal != 0 ? ((double) change / beforeVal) * 100 : 0;

        String changeStr;
        if (change >= 0) {
            changeStr = String.format("+%,d (%.1f%%)", change, percentChange);
        } else {
            changeStr = String.format("%,d (%.1f%%)", change, percentChange);
        }

        ChangeResult result = new ChangeResult(
            revenue.getCode(),
            truncateDescription(revenue.getDescription(), 35),
            String.valueOf(revenue.getLevelOfHierarchy()),
            Theme.formatAmount(beforeVal),
            Theme.formatAmount(afterVal),
            changeStr,
            role
        );

        resultsData.add(result);
    }

    private String truncateDescription(String desc, int maxLength) {
        if (desc == null) {
            return "";
        }
        if (desc.length() <= maxLength) {
            return desc;
        }
        return desc.substring(0, maxLength - 3) + "...";
    }

    private void animateResults() {
        resultsTable.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), resultsTable);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(250), resultsTable);
        scale.setFromX(0.98);
        scale.setFromY(0.98);
        scale.setToX(1);
        scale.setToY(1);

        ParallelTransition parallel = new ParallelTransition(fadeIn, scale);
        parallel.play();
    }

    private void showError(String message) {
        statusLabel.setText("! " + message);
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.ERROR_LIGHT + "; -fx-font-weight: 500;");
    }

    private void showSuccess(String message) {
        statusLabel.setText("+ " + message);
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Theme.SUCCESS_LIGHT + "; -fx-font-weight: 500;");
    }

    public Region getView() {
        return scrollPane;
    }

    public static class ChangeResult {
        public final String code;
        public final String description;
        public final String level;
        public final String before;
        public final String after;
        public final String change;
        public final String role;

        public ChangeResult(String code, String description, String level, String before, String after, String change, String role) {
            this.code = code;
            this.description = description;
            this.level = level;
            this.before = before;
            this.after = after;
            this.change = change;
            this.role = role;
        }
    }
}
