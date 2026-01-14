package com.financial.services.expenses;

import com.financial.entries.*;
import com.financial.services.BudgetType;

import java.util.*;

/**
 * Manages the state history of budget expenses, providing undo (revert) capabilities using a LIFO stack mechanism.
 * <p>
 * This utility class snapshots expense amounts before modifications and stores them along with their
 * respective {@link BudgetType} context to allow precise restoration of previous financial states.
 * </p>
 */
public class ExpensesHistory {

    /** Double-ended queue storing snapshots of expense amounts mapped by their composite primary keys. */
    public static Deque<Map<String[], Long>> historyDeque = new ArrayDeque<>();

    /** Double-ended queue tracking the budget type context for each historical snapshot. */
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    private ExpensesHistory() {
        // utility class – no instances
    }

    /** @return The collection of all historical snapshots currently in the stack. */
    public static Deque<Map<String[], Long>> getHistoryDeque() {
        return historyDeque;
    }

    /** @return The stack of budget types corresponding to the stored history snapshots. */
    public static Deque<BudgetType> getTypeDeque() {
        return typeDeque;
    }

    /** Captures the current amounts of the provided expenses and pushes them onto the history stack. */
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

    /** Retrieves the budget type of the most recently saved state without removing it from the stack. */
    public static BudgetType getMostRecentBudgetType() {
        try {
            return typeDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    /** Retrieves the map of expense amounts from the most recently saved state. */
    public static Map<String[], Long> getMostRecentExpensesHistory() {
        try {
            return historyDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    /** Pops the latest state from the stack and restores the previous amounts to the corresponding expense objects. */
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
