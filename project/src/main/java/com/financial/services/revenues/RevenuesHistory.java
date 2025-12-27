package com.financial.services.revenues;

import com.financial.entries.*;
import com.financial.services.BudgetType;

import java.util.*;

/**
 * Utility service class responsible for maintaining a history of budget revenue states.
 * <p>
 * This class implements an "Undo" mechanism using {@link Deque} structures to store
 * snapshots of revenue amounts and their corresponding budget types.
 */
public class RevenuesHistory {

    /**
     * Stack-like structure to store snapshots of revenue codes and their amounts.
     */
    public static Deque<Map<String, Long>> historyDeque = new ArrayDeque<>();

    /**
     * Stack-like structure to store the {@link BudgetType} associated with each history snapshot.
     */
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RevenuesHistory() {
        // utility class – no instances
    }

    /**
     * Returns the full history of revenue snapshots.
     * * @return a Deque containing maps of revenue codes and amounts
     */
    public static Deque<Map<String, Long>> getHistoryDeque() {
        return historyDeque;
    }

    /**
     * Returns the full history of budget types processed.
     * * @return a Deque containing the history of BudgetTypes
     */
    public static Deque<BudgetType> getTypeDeque() {
        return typeDeque;
    }

    /**
     * Captures a snapshot of the current state of a list of revenues and adds it to the history.
     *
     * @param revenues the list of revenue objects to record
     * @param type     the type of budget associated with these revenues
     */
    public static void keepHistory(ArrayList<? extends BudgetRevenue> revenues, BudgetType type) {
        Map<String, Long> modifiedElement = new HashMap<>();
        for (BudgetRevenue revenue : revenues) {
            modifiedElement.put(revenue.getCode(), revenue.getAmount());
        }
        historyDeque.addFirst(modifiedElement);
        typeDeque.addFirst(type);
    }

    /**
     * Retrieves the {@link BudgetType} of the most recent history entry.
     *
     * @return the most recent BudgetType, or {@code null} if history is empty
     */
    public static BudgetType getMostRecentBudgetType() {
        try {
            return typeDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    /**
     * Retrieves the revenue amount mapping from the most recent history entry.
     *
     * @return a Map of codes and amounts, or {@code null} if history is empty
     */
    public static Map<String, Long> getMostRecentRevenuesHistory() {
        try {
            return historyDeque.getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
            return null;
        }
    }

    /**
     * Reverts the application state to the most recent snapshot stored in history.
     * <p>
     * This method restores amounts for Regular, National, or Co-funded revenues
     * and removes the restored state from the history stacks.
     */
    public static void returnToPreviousState() {
        try {
            BudgetType type = getMostRecentBudgetType();
            Map<String, Long> recentHistory = getMostRecentRevenuesHistory();

            if (recentHistory != null && type != null) {
                for (Map.Entry<String, Long> entry : recentHistory.entrySet()) {
                    if (type.equals(BudgetType.REGULAR_BUDGET)) {
                        RegularBudgetRevenue revenue = RegularBudgetRevenue.findRegularBudgetRevenueWithCode(entry.getKey());
                        if (revenue != null) {
                            revenue.setAmount(entry.getValue());
                        }
                    } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL)) {
                        PublicInvestmentBudgetNationalRevenue revenue = PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(entry.getKey());
                        if (revenue != null) {
                            revenue.setAmount(entry.getValue());
                        }
                    } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED)) {
                        PublicInvestmentBudgetCoFundedRevenue revenue = PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(entry.getKey());
                        if (revenue != null) {
                            revenue.setAmount(entry.getValue());
                        }
                    }
                }
                historyDeque.pop();
                typeDeque.pop();
            }
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
        }
    }
}