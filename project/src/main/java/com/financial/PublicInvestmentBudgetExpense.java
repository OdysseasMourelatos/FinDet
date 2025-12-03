package com.financial;

import java.util.ArrayList;

public class PublicInvestmentBudgetExpense extends BudgetExpense {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    PublicInvestmentBudgetExpense (String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode,entityName,serviceCode,serviceName,code,description,category,amount);
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetExpense> getAllPublicInvestmentBudgetExpenses() {
        return publicInvestmentBudgetExpenses;
    }

    public static void printAllPublicInvestmentBudgetExpenses() {
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            System.out.println(publicInvestmentBudgetExpense);
        }
    }

    public static ArrayList<PublicInvestmentBudgetExpense> getPublicInvestmentBudgetNationalExpenses() {
        ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetNationalExpenses = new ArrayList<>();
        for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
            if (expense.getType().equals("ΕΘΝΙΚΟ") || expense.getType().equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ") ) {
                publicInvestmentBudgetNationalExpenses.add(expense);
            }
        }
        return publicInvestmentBudgetNationalExpenses;
    }

    public static ArrayList<PublicInvestmentBudgetExpense> getPublicInvestmentBudgetCoFundedExpenses() {
        ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetCoFundedExpenses = new ArrayList<>();
        for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
            if (expense.getType().equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || expense.getType().equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
                publicInvestmentBudgetCoFundedExpenses.add(expense);
            }
        }
        return publicInvestmentBudgetCoFundedExpenses;
    }

    public static long getSumOfPublicInvestmentBudgetExpenses() {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            sum += publicInvestmentBudgetExpense.getAmount();
        }
        return sum;
    }

    public static long getSumOfPublicInvestmentBudgetNationalExpenses() {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : getPublicInvestmentBudgetNationalExpenses()) {
            sum += publicInvestmentBudgetExpense.getAmount();
        }
        return sum;
    }

    public static long getSumOfPublicInvestmentBudgetCoFundedExpenses() {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : getPublicInvestmentBudgetCoFundedExpenses()) {
            sum += publicInvestmentBudgetExpense.getAmount();
        }
        return sum;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Entity Code: " + entityCode + ", Category Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}