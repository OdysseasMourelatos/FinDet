package com.financial;

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
}
