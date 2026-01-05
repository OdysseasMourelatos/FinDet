package com.financial.ui.views;

import com.financial.statistical_analysis.FrequencyRow;
import com.financial.statistical_analysis.FrequencyTable;
import com.financial.statistical_analysis.StatisticalExpenses;
import com.financial.ui.Theme;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;

import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StatisticsView {

    // Use Theme class constants for consistent styling
    private static final String BG_PRIMARY = Theme.BG_BASE;
    private static final String BG_SECONDARY = Theme.BG_SURFACE;
    private static final String TEXT_PRIMARY = Theme.TEXT_PRIMARY;
    private static final String TEXT_SECONDARY = Theme.TEXT_SECONDARY;
    private static final String BORDER_COLOR = Theme.BORDER_DEFAULT;
    private static final String ACCENT_COLOR = Theme.ACCENT_BRIGHT;

    private final VBox view;
    private final VBox contentArea;

    public StatisticsView() {
        view = new VBox(0);
        view.setStyle("-fx-background-color: " + BG_PRIMARY + ";");

        VBox header = createHeader();

        HBox navBar = new HBox(12);
        navBar.setPadding(new Insets(0, 24, 20, 24));
        navBar.setAlignment(Pos.CENTER_LEFT);

        Button descriptiveBtn = createNavButton("Περιγραφικά Στατιστικά");
        Button frequencyBtn = createNavButton("Πίνακας Συχνοτήτων");
        Button histogramBtn = createNavButton("Ιστόγραμμα");
        Button polygonBtn = createNavButton("Πολυγωνική γραμμή");

        navBar.getChildren().addAll(descriptiveBtn, frequencyBtn, histogramBtn, polygonBtn);

        contentArea = new VBox();
        contentArea.setPadding(new Insets(0, 24, 24, 24));
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        Label placeholder = new Label("Επιλέξτε μια αναφορά για προβολή δεδομένων.");
        placeholder.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px;");
        contentArea.getChildren().add(placeholder);

        descriptiveBtn.setOnAction(e -> updateContent("Descriptive"));
        frequencyBtn.setOnAction(e -> updateContent("Frequency"));
        histogramBtn.setOnAction(e -> updateContent("Histogram"));
        polygonBtn.setOnAction(e -> updateContent("Polygon"));

        view.getChildren().addAll(header, navBar, contentArea);
    }

    private VBox createHeader() {
        VBox header = new VBox(4);
        header.setPadding(new Insets(32, 24, 20, 24));
        Label title = new Label("Στατιστική Ανάλυση");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600; -fx-text-fill: " + TEXT_PRIMARY + ";");
        Label subtitle = new Label("Ανάλυση των φορέων βάσει του ύψους του προϋπολογισμού τους");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_SECONDARY + ";");
        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(40);
        btn.setPadding(new Insets(0, 20, 0, 20));
        btn.setCursor(javafx.scene.Cursor.HAND);
        String style = "-fx-background-color: " + BG_SECONDARY + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 13px; -fx-background-radius: 8; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-border-width: 1;";
        btn.setStyle(style);
        btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-border-color: " + ACCENT_COLOR + ";"));
        btn.setOnMouseExited(e -> btn.setStyle(style));
        return btn;
    }

    private void updateContent(String type) {
        contentArea.getChildren().clear();
        switch (type) {
            case "Descriptive" -> showDescriptiveStats();
            case "Frequency" -> showFrequencyTable();
            case "Histogram" -> showHistogram();
            case "Polygon" -> showFrequencyPolygon();
        }
    }

    private void showDescriptiveStats() {
        // 1. Λήψη δεδομένων από την StatisticalExpenses
        java.util.Map<String, Long> sums = StatisticalExpenses.getMinistrySums();
        DescriptiveStatistics stats = StatisticalExpenses.descriptiveStats(sums);

        // 2. Δημιουργία Dashboard (FlowPane για αυτόματη αλλαγή σειράς)
        FlowPane dashboard = new javafx.scene.layout.FlowPane(20, 20);
        dashboard.setPadding(new Insets(10, 0, 0, 0));
        dashboard.setAlignment(Pos.TOP_LEFT);

        double q1 = stats.getPercentile(25);
        double q3 = stats.getPercentile(75);
        double iqr = q3 - q1;
        double range = stats.getMax() - stats.getMin();

        // 3. Προσθήκη καρτών με τους βασικούς δείκτες
        dashboard.getChildren().addAll(
                createStatCard("Μέσος Όρος (Mean)", formatSimplified(String.valueOf(stats.getMean()))),
                createStatCard("Διάμεσος (Median - Q2)", formatSimplified(String.valueOf(stats.getPercentile(50)))),
                createStatCard("Τυπική Απόκλιση (Std. Deviation) ", formatSimplified(String.valueOf(stats.getStandardDeviation()))),
                createStatCard("1ο Τεταρτημόριο (Q1)", formatSimplified(String.valueOf(q1))),
                createStatCard("3ο Τεταρτημόριο (Q3)", formatSimplified(String.valueOf(q3))),
                createStatCard("Ενδοτεταρτημοριακό Εύρος (IQR)", formatSimplified(String.valueOf(iqr))),
                createStatCard("Ελάχιστο (Min)", formatSimplified(String.valueOf(stats.getMin()))),
                createStatCard("Μέγιστο (Max)", formatSimplified(String.valueOf(stats.getMax()))),
                createStatCard("Εύρος (Range)", formatSimplified(String.valueOf(range))),
                createStatCard("Ασυμμετρία (Skewness)", String.format("%.4f", stats.getSkewness())),
                createStatCard("Κυρτότητα (Kurtosis)", String.format("%.4f", stats.getKurtosis())),
                createStatCard("Συνολικοί Φορείς", String.valueOf(stats.getN()))
        );

        // 4. Εύρεση Outliers με δύο μεθόδους
        Map<String, Long> outliersIQR = StatisticalExpenses.findOutliersIQR(sums, stats);
        Map<String, Long> outliersZ = StatisticalExpenses.findOutliersZscore(sums, stats);

        if (!outliersIQR.isEmpty() || !outliersZ.isEmpty()) {

            VBox outliersContainer = new VBox(15);
            outliersContainer.setPadding(new Insets(40, 0, 0, 0));

            // --- Προσθήκη Κεντρικού Τίτλου Ενότητας ---
            Label mainOutlierHeader = new Label("ΑΝΑΛΥΣΗ ΑΚΡΑΙΩΝ ΤΙΜΩΝ (OUTLIERS)");
            mainOutlierHeader.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 2;");

            Line separator = new Line(0, 0, 400, 0);
            separator.setStroke(Color.web("#ef4444", 0.3));

            // Προσθέτουμε τίτλο και γραμμή στο container
            outliersContainer.getChildren().addAll(mainOutlierHeader, separator);

            // Ενότητα IQR (Προσθήκη στο ΗΔΗ υπάρχον container)
            if (!outliersIQR.isEmpty()) {
                outliersContainer.getChildren().add(createOutlierSection("Μέθοδος IQR (Ενδοτεταρτημοριακό Εύρος):", outliersIQR));
            }

            // Ενότητα Z-Score (Προσθήκη στο ΗΔΗ υπάρχον container)
            if (!outliersZ.isEmpty()) {
                outliersContainer.getChildren().add(createOutlierSection("Μέθοδος Z-Score (Τυπικές Αποκλίσεις):", outliersZ));
            }

            // Τελικό Layout
            VBox mainContainer = new VBox(20);
            mainContainer.getChildren().addAll(dashboard, outliersContainer);
            contentArea.getChildren().add(mainContainer);
        } else {
            contentArea.getChildren().add(dashboard);
        }
    }

    private VBox createOutlierSection(String title, Map<String, Long> outliers) {
        VBox section = new VBox(8);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-font-size: 14px;");

        VBox list = new VBox(5);
        list.setPadding(new Insets(0, 0, 0, 15)); // Εσοχή για τη λίστα

        outliers.forEach((name, value) -> {
            Label item = new Label("• " + name + " (" + formatSimplified(String.valueOf(value)) + ")");
            item.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 13px;");
            item.setWrapText(true);
            list.getChildren().add(item);
        });

        section.getChildren().addAll(lblTitle, list);
        return section;
    }

    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(8);
        card.setPrefSize(220, 100);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: " + BG_SECONDARY + "; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: " + BORDER_COLOR + "; " +
                "-fx-border-radius: 12; " +
                "-fx-border-width: 1;");

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 12px; -fx-text-transform: uppercase;");

        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 18px; -fx-font-weight: bold;");

        card.getChildren().addAll(lblTitle, lblValue);

        // Εφέ κατά το πέρασμα του ποντικιού
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-border-color: " + ACCENT_COLOR + ";"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle() + "-fx-border-color: " + BORDER_COLOR + ";"));

        return card;
    }

    private void showFrequencyTable() {
        List<FrequencyRow> data = FrequencyTable.buildFromStat();
        TableView<FrequencyRow> table = new TableView<>();

        table.setFixedCellSize(35);
        table.setItems(FXCollections.observableArrayList(data));
        table.setStyle("-fx-background-color: " + BG_SECONDARY + "; -fx-control-inner-background: " + BG_SECONDARY + ";");
        VBox.setVgrow(table, Priority.ALWAYS);

        // 1. Στήλη Διάστημα (Καθαρισμός Scientific Notation)
        TableColumn<FrequencyRow, String> intervalCol = new TableColumn<>("Διάστημα (Budget Φορέων)");
        intervalCol.setCellValueFactory(new PropertyValueFactory<>("interval"));
        intervalCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String[] parts = item.split("-");
                    if (parts.length == 2) {
                        // Μετατροπή π.χ. από 1000000000 σε "1 δις"
                        setText(formatSimplified(parts[0]) + " - " + formatSimplified(parts[1]));
                    } else {
                        setText(item);
                    }
                    setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-weight: bold;");
                }
            }
        });

        // 2. Στήλη Συχνότητα
        TableColumn<FrequencyRow, Integer> freqCol = new TableColumn<>("Συχνότητα Φορέων");
        freqCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        styleIntegerColumn(freqCol);

        // 3. Στήλη Ποσοστό % (Περιορισμός σε 2 δεκαδικά)
        TableColumn<FrequencyRow, Double> percCol = new TableColumn<>("Ποσοστό % Φορέων");
        percCol.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        styleDoubleColumn(percCol);

        // 4. Στήλη Αθρ. Συχνότητα
        TableColumn<FrequencyRow, Integer> cumFreqCol = new TableColumn<>("Αθρ. Συχνότητα Φορέων");
        cumFreqCol.setCellValueFactory(new PropertyValueFactory<>("cumulativeFrequency"));
        styleIntegerColumn(cumFreqCol);

        // 5. Στήλη Αθρ. Ποσοστό %
        TableColumn<FrequencyRow, Double> cumPercCol = new TableColumn<>("Αθρ. Ποσοστό % Φορέων");
        cumPercCol.setCellValueFactory(new PropertyValueFactory<>("cumulativePercentage"));
        styleDoubleColumn(cumPercCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        table.getColumns().addAll(intervalCol, freqCol, percCol, cumFreqCol, cumPercCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        contentArea.getChildren().add(table);
    }

    private void showHistogram() {
        List<FrequencyRow> data = FrequencyTable.buildFromStat().stream().filter(row -> row.getFrequency() > 0).collect(Collectors.toList());

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Κλίμακα Προϋπολογισμού");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Πλήθος Φορέων");
        yAxis.setTickUnit(1);
        yAxis.setMinorTickVisible(false);

        BarChart<String, Number> histogram = new BarChart<>(xAxis, yAxis);
        histogram.setTitle("Κατανομή Φορέων ανά Μέγεθος Δαπανών");
        histogram.setLegendVisible(false);
        histogram.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (FrequencyRow row : data) {
            String[] parts = row.getInterval().split("-");
            String label = formatSimplified(parts[0]) + " - " + formatSimplified(parts[1]);
            series.getData().add(new XYChart.Data<>(label, row.getFrequency()));
        }

        histogram.getData().add(series);

        for (XYChart.Data<String, Number> dataNode : series.getData()) {
            dataNode.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: #87CEEB;");
                    Tooltip tooltip = new Tooltip(dataNode.getYValue().intValue() + " Φορείς");
                    Tooltip.install(newNode, tooltip);
                    newNode.setCursor(javafx.scene.Cursor.HAND);
                }
            });
        }

        histogram.setStyle("-fx-background-color: transparent;");
        histogram.getStylesheets().add("data:text/css," +
                ".default-color0.chart-bar { -fx-bar-fill: #87CEEB; }");

        VBox.setVgrow(histogram, Priority.ALWAYS);
        contentArea.getChildren().add(histogram);
    }

    private void showFrequencyPolygon() {
        List<FrequencyRow> data = FrequencyTable.buildFromStat().stream().filter(row -> row.getFrequency() > 0).collect(Collectors.toList());

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Κλίμακα Προϋπολογισμού");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Πλήθος Φορέων");
        yAxis.setTickUnit(1);

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Τάση Συγκέντρωσης Φορέων ανά Επίπεδο Δαπανών");
        lineChart.setLegendVisible(false);
        lineChart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (FrequencyRow row : data) {
            String[] parts = row.getInterval().split("-");
            String label = formatSimplified(parts[0]) + " - " + formatSimplified(parts[1]);
            series.getData().add(new XYChart.Data<>(label, row.getFrequency()));
        }

        lineChart.getData().add(series);

        // Styling για να φαίνεται "Fintech"
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.getStylesheets().add("data:text/css," +
                ".chart-series-line { -fx-stroke: #87CEEB; -fx-stroke-width: 3px; } " +
                ".chart-line-symbol { -fx-background-color: #ffffff, #87CEEB; -fx-background-radius: 5px; }");

        VBox.setVgrow(lineChart, Priority.ALWAYS);
        contentArea.getChildren().add(lineChart);
    }

    // Helper μέθοδοι για ομοιομορφία στο styling
    private void styleDoubleColumn(TableColumn<FrequencyRow, Double> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", item));
                    setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-alignment: CENTER-RIGHT;");
                }
            }
        });
    }

    private void styleIntegerColumn(TableColumn<FrequencyRow, Integer> col) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: " + TEXT_PRIMARY + "; -fx-alignment: CENTER;");
                }
            }
        });
    }

    private String formatSimplified(String value) {
        try {
            double val = Double.parseDouble(value);
            if (val >= 1_000_000_000_000L) {
                return String.format("%.0f τρις €", val / 1_000_000_000_000.0);
            }
            if (val >= 1_000_000_000L) {
                return String.format("%.0f δις €", val / 1_000_000_000.0);
            }
            if (val >= 1_000_000L) {
                return String.format("%.0f εκ. €", val / 1_000_000.0);
            }
            return String.format("%,.0f €", val);
        } catch (Exception e) {
            return value;
        }
    }

    public Region getView() {
        return view;
    }
}