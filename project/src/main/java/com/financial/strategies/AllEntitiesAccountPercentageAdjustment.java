package com.financial.strategies;

import java.util.ArrayList;
import com.financial.*;

public class AllEntitiesAccountPercentageAdjustment implements IExpenseAdjustmentStrategy {

    private final String targetExpenseCode;

    public AllEntitiesAccountPercentageAdjustment(String code) {
        this.targetExpenseCode = code;
    }

    @Override
    public long applyAdjustment(ArrayList<? extends BudgetExpense> expenses, double percentage, long fixedAmount) {
        long newTotal = 0;
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(targetExpenseCode)) {
                expense.setAmount((long) (expense.getAmount() * (1 + percentage)));
            }
            newTotal += expense.getAmount();
        }
        return newTotal;
    }
}