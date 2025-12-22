package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiYearBudgetExpense extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetExpense> multiYearBudgetExpenses = new ArrayList<>();

    public MultiYearBudgetExpense(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        multiYearBudgetExpenses.add(this);
    }

    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpenses() {
        return multiYearBudgetExpenses;
    }

    protected static List<MultiYearBudgetExpense> multiYearBudgetExpensesOfEntities = new ArrayList<>();

    private String entityCode;
    private String entityName;
    private long regularAmount;
    private long publicInvestmentAmount;

    public MultiYearBudgetExpense(String entityCode, String entityName, String code, String description, String category, long regularAmount, long publicInvestmentAmount, int year) {
        super(code, description, category, regularAmount + publicInvestmentAmount,  year);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.regularAmount = regularAmount;
        this.publicInvestmentAmount = publicInvestmentAmount;
        multiYearBudgetExpenses.add(this);
    }

    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpensesOfSpecificYear(int year) {
        List<MultiYearBudgetExpense> multiYearBudgetExpensesOfSpecificYear = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getYear() == year) {
                multiYearBudgetExpensesOfSpecificYear.add(multiYearBudgetExpense);
            }
        }
        return multiYearBudgetExpensesOfSpecificYear;
    }

    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpensesOfSpecificCode(String code) {
        List<MultiYearBudgetExpense> multiYearBudgetExpensesOfSpecificCode = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getCode().equals(code)) {
                multiYearBudgetExpensesOfSpecificCode.add(multiYearBudgetExpense);
            }
        }
        return multiYearBudgetExpensesOfSpecificCode;
    }

    public static List<MultiYearBudgetExpense> getMultiYearExpensesOfEntityWithEntityCode(String entityCode) {
        List<MultiYearBudgetExpense> multiYearExpensesOfEntity = new ArrayList<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpensesOfEntities) {
            if (multiYearBudgetExpense.entityCode.equals(entityCode)) {
                multiYearExpensesOfEntity.add(multiYearBudgetExpense);
            }
        }
        return multiYearExpensesOfEntity;
    }

    public static long getSumOfSpecificYear(int year) {
        long sum = 0;
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearBudgetExpenses) {
            if (multiYearBudgetExpense.getYear() == year) {
                sum += multiYearBudgetExpense.getAmount();
            }
        }
        return sum;
    }

    public static Map<Integer, Long> getSumOfAllYears() {
        Map<Integer, Long> sumOfAllYears = new HashMap<>();
        List<Integer> uniqueYears = multiYearBudgetExpenses.stream()
                .map(MultiYearBudgetExpense::getYear)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        for (Integer year : uniqueYears) {
            sumOfAllYears.put(year, getSumOfSpecificYear(year));
        }
        return sumOfAllYears;
    }

    public long getRegularAmount() {
        return regularAmount;
    }

    public long getPublicInvestmentAmount() {
        return publicInvestmentAmount;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
