package com.financial;

import java.util.ArrayList;

public class BudgetExpense extends BudgetEntry{

    private static ArrayList <BudgetExpense> expenses = new ArrayList<>();

    public BudgetExpense(int code, String description, String category, long amount) {
        super(code, description, category, amount);
        expenses.add(this);
    }

    public static long calculateSum(){
        long sum = 0;
        for (BudgetExpense expense : expenses) {
            sum += expense.getAmount();
        }
        return sum;
    }

    public static void printExpenses() {
        for (BudgetExpense expense : expenses) {
            System.out.println(expense);
        }
    }

    @Override
    public String toString () {
        return super.toString();
    }
}

