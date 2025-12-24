package com.financial.entries;

import java.util.ArrayList;

public class PublicInvestmentBudgetExpense extends BudgetExpense {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    public PublicInvestmentBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }


    public static ArrayList<PublicInvestmentBudgetExpense> getAllPublicInvestmentBudgetExpenses() {
        return publicInvestmentBudgetExpenses;
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


    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + ", Σκέλος : " + type;
    }
}