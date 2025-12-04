package com.financial.strategies;

import java.util.ArrayList;
import com.financial.*;

public class AllEntitiesAccountPercentageAdjustment implements IExpenseAdjustmentStrategy {

    private final String targetExpenseCode;

    public AllEntitiesAccountPercentageAdjustment(String code) {
        this.targetExpenseCode = code;
    }
}