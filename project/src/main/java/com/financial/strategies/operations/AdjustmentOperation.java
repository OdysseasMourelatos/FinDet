package com.financial.strategies.operations;

public interface AdjustmentOperation {
    long apply(long oldAmount, double percentage, long fixedAmount);
}