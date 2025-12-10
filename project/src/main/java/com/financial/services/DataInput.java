package com.financial.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.financial.entries.*;
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
            String[] header = reader.readNext();
            String csvType = determineCSVType(header);
            String[] values;
            while ((values = reader.readNext()) != null) {
                processCSVRow(csvType, values);
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

    private static String determineCSVType(String[] header) {
        Set<String> headerSet = new HashSet<>(Arrays.asList(header));

        if (headerSet.contains("Σκέλος")) {

            if (headerSet.contains("Κωδικός_Υπηρεσίας")) {
                return "PUBLIC_INVESTMENT_EXPENSES_PER_SERVICE";
            }

            return "PUBLIC_INVESTMENT_REVENUES";

        } else if (headerSet.contains("Κωδικός_Υπηρεσίας")) {
            return "REGULAR_EXPENSES_PER_SERVICE";
        } else if (headerSet.size() == 3) {
            return "REGULAR_REVENUES";
        }
        return "UNKNOWN";
    }

    private static void processCSVRow(String csvType, String[] values) {
        switch (csvType) {
            case "REGULAR_REVENUES" -> createRegularBudgetRevenueFromCSV(values);
            case "PUBLIC_INVESTMENT_REVENUES" -> createPublicInvestmentBudgetRevenueFromCSV(values);
            case "REGULAR_EXPENSES_PER_SERVICE" -> createRegularBudgetExpenseFromCSV(values);
            case "PUBLIC_INVESTMENT_EXPENSES_PER_SERVICE" -> createPublicInvestmentBudgetExpenseFromCSV(values);
            case "UNKNOWN" -> System.out.println("Σφάλμα: Άγνωστος τύπος CSV.");
            default -> System.out.println("Σφάλμα στη γραμμή: " + Arrays.toString(values));
        }
    }

    public static void createBudgetRevenueFromCSV() {
        ArrayList<RegularBudgetRevenue> regularBudgetRevenues = RegularBudgetRevenue.getAllRegularBudgetRevenues();
        ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues =  PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues();
        for (RegularBudgetRevenue revenue : regularBudgetRevenues) {
            BudgetRevenue budgetRevenue = new BudgetRevenue(revenue.getCode(), revenue.getDescription(), revenue.getCategory(), revenue.getAmount());
            budgetRevenue.addBudgetRevenueToArrayList();
        }

        for (PublicInvestmentBudgetRevenue revenue : publicInvestmentBudgetRevenues) {

            BudgetRevenue existingRevenue = BudgetRevenueHandling.findRevenueWithCode(revenue.getCode(), BudgetRevenue.getAllBudgetRevenues());

            if (existingRevenue != null) {
                // Περίπτωση Α: Ο κωδικός υπάρχει, Κάνουμε Άθροισμα
                long newAmount = existingRevenue.getAmount() + revenue.getAmount();
                existingRevenue.setAmount(newAmount);

            } else {
                // Περίπτωση Β: Ο κωδικός δεν υπάρχει (Μόνο Π.Δ.Ε.), Δημιουργία Νέου
                BudgetRevenue budgetRevenue = new BudgetRevenue(revenue.getCode(), revenue.getDescription(), revenue.getCategory(), revenue.getAmount());
                budgetRevenue.addBudgetRevenueToArrayList();
            }
        }
        BudgetRevenue.sortBudgetRevenuesByCode();
    }

    private static void createRegularBudgetRevenueFromCSV(String[] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        BudgetRevenue regularBudgetRevenue = new RegularBudgetRevenue(code, description, category, amount);
    }

    private static void createPublicInvestmentBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        String type = values[2];
        long amount = Long.parseLong(values[3]);
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = new PublicInvestmentBudgetNationalRevenue(code, description, category, type, amount);
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenueFiltered = new PublicInvestmentBudgetRevenue(code, description, category, type, amount, 0, amount);
        } else if (type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = new PublicInvestmentBudgetCoFundedRevenue(code, description, category, type, amount);
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenueFiltered = new PublicInvestmentBudgetRevenue(code, description, category, type, 0, amount, amount);
        }
    }

    private static void createRegularBudgetExpenseFromCSV(String[] values) {
        String entityCode = values[0];
        String entityName = values[1];
        String serviceCode = values[2];
        String serviceName = values[3];
        String expenseCode = values[4];
        String description = values[5];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[6]);
        BudgetExpense regularBudgetExpense = new RegularBudgetExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, category, amount);
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
        BudgetExpense publicInvestmentBudgetExpense = new PublicInvestmentBudgetExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
    }

    public static void createEntityFromCSV() {
        Map<String, String> entityMap = BudgetExpense.getExpenses().stream().collect(Collectors.toMap(BudgetExpense::getEntityCode, BudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new Entity(entityCode, entityMap.get(entityCode)));
    }
}
