package com.financial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegularBudgetExpense extends BudgetExpense {
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    public RegularBudgetExpense (String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    public RegularBudgetExpense (String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static void printAllRegularBudgetExpenses() {
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            System.out.println(regularBudgetExpense);
        }
    }

    public static long getSumOfRegularBudgetExpenses() {
        long sum = 0;
        for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
            sum += regularBudgetExpense.getAmount();
        }
        return sum;
    }

    public static Map<String, Long> getRegularSumOfEveryEntity() {
        String[] entityCodes = regularBudgetExpenses.stream().map(RegularBudgetExpense::getEntityCode).distinct().sorted().toArray(String[]::new);
        Map<String, Long> regularEntitySums = new HashMap<>();
        for (String entityCode : entityCodes) {
            long sum = 0;
            for (RegularBudgetExpense regularBudgetExpense : regularBudgetExpenses) {
                if (entityCode.equals(regularBudgetExpense.entityCode)) {
                    sum += regularBudgetExpense.getAmount();
                }
            }
            regularEntitySums.merge(entityCode, sum, Long::sum);
        }
        return regularEntitySums;
    }

    public static void printRegularSumOfEveryEntity() {
        Map<String, Long> entitySums = getRegularSumOfEveryEntity();
        entitySums.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry ->
                        System.out.println("Κωδικός Φορέα " + entry.getKey() + ": " + String.format("%,d", entry.getValue())));
    }

    public static long getRegularSumOfEntityWithEntityCode(String entityCode) {
        Map<String, Long> entitySums = getRegularSumOfEveryEntity();
        if (entitySums.containsKey(entityCode)) {
            return entitySums.get(entityCode);
        }
        return 0;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
