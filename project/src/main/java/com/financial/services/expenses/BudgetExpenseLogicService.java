package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility service providing stateless logic for filtering, searching, and aggregating {@link BudgetExpense} data.
 */
public class BudgetExpenseLogicService {

    private BudgetExpenseLogicService() {
        // utility class – no instances
    }

    /** Filters a list of expenses to return only those belonging to a specific entity code. */
    public static <T extends BudgetExpense> ArrayList<T> getExpensesOfEntityWithCode(String entityCode, ArrayList<T> expenses) {
        ArrayList<T> expensesOfEntity = new ArrayList<>();

        for (T expense : expenses) {
            if (expense.getEntityCode().equals(entityCode)) {
                expensesOfEntity.add(expense);
            }
        }

        return expensesOfEntity;
    }

    /** Filters a list of expenses to return only those belonging to a specific category (account) code. */
    public static <T extends BudgetExpense> ArrayList<T> getExpensesOfCategoryWithCode(String expenseCode, ArrayList<T> expenses) {
        ArrayList<T> expensesOfCategory = new ArrayList<>();

        for (T expense : expenses) {
            if (expense.getCode().equals(expenseCode)) {
                expensesOfCategory.add(expense);
            }
        }

        return expensesOfCategory;
    }

    /** Locates a unique expense record using its composite primary key: entity, service, and expense codes. */
    public static BudgetExpense findExpenseWithCode(String entityCode, String serviceCode, String expenseCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(expenseCode) && expense.getEntityCode().equals(entityCode) && expense.getServiceCode().equals(serviceCode)) {
                return expense;
            }
        }
        return null;
    }

    /** Aggregates the total monetary amount from a collection of budget expenses. */
    public static long calculateSum(ArrayList<? extends BudgetExpense> expenses) {
        long totalExpensesSum = 0;
        for (BudgetExpense expense : expenses) {
            totalExpensesSum += expense.getAmount();
        }
        return totalExpensesSum;
    }

    /** Generates a map linking unique category codes to their respective total financial sums. */
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

    /** Retrieves the description associated with a specific code, or a default message if not found. */
    public static String getDescriptionWithCode(String code, ArrayList<? extends BudgetExpense> expenses) {
        return expenses.stream().filter(e -> e.getCode().equals(code)).findFirst().map(BudgetExpense::getDescription).orElse("Περιγραφή μη διαθέσιμη");
    }
}
