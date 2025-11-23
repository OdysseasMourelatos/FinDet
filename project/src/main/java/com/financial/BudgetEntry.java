package com.financial;

import java.util.ArrayList;

public abstract class BudgetEntry {
    private final String code;
    private final String description;
    private final String category;
    private long amount;
    protected static ArrayList<BudgetEntry> budgetEntries= new ArrayList<>();

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

    public String getCategory(){
        return this.category;
    }

    public long getAmount(){
        return this.amount;
    }

    public void setAmount(long amount) {
        if (amount>0) {
            this.amount = amount;
        } else {
            System.out.println("Το ποσό δεν μπορεί να είναι αρνητικό");
        }
    }

    public static ArrayList<BudgetEntry> mergeListsOfMainRevenuesAndMainExpenses() {
        ArrayList<BudgetEntry> merged = new ArrayList<>();
        merged.addAll(BudgetRevenue.getMainBudgetRevenues());
        merged.addAll(BudgetExpense.getSumOfEveryCategory());
        return merged;
    }

    public static void printMergedListsOfMainRevenuesAndMainExpenses(){
        ListHandling.printList(mergeListsOfMainRevenuesAndMainExpenses());
    }

    public static void printWithAsciiTable(ArrayList<BudgetEntry> entries) {
        AsciiTable at = new AsciiTable();

        at.addRule();
        at.addRow("Κωδικός", "Περιγραφή", "Κατηγορία", "Ποσό");
        at.addRule();

        for (BudgetEntry entry : entries) {
            at.addRow(
                    entry.getCode(),
                    entry.getDescription(),
                    entry.getCategory(),
                    entry.getAmount()
            );
            at.addRule();
        }

        System.out.println(at.render());
    }

    @Override
    public String toString(){
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + String.format("%,d", amount);
    }
}
