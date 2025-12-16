package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;

public class UndoButton implements Command {
    public static Deque<Map<String,Long>> historyDeque = new ArrayDeque<>();
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    @Override execute(int times) {
        if (times ==1) {
        BudgetType type = typeDeque.getFirst();
        }     
    }

    public static void keepHistory(ArrayList<? extends BudgetRevenue> revenues, BudgetType type) {
        Map<String, Long> modifiedElement = new HashMap<>();
        for(BudgetRevenue revenue : revenues) {
            revenueMap.put(revenue.getDescription(), revenue.getAmount());
        }

        historyDeque.addFirst(modifiedElement);
        typeDeque.addFirst(type);
    }

    public static BudgetType getPreviousType () {
        return typeDeque.getFirst();
    }

    public static Map<String,Long> getHistory() {
        return historyDeque.getFirst();
    }
    
}
