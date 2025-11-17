package com.financial;

import java.util.ArrayList;


public class BudgetRevenue extends BudgetEntry{

    protected static ArrayList <BudgetRevenue> budgetRevenues = new ArrayList<>();

    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        budgetRevenues.add(this);
    }

    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenues;
    }

    public static void printAllBudgetRevenues(){
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            System.out.println(budgetRevenue);
        }
    }
    

    @Override
    public String toString () {
        return super.toString();
    }
}

