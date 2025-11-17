package com.financial;

import java.util.ArrayList;

public class PublicInvestmentBudgetExpense extends BudgetExpense{
    String entityCode;
    String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    PublicInvestmentBudgetExpense (String entityCode, String code, String description, String type, String category, long amount) {
        this.entityCode=entityCode;
        this.type = type;
        super(code,description,category,amount);
        publicInvestmentBudgetExpenses.add(this);
    }
}