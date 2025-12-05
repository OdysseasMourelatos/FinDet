package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

public interface ExpenseFilter {

    boolean matches(BudgetExpense expense);
}