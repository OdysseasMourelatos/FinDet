package com.financial.ui;

import com.financial.ui.views.DashboardView;
import com.financial.ui.views.RevenuesView;
import com.financial.ui.views.ExpensesView;
import com.financial.ui.views.EntitiesView;
import com.financial.ui.views.AccountExplorerView;
import com.financial.ui.views.BudgetChangesView;
import com.financial.ui.views.ChartsView;
import com.financial.ui.views.ExportView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Main window with sidebar navigation.
 */
public class MainWindow {

    private final BorderPane root;
    private final StackPane contentArea;
    private final VBox sidebar;
    private Button activeButton;

    // Views
    private final DashboardView dashboardView;
    private final RevenuesView revenuesView;
    private final ExpensesView expensesView;
    private final EntitiesView entitiesView;
    private final AccountExplorerView accountExplorerView;
    private final BudgetChangesView budgetChangesView;
    private final ChartsView chartsView;
    private final ExportView exportView;

    public MainWindow() {
        root = new BorderPane();

        // Initialize views
        dashboardView = new DashboardView();
        revenuesView = new RevenuesView();
        expensesView = new ExpensesView();
        entitiesView = new EntitiesView();
        accountExplorerView = new AccountExplorerView();
        budgetChangesView = new BudgetChangesView();
        chartsView = new ChartsView();
        exportView = new ExportView();

        // Create sidebar
        sidebar = createSidebar();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setStyle("-fx-background-color: #1e1e2e; -fx-padding: 0;");

        // Create content area
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #11111b;");
        contentArea.getChildren().add(dashboardView.getView());

        // Create header
        HBox header = createHeader();

        // Layout
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(contentArea);
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #181825; -fx-border-color: #313244; "
                + "-fx-border-width: 0 0 1 0;");

        Label title = new Label("FinDet");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #cdd6f4;");

        Label subtitle = new Label("Διαχείριση Κρατικού Προϋπολογισμού");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c7086;");

        VBox titleBox = new VBox(2, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label version = new Label("v1.0");
        version.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c7086;");

        header.getChildren().addAll(titleBox, spacer, version);
        return header;
    }

    private VBox createSidebar() {
        VBox sidebarBox = new VBox(5);
        sidebarBox.setPadding(new Insets(20, 10, 20, 10));
        sidebarBox.setPrefWidth(200);
        sidebarBox.setAlignment(Pos.TOP_CENTER);

        // Navigation buttons
        Button dashboardBtn = createNavButton("Αρχική", "dashboard");
        Button revenuesBtn = createNavButton("Έσοδα", "revenues");
        Button expensesBtn = createNavButton("Έξοδα", "expenses");
        Button entitiesBtn = createNavButton("Φορείς", "entities");
        Button explorerBtn = createNavButton("Εξερεύνηση", "explorer");
        Button changesBtn = createNavButton("Αλλαγές", "changes");
        Button chartsBtn = createNavButton("Γραφήματα", "charts");
        Button exportBtn = createNavButton("Εξαγωγή", "export");

        // Set dashboard as active by default
        setActiveButton(dashboardBtn);

        // Click handlers
        dashboardBtn.setOnAction(e -> {
            setActiveButton(dashboardBtn);
            showView(dashboardView.getView());
        });
        revenuesBtn.setOnAction(e -> {
            setActiveButton(revenuesBtn);
            showView(revenuesView.getView());
        });
        expensesBtn.setOnAction(e -> {
            setActiveButton(expensesBtn);
            showView(expensesView.getView());
        });
        entitiesBtn.setOnAction(e -> {
            setActiveButton(entitiesBtn);
            showView(entitiesView.getView());
        });
        explorerBtn.setOnAction(e -> {
            setActiveButton(explorerBtn);
            showView(accountExplorerView.getView());
        });
        changesBtn.setOnAction(e -> {
            setActiveButton(changesBtn);
            showView(budgetChangesView.getView());
        });
        chartsBtn.setOnAction(e -> {
            setActiveButton(chartsBtn);
            showView(chartsView.getView());
        });
        exportBtn.setOnAction(e -> {
            setActiveButton(exportBtn);
            showView(exportView.getView());
        });

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #313244;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Footer info
        Label footerLabel = new Label("Ελληνική Δημοκρατία");
        footerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6c7086;");

        sidebarBox.getChildren().addAll(
            dashboardBtn,
            revenuesBtn,
            expensesBtn,
            entitiesBtn,
            explorerBtn,
            changesBtn,
            new Separator(),
            chartsBtn,
            exportBtn,
            spacer,
            footerLabel
        );

        return sidebarBox;
    }

    private Button createNavButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(getInactiveButtonStyle());

        button.setOnMouseEntered(e -> {
            if (button != activeButton) {
                button.setStyle(getHoverButtonStyle());
            }
        });
        button.setOnMouseExited(e -> {
            if (button != activeButton) {
                button.setStyle(getInactiveButtonStyle());
            }
        });

        return button;
    }

    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.setStyle(getInactiveButtonStyle());
        }
        activeButton = button;
        activeButton.setStyle(getActiveButtonStyle());
    }

    private String getInactiveButtonStyle() {
        return "-fx-background-color: transparent; -fx-text-fill: #cdd6f4; "
             + "-fx-font-size: 14px; -fx-padding: 10 15; -fx-cursor: hand;";
    }

    private String getHoverButtonStyle() {
        return "-fx-background-color: #313244; -fx-text-fill: #cdd6f4; "
             + "-fx-font-size: 14px; -fx-padding: 10 15; -fx-cursor: hand; "
             + "-fx-background-radius: 8;";
    }

    private String getActiveButtonStyle() {
        return "-fx-background-color: #89b4fa; -fx-text-fill: #1e1e2e; "
             + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 15; "
             + "-fx-background-radius: 8;";
    }

    private void showView(Region view) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
    }

    public BorderPane getRoot() {
        return root;
    }
}
