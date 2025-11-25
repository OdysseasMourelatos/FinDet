package com.financial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataUpdate {
    public static void csvUpdate() {
        BufferedWriter writer = null;
        ArrayList<BudgetEntry> mainBudgetEntries = new ArrayList<>();
        try {
            writer = new BufferedWriter(new FileWriter("GovernmentBudget.csv"));
            writer.write("code,description,category,amount");
            for (BudgetEntry budgetEntry : BudgetEntry.mergeListsOfMainRevenuesAndMainExpenses()) {
                writer.write("\n" + budgetEntry.getCode() + "," + budgetEntry.getDescription() + "," + budgetEntry.getCategory() + "," + budgetEntry.getAmount() );
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
