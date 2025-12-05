package com.financial.strategies;

import com.financial.strategies.filters.ExpenseFilter;
import com.financial.strategies.operations.AdjustmentOperation;
import com.financial.entries.BudgetExpense;
import java.util.ArrayList;

public class PercentageAllocationAdjustmentStrategy implements IExpenseAdjustmentStrategy {

    private final ExpenseFilter filter;
    private final AdjustmentOperation operation;

    public PercentageAllocationAdjustmentStrategy(ExpenseFilter filter, AdjustmentOperation operation) {
        this.filter = filter;
        this.operation = operation;
    }

    @Override
    public void applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount) {
        for (BudgetExpense expense : expenses) {
            if (filter.matches(expense)) {
                long newAmount = operation.apply(expense.getAmount(), percentage, fixedAmount);
                expense.setAmount(roundToNearestHundred(newAmount));
            }
        }
    }
}