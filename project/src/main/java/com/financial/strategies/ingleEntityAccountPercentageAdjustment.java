package com.financial.strategies;
import com.financial.*;

import java.util.ArrayList;

public class SingleEntityAccountPercentageAdjustment implements  IExpenseAdjustmentStrategy {

    private final String targetExpenseCode;

    public SingleEntityAccountPercentageAdjustment(String targetExpenseCode) {
        this.targetExpenseCode = targetExpenseCode;
    }
}