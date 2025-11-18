package com.financial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
                if (values.length == 2) {
                    createEntityFromCSV(values);
                } else if (values.length == 3) {
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

    private static void createBudgetRevenueFromCSV(String[] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        BudgetEntry budgetRevenue = switch (code.substring(0, 2)) {
            case "11" -> new TaxRevenue(code, description, category, amount);
            case "12" -> new SocialContributionsRevenue(code, description, category, amount);
            default -> new BudgetRevenue(code, description, category, amount);
        };
    }

    private static void createPublicInvestmentBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        String type = values[2];
        long amount = Long.parseLong(values[3]);
        BudgetEntry publicInvestmentBudgetRevenue = switch (code.substring(0, 2)) {
            default -> new PublicInvestmentBudgetRevenue(code, description, category, type, amount);
        };
    }

    private static void createRegularBudgetExpenseFromCSV(String[] values) {
        String entityCode = values[0];
        String code = values[1];
        String description = values[2];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[3]);
        BudgetEntry regularBudgetExpense = new RegularBudgetExpense(entityCode, code, description, category, amount);
    }

    private static void createPublicInvestmentBudgetExpenseFromCSV(String [] values) {
        String entityCode = values[0];
        String code = values[1];
        String description = values[2];
        String type = values[3];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[4]);
        BudgetEntry regularBudgetExpense = new PublicInvestmentBudgetExpense(entityCode, code, description, type, category, amount);
    }

    private static void createEntityFromCSV(String [] values) {
        String entityCode = values[0];
        String name = values[1];
        Entity entity = new Entity(entityCode, name);
    }
}
