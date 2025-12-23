package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseHandling;
import com.financial.services.revenues.BudgetRevenueLogicService;

import java.util.ArrayList;

public abstract class BudgetEntry {
    private final String code;
    private final String description;
    private final String category;
    protected long amount;

    public BudgetEntry(String code, String description, String category, long amount) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
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
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static ArrayList<BudgetEntry> mergeListsOfMainRevenuesAndMainExpenses() {
        ArrayList<BudgetEntry> merged = new ArrayList<>();
        merged.addAll(BudgetRevenueLogicService.getMainBudgetRevenues(BudgetRevenue.budgetRevenues));
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
