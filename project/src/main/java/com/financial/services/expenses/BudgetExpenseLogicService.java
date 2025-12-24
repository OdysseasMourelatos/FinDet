package com.financial.services.expenses;

import com.financial.entries.Entity;
import com.financial.entries.BudgetExpense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BudgetExpenseLogicService {

    private BudgetExpenseLogicService() {
        // utility class – no instances
    }

    //Filters expenses based on the entity code
    public static <T extends BudgetExpense> ArrayList<T> getExpensesOfEntityWithCode(String entityCode, ArrayList<T> expenses) {
        ArrayList<T> expensesOfEntity = new ArrayList<>();

        for (T expense : expenses) {
            if (expense.getEntityCode().equals(entityCode)) {
                expensesOfEntity.add(expense);
            }
        }

        return expensesOfEntity;
    }

    //Filters expenses based on the expenseCode
    public static <T extends BudgetExpense> ArrayList<T> getExpensesOfCategoryWithCode(String expenseCode, ArrayList<T> expenses) {
        ArrayList<T> expensesOfCategory = new ArrayList<>();

        for (T expense : expenses) {
            if (expense.getCode().equals(expenseCode)) {
                expensesOfCategory.add(expense);
            }
        }

        return expensesOfCategory;
    }

    //Finds specific expense based on the primary key (entityCode, serviceCode, expenseCode)
    public static BudgetExpense findExpenseWithCode(String entityCode, String serviceCode, String expenseCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(expenseCode) && expense.getEntityCode().equals(entityCode) && expense.getServiceCode().equals(serviceCode)) {
                return expense;
            }
        }
        return null;
    }

    //Calculates sum of expenses
    public static long calculateSum(ArrayList<? extends BudgetExpense> expenses) {
        long totalExpensesSum = 0;
        for (BudgetExpense expense : expenses) {
            totalExpensesSum += expense.getAmount();
        }
        return totalExpensesSum;
    }

    //Sums Of Every Expense Category
    public static Map<String, Long> getSumOfEveryExpenseCategory(ArrayList<? extends BudgetExpense> expenses) {
        String[] categoryCodes = expenses.stream().map(BudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> expensesPerCategory = new HashMap<>();

        for (String categoryCode : categoryCodes) {
            long sum = 0;
            for (BudgetExpense expense : expenses) {
                if (categoryCode.equals(expense.getCode())) {
                    sum += expense.getAmount();
                }
            }
            expensesPerCategory.put(categoryCode, sum);
        }
        return expensesPerCategory;
    }

    //Sums Of Every Entity
    public static ArrayList<? extends BudgetExpense> getSumOfEveryEntity(ArrayList<? extends BudgetExpense> expenses) {
        ArrayList<BudgetExpense> expensesPerEntity = new ArrayList<>();
        for (Entity entity : Entity.getEntities()) {
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

    public static String getDescriptionWithCode(String code, ArrayList<? extends BudgetExpense> expenses) {
        return expenses.stream().filter(e -> e.getCode().equals(code)).findFirst().map(BudgetExpense::getDescription).orElse("Περιγραφή μη διαθέσιμη");
    }
}
