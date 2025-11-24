package com.financial;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PieChartGenerator {


    static Map<String, Long> instanceMap = new HashMap<>();

    public static Map<String, Long> fillMap(ArrayList<? extends BudgetEntry> entries) {
        instanceMap.clear();
        for (BudgetEntry entry : entries) {
            instanceMap.put(entry.getDescription(), entry.getAmount());
        }
        return instanceMap;
    }

    private static JFreeChart createRevenuePie(Map<String, Long> instanceMap) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Long> entry : instanceMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(title, dataset);
        ChartUtils.saveChartAsPNG(new File("chart.png"), chart, 800, 600);
        ChartFrame frame = new ChartFrame(title, chart);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return chart;
    }

    public static void generateChart(ArrayList<? extends BudgetEntry> entries, String title) {
        try {
            fillMap(entries);
            createRevenuePie(instanceMap);
            System.out.println(Menu.GREEN + "Το διάγραμμα δημιουργήθηκε" + Menu.RESET);
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }

}


