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

    public int getLevelOfHierarchy() {
        return switch (getCode().length()) {
            case 2 -> 1;   // "11" - top level
            case 3 -> 2;   // "111" - second level
            case 5 -> 3;   // "11101" - third level
            case 7 -> 4;   // "1110103" - fourth level
            case 10 -> 5;  // "1110103001" - fifth level
            default -> 0;  // unknown
        };
    }

    @Override
    public String toString () {
        return super.toString();
    }
}

