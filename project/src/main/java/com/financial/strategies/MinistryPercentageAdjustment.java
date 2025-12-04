package com.financial.strategies;

import java.util.ArrayList;
import com.financial.*;

public class MinistryPercentageAdjustment implements IExpenseAdjustmentStrategy {

    @Override
    public long applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount) {
        long newTotal = 0;
        for (BudgetExpense expense : expenses) {
            expense.setAmount((long) (expense.getAmount() * (1 + percentage)));
            newTotal += expense.getAmount();
        }
        return newTotal;
    }
}
