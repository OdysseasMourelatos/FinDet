package com.financial.strategies.operations;

/** Strategy interface for calculating a new budget amount based on percentage or fixed value adjustments. */
public interface AdjustmentOperation {
    long apply(long oldAmount, double percentage, long fixedAmount);
}