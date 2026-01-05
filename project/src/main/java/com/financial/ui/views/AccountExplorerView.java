package com.financial.ui.views;

import com.financial.entries.BudgetRevenue;
import com.financial.ui.Theme;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

import java.util.ArrayList;

/**
 * Account Explorer view - search by code and display hierarchy with clean design.
 */
public class AccountExplorerView {

    private final ScrollPane scrollPane;
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
        view.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Header
        VBox header = createHeader();

        // Search section
        HBox searchSection = createSearchSection();

        // Selected account card
        VBox accountCard = createAccountCard();

        // Tables section
        HBox tablesSection = createTablesSection();
        VBox.setVgrow(tablesSection, Priority.ALWAYS);

        // Status bar
        HBox statusBar = createStatusBar();

        view.getChildren().addAll(header, searchSection, accountCard, tablesSection, statusBar);

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
        header.setPadding(new Insets(32, 24, 20, 24));

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Circle icon = new Circle(16);
        icon.setFill(javafx.scene.paint.Color.web(Theme.ACCENT_LIGHT, 0.15));

        Label iconText = new Label("?");
        iconText.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";"
        );

        StackPane iconContainer = new StackPane(icon, iconText);

        Label title = new Label("Εξερεύνηση Λογαριασμών");
        title.setStyle(Theme.pageTitle());

        titleRow.getChildren().addAll(iconContainer, title);

        Label subtitle = new Label("Αναζητήστε κωδικό για να δείτε την ιεραρχία κατηγοριών εσόδων");
        subtitle.setStyle(Theme.subtitle());

        header.getChildren().addAll(titleRow, subtitle);
        return header;
    }

    private HBox createSearchSection() {
        HBox section = new HBox(12);
        section.setPadding(new Insets(0, 24, 16, 24));
        section.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Εισάγετε κωδικό (π.χ. 11, 111, 11101)");
        searchField.setPrefWidth(320);
        searchField.setStyle(Theme.textField());

        searchField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            searchField.setStyle(isFocused ? Theme.textFieldFocused() : Theme.textField());
        });

        Button searchButton = new Button("Αναζήτηση");
        searchButton.setStyle(Theme.buttonPrimary());
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(Theme.buttonPrimaryHover()));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(Theme.buttonPrimary()));
        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());

        section.getChildren().addAll(searchField, searchButton);
        return section;
    }

    private VBox createAccountCard() {
        VBox card = new VBox(8);
        card.setPadding(new Insets(0, 24, 16, 24));

        VBox innerCard = new VBox(8);
        innerCard.setPadding(new Insets(16));
        innerCard.setStyle(Theme.card());

        HBox topRow = new HBox(12);
        topRow.setAlignment(Pos.CENTER_LEFT);

        levelIndicator = new Label("");
        levelIndicator.setStyle(Theme.badge(Theme.ACCENT_PRIMARY, "white"));
        levelIndicator.setVisible(false);

        selectedAccountLabel = new Label("Εισάγετε κωδικό για αναζήτηση");
        selectedAccountLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        topRow.getChildren().addAll(levelIndicator, selectedAccountLabel);
        innerCard.getChildren().add(topRow);
        card.getChildren().add(innerCard);

        return card;
    }

    private HBox createTablesSection() {
        HBox section = new HBox(16);
        section.setPadding(new Insets(0, 24, 16, 24));

        superCategoriesData = FXCollections.observableArrayList();
        subCategoriesData = FXCollections.observableArrayList();

        VBox superBox = createTableBox("Ανώτερες Κατηγορίες", superCategoriesData, true);
        HBox.setHgrow(superBox, Priority.ALWAYS);

        VBox subBox = createTableBox("Υποκατηγορίες", subCategoriesData, false);
        HBox.setHgrow(subBox, Priority.ALWAYS);

        section.getChildren().addAll(superBox, subBox);
        return section;
    }

    private VBox createTableBox(String title, ObservableList<BudgetRevenue> data, boolean isSuper) {
        VBox box = new VBox(8);
        VBox.setVgrow(box, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

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
        table.setStyle(Theme.table());

        Label placeholder = new Label("");
        placeholder.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        table.setPlaceholder(placeholder);

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
        amountCol.setCellValueFactory(d -> new SimpleStringProperty(Theme.formatAmount(d.getValue().getAmount())));
        amountCol.setPrefWidth(100);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        table.getColumns().addAll(codeCol, levelCol, descCol, amountCol);
        table.setItems(data);

        return table;
    }

    private HBox createStatusBar() {
        HBox bar = new HBox(24);
        bar.setPadding(new Insets(12, 24, 12, 24));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 1 0 0 0;"
        );

        statusLabel = new Label("Διπλό κλικ σε εγγραφή για πλοήγηση στην ιεραρχία");
        statusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

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
        selectedAccountLabel.setText(revenue.getCode() + "  •  " + revenue.getDescription() + "  •  " + Theme.formatAmount(revenue.getAmount()));
        selectedAccountLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

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
            "  •  Σύνολο υποκατηγοριών: " + Theme.formatAmount(subTotal)
        );
        statusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );
    }

    private void showError(String message) {
        levelIndicator.setVisible(false);
        selectedAccountLabel.setText(message);
        selectedAccountLabel.setStyle(
            "-fx-font-size: 15px;" +
            "-fx-text-fill: " + Theme.ERROR_LIGHT + ";"
        );
        statusLabel.setText("");
    }

    public Region getView() {
        return scrollPane;
    }
}
