package com.financial.services.expenses;

import com.financial.entries.*;
import com.financial.services.BudgetType;

import java.util.*;

public class ExpensesHistory {
    public static Deque<Map<String[], Long>> historyDeque = new ArrayDeque<>();
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    private ExpensesHistory() {
        // utility class – no instances
    }

    public static Deque<Map<String[], Long>> getHistoryDeque() {
        return historyDeque;
    }

    public static Deque<BudgetType> getTypeDeque() {
        return typeDeque;
    }

    public static void keepHistory(ArrayList<? extends BudgetExpense> expenses, BudgetType type) {
        Map<String[], Long> modifiedElement = new HashMap<>();
        for (BudgetExpense expense : expenses) {
            String[] primaryKey = new String[3];
            primaryKey[0] = expense.getEntityCode();
            primaryKey[1] = expense.getServiceCode();
            primaryKey[2] = expense.getCode();
            modifiedElement.put(primaryKey, expense.getAmount());
        }
        historyDeque.addFirst(modifiedElement);
        typeDeque.addFirst(type);
    }

    public static BudgetType getMostRecentBudgetType() {
        try {
            return typeDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    public static Map<String[], Long> getMostRecentExpensesHistory() {
        try {
            return historyDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    public static void returnToPreviousState() {
        try {
            BudgetType type = getMostRecentBudgetType();
            for (Map.Entry<String[], Long> entry : Objects.requireNonNull(getMostRecentExpensesHistory()).entrySet()) {
                String[] primaryKey = entry.getKey();
                if (Objects.requireNonNull(type).equals(BudgetType.REGULAR_BUDGET)) {
                    RegularBudgetExpense expense = RegularBudgetExpense.findRegularBudgetExpenseWithCodes(primaryKey[0], primaryKey[1], primaryKey[2]);
                    expense.setAmount(entry.getValue());
                } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL)) {
                    PublicInvestmentBudgetNationalExpense expense = PublicInvestmentBudgetNationalExpense.findPublicInvestmentBudgetNationalExpenseWithCodes(primaryKey[0], primaryKey[1], primaryKey[2]);
                    expense.setAmount(entry.getValue());
                } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED)) {
                    PublicInvestmentBudgetCoFundedExpense expense = PublicInvestmentBudgetCoFundedExpense.findPublicInvestmentBudgetCoFundedExpenseWithCodes(primaryKey[0], primaryKey[1], primaryKey[2]);
                    expense.setAmount(entry.getValue());
                }
            }
            historyDeque.pop();
            typeDeque.pop();
        } catch (NoSuchElementException | NullPointerException e) {
            System.out.println("NO HISTORY FOUND");
        }
    }
}
