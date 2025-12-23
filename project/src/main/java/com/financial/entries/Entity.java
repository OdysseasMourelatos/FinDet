package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Entity {
    private final String entityCode;
    private final String entityName;
    protected ArrayList<RegularBudgetExpense> regularBudgetExpenses;
    protected ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses;

    protected static ArrayList<Entity> entities = new ArrayList<>();

    public Entity(String entityCode, String entityName) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        regularBudgetExpenses = RegularBudgetExpense.getRegularBudgetExpensesOfEntityWithCode(entityCode);
        publicInvestmentBudgetExpenses = BudgetExpenseLogicService.getExpensesOfEntityWithCode(entityCode, PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses());
        entities.add(this);
    }

    // Find Methods

    public String findRegularServiceNameWithCode(String serviceCode) {
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            if (regularBudgetExpense.getServiceCode().equals(serviceCode)) {
                return regularBudgetExpense.getServiceName();
            }
        }
        return null;
    }

    public String findPublicInvestmentServiceNameWithCode(String serviceCode, String type) {
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            if (publicInvestmentBudgetExpense.getServiceCode().equals(serviceCode)) {
                return publicInvestmentBudgetExpense.getServiceName();
            }
        }
        return null;
    }

    // Get Service Codes

    public ArrayList<String> getRegularServiceCodes() {
        ArrayList<String> regularServiceCodes = new ArrayList<>();
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            if (!regularServiceCodes.contains(regularBudgetExpense.getServiceCode())) {
                regularServiceCodes.add(regularBudgetExpense.getServiceCode());
            }
        }
        return regularServiceCodes;
    }

    public ArrayList<String> getPublicInvestmentServiceCodes(String type) {
        ArrayList<String> publicInvestmentServiceCodes = new ArrayList<>();
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            if (!publicInvestmentServiceCodes.contains(publicInvestmentBudgetExpense.getServiceCode()) && publicInvestmentBudgetExpense.getType().equals(type)) {
                publicInvestmentServiceCodes.add(publicInvestmentBudgetExpense.getServiceCode());
            }
        }
        return publicInvestmentServiceCodes;
    }

    // Get Sums

    public long getTotalSum() {
        return getRegularSum() + getPublicInvestmentSum();
    }

    public long getRegularSum() {
        long sum = 0;
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            sum += regularBudgetExpense.getAmount();
        }
        return sum;
    }

    public long getPublicInvestmentSum() {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            sum += publicInvestmentBudgetExpense.getAmount();
        }
        return sum;
    }

    public long getPublicInvestmentSum(String type) {
        long sum = 0;
        for (PublicInvestmentBudgetExpense publicInvestmentBudgetExpense : publicInvestmentBudgetExpenses) {
            if (publicInvestmentBudgetExpense.getType().equals(type)) {
                sum += publicInvestmentBudgetExpense.getAmount();
            }
        }
        return sum;
    }

    public long getSum(int budgetType) {
        return switch (budgetType) {
            case 0 -> getTotalSum();
            case 1 -> getRegularSum();
            case 2 -> getPublicInvestmentSum();
            default -> 0;
        };
    }

    public static Entity findEntityWithEntityCode(String entityCode) {
        for (Entity entity : entities) {
            if (entity.getEntityCode().equals(entityCode)) {
                return entity;
            }
        }
        return null;
    }


    // Get Sums Of Service With Code

    public long getRegularSumOfServiceWithCode(String serviceCode) {
        long sum = 0;
        for (RegularBudgetExpense expense : regularBudgetExpenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    public long getPublicInvestmentSumOfServiceWithCode(String serviceCode, String type) {
        long sum = 0;
        for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
            if (serviceCode.equals(expense.getServiceCode()) && type.equals(expense.getType())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    //Get Expenses Of Service With Code

    public ArrayList<RegularBudgetExpense> getRegularExpensesOfServiceWithCode(String serviceCode) {
        ArrayList<RegularBudgetExpense> regularExpenses = new ArrayList<>();
        for (RegularBudgetExpense expense : regularBudgetExpenses) {
            if (serviceCode.equals(expense.getServiceCode())) {
                regularExpenses.add(expense);
            }
        }
        return  regularExpenses;
    }

    public ArrayList<PublicInvestmentBudgetExpense> getPublicInvestmentExpensesOfServiceWithCode(String serviceCode, String type) {
        ArrayList<PublicInvestmentBudgetExpense> publicInvestmentExpenses = new ArrayList<>();
        for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
            if (serviceCode.equals(expense.getServiceCode()) && type.equals(expense.getType())) {
                publicInvestmentExpenses.add(expense);
            }
        }
        return  publicInvestmentExpenses;
    }

    //Get Sums Of Every Service

    public Map<String, Long> getRegularSumOfEveryService() {
        String[] serviceCodes = regularBudgetExpenses.stream().map(RegularBudgetExpense::getServiceCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> regularServiceSums = new HashMap<>();
        for (String serviceCode : serviceCodes) {
            regularServiceSums.put(serviceCode, getRegularSumOfServiceWithCode(serviceCode));
        }
        return regularServiceSums;
    }

    public Map<String, Long> getPublicInvestmentSumOfEveryService(String type) {
        String[] serviceCodes = publicInvestmentBudgetExpenses.stream().map(PublicInvestmentBudgetExpense::getServiceCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> publicInvestmentServiceSums = new HashMap<>();
        for (String serviceCode : serviceCodes) {
            publicInvestmentServiceSums.put(serviceCode, getPublicInvestmentSumOfServiceWithCode(serviceCode, type));
        }
        return publicInvestmentServiceSums;
    }

    // Get Sums Of Specific Expense Category

    public long getRegularSumOfExpenseCategoryWithCode(String code) {
        long sum = 0;
        for (RegularBudgetExpense expense : regularBudgetExpenses) {
            if (code.equals(expense.getCode())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    public long getPublicInvestmentSumOfExpenseCategoryWithCode(String code, String type) {
        long sum = 0;
        for (PublicInvestmentBudgetExpense expense : publicInvestmentBudgetExpenses) {
            if (code.equals(expense.getCode()) && type.equals(expense.getType())) {
                sum += expense.getAmount();
            }
        }
        return sum;
    }

    //Get Sums Of Every Expense Category

    public Map<String, Long> getRegularSumOfEveryExpenseCategory() {
        String[] expenseCategories = regularBudgetExpenses.stream().map(RegularBudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> regularExpensesSums = new LinkedHashMap<>();
        for (String expenseCategory : expenseCategories) {
            regularExpensesSums.put(expenseCategory, getRegularSumOfExpenseCategoryWithCode(expenseCategory));
        }
        return regularExpensesSums;
    }

    public Map<String, Long> getPublicInvestmentSumOfEveryExpenseCategory(String type) {
        String[] expenseCategories = publicInvestmentBudgetExpenses.stream().map(PublicInvestmentBudgetExpense::getCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> publicInvestmentExpensesSums = new LinkedHashMap<>();
        for (String expenseCategory : expenseCategories) {
            publicInvestmentExpensesSums.put(expenseCategory, getPublicInvestmentSumOfExpenseCategoryWithCode(expenseCategory, type));
        }
        return publicInvestmentExpensesSums;
    }

    //Getters

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public ArrayList<RegularBudgetExpense> getRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public ArrayList<PublicInvestmentBudgetExpense> getPublicInvestmentExpenses() {
        return publicInvestmentBudgetExpenses;
    }

    public static ArrayList<Entity> getEntities() {
        return entities;
    }

    @Override
    public String toString() {
        return "Entity Code: " + entityCode + ", Name: " + entityName;
    }
}
