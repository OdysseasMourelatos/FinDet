package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;
import com.financial.services.BudgetType;

import java.util.*;

public class ExpensesHistory {
    public static Deque<Map<String[], Long>> historyDeque = new ArrayDeque<>();
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    private ExpensesHistory() {
        // utility class – no instances
    }

    public static void keepHistory(ArrayList<? extends BudgetExpense> expenses, BudgetType type) {
        Map<String[], Long> modifiedElement = new HashMap<>();
        for (BudgetExpense expense : expenses) {
            String[] primaryKey = new String[2];
            primaryKey[0] = expense.getServiceCode();
            primaryKey[1] = expense.getCode();
            modifiedElement.put(primaryKey, expense.getAmount());
        }
        historyDeque.addFirst(modifiedElement);
        typeDeque.addFirst(type);
    }
}
