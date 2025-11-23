package con.financial;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.io.File;
import java.io.IOException;
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

    private static JFreeChart createRevenuePie (Map<String, Long> MainRevenueMap) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Long> entry : MainRevenueMap.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart("Main Revenues distribution", dataset);
        ChartUtils.saveChartAsPNG(new File("revenue_pie_chart.png"), chart, 800, 600);
        return chart;
    }

    public static void generateChart() {
        try {
            fillMap();
            createRevenuePie(MainRevenueMap);
            System.out.println("Το διάγραμμα δημιουργήθηκε: revenue_pie_chart.png");
        } catch (IOException e) {
            System.err.println("Σφάλμα: " + e.getMessage());
        }
    }

}
