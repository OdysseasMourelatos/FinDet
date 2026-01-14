package com.financial.strategies.filters;

import com.financial.entries.BudgetExpense;

/** A bypass filter used for global operations that accepts every expense entry. */
public class MatchAllFilter implements ExpenseFilter {

    @Override
    public boolean matches(BudgetExpense expense) {
        // Επιστρέφει πάντα true. Χρησιμοποιείται για Global ή Entity-Wide αλλαγές.
        return true;
    }
}