package com.financial.strategies;

import com.financial.entries.BudgetExpense;

import java.util.ArrayList;

public interface ExpenseAdjustmentStrategy {
    void applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount);
}