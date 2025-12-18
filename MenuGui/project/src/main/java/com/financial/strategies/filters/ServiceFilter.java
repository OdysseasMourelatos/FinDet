package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

public class ServiceFilter implements ExpenseFilter {
    private final String targetServiceCode;

    public ServiceFilter(String targetServiceCode) {
        this.targetServiceCode = targetServiceCode;
    }

    @Override
    public boolean matches(BudgetExpense expense) {
        return expense.getServiceCode().equals(targetServiceCode);
    }
}