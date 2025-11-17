package com.financial;

import java.util.ArrayList;

public class TaxRevenue extends BudgetRevenue{
    public TaxRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    public static ArrayList<TaxRevenue> getTaxRevenues() {
        ArrayList<TaxRevenue> result = new ArrayList<>();
        for (BudgetRevenue revenue : budgetRevenues) {
            if (revenue instanceof TaxRevenue) {
                result.add((TaxRevenue) revenue);
            }
        }
        return result;
    }

    public static void printTaxRevenues(){
        for (BudgetRevenue budgetRevenue : getTaxRevenues()) {
            System.out.println(budgetRevenue);
        }
    }

    public static TaxRevenue findTaxRevenueWithCode (String code) {
        for (TaxRevenue taxRevenue : getTaxRevenues()) {
            if (taxRevenue.getCode().equals(code)) {
                return taxRevenue;
            }
        }
        return null;
    }
    
}