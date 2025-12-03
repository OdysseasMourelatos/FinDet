package com.financial;

import java.util.ArrayList;

public class Entity {
    private final String entityCode;
    private final String entityName;
    protected ArrayList<RegularBudgetExpense> regularBudgetExpenses;
    protected ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses;

    protected static ArrayList<Entity> entities = new ArrayList<>();

    public Entity(String entityCode, String entityName) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        regularBudgetExpenses = BudgetExpenseHandling.getExpensesOfEntityWithCode(entityCode, RegularBudgetExpense.getAllRegularBudgetExpenses());
        publicInvestmentBudgetExpenses = BudgetExpenseHandling.getExpensesOfEntityWithCode(entityCode, PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses());
        entities.add(this);
    }

    public static void printAllEntities() {
        for (Entity entity : entities) {
            System.out.println(entity);
        }
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

    public ArrayList<String> getRegularServiceCodes(){
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

    public static Entity findEntityWithEntityCode(String entityCode) {
        for (Entity entity : entities) {
            if (entity.getEntityCode().equals(entityCode)) {
                return entity;
            }
        }
        return null;
    }

    public static void printEntitiesWithTheirTotalRegularExpenses() {
        for (Entity entity: entities) {
            long totalRegularExpenses = RegularBudgetExpense.getRegularSumOfEntityWithEntityCode(entity.entityCode);
            System.out.println(entity + ", Total Regular Expenses: " + String.format("%,d", totalRegularExpenses));
        }
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }


    @Override
    public String toString() {
        return "Entity Code: " + entityCode + ", Name: " + name;
    }
}
