package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.util.HashMap;
import java.util.Map;

public class PieChartGenerator {


    static Map<String, Long> MainRevenueMap = new HashMap<>();

    public static Map<String,Long> fillMap(){
        for (BudgetRevenue budgetRevenue : BudgetRevenue.getMainBudgetRevenues()) {
            if (budgetRevenue.getCode().length()== 2) {
                MainRevenueMap.put(budgetRevenue.getDescription(),budgetRevenue.getAmount());
            }
        }
        return MainRevenueMap;
    }

    private static JFreeChart createRevenuePie (Map<String, Long> MainRevenueMap){
        JFreeChart chart = ChartFactory.createPieChart("Main Revenues distribution", (PieDataset) MainRevenueMap);
        return chart;
    }
}
