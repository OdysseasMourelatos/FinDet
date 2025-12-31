package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Account Explorer view - search by code and display hierarchy.
 */
public class AccountExplorerView {

    // Design constants
    private static final String BG_PRIMARY = "#0a0a0f";
    private static final String BG_SECONDARY = "#12121a";
    private static final String TEXT_PRIMARY = "#e4e4e7";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String BORDER_COLOR = "#27272a";
    private static final String ACCENT = "#3b82f6";

    private final VBox view;
    private TextField searchField;
    private Label selectedAccountLabel;
    private Label levelIndicator;
    private Label statusLabel;
    private TableView<BudgetRevenue> superCategoriesTable;
    private TableView<BudgetRevenue> subCategoriesTable;
    private ObservableList<BudgetRevenue> superCategoriesData;
    private ObservableList<BudgetRevenue> subCategoriesData;

    public AccountExplorerView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        // Header
        VBox header = createHeader();

        // Search section
        HBox searchSection = createSearchSection();

        // Selected account card
        VBox accountCard = createAccountCard();

        // Tables section
        HBox tablesSection = createTablesSection();

        // Status bar
        HBox statusBar = createStatusBar();

        view.getChildren().addAll(header, searchSection, accountCard, tablesSection, statusBar);
    }

    private VBox createHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(32, 24, 16, 24));

        Label title = new Label("Εξερεύνηση Λογαριασμών");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");

        Label subtitle = new Label("Αναζητήστε κωδικό για να δείτε την ιεραρχία κατηγοριών");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox createSearchSection() {
        HBox section = new HBox(12);
        section.setPadding(new Insets(0, 24, 16, 24));
        section.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Εισάγετε κωδικό (π.χ. 11, 111, 11101)");
        searchField.setPrefWidth(320);
        searchField.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 6;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 14px;"
        );

        Button searchButton = new Button("Αναζήτηση");
        searchButton.setStyle(
            "-fx-background-color: " + ACCENT + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;"
        );
        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());

        section.getChildren().addAll(searchField, searchButton);
        return section;
    }

    private VBox createAccountCard() {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16, 24, 16, 24));

        VBox innerCard = new VBox(8);
        innerCard.setPadding(new Insets(16));
        innerCard.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 8;"
        );

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        levelIndicator = new Label("");
        levelIndicator.setStyle(
            "-fx-background-color: " + ACCENT + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 4 10;" +
            "-fx-background-radius: 4;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: 600;"
        );
        levelIndicator.setVisible(false);

        selectedAccountLabel = new Label("Εισάγετε κωδικό για αναζήτηση");
        selectedAccountLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        topRow.getChildren().addAll(levelIndicator, selectedAccountLabel);
        innerCard.getChildren().add(topRow);
        card.getChildren().add(innerCard);

        return card;
    }

    private HBox createTablesSection() {
        HBox section = new HBox(16);
        section.setPadding(new Insets(0, 24, 16, 24));
        VBox.setVgrow(section, Priority.ALWAYS);

        // Initialize data
        superCategoriesData = FXCollections.observableArrayList();
        subCategoriesData = FXCollections.observableArrayList();

        // Super categories
        VBox superBox = createTableBox("Ανώτερες Κατηγορίες", superCategoriesData, true);
        HBox.setHgrow(superBox, Priority.ALWAYS);

        // Sub categories
        VBox subBox = createTableBox("Υποκατηγορίες", subCategoriesData, false);
        HBox.setHgrow(subBox, Priority.ALWAYS);

        section.getChildren().addAll(superBox, subBox);
        return section;
    }

    private VBox createTableBox(String title, ObservableList<BudgetRevenue> data, boolean isSuper) {
        VBox box = new VBox(8);
        VBox.setVgrow(box, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_SECONDARY + ";");

        TableView<BudgetRevenue> table = createTable(data);
        VBox.setVgrow(table, Priority.ALWAYS);

        if (isSuper) {
            superCategoriesTable = table;
        } else {
            subCategoriesTable = table;
        }

        // Double-click navigation
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                BudgetRevenue selected = table.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    searchField.setText(selected.getCode());
                    performSearch();
                }
            }
        });

        box.getChildren().addAll(titleLabel, table);
        return box;
    }

    private TableView<BudgetRevenue> createTable(ObservableList<BudgetRevenue> data) {
        TableView<BudgetRevenue> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: " + BG_SECONDARY + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + BORDER_COLOR + ";" +
            "-fx-border-radius: 8;"
        );
        table.setPlaceholder(new Label(""));

        TableColumn<BudgetRevenue, String> codeCol = new TableColumn<>("Κωδικός");
        codeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCode()));
        codeCol.setPrefWidth(80);

        TableColumn<BudgetRevenue, String> levelCol = new TableColumn<>("Επ.");
        levelCol.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getLevelOfHierarchy())));
        levelCol.setPrefWidth(40);
        levelCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<BudgetRevenue, String> descCol = new TableColumn<>("Περιγραφή");
        descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
        descCol.setPrefWidth(200);

        TableColumn<BudgetRevenue, String> amountCol = new TableColumn<>("Ποσό");
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(90);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(codeCol, levelCol, descCol, amountCol);
        table.setItems(data);

        return table;
    }

    private HBox createStatusBar() {
        HBox bar = new HBox(24);
        bar.setPadding(new Insets(12, 24, 12, 24));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1 0 0 0;");

        statusLabel = new Label("Διπλό κλικ σε εγγραφή για πλοήγηση");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");

        bar.getChildren().add(statusLabel);
        return bar;
    }

    private void performSearch() {
        String code = searchField.getText().trim();

        if (code.isEmpty()) {
            showError("Παρακαλώ εισάγετε κωδικό");
            return;
        }

        BudgetRevenue revenue = BudgetRevenue.findBudgetRevenueWithCode(code);

        if (revenue == null) {
            showError("Δεν βρέθηκε λογαριασμός με κωδικό: " + code);
            superCategoriesData.clear();
            subCategoriesData.clear();
            return;
        }

        // Show level indicator
        levelIndicator.setText("Επίπεδο " + revenue.getLevelOfHierarchy());
        levelIndicator.setVisible(true);

        // Update account info
        selectedAccountLabel.setText(revenue.getCode() + "  •  " + revenue.getDescription() + "  •  " + formatAmount(revenue.getAmount()));
        selectedAccountLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: " + TEXT_PRIMARY + ";");

        // Get hierarchy
        ArrayList<BudgetRevenue> superCategories = revenue.getAllSuperCategories();
        superCategoriesData.clear();
        if (superCategories != null) {
            superCategoriesData.addAll(superCategories);
        }

        ArrayList<BudgetRevenue> subCategories = revenue.getAllSubCategories();
        subCategoriesData.clear();
        if (subCategories != null) {
            subCategoriesData.addAll(subCategories);
        }

        // Update status
        long subTotal = subCategoriesData.stream().mapToLong(BudgetRevenue::getAmount).sum();
        statusLabel.setText(
            "Ανώτερες: " + superCategoriesData.size() +
            "  •  Υποκατηγορίες: " + subCategoriesData.size() +
            "  •  Σύνολο υποκατηγοριών: " + formatAmount(subTotal)
        );
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + TEXT_SECONDARY + ";");
    }

    private void showError(String message) {
        levelIndicator.setVisible(false);
        selectedAccountLabel.setText(message);
        selectedAccountLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #ef4444;");
        statusLabel.setText("");
    }

    private String formatAmount(long amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.2f δισ.", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.2f εκ.", amount / 1_000_000.0);
        }
        return String.format("%,d €", amount);
    }

    public Region getView() {
        return view;
    }
}
