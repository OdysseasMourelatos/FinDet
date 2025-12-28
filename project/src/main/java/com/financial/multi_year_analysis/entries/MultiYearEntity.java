package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiYearEntity {
    protected List<MultiYearBudgetExpense> multiYearExpensesOfEntity = new ArrayList<>();
    private final String entityCode;
    private final String entityName;
    protected static List<MultiYearEntity> multiYearEntities = new ArrayList<>();

    public MultiYearEntity(String entityCode, String entityName, List<MultiYearBudgetExpense> multiYearExpensesOfEntity) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        // Σύνδεση με τα έξοδα του συγκεκριμένου φορέα
        this.multiYearExpensesOfEntity = MultiYearBudgetExpense.getMultiYearExpensesOfEntityWithEntityCode(entityCode);
        multiYearEntities.add(this);
    }

    public static MultiYearEntity findMultiYearEntityWithCode(String entityCode) {
        for (MultiYearEntity multiYearEntity : multiYearEntities) {
            if (multiYearEntity.getEntityCode().equals(entityCode)) {
                return multiYearEntity;
            }
        }
        return null;
    }

    public Map<Integer, Long> getTotalExpensesOfEntityPerYear() {
        Map<Integer, Long> totalExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getAmount());
        }
        return totalExpensesOfEntity;
    }

    public Map<Integer, Long> getTotalRegularExpensesOfEntityPerYear() {
        Map<Integer, Long> totalRegularExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalRegularExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getRegularAmount());
        }
        return totalRegularExpensesOfEntity;
    }

    public Map<Integer, Long> getTotalPublicInvestmentExpensesOfEntityPerYear() {
        Map<Integer, Long> totalPublicInvestmentExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalPublicInvestmentExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getPublicInvestmentAmount());
        }
        return totalPublicInvestmentExpensesOfEntity;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<MultiYearBudgetExpense> getMultiYearExpensesOfEntity() {
        return multiYearExpensesOfEntity;
    }

    public static List<MultiYearEntity> getMultiYearEntities() {
        return multiYearEntities;
    }
    @Override
    public String toString() {
        return "EntityCode: " + entityCode + ", EntityName: " + entityName;
    }
}
