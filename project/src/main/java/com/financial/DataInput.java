package com.financial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataInput {

    public static void csvReader(String filePath) {
        String line;
        int lineNumber=0;
        BudgetEntry budgetEntry = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }
                String[] values = line.split(",");
                createBudgetEntryFromCSV(values);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createBudgetEntryFromCSV(String[] values){
        BudgetEntry budgetEntry = null;
        int code = Integer.parseInt(values[0]);
        String description = values[1];
        String category = values[2];
        long amount = Long.parseLong(values[3]);
        if (values[2].equals("ΕΣΟΔΑ")) {
            budgetEntry = new BudgetRevenue(code, description, category, amount);
        } else if (values[2].equals("ΕΞΟΔΑ")) {
            budgetEntry = new BudgetExpense(code, description, category, amount);
        }

    }
}
