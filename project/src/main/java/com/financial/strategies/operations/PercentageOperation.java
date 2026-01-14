package com.financial.strategies.operations;

/** Implements a scaling adjustment by applying a percentage-based increase or decrease to the budget value. */
public class PercentageOperation implements AdjustmentOperation {

    @Override
    public long apply(long oldAmount, double percentage, long fixedAmount) {
        return (long)(oldAmount * (1.0 + percentage));
    }
}
