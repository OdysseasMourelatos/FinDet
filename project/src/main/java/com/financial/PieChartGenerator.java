package com.financial;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PieChartGenerator {


    static Map<String, Long> InstanceMap = new HashMap<>();

    public static Map<String,Long> fillMap(Arraylist<? extends BudgetEnrries> entry) {
        for (BudgetEntry entry : entries) {
            if (budgetRevenue.getCode().length()== 2) {
                InstanceMap.put(entry.getDescription(),entry.getAmount());
            }
        }
        return InstanceMap;
    }

    private static JFreeChart createRevenuePie (Map<String, Long> InstanceMap) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Long> entry : InstanceMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(title, dataset);
        ChartUtils.saveChartAsPNG(new File("revenue_pie_chart.png"), chart, 800, 600);
        return chart;
    }

    public static void generateChart(Arraylist<? extends BudgetEnrries> entry, String title) {
        try {
            fillMap(entries);
            createRevenuePie(MainRevenueMap);
            System.out.println("Το διάγραμμα δημιουργήθηκε: revenue_pie_chart.png");
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }

}


