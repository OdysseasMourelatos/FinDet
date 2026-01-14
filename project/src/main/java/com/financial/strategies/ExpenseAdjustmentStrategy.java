package com.financial.strategies;

import com.financial.entries.BudgetExpense;

import java.util.ArrayList;

/** Defines the contract for applying financial modifications to a collection of budget expenses. */
public interface ExpenseAdjustmentStrategy {
    void applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount);
}