package com.financial.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.financial.entries.*;
import com.financial.multi_year_analysis.entries.MultiYearBudgetExpense;
import com.financial.multi_year_analysis.entries.MultiYearBudgetRevenue;
import com.financial.multi_year_analysis.entries.MultiYearEntity;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DataInput {

    /** * Stores the year detected in the CSV header (e.g., 2025).
     * Defaults to -1 if no year is found.
     */
    public static int detectedBaseYear = -1;

    /** * Stores the index of the column containing the amounts,
     * identified by a year-format header.
     */
    public static int amountColumnIndex = -1;

    public static void advancedCSVReader(String filePath, String forcedType) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath));
            String[] header = reader.readNext();
            String csvType = determineCSVType(header);
            if (csvType.equals("HISTORICAL_GENERIC")) {
                csvType = forcedType;
            }
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

    /**
     * Detects the CSV type and dynamically identifies the base year and
     * amount column index from the headers.
     * * @param header The array of header strings from the CSV.
     * @return The determined CSV type string.
     */
    private static String determineCSVType(String[] header) {
        Set<String> headerSet = new HashSet<>(Arrays.asList(header));

        // Scan headers for a year (4 consecutive digits)
        for (int i = 0; i < header.length; i++) {
            String h = header[i].trim();
            if (h.matches("\\d{4}")) { // Matches exactly 4 digits like 2025, 2026
                detectedBaseYear = Integer.parseInt(h);
                amountColumnIndex = i; // This column now points to the amounts
            }
        }

        if (headerSet.contains("Σκέλος")) {

            if (headerSet.contains("Κωδικός_Υπηρεσίας")) {
                return "PUBLIC_INVESTMENT_EXPENSES_PER_SERVICE";
            }

            return "PUBLIC_INVESTMENT_REVENUES";

        } else if (headerSet.contains("Κωδικός_Υπηρεσίας")) {
            return "REGULAR_EXPENSES_PER_SERVICE";
        } else if (headerSet.size() == 3) {
            return "REGULAR_REVENUES";
        } else if (headerSet.contains("Έτος")) {
            if (headerSet.size() == 4) {
                return "HISTORICAL_GENERIC";
            } else if (headerSet.contains("Τακτικός Προϋπολογισμός")) {
                return "HISTORICAL_BUDGET_EXPENSES_PER_ENTITY";
            }
        }
        return "UNKNOWN";
    }

    private static void processCSVRow(String csvType, String[] values) {
        switch (csvType) {
            case "REGULAR_REVENUES" -> createRegularBudgetRevenueFromCSV(values);
            case "PUBLIC_INVESTMENT_REVENUES" -> createPublicInvestmentBudgetRevenueFromCSV(values);
            case "REGULAR_EXPENSES_PER_SERVICE" -> createRegularBudgetExpenseFromCSV(values);
            case "PUBLIC_INVESTMENT_EXPENSES_PER_SERVICE" -> createPublicInvestmentBudgetExpenseFromCSV(values);
            case "HISTORICAL_BUDGET_REVENUES" -> createHistoricalBudgetRevenueFromCSV(values);
            case "HISTORICAL_BUDGET_EXPENSES" -> createHistoricalBudgetExpenseFromCSV(values);
            case "HISTORICAL_BUDGET_EXPENSES_PER_ENTITY" -> createHistoricalBudgetExpensePerEntityFromCSV(values);
            case "UNKNOWN" -> System.out.println("Σφάλμα: Άγνωστος τύπος CSV.");
            default -> System.out.println("Σφάλμα στη γραμμή: " + Arrays.toString(values));
        }
    }

    private static void createRegularBudgetRevenueFromCSV(String[] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        new RegularBudgetRevenue(code, description, category, amount);
    }

    //Activated when all PublicInvestmentBudgetRevenues are filtered
    public static void createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue() {
        for (PublicInvestmentBudgetRevenue revenue : PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues()) {
            new BudgetRevenue(revenue.getCode(), revenue.getDescription(), revenue.getCategory(), 0, revenue.getAmount(), revenue.getAmount());
        }
    }

    private static void createPublicInvestmentBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        String type = values[2];
        long amount = Long.parseLong(values[3]);
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            new PublicInvestmentBudgetNationalRevenue(code, description, category, type, amount);
        } else if (type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
            new PublicInvestmentBudgetCoFundedRevenue(code, description, category, type, amount);
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
        new RegularBudgetExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, category, amount);
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
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            new PublicInvestmentBudgetNationalExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
        } else if (type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
            new PublicInvestmentBudgetCoFundedExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
        }
    }

    public static void createEntityFromCSV() {
        Map<String, String> entityMap = BudgetExpense.getBudgetExpenses().stream().collect(Collectors.toMap(BudgetExpense::getEntityCode, BudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new Entity(entityCode, entityMap.get(entityCode)));
    }

    public static void createMultiYearEntityFromCSV() {
        Map<String, String> entityMap = MultiYearBudgetExpense.getMultiYearBudgetExpensesOfEntities().stream().collect(Collectors.toMap(MultiYearBudgetExpense::getEntityCode, MultiYearBudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new MultiYearEntity(entityCode, entityMap.get(entityCode)));
    }

    private static void createHistoricalBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        int year = Integer.parseInt(values[3]);
        new MultiYearBudgetRevenue(code, description, category, amount, year);
    }

    private static void createHistoricalBudgetExpenseFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[2]);
        int year = Integer.parseInt(values[3]);
        new MultiYearBudgetExpense(code, description, category, amount, year);
    }

    private static void createHistoricalBudgetExpensePerEntityFromCSV(String [] values) {
        String entityCode = values[0];
        String entityName = values[1];
        long regularAmount = Long.parseLong(values[2]);
        long publicInvestmentAmount = Long.parseLong(values[3]);
        int year = Integer.parseInt(values[4]);
        new MultiYearBudgetExpense(entityCode, entityName, regularAmount, publicInvestmentAmount, year);
    }

    public static void mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues() {
        for (BudgetRevenue br : BudgetRevenue.getMainBudgetRevenues()) {
            new MultiYearBudgetRevenue(br.getCode(), br.getDescription(), br.getCategory(), br.getAmount(), detectedBaseYear);
        }
    }

    public static void mergeBudgetExpensesOfBaseYearWithMultiYearBudgetExpenses() {
        for (Map.Entry<String, Long> entry : BudgetExpense.getSumOfEveryBudgetExpenseCategory().entrySet()) {
            new MultiYearBudgetExpense(entry.getKey(), BudgetExpense.getDescriptionWithCode(entry.getKey()), "ΕΞΟΔΑ", entry.getValue(), detectedBaseYear);
        }
    }

    public static void mergeBudgetExpensesPerEntityOfBaseYearWithMultiYearBudgetExpensesPerEntity() {
        for (Entity entity : Entity.getEntities()) {
            new MultiYearBudgetExpense(entity.getEntityCode(), entity.getEntityName(), entity.calculateRegularSum(), entity.calculatePublicInvestmentSum(), detectedBaseYear);
        }
    }
}
