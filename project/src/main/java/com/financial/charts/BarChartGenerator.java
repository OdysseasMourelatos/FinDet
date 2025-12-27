package com.financial.charts;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartUtils;
import static com.financial.menu.Colors.*;

import com.financial.entries.BudgetEntry;


public class BarChartGenerator {
    static Map<String, Long> barMap = new HashMap<>();

    public static Map<String, Long> fillMap(ArrayList<? extends BudgetEntry> entries) {
        barMap.clear();
        for (BudgetEntry entry : entries) {
            barMap.put(entry.getDescription(), entry.getAmount());
        }
        return barMap;
    }

    public static JFreeChart createBarChart(Map<String, Long> chartMap, String title, String xAxis, String yAxis) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series = title;
        for (Map.Entry<String, Long> entry: chartMap.entrySet()) {
            dataset.setValue(entry.getValue(), series, entry.getKey());
        }
        JFreeChart barChart3d = ChartFactory.createBarChart(title, xAxis, yAxis, dataset, PlotOrientation.VERTICAL, true, true, false);
        File barChart3dFile = new File("output/BarChart3d.jpeg");
        ChartUtils.saveChartAsJPEG(barChart3dFile, barChart3d, 650, 500);
        ChartFrame frame = new ChartFrame(title, barChart3d);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return barChart3d;
    }

    public static void generateChart(ArrayList<? extends BudgetEntry> entries, String title, String xAxis, String yAxis) {
        try {
            createBarChart(BarChartGenerator.fillMap(entries), title, xAxis, yAxis);
            System.out.println();
            System.out.println(GREEN + "Το διάγραμμα δημιουργήθηκε επιτυχώς" + RESET);
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }
}
