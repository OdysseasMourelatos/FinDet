package com.financial.ui;

import com.financial.ui.views.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Main window with Apple-like sidebar navigation and Greek governmental branding.
 * "President of the Greek Government for One Day" - FinDet
 */
public class MainWindow {

    private final BorderPane root;
    private final StackPane contentArea;
    private final VBox sidebar;
    private NavButton activeButton;
    private Region currentView;

    // Views
    private final DashboardView dashboardView;
    private final RevenuesView revenuesView;
    private final ExpensesView expensesView;
    private final EntitiesView entitiesView;
    private final AccountExplorerView accountExplorerView;
    private final BudgetChangesView budgetChangesView;
    private final ChartsView chartsView;
    private final StatisticsView statisticsView;
    private final ExportView exportView;

    public MainWindow() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Initialize views
        dashboardView = new DashboardView();
        revenuesView = new RevenuesView();
        expensesView = new ExpensesView();
        entitiesView = new EntitiesView();
        accountExplorerView = new AccountExplorerView();
        budgetChangesView = new BudgetChangesView();
        chartsView = new ChartsView();
        statisticsView = new StatisticsView();
        exportView = new ExportView();

        // Create sidebar
        sidebar = createSidebar();

        // Create content area
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");
        currentView = dashboardView.getView();
        contentArea.getChildren().add(currentView);

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
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setSpacing(16);
        header.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        // Greek government emblem placeholder (simplified circle with cross)
        StackPane emblem = createGreekEmblem();

