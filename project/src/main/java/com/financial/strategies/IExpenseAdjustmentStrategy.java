package com.financial.strategies;

import com.financial.*;
import java.util.ArrayList;

public interface IExpenseAdjustmentStrategy {
    void applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount);
}