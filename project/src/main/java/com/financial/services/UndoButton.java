package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.*;

public class UndoButton {
    public static Deque<Map<String, Long>> historyDeque = new ArrayDeque<>();
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();


    public static void keepHistory(ArrayList<? extends BudgetRevenue> revenues, BudgetType type) {
        Map<String, Long> modifiedElement = new HashMap<>();
        for (BudgetRevenue revenue : revenues) {
            modifiedElement.put(revenue.getCode(), revenue.getAmount());
        }
        historyDeque.add(modifiedElement);
        typeDeque.add(type);
    }

    public static BudgetType getLastTypeInHistory() {
        return typeDeque.getLast();
    }

    public static Map<String, Long> getLastEntriesInRevenuesHistory() {
        return historyDeque.getLast();
    }

}
