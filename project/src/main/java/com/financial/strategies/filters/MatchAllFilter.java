package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

public class MatchAllFilter implements ExpenseFilter {

    @Override
    public boolean matches(BudgetExpense expense) {
        // Επιστρέφει πάντα true. Χρησιμοποιείται για Global ή Entity-Wide αλλαγές.
        return true;
    }
}