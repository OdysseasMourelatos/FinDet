package com.financial;

import java.util.ArrayList;

public class PublicInvestmentBudgetExpense extends BudgetExpense{
    String entityCode;
    String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    PublicInvestmentBudgetExpense (String entityCode, String code, String description, String type, String category, long amount) {
        super(code,description,category,amount);
        this.entityCode=entityCode;
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetExpense> getAllPublicInvestmentBudgetExpenses() {
        return publicInvestmentBudgetExpenses;
    }

    public static void printAllPublicInvestmentBudgetExpenses(){
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            System.out.println(publicInvestmentBudgetExpense);
        }
    }

    public static long getSumOfPublicInvestmentBudgetExpenses() {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            sum += publicInvestmentBudgetExpense.getAmount();
        }
        return sum;
    }

    @Override
    public String toString(){
        return "Entity Code: " + entityCode + ", Category Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}