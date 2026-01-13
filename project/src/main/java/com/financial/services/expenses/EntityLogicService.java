package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;

import java.util.*;

public class EntityLogicService {

    private EntityLogicService() {
        // utility class – no instances
    }

    public static String getServiceNameWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getServiceCode().equals(serviceCode)) {
                return expense.getServiceName();
            }
        }
        return null;
    }

    public static List<String> getAllServiceCodes(ArrayList<? extends BudgetExpense> expenses) {
        return expenses.stream().map(BudgetExpense::getServiceCode).distinct().sorted().toList();
    }

    public static long getSumOfServiceWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        long sum = 0;
        for (BudgetExpense expense : expenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    public static ArrayList<BudgetExpense> getExpensesOfServiceWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        ArrayList<BudgetExpense> expensesOfService = new ArrayList<>();
        for (BudgetExpense expense : expenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                expensesOfService.add(expense);
            }
        }
        return expensesOfService;
    }

    public static Map<String, Long> getSumOfEveryService(ArrayList<? extends BudgetExpense> expenses) {
        List<String> serviceCodes = getAllServiceCodes(expenses);
        Map<String, Long> serviceSums = new HashMap<>();
        for (String serviceCode : serviceCodes) {
            serviceSums.put(serviceCode, getSumOfServiceWithCode(serviceCode, expenses));
        }
        return serviceSums;
    }

    public static long getSumOfExpenseCategoryWithCode(String code, ArrayList<? extends BudgetExpense> expenses) {
        long sum = 0;
        for (BudgetExpense expense : expenses) {
            if (code.equals(expense.getCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    public static Map<String, Long> getSumOfEveryExpenseCategory(ArrayList<? extends BudgetExpense> expenses) {
        String[] expenseCategories = expenses.stream().map(BudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> expensesSums = new LinkedHashMap<>();
        for (String expenseCategory : expenseCategories) {
            expensesSums.put(expenseCategory, getSumOfExpenseCategoryWithCode(expenseCategory, expenses));
        }
        return expensesSums;
    }
}
