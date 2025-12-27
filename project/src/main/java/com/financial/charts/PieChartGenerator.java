package com.financial.charts;

import com.financial.entries.BudgetEntry;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.financial.menu.Colors.*;


public class PieChartGenerator {


    static Map<String, Long> InstanceMap = new HashMap<>();

    public static Map<String, Long> fillMap(ArrayList<? extends BudgetEntry> entries) {
        InstanceMap.clear();
        for (BudgetEntry entry : entries) {
            InstanceMap.put(entry.getDescription(), entry.getAmount());
        }
        return InstanceMap;
    }

    private static JFreeChart createPie(Map<String, Long> instanceMap, String title) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Long> entry : instanceMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(title, dataset);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} = {2}", new DecimalFormat("0"), new DecimalFormat("0.00%")));
        ChartUtils.saveChartAsPNG(new File("output/chart.png"), chart, 800, 600);
        ChartFrame frame = new ChartFrame(title, chart);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return chart;
    }

    public static void generateChart(ArrayList<? extends BudgetEntry> entries, String title) {
        try {
            fillMap(entries);
            createPie(InstanceMap, title);
            System.out.println();
            System.out.println(GREEN + "Το διάγραμμα δημιουργήθηκε επιτυχώς" + RESET);
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }

}


