package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

/** Functional interface for defining a filter criterion for budget expenses. */
public interface ExpenseFilter {

    boolean matches(BudgetExpense expense);
}