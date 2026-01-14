package com.financial.multi_year_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an organizational entity (such as a government department, agency, or organization)
 * that has associated budget expenses tracked across multiple years.
 * This class provides methods to analyze total, regular, and public investment expenses per year.
 * 
 * @author Financial Analysis System
 * @version 1.0
 */
public class MultiYearEntity {
    protected List<MultiYearBudgetExpense> multiYearExpensesOfEntity;
    private final String entityCode;
    private final String entityName;
    protected static List<MultiYearEntity> multiYearEntities = new ArrayList<>();

    /**
     * Constructs a new MultiYearEntity and automatically links it with its associated expenses.
     * The entity is registered in the global entities collection for later retrieval.
     * Note: The multiYearExpensesOfEntity parameter is overridden by expenses loaded
     * from the expense collection based on entityCode.
     * 
     * @param entityCode the unique identifier code for this entity
     * @param entityName the display name of this entity
     */
    public MultiYearEntity(String entityCode, String entityName) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        // Link with expenses from the specific entity
        this.multiYearExpensesOfEntity = MultiYearBudgetExpense.getMultiYearExpensesOfEntityWithEntityCode(entityCode);
        multiYearEntities.add(this);
    }

    /**
     * Returns the complete list of all multi-year entities.
     *
     * @return a list containing all entity entries created
     */
    public static List<MultiYearEntity> getMultiYearEntities() {
        return multiYearEntities;
    }

    /**
     * Searches for and returns an entity with the specified entity code.
     *
     * @param entityCode the entity code to search for
     * @return the matching MultiYearEntity, or null if no entity with that code exists
     */
    public static MultiYearEntity findMultiYearEntityWithCode(String entityCode) {
        for (MultiYearEntity multiYearEntity : multiYearEntities) {
            if (multiYearEntity.getEntityCode().equals(entityCode)) {
                return multiYearEntity;
            }
        }
        return null;
    }

    /**
     * Calculates the total expenses (regular + public investment) for this entity
     * broken down by year.
     * 
     * @return a map where keys are years and values are total expense amounts for each year
     */
    public Map<Integer, Long> getTotalExpensesOfEntityPerYear() {
        Map<Integer, Long> totalExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getAmount());
        }
        return totalExpensesOfEntity;
    }

    /**
     * Calculates the regular (non-investment) expenses for this entity
     * broken down by year.
     * 
     * @return a map where keys are years and values are regular expense amounts for each year
     */
    public Map<Integer, Long> getTotalRegularExpensesOfEntityPerYear() {
        Map<Integer, Long> totalRegularExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalRegularExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getRegularAmount());
        }
        return totalRegularExpensesOfEntity;
    }

    /**
     * Calculates the public investment expenses for this entity
     * broken down by year.
     * 
     * @return a map where keys are years and values are public investment expense amounts for each year
     */
    public Map<Integer, Long> getTotalPublicInvestmentExpensesOfEntityPerYear() {
        Map<Integer, Long> totalPublicInvestmentExpensesOfEntity = new HashMap<>();
        for (MultiYearBudgetExpense multiYearBudgetExpense : multiYearExpensesOfEntity) {
            totalPublicInvestmentExpensesOfEntity.put(multiYearBudgetExpense.getYear(), multiYearBudgetExpense.getPublicInvestmentAmount());
        }
        return totalPublicInvestmentExpensesOfEntity;
    }

    /**
     * Returns the unique identifier code for this entity.
     * 
     * @return the entity code
     */
    public String getEntityCode() {
        return entityCode;
    }

    /**
     * Returns the display name of this entity.
     * 
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Returns the list of all expense entries associated with this entity.
     * 
     * @return a list of MultiYearBudgetExpense objects for this entity
     */
    public List<MultiYearBudgetExpense> getMultiYearExpensesOfEntity() {
        return multiYearExpensesOfEntity;
    }

    /**
     * Returns a formatted string representation of this entity.
     * 
     * @return a string containing the entity code and name
     */
    @Override
    public String toString() {
        return "EntityCode: " + entityCode + ", EntityName: " + entityName;
    }
}
