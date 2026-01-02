package com.financial.entries;

import java.util.ArrayList;

public class BudgetExpense extends BudgetEntry {

    private final String entityCode;
    private final String entityName;
    private final String serviceCode;
    private final String serviceName;
    protected static ArrayList <BudgetExpense> budgetExpenses = new ArrayList<>();

    public BudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(code, description, category, amount);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        budgetExpenses.add(this);
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static ArrayList <BudgetExpense> getBudgetExpenses() {
        return budgetExpenses;
    }

    @Override
    public String toString() {
        return "Entity Code: " + getEntityCode() + ", Entity Name : " + entityName + ", Service Code: " + serviceCode + ", Service Name: " + serviceName + ", Expense Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Amount: " + String.format("%,d", getAmount());
    }
}

