package com.financial.strategies.operations;

/** Implements a linear adjustment by adding a constant fixed amount to the existing budget value. */
public class FixedAmountOperation implements AdjustmentOperation {

    @Override
    public long apply(long oldAmount, double percentage, long fixedAmount) {
        return oldAmount + fixedAmount;
    }
}