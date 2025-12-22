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
}
