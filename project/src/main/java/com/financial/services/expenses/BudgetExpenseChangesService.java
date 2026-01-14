package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;
import com.financial.strategies.ExpenseAdjustmentStrategy;
import com.financial.strategies.FilteredExpenseAdjustmentStrategy;
import com.financial.strategies.filters.AccountFilter;
import com.financial.strategies.filters.MatchAllFilter;
import com.financial.strategies.operations.FixedAmountOperation;
import com.financial.strategies.operations.PercentageOperation;

import java.util.ArrayList;

/**
 * Utility service that orchestrates mass budget adjustments by selecting and
 * executing the appropriate {@link ExpenseAdjustmentStrategy}.
 */
public class BudgetExpenseChangesService {

    private BudgetExpenseChangesService() {
        // utility class – no instances
    }

    /** Applies a percentage or fixed amount adjustment to a specific expense category across all entities. */
    public static void implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount, ArrayList<? extends BudgetExpense> expenses) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new FilteredExpenseAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
        } else {
            strategy = new FilteredExpenseAdjustmentStrategy(new AccountFilter(code), new FixedAmountOperation());
        }
        strategy.applyAdjustment(expenses, percentage, fixedAmount);
    }

    /** Applies a global percentage or fixed amount adjustment to every expense category across all entities. */
    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount, ArrayList<? extends BudgetExpense> expenses) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new FilteredExpenseAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
        } else {
            strategy = new FilteredExpenseAdjustmentStrategy(new MatchAllFilter(), new FixedAmountOperation());
        }
        strategy.applyAdjustment(expenses, percentage, fixedAmount);
    }
}
