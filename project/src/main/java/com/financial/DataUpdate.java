package com.financial;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataUpdate {
    public static void csvUpdate() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("GovernmentBudgetUpdated.csv"));
            writer.write("code,description,category,amount");
            for (BudgetEntry budgetEntry : BudgetEntry.budgetEntries) {
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
