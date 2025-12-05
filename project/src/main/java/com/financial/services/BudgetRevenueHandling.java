package com.financial.services;

import com.financial.entries.BudgetRevenue;

import java.util.ArrayList;

public class BudgetRevenueHandling {

    // Μέθοδος 1: Εύρεση Εσόδου με Βάση τον Κωδικό
    public static <T extends BudgetRevenue> T findRevenueWithCode(String code, ArrayList<T> revenues) {
        for (T revenue : revenues) {
            if (revenue.getCode().equals(code)) {
                return revenue;
            }
        }
        return null;
    }
}