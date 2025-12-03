package com.financial;

import java.util.ArrayList;

public class BudgetExpense extends BudgetEntry {

    private String entityCode;
    private String entityName;
    private String serviceCode;
    private String serviceName;
    protected static ArrayList <BudgetExpense> expenses = new ArrayList<>();

    public BudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(code, description, category, amount);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        expenses.add(this);
    }

    public BudgetExpense(String code, String description, String category, long amount) {
        super(code, description, category, amount);
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

    @Override
    public String toString () {
        return super.toString();
    }
}

