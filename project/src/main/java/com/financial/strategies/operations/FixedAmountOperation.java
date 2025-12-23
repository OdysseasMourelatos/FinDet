package com.financial.strategies.operations;

public class FixedAmountOperation implements AdjustmentOperation {

    @Override
    public long apply(long oldAmount, double percentage, long fixedAmount) {
        return oldAmount + fixedAmount;
    }
}