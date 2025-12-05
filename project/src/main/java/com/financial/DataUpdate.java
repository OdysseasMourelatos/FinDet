package com.financial;

import com.financial.entries.BudgetEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataUpdate {

    public static void csvUpdate() {
        csvUpdate("GovernmentBudget.csv"); // default file
    }

    public static void csvUpdate(String filePath) {
        BufferedWriter writer = null;
        ArrayList<BudgetEntry> mainBudgetEntries = new ArrayList<>();
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
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
