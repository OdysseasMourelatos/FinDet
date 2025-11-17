package com.financial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DataInput {

    public static void simpleCSVReader(String filePath) {
        String line;
        int lineNumber=0;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }
                String[] values = line.split(",");
                if (values.length > 5) {
                    System.out.println("Σφάλμα στη γραμμή: " + Arrays.toString(values));
                } else {
                    createBudgetRevenueFromCSV(values);
                }
            }
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

    public static void advancedCSVReader(String filePath) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
            reader.readNext();
            String[] values;
            while ((values = reader.readNext()) != null) {
                if (values.length == 3) {
                    createBudgetRevenueFromCSV(values);
                } else if (values.length == 4) {
                    if (values[0].length() == 4) {
                        createRegularBudgetExpenseFromCSV(values);
                    } else {
                        createPublicInvestmentBudgetRevenueFromCSV(values);
                    }
                } else if (values.length == 5) {
                    createPublicInvestmentBudgetExpenseFromCSV(values);
                } else if (values.length > 5) {
                    System.out.println("Σφάλμα στη γραμμή: " + Arrays.toString(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
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
        String code = values[0];
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
