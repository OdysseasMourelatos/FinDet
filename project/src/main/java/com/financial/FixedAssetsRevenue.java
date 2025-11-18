package com.financial;

import java.util.ArrayList;

public class FixedAssetsRevenue extends BudgetRevenue {
    public FixedAssetsRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }
    public static ArrayList<FixedAssetsRevenue> getFixedAssetsRevenue() {
        ArrayList<FixedAssetsRevenue> result = new ArrayList<>();
        for (BudgetRevenue revenue : budgetRevenues) {
            if (revenue instanceof FixedAssetsRevenue) {
                result.add((FixedAssetsRevenue) revenue);
            }
        }
        return result;
    }

    public static void printFixedAssetsRevenue(){
        for (BudgetRevenue budgetRevenue : getFixedAssetsRevenue()) {
            System.out.println(budgetRevenue);
        }
    }
}
