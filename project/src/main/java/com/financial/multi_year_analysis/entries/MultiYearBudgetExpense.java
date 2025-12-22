package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.List;

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
