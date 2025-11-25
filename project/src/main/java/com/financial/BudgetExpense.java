package com.financial;

import java.util.ArrayList;

public class BudgetExpense extends BudgetEntry {

    protected static ArrayList <BudgetExpense> expenses = new ArrayList<>();

    public BudgetExpense(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        expenses.add(this);
    }

    public BudgetExpense(String code, String description, String category, long amount, boolean includeList) {
        super(code, description, category, amount);
    }

    public static long calculateSum() {
        long totalExpensesSum = 0;
        for (BudgetExpense expense : getSumOfEveryCategory()) {
            totalExpensesSum += expense.getAmount();
        }
        return totalExpensesSum;
    }

    public static ArrayList<BudgetExpense> getSumOfEveryCategory() {
        String[] categoryCodes = expenses.stream().map(BudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        ArrayList<BudgetExpense> expensesPerCategory = new ArrayList<>();
        for (String categoryCode : categoryCodes) {
            long sum = 0;
            for (BudgetExpense expense : expenses) {
                if (categoryCode.equals(expense.getCode())) {
                    sum += expense.getAmount();
                }
            }
            expensesPerCategory.add(new BudgetExpense(categoryCode, findExpenseWithCode(categoryCode).getDescription(), "ΕΞΟΔΑ", sum, false));
        }
        return expensesPerCategory;
    }

    public static void printSumOfEveryCategory() {
        DataOutput.printWithAsciiTable(getSumOfEveryCategory());
    }

    public static void printExpenses() {
        DataOutput.printWithAsciiTable(expenses);
    }

    public static BudgetExpense findExpenseWithCode (String code) {
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(code)) {
                return expense;
            }
        }
        return null;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}

