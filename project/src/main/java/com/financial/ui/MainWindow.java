package com.financial.ui;

import com.financial.ui.views.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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

    // Views - lazily initialized
    private DashboardView dashboardView;
    private RevenuesView revenuesView;
    private ExpensesView expensesView;
    private EntitiesView entitiesView;
    private AccountExplorerView accountExplorerView;
    private BudgetChangesView budgetChangesView;
    private ChartsView chartsView;
    private MultiYearAnalysisView multiYearAnalysisView;
    private StatisticsView statisticsView;
    private ExportView exportView;

    public MainWindow() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + Theme.BG_BASE + ";");

        // Only initialize dashboard view eagerly (shown by default)
        // Other views are lazily initialized when first accessed
        dashboardView = new DashboardView();

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

    // Lazy view getters - initialize views only when first accessed
    private Region getRevenuesView() {
        if (revenuesView == null) {
            revenuesView = new RevenuesView();
        }
        return revenuesView.getView();
    }

    private Region getExpensesView() {
        if (expensesView == null) {
            expensesView = new ExpensesView();
        }
        return expensesView.getView();
    }

    private Region getEntitiesView() {
        if (entitiesView == null) {
            entitiesView = new EntitiesView();
        }
        return entitiesView.getView();
    }

    private Region getAccountExplorerView() {
        if (accountExplorerView == null) {
            accountExplorerView = new AccountExplorerView();
        }
        return accountExplorerView.getView();
    }

    private Region getBudgetChangesView() {
        if (budgetChangesView == null) {
            budgetChangesView = new BudgetChangesView();
        }
        return budgetChangesView.getView();
    }

    private Region getChartsView() {
        if (chartsView == null) {
            chartsView = new ChartsView();
        }
        return chartsView.getView();
    }

    private Region getMultiYearAnalysisView() {
        if (multiYearAnalysisView == null) {
            multiYearAnalysisView = new MultiYearAnalysisView();
        }
        return multiYearAnalysisView.getView();
    }

    private Region getStatisticsView() {
        if (statisticsView == null) {
            statisticsView = new StatisticsView();
        }
        return statisticsView.getView();
    }

    private Region getExportView() {
        if (exportView == null) {
            exportView = new ExportView();
        }
        return exportView.getView();
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

        header.getChildren().addAll(emblem, titleBox, spacer, yearBadge);
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

        // Greek Delta symbol for FinDet
        Label symbol = new Label("Δ");
        symbol.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + Theme.ACCENT_PRIMARY + ";"
        );

        emblem.getChildren().addAll(outer, inner, symbol);
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
        NavButton multiYearBtn = new NavButton("Πολυετής Ανάλυση", "multiYear");
        NavButton statisticalBtn = new NavButton("Στατιστική", "statistics");

        // Export section
        Label exportSection = createSectionLabel("ΕΞΑΓΩΓΗ");
        NavButton exportBtn = new NavButton("Εξαγωγή PDF", "export");

        // Click handlers - using lazy getters for views
        dashboardBtn.setOnAction(() -> navigateToDashboard(dashboardBtn));
        revenuesBtn.setOnAction(() -> navigateTo(revenuesBtn, getRevenuesView()));
        expensesBtn.setOnAction(() -> navigateTo(expensesBtn, getExpensesView()));
        entitiesBtn.setOnAction(() -> navigateTo(entitiesBtn, getEntitiesView()));
        explorerBtn.setOnAction(() -> navigateTo(explorerBtn, getAccountExplorerView()));
        changesBtn.setOnAction(() -> navigateTo(changesBtn, getBudgetChangesView()));
        chartsBtn.setOnAction(() -> navigateTo(chartsBtn, getChartsView()));
        multiYearBtn.setOnAction(() -> navigateTo(multiYearBtn, getMultiYearAnalysisView()));
        statisticalBtn.setOnAction(() -> navigateTo(statisticalBtn, getStatisticsView()));
        exportBtn.setOnAction(() -> navigateTo(exportBtn, getExportView()));

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
            analysisSection,
            changesBtn,
            chartsBtn,
            multiYearBtn,
            statisticalBtn,
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

    private void navigateToDashboard(NavButton button) {
        Region view = dashboardView.getView();

        // Always replay animations when navigating to dashboard
        setActiveButton(button);

        // Fade out old view
        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), currentView);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            contentArea.getChildren().clear();

            // Replay dashboard animations
            dashboardView.replayAnimations();

            // Prepare view
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
     * Custom navigation button with futuristic hover animations
     */
    private class NavButton extends HBox {
        private boolean isActive = false;
        private final Label textLabel;
        private Runnable onAction;

        public NavButton(String text, String id) {
            super(8);
            setId(id);
            setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(10, 16, 10, 16));
            setMaxWidth(Double.MAX_VALUE);
            setCursor(javafx.scene.Cursor.HAND);

            textLabel = new Label(text);
            textLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
            );

            getChildren().add(textLabel);
            updateStyle();

            setOnMouseEntered(e -> {
                if (!isActive) {
                    playHoverAnimation(true);
                }
            });

            setOnMouseExited(e -> {
                if (!isActive) {
                    playHoverAnimation(false);
                }
            });

            setOnMouseClicked(e -> {
                if (onAction != null) {
                    onAction.run();
                }
            });
        }

        private void playHoverAnimation(boolean entering) {
            // Smooth slide animation (no scale to keep crisp text)
            TranslateTransition translate = new TranslateTransition(Duration.millis(120), this);
            translate.setToX(entering ? 6 : 0);

            // Update styles - no blur effects, just clean color changes
            if (entering) {
                setStyle(Theme.navButtonHover());
                textLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: " + Theme.TEXT_PRIMARY + ";");
            } else {
                updateStyle();
            }

            translate.play();
        }

        public void setOnAction(Runnable action) {
            this.onAction = action;
        }

        public void setActive(boolean active) {
            this.isActive = active;
            updateStyle();
        }

        private void updateStyle() {
            // Reset transforms
            setTranslateX(0);

            if (isActive) {
                setStyle(Theme.navButtonActive());
                textLabel.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: " + Theme.TEXT_PRIMARY + ";"
                );
            } else {
                setStyle(Theme.navButton());
                textLabel.setStyle(
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + Theme.TEXT_SECONDARY + ";"
                );
            }
        }
    }
}
