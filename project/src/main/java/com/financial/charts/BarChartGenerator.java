package com.financial.charts;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 



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

    private static JFreeChart createBars (Map<String, Long> barMap, String title, String Xaxis, String Yaxis) throws IOException{
        DefaultCategoryDataset<String> dataset = new DefaultCategoryDataset<>();
        for (Map.Entry<String, Long> Entry: barMap.entrySet()){
            dataset.setValue(Entry.getkey(), entry.getValue());
        }
        JFreeChart barChart3d = ChartFactory.createBarChart3D(title, Xaxis, Yaxis, dataset, PlotOrientation.VERTICAL, true ,true ,false);
        File BarChart3d = new File(BarChart3d.jpeg);
        ChartUtilities.SaveChartAsJPEG(bar3dChart, barChart, 650, 500);
        ChartFrame frame = new ChartFrame(title, barChart3d);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return barChart3d;
    }

    public static void generateChart(ArrayList<? extends BudgetEntry> entries, String title, String Xaxis, String Yaxis){
        try {
            createBars(BarChartGenerator.fillMap(entries), title, Xaxis, Yaxis);
            System.out.println();
            System.out.println(GREEN + "Το διάγραμμα δημιουργήθηκε επιτυχώς" + RESET);
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }


}
