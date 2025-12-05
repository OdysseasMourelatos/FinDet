package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class RegularBudgetRevenue extends BudgetRevenue {

    protected static ArrayList<RegularBudgetRevenue> regularBudgetRevenues = new ArrayList<>();

    public RegularBudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        regularBudgetRevenues.add(this);
    }

    public static ArrayList<RegularBudgetRevenue> getAllRegularBudgetRevenues() {
        return regularBudgetRevenues;
    }

    public static void printAllRegularBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(regularBudgetRevenues, BudgetRevenueHandling.calculateSum(regularBudgetRevenues));
    }

    @Override
    public RegularBudgetRevenue findSuperCategory() {
        return BudgetRevenueHandling.findSuperCategory(this, regularBudgetRevenues);
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
