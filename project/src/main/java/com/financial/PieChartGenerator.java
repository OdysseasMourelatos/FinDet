package con.financial ;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class PieChartGenerator {


static Map <String, Long> MainRevenueMap = new HashMap<>();

public static Map<String,Long> fillMap(){
    BudgetEntry.getMainBudgetRevenues() ;
  for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length()== 2) {
                MainRevenueMap.put(budgetRevenue.getDescription(),budgetRevenue.getAmount());
        }
   }
   return MainRevenueMap;
}

private static JFreeChart createRevenuePie (Map MainRevenueMap){
    JFreeChart chart = ChartFactory.createPieChart("Main Revenues distribution", MainRevenueMap)
    return chart;
}

}