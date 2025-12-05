package com.financial;

import com.financial.entries.BudgetExpense;
import com.financial.strategies.*;

import java.util.ArrayList;

public class BudgetExpenseHandling {

    public static BudgetExpense findExpenseWithCode (String code, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(code)) {
                return expense;
            }
        }
        return null;
    }

    public static <T extends BudgetExpense> ArrayList<T> getExpensesOfEntityWithCode(String entityCode, ArrayList<T> expenses) {
        ArrayList<T> expensesOfEntity = new ArrayList<>();

        for (T expense : expenses) {
            if (expense.getEntityCode().equals(entityCode)) {
                expensesOfEntity.add(expense);
            }
        }

        return expensesOfEntity;
    }

    public static long calculateSumOfExpenses(ArrayList<? extends BudgetExpense> expenses) {
        long totalExpensesSum = 0;
        for (BudgetExpense expense : expenses) {
            totalExpensesSum += expense.getAmount();
        }
        return totalExpensesSum;
    }

    public static ArrayList<? extends BudgetExpense> getSumOfEveryCategory(ArrayList<? extends BudgetExpense> expenses) {
        String[] categoryCodes = expenses.stream().map(BudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        ArrayList<BudgetExpense> expensesPerCategory = new ArrayList<>();

        for (String categoryCode : categoryCodes) {
            long sum = 0;
            for (BudgetExpense expense : expenses) {
                if (categoryCode.equals(expense.getCode())) {
                    sum += expense.getAmount();
                }
            }
            expensesPerCategory.add(new BudgetExpense(categoryCode, BudgetExpenseHandling.findExpenseWithCode(categoryCode, expenses).getDescription(), "ΕΞΟΔΑ", sum));
        }
        return expensesPerCategory;
    }

    public static void printSumOfEveryCategory(ArrayList<? extends BudgetExpense> expenses) {
        DataOutput.printExpenseWithAsciiTable(getSumOfEveryCategory(expenses));
    }

    //Sums Of Every Entity
    public static ArrayList<? extends BudgetExpense> getSumOfEveryEntity(ArrayList<? extends BudgetExpense> expenses) {
        ArrayList<BudgetExpense> expensesPerEntity = new ArrayList<>();
        for (Entity entity : Entity.entities) {
            long sum = 0;
            for (BudgetExpense expense : expenses) {
                if (entity.getEntityCode().equals(expense.getEntityCode())) {
                    sum += expense.getAmount();
                }
            }
            expensesPerEntity.add(new BudgetExpense(entity.getEntityCode(), entity.getEntityName(), "ΕΞΟΔΑ", sum));
        }
        return expensesPerEntity;
    }

    public static void applyGlobalAdjustment(IExpenseAdjustmentStrategy strategy, double percentage, long fixedAmount, ArrayList<BudgetExpense> expenses) {
        strategy.applyAdjustment(expenses, percentage, fixedAmount);
    }

}
