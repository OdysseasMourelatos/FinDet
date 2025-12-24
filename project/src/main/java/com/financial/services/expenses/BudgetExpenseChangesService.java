package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;
import com.financial.strategies.ExpenseAdjustmentStrategy;
import com.financial.strategies.PercentageAllocationAdjustmentStrategy;
import com.financial.strategies.filters.AccountFilter;
import com.financial.strategies.filters.MatchAllFilter;
import com.financial.strategies.operations.FixedAmountOperation;
import com.financial.strategies.operations.PercentageOperation;

import java.util.ArrayList;

public class BudgetExpenseChangesService {

    private BudgetExpenseChangesService() {
        // utility class – no instances
    }

    public static void implementGlobalChangesInCertainExpenseCategoryWithPercentageAllocation(String code, double percentage, long fixedAmount, ArrayList<? extends BudgetExpense> expenses) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
        } else {
            strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new FixedAmountOperation());
        }
        strategy.applyAdjustment(expenses, percentage, fixedAmount);
    }

    public static void implementGlobalChangesInAllExpenseCategoriesWithPercentageAllocation(double percentage, long fixedAmount, ArrayList<? extends BudgetExpense> expenses) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
        } else {
            strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new FixedAmountOperation());
        }
        strategy.applyAdjustment(expenses, percentage, fixedAmount);
    }
}