        // Title section
        VBox titleBox = new VBox(2);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("FinDet");
        title.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
        );

        Label subtitle = new Label("Πρόεδρος της Ελληνικής Κυβέρνησης για Μία Ημέρα");
        subtitle.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Year badge
        Label yearBadge = new Label("2025");
        yearBadge.setStyle(Theme.badge(Theme.ACCENT_SUBTLE, Theme.ACCENT_BRIGHT));

        // Status indicator
        HBox statusBox = new HBox(8);
        statusBox.setAlignment(Pos.CENTER);

        Circle statusDot = new Circle(4);
        statusDot.setFill(javafx.scene.paint.Color.web(Theme.SUCCESS_LIGHT));

        Label statusLabel = new Label("Ενεργός");
        statusLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
        );

        statusBox.getChildren().addAll(statusDot, statusLabel);

        header.getChildren().addAll(emblem, titleBox, spacer, yearBadge, statusBox);
        return header;
    }

    private StackPane createGreekEmblem() {
        StackPane emblem = new StackPane();
        emblem.setPrefSize(40, 40);
        emblem.setMinSize(40, 40);

        // Outer circle (Greek blue)
        Circle outer = new Circle(20);
        outer.setFill(javafx.scene.paint.Color.web(Theme.ACCENT_PRIMARY));

        // Inner circle (white)
        Circle inner = new Circle(16);
        inner.setFill(javafx.scene.paint.Color.web(Theme.BG_SURFACE));

        // Greek cross simplified
        Label cross = new Label("+");
        cross.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.ACCENT_PRIMARY + ";"
        );

        emblem.getChildren().addAll(outer, inner, cross);
        return emblem;
    }

    private VBox createSidebar() {
        VBox sidebarBox = new VBox(4);
        sidebarBox.setPadding(new Insets(20, 12, 20, 12));
        sidebarBox.setPrefWidth(220);
        sidebarBox.setMinWidth(220);
        sidebarBox.setStyle(
            "-fx-background-color: " + Theme.BG_SURFACE + ";" +
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 0 1 0 0;"
        );

        // Main navigation section
        Label mainSection = createSectionLabel("ΚΥΡΙΟ ΜΕΝΟΥ");

        NavButton dashboardBtn = new NavButton("Επισκόπηση", "dashboard");
        NavButton revenuesBtn = new NavButton("Έσοδα", "revenues");
        NavButton expensesBtn = new NavButton("Έξοδα", "expenses");
        NavButton entitiesBtn = new NavButton("Φορείς", "entities");
        NavButton explorerBtn = new NavButton("Εξερεύνηση", "explorer");

        // Set dashboard as active by default
        setActiveButton(dashboardBtn);

        // Analysis section
        Label analysisSection = createSectionLabel("ΑΝΑΛΥΣΗ");

        NavButton changesBtn = new NavButton("Αλλαγές", "changes");
        NavButton chartsBtn = new NavButton("Γραφήματα", "charts");
        NavButton statisticalBtn = new NavButton("Στατιστική", "statistics");

        // Export section
        Label exportSection = createSectionLabel("ΕΞΑΓΩΓΗ");
        NavButton exportBtn = new NavButton("Εξαγωγή PDF", "export");

        // Click handlers
        dashboardBtn.setOnAction(() -> navigateTo(dashboardBtn, dashboardView.getView()));
        revenuesBtn.setOnAction(() -> navigateTo(revenuesBtn, revenuesView.getView()));
        expensesBtn.setOnAction(() -> navigateTo(expensesBtn, expensesView.getView()));
        entitiesBtn.setOnAction(() -> navigateTo(entitiesBtn, entitiesView.getView()));
        explorerBtn.setOnAction(() -> navigateTo(explorerBtn, accountExplorerView.getView()));
        changesBtn.setOnAction(() -> navigateTo(changesBtn, budgetChangesView.getView()));
        chartsBtn.setOnAction(() -> navigateTo(chartsBtn, chartsView.getView()));
        statisticalBtn.setOnAction(() -> navigateTo(statisticalBtn, statisticsView.getView()));
        exportBtn.setOnAction(() -> navigateTo(exportBtn, exportView.getView()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Footer
        VBox footer = createFooter();

        sidebarBox.getChildren().addAll(
            mainSection,
            dashboardBtn,
            revenuesBtn,
            expensesBtn,
            entitiesBtn,
            explorerBtn,
            createSeparator(),
            analysisSection,
            changesBtn,
            chartsBtn,
            statisticalBtn,
            createSeparator(),
            exportSection,
            exportBtn,
            spacer,
            footer
        );

        return sidebarBox;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";" +
            "-fx-padding: 16 8 8 8;"
        );
        return label;
    }

    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + Theme.BORDER_MUTED + ";");
        VBox.setMargin(sep, new Insets(8, 0, 8, 0));
        return sep;
    }

    private VBox createFooter() {
        VBox footer = new VBox(4);
        footer.setPadding(new Insets(16, 8, 0, 8));
        footer.setStyle(
            "-fx-border-color: " + Theme.BORDER_MUTED + ";" +
            "-fx-border-width: 1 0 0 0;"
        );

        Label emblemText = new Label("ΕΛΛΗΝΙΚΗ ΔΗΜΟΚΡΑΤΙΑ");
        emblemText.setStyle(
            "-fx-font-size: 9px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";"
        );

        Label versionLabel = new Label("FinDet v1.0");
        versionLabel.setStyle(
            "-fx-font-size: 10px;" +
            "-fx-text-fill: " + Theme.TEXT_MUTED + ";"
        );

        footer.getChildren().addAll(emblemText, versionLabel);
        return footer;
    }

    private void setActiveButton(NavButton button) {
        if (activeButton != null) {
            activeButton.setActive(false);
        }
        activeButton = button;
        activeButton.setActive(true);
    }

    private void navigateTo(NavButton button, Region view) {
        if (currentView == view) {
            return;
        }

        setActiveButton(button);

        // Fade out old view
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), currentView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().clear();

            // Prepare new view
            view.setOpacity(0);
            view.setTranslateY(10);
            contentArea.getChildren().add(view);
            currentView = view;

            // Fade in new view
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), view);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(200), view);
            slideIn.setFromY(10);
            slideIn.setToY(0);

            ParallelTransition parallel = new ParallelTransition(fadeIn, slideIn);
            parallel.play();
        });

        fadeOut.play();
    }

    public BorderPane getRoot() {
        return root;
    }

    /**
     * Custom navigation button with Apple-like styling
     */
    private class NavButton extends HBox {
        private boolean isActive = false;
        private final Label textLabel;
        private Runnable onAction;

        public NavButton(String text, String id) {
            super(12);
            setId(id);
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(10, 12, 10, 12));
            setMaxWidth(Double.MAX_VALUE);
            setCursor(javafx.scene.Cursor.HAND);

            // Icon placeholder (small colored dot)
            Circle icon = new Circle(4);
            icon.setFill(javafx.scene.paint.Color.web(Theme.TEXT_MUTED));

            textLabel = new Label(text);
            textLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
            );

            getChildren().addAll(icon, textLabel);
            updateStyle();

            setOnMouseEntered(e -> {
                if (!isActive) {
                    setStyle(Theme.navButtonHover());
                    textLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");
                }
            });

            setOnMouseExited(e -> {
                if (!isActive) {
                    updateStyle();
                }
            });

            setOnMouseClicked(e -> {
                if (onAction != null) {
                    onAction.run();
                }
            });
        }

        public void setOnAction(Runnable action) {
            this.onAction = action;
        }

        public void setActive(boolean active) {
            this.isActive = active;
            updateStyle();
        }

        private void updateStyle() {
            if (isActive) {
                setStyle(Theme.navButtonActive());
                textLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: " + Theme.ACCENT_BRIGHT + ";"
                );
                // Update icon color
                if (!getChildren().isEmpty() && getChildren().get(0) instanceof Circle icon) {
                    icon.setFill(javafx.scene.paint.Color.web(Theme.ACCENT_BRIGHT));
                }
            } else {
                setStyle(Theme.navButton());
                textLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
                );
                // Reset icon color
                if (!getChildren().isEmpty() && getChildren().get(0) instanceof Circle icon) {
                    icon.setFill(javafx.scene.paint.Color.web(Theme.TEXT_MUTED));
                }
            }
        }
    }
}
