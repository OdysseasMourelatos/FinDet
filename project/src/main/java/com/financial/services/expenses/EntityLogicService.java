package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.*;

/**
 * Utility service that provides stateless logic for grouping, filtering, and
 * aggregating expenses specifically focused on organizational entities and services.
 */
public class EntityLogicService {

    private EntityLogicService() {
        // utility class – no instances
    }

    /** Retrieves the descriptive name of a service based on its unique service code from the expense list. */
    public static String getServiceNameWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getServiceCode().equals(serviceCode)) {
                return expense.getServiceName();
            }
        }
        return null;
    }

    /** Returns a distinct and sorted list of all service codes present in the provided expenses. */
    public static List<String> getAllServiceCodes(ArrayList<? extends BudgetExpense> expenses) {
        return expenses.stream().map(BudgetExpense::getServiceCode).distinct().sorted().toList();
    }

    /** Calculates the total monetary sum for a specific service identified by its code. */
    public static long getSumOfServiceWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        long sum = 0;
        for (BudgetExpense expense : expenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    /** Filters and returns all expense entries associated with a specific service code. */
    public static ArrayList<BudgetExpense> getExpensesOfServiceWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        ArrayList<BudgetExpense> expensesOfService = new ArrayList<>();
        for (BudgetExpense expense : expenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                expensesOfService.add(expense);
            }
        }
        return expensesOfService;
    }

    /** Generates a map where each service code is associated with its total financial sum. */
    public static Map<String, Long> getSumOfEveryService(ArrayList<? extends BudgetExpense> expenses) {
        List<String> serviceCodes = getAllServiceCodes(expenses);
        Map<String, Long> serviceSums = new HashMap<>();
        for (String serviceCode : serviceCodes) {
            serviceSums.put(serviceCode, getSumOfServiceWithCode(serviceCode, expenses));
        }
        return serviceSums;
    }

    /** Calculates the total monetary sum for a specific expense category (account code). */
    public static long getSumOfExpenseCategoryWithCode(String code, ArrayList<? extends BudgetExpense> expenses) {
        long sum = 0;
        for (BudgetExpense expense : expenses) {
            if (code.equals(expense.getCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    /** Generates a sorted map linking each unique expense category code to its total financial sum. */
    public static Map<String, Long> getSumOfEveryExpenseCategory(ArrayList<? extends BudgetExpense> expenses) {
        String[] expenseCategories = expenses.stream().map(BudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> expensesSums = new LinkedHashMap<>();
        for (String expenseCategory : expenseCategories) {
            expensesSums.put(expenseCategory, getSumOfExpenseCategoryWithCode(expenseCategory, expenses));
        }
        return expensesSums;
    }
}
