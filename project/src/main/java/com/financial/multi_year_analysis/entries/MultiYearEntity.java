package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.List;

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

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<MultiYearBudgetExpense> getMultiYearExpensesOfEntity() {
        return multiYearExpensesOfEntity;
    }

    @Override
    public String toString() {
        return "EntityCode: " + entityCode + ", EntityName: " + entityName;
    }
}
