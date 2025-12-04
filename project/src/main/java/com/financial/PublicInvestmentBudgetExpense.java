package com.financial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublicInvestmentBudgetExpense extends BudgetExpense {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    PublicInvestmentBudgetExpense (String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }

    //Get Lists

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

    //Print Lists

    public static void printAllPublicInvestmentBudgetExpenses() {
        DataOutput.printPublicInvestmentBudgetExpenseWithAsciiTable(getAllPublicInvestmentBudgetExpenses());
    }

    public static void printPublicInvestmentBudgetNationalExpenses() {
        DataOutput.printPublicInvestmentBudgetExpenseWithAsciiTable(getPublicInvestmentBudgetNationalExpenses());
    }

    public static void printPublicInvestmentBudgetCoFundedExpenses() {
        DataOutput.printPublicInvestmentBudgetExpenseWithAsciiTable(getPublicInvestmentBudgetCoFundedExpenses());
    }

    //Sum Getters

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

    // Gets Sum Of Every Entity
    public static Map<String, Long> getPublicInvestmentSumOfEveryEntity() {
        String[] entityCodes = publicInvestmentBudgetExpenses.stream().map(PublicInvestmentBudgetExpense::getEntityCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> publicInvestmentEntitySums = new HashMap<>();
        for (String entityCode : entityCodes) {
            long sum = 0;
            for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
                if (entityCode.equals(expense.getEntityCode())) {
                    sum += expense.getAmount();
                }
            }
            publicInvestmentEntitySums.put(entityCode, sum);
        }
        return publicInvestmentEntitySums;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + ", Σκέλος : " + type;
    }
}