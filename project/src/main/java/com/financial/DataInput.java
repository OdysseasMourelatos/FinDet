package com.financial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.financial.entries.BudgetEntry;
import com.financial.entries.BudgetExpense;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DataInput {

    public static void simpleCSVReader(String filePath) {
        String line;
        int lineNumber = 0;
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
                    return;
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
                } else if (values.length == 7) {
                    createRegularBudgetExpensePerServiceFromCSV(values);
                } else if (values.length == 8) {
                    createPublicInvestmentBudgetExpenseFromCSV(values);
                } else if (values.length > 8) {
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
        BudgetRevenue budgetRevenue = new BudgetRevenue(code, description, category, amount);
        budgetRevenue.addBudgetRevenueToArrayList();
    }

    protected static void createRegularBudgetRevenueFromCSV() {
        ArrayList<BudgetRevenue> budgetRevenues = BudgetRevenue.getAllBudgetRevenues();
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetNationalRevenues = PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetNationalRevenues();
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetCoFundedRevenues = PublicInvestmentBudgetRevenue.getPublicInvestmentBudgetCoFundedRevenues();
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            //Αντιγραφή όλων τον αντικειμένων στη λίστα budgetRevenues
            RegularBudgetRevenue regularBudgetRevenue = new RegularBudgetRevenue(budgetRevenue.getCode(), budgetRevenue.getDescription(), "ΕΞΟΔΑ", budgetRevenue.getAmount());
            for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : publicInvestmentBudgetNationalRevenues) {
                if (budgetRevenue.getCode().equals(publicInvestmentBudgetRevenue.getCode())) {
                    long amount = budgetRevenue.getAmount() - publicInvestmentBudgetRevenue.getAmount();
                    if (amount == 0) {
                        RegularBudgetRevenue.regularBudgetRevenues.remove(regularBudgetRevenue);
                    } else {
                        regularBudgetRevenue.setAmount(amount);
                    }
                }
            }
            for (PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue : publicInvestmentBudgetCoFundedRevenues) {
                if (budgetRevenue.getCode().equals(publicInvestmentBudgetRevenue.getCode())) {
                    long amount = regularBudgetRevenue.getAmount() - publicInvestmentBudgetRevenue.getAmount();
                    if (amount == 0) {
                        RegularBudgetRevenue.regularBudgetRevenues.remove(regularBudgetRevenue);
                    } else {
                        regularBudgetRevenue.setAmount(amount);
                    }
                }
            }
        }
    }

    private static void createPublicInvestmentBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        String type = values[2];
        long amount = Long.parseLong(values[3]);
        BudgetEntry publicInvestmentBudgetRevenue = new PublicInvestmentBudgetRevenue(code, description, category, type, amount);
    }

    private static void createRegularBudgetExpenseFromCSV(String[] values) {
        String entityCode = values[0];
        String code = values[1];
        String description = values[2];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[3]);
        //BudgetEntry regularBudgetExpense = new RegularBudgetExpense(entityCode, code, description, category, amount);
    }

    private static void createRegularBudgetExpensePerServiceFromCSV(String[] values) {
        String entityCode = values[0];
        String entityName = values[1];
        String serviceCode = values[2];
        String serviceName = values[3];
        String expenseCode = values[4];
        String description = values[5];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[6]);
        BudgetEntry regularBudgetExpense = new RegularBudgetExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, category, amount);
    }

    private static void createPublicInvestmentBudgetExpenseFromCSV(String [] values) {
        String entityCode = values[0];
        String entityName = values[1];
        String serviceCode = values[2];
        String serviceName = values[3];
        String expenseCode = values[4];
        String description = values[5];
        String type = values[6];
        long amount = Long.parseLong(values[7]);
        String category = "ΕΞΟΔΑ";
        BudgetEntry publicInvestmentBudgetExpense = new PublicInvestmentBudgetExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
    }

    protected static void createEntityFromCSV() {
        Map<String, String> entityMap = BudgetExpense.expenses.stream().collect(Collectors.toMap(BudgetExpense::getEntityCode, BudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new Entity(entityCode, entityMap.get(entityCode)));
    }
}
