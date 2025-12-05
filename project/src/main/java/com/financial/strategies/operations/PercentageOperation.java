package com.financial.strategies.operations;

public class PercentageOperation implements AdjustmentOperation {

    @Override
    public long apply(long oldAmount, double percentage, long fixedAmount) {
        long newAmount = (long)(oldAmount * (1.0 + percentage));
        return newAmount;
    }
}
