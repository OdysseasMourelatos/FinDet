package com.financial.ui.views;

import com.financial.statistical_analysis.FrequencyRow;
import com.financial.statistical_analysis.FrequencyTable;
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

import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;

import java.util.List;
import java.util.stream.Collectors;


public class StatisticsView {

    private static final String BG_PRIMARY = "#0a0a0f";
    private static final String BG_SECONDARY = "#12121a";
    private static final String TEXT_PRIMARY = "#e4e4e7";
    private static final String TEXT_SECONDARY = "#71717a";
    private static final String BORDER_COLOR = "#27272a";
    private static final String ACCENT_COLOR = "#3b82f6";

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
        if (type.equals("Frequency")) {
            showFrequencyTable();
        } else if (type.equals("Histogram")) {
            showHistogram();
        } else if  (type.equals("Polygon")) {
            showFrequencyPolygon();
        }
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