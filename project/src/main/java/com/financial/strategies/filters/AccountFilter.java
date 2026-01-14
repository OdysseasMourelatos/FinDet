package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

/** Filters expenses by comparing their account code against a specific target. */
public class AccountFilter implements ExpenseFilter {
    private final String targetAccount;

    public AccountFilter(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    @Override
    public boolean matches(BudgetExpense expense) {
        return expense.getCode().equals(targetAccount);
    }
}