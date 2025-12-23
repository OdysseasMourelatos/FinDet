package com.financial.entries;

import com.financial.services.BudgetExpenseHandling;
import com.financial.services.BudgetRevenueHandling;

import java.util.ArrayList;

public abstract class BudgetEntry {
    private final String code;
    private final String description;
    private final String category;
    private long amount;
    protected static ArrayList<BudgetEntry> budgetEntries = new ArrayList<>();

    public BudgetEntry(String code, String description, String category, long amount) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
        budgetEntries.add(this);
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory() {
        return this.category;
    }

    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        if (amount > 0) {
            this.amount = amount;
        } else {
            System.out.println("Το ποσό δεν μπορεί να είναι αρνητικό");
        }
    }

    public static ArrayList<BudgetEntry> mergeListsOfMainRevenuesAndMainExpenses() {
        ArrayList<BudgetEntry> merged = new ArrayList<>();
        merged.addAll(BudgetRevenueHandling.getMainBudgetRevenues(BudgetRevenue.budgetRevenuesFiltered));
        merged.addAll(BudgetExpenseHandling.getSumOfEveryCategory(BudgetExpense.expenses));
        return merged;
    }

    public static void printMergedListsOfMainRevenuesAndMainExpenses() {
        //DataOutput.printWithAsciiTable(mergeListsOfMainRevenuesAndMainExpenses());
    }

    @Override
    public String toString() {
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + String.format("%,d", amount);
    }
}
