package com.financial;

import java.util.ArrayList;

public class RegularBudgetExpense extends BudgetExpense {
    String entityCode;
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    RegularBudgetExpense (String entityCode, String code, String description, String category, long amount) {
        super(code,description,category,amount);
        this.entityCode=entityCode;
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

    public static long getSumOfRegularBudgetExpenses() {
        long sum = 0;
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            sum += regularBudgetExpense.getAmount();
        }
        return sum;
    }

    @Override
    public String toString(){
        return "Entity Code: " + entityCode + ", Category Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Amount: " + String.format("%,d", getAmount());
    }

}