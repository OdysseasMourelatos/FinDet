package com.financial;

import java.util.ArrayList;

public class RegularBudgetExpense extends BudgetExpense {
    String entityCode;
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    RegularBudgetExpense (String entityCode, String code, String description, String category, long amount) {
        this.entityCode=entityCode;
        super(code,description,category,amount);
        regularBudgetExpenses.add(this);
    }

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static void printAllRegularBudgetExpenses(){
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            System.out.println(regularBudgetExpense);
        }
    }
    
}