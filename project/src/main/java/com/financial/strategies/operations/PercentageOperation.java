package com.financial.strategies.operations;

public class PercentageOperation implements AdjustmentOperation {

    @Override
    public long apply(long oldAmount, double percentage, long fixedAmount) {
        return (long)(oldAmount * (1.0 + percentage));
    }
}
