package com.financial.services.revenues;

import com.financial.entries.*;
import com.financial.services.BudgetType;

import java.util.*;

public class RevenuesHistory {
    public static Deque<Map<String, Long>> historyDeque = new ArrayDeque<>();
    public static Deque<BudgetType> typeDeque = new ArrayDeque<>();

    private RevenuesHistory() {
        // utility class – no instances
    }

    public static Deque<Map<String, Long>> getHistoryDeque() {
        return historyDeque;
    }

    public static Deque<BudgetType> getTypeDeque() {
        return typeDeque;
    }

    public static void keepHistory(ArrayList<? extends BudgetRevenue> revenues, BudgetType type) {
        Map<String, Long> modifiedElement = new HashMap<>();
        for (BudgetRevenue revenue : revenues) {
            modifiedElement.put(revenue.getCode(), revenue.getAmount());
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

    public static Map<String, Long> getMostRecentRevenuesHistory() {
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
            for (Map.Entry<String, Long> entry : getMostRecentRevenuesHistory().entrySet()) {
                if (type.equals(BudgetType.REGULAR_BUDGET)) {
                    RegularBudgetRevenue revenue = RegularBudgetRevenue.findRegularBudgetRevenueWithCode(entry.getKey());
                    revenue.setAmount(entry.getValue());
                } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL)) {
                    PublicInvestmentBudgetNationalRevenue revenue = PublicInvestmentBudgetNationalRevenue.findPublicInvestmentBudgetNationalRevenueWithCode(entry.getKey());
                    revenue.setAmount(entry.getValue());
                } else if (type.equals(BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED)) {
                    PublicInvestmentBudgetCoFundedRevenue revenue = PublicInvestmentBudgetCoFundedRevenue.findPublicInvestmentBudgetCoFundedRevenueWithCode(entry.getKey());
                    revenue.setAmount(entry.getValue());
                }
            }
            historyDeque.pop();
            typeDeque.pop();
        } catch (NoSuchElementException e) {
            System.out.println("NO HISTORY FOUND");
        }
    }
}
