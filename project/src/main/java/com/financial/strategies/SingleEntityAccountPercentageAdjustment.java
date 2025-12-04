package com.financial.strategies;
import com.financial.*;

import java.util.ArrayList;

public class SingleEntityAccountPercentageAdjustment implements  IExpenseAdjustmentStrategy {

    private final String targetExpenseCode;

    public SingleEntityAccountPercentageAdjustment(String targetExpenseCode) {
        this.targetExpenseCode = targetExpenseCode;
    }

    @Override
    public long applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount) {
        long newTotal = 0;
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(targetExpenseCode)) {
                expense.setAmount( (long) (expense.getAmount() * (1+percentage)));
            }
            newTotal += expense.getAmount();
        }
        return newTotal;
    }
}