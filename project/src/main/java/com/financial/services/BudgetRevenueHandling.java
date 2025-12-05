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

    // Μέθοδος 2: Εύρεση Bασικών Λογαριασμών
    public static ArrayList<BudgetRevenue> getMainBudgetRevenues(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        ArrayList<BudgetRevenue> mainBudgetRevenues = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                mainBudgetRevenues.add(budgetRevenue);
            }
        }
        return mainBudgetRevenues;
    }

    // Μέθοδος 3: Υπολογισμός Αθροίσματος
    public static long calculateSum(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        long sum = 0;
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().length() == 2) {
                sum += budgetRevenue.getAmount();
            }
        }
        return sum;
    }

    // Μέθοδος 4: Εκτύπωση Λογαριασμών
    public static void printMainBudgetRevenues(ArrayList<? extends BudgetRevenue> budgetRevenues) {
        DataOutput.printRevenueWithAsciiTable(getMainBudgetRevenues(budgetRevenues), calculateSum(budgetRevenues));
    }
}