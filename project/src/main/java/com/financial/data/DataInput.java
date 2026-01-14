package com.financial.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.financial.entries.*;
import com.financial.multi_year_analysis.MultiYearBudgetExpense;
import com.financial.multi_year_analysis.MultiYearBudgetRevenue;
import com.financial.multi_year_analysis.MultiYearEntity;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 * The core data ingestion engine responsible for parsing CSV financial reports.
 * <p>
 * This class utilizes the {@code opencsv} library to read various budget data formats,
 * including Regular Budgets, Public Investment Budgets (PIB), and Historical datasets.
 * It features dynamic header analysis to automatically detect the budget year and
 * identify which column contains financial amounts based on year-formatted headers.
 * </p>
 */
public class DataInput {

    /** * Stores the year detected in the CSV header (e.g., 2025).
     * Defaults to -1 if no year is found.
     */
    public static int detectedBaseYear = -1;

    /**
     * Primary entry point for CSV ingestion.
     * <p>
     * Utilizes a try-with-resources block to ensure proper closure of the {@link CSVReader}.
     * It reads the header row to determine the file schema before iterating through data rows.
     * </p>
     *
     * @param filePath   The system path to the source CSV file.
     * @param forcedType An optional type override used if the file is flagged as generic historical data.
     */
    public static void advancedCSVReader(String filePath, String forcedType) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            try {
                String[] header = reader.readNext();
                String csvType = determineCSVType(header);
                if (csvType.equals("HISTORICAL_GENERIC")) {
                    csvType = forcedType;
                }
                String[] values;
                while ((values = reader.readNext()) != null) {
                    processCSVRow(csvType, values);
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(e.getMessage());
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Analyzes CSV headers to identify the budget category and extract the base year.
     * <p>
     * <b>Year Detection:</b> Scans all header cells for a 4-digit sequence (RegEx: {@code \\d{4}}).
     * If a year is found, it is stored in {@link #detectedBaseYear} for subsequent analytical merges.
     * </p>
     *
     * @param header The first row of the CSV file.
     * @return A string identifier for the detected schema type.
     */
    private static String determineCSVType(String[] header) {
        Set<String> headerSet = new HashSet<>(Arrays.asList(header));

        // Scan headers for a year (4 consecutive digits)
        for (String string : header) {
            String h = string.trim();
            if (h.matches("\\d{4}")) { // Matches exactly 4 digits like 2025, 2026
                detectedBaseYear = Integer.parseInt(h);
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

    /**
     * Routes row data to specialized parser methods based on the identified CSV type.
     *
     * @param csvType The identified schema type.
     * @param values  The string array representing a single row of data.
     */
    private static void processCSVRow(String csvType, String[] values) {
        switch (csvType) {
            case "REGULAR_REVENUES" -> createRegularBudgetRevenueFromCSV(values);
            case "PUBLIC_INVESTMENT_REVENUES" -> createPublicInvestmentBudgetRevenueFromCSV(values);
            case "REGULAR_EXPENSES_PER_SERVICE" -> createRegularBudgetExpenseFromCSV(values);
            case "PUBLIC_INVESTMENT_EXPENSES_PER_SERVICE" -> createPublicInvestmentBudgetExpenseFromCSV(values);
            case "HISTORICAL_BUDGET_REVENUES" -> createMultiYearBudgetRevenueFromCSV(values);
            case "HISTORICAL_BUDGET_EXPENSES" -> createMultiYearBudgetExpenseFromCSV(values);
            case "HISTORICAL_BUDGET_EXPENSES_PER_ENTITY" -> createMultiYearBudgetExpensePerEntityFromCSV(values);
            case "UNKNOWN" -> System.out.println("Σφάλμα: Άγνωστος τύπος CSV.");
            default -> System.out.println("Σφάλμα στη γραμμή: " + Arrays.toString(values));
        }
    }

    /**
     * Parses Regular Budget Revenue.
     * <i>Contract:</i> [0]: Code, [1]: Description, [2]: Amount.
     */
    private static void createRegularBudgetRevenueFromCSV(String[] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        new RegularBudgetRevenue(code, description, category, amount);
    }

    /**
     * Finalizes the ingestion of Public Investment Revenues by creating
     * filtered {@link BudgetRevenue} objects for the primary registry.
     */

    //Activated when all PublicInvestmentBudgetRevenues are filtered
    public static void createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue() {
        for (PublicInvestmentBudgetRevenue revenue : PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues()) {
            new BudgetRevenue(revenue.getCode(), revenue.getDescription(), revenue.getCategory(), 0, revenue.getAmount(), revenue.getAmount());
        }
    }

    /**
     * Parses Public Investment Revenues.
     * <i>Contract:</i> [0]: Code, [1]: Description, [2]: Type (National/Co-funded), [3]: Amount.
     */
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

    /**
     * Parses Regular Budget Expenses per Service.
     * <i>Contract:</i> [0]: EntityCode, [1]: EntityName, [2]: ServiceCode, [4]: ExpenseCode, [6]: Amount.
     */
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

    /**
     * Parses Public Investment Expenses per Service.
     */
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

    /**
     * Generates {@link Entity} objects from current session expenses.
     */
    public static void createEntityFromCSV() {
        Map<String, String> entityMap = BudgetExpense.getBudgetExpenses().stream().collect(Collectors.toMap(BudgetExpense::getEntityCode, BudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new Entity(entityCode, entityMap.get(entityCode)));
    }

    /**
     * Generates {@link MultiYearEntity} objects from historical expenses.
     */
    public static void createMultiYearEntityFromCSV() {
        Map<String, String> entityMap = MultiYearBudgetExpense.getMultiYearBudgetExpensesOfEntities().stream().collect(Collectors.toMap(MultiYearBudgetExpense::getEntityCode, MultiYearBudgetExpense::getEntityName, (existing, replacement) -> existing));
        entityMap.keySet().stream().sorted().forEach(entityCode -> new MultiYearEntity(entityCode, entityMap.get(entityCode)));
    }

    /**
     * Parses a 4-column CSV row (Code, Desc, Amount, Year) into a {@link MultiYearBudgetRevenue}.
     */
    private static void createMultiYearBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        long amount = Long.parseLong(values[2]);
        int year = Integer.parseInt(values[3]);
        new MultiYearBudgetRevenue(code, description, category, amount, year);
    }

    /**
     * Parses a 4-column CSV row (Code, Desc, Amount, Year) into a {@link MultiYearBudgetExpense}.
     */
    private static void createMultiYearBudgetExpenseFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[2]);
        int year = Integer.parseInt(values[3]);
        new MultiYearBudgetExpense(code, description, category, amount, year);
    }

    /**
     * Parses a 5-column CSV row (EntityCode, Name, RegAmt, PIBAmt, Year) into a {@link MultiYearBudgetExpense}.
     */
    private static void createMultiYearBudgetExpensePerEntityFromCSV(String [] values) {
        String entityCode = values[0];
        String entityName = values[1];
        long regularAmount = Long.parseLong(values[2]);
        long publicInvestmentAmount = Long.parseLong(values[3]);
        int year = Integer.parseInt(values[4]);
        new MultiYearBudgetExpense(entityCode, entityName, regularAmount, publicInvestmentAmount, year);
    }

    /**
     * Synchronizes main budget revenues with the multi-year model using the detected year.
     */
    public static void mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues() {
        for (BudgetRevenue br : BudgetRevenue.getMainBudgetRevenues()) {
            new MultiYearBudgetRevenue(br.getCode(), br.getDescription(), br.getCategory(), br.getAmount(), detectedBaseYear);
        }
    }

    /**
     * Synchronizes aggregated expenses with the multi-year model using the detected year.
     */
    public static void mergeBudgetExpensesOfBaseYearWithMultiYearBudgetExpenses() {
        for (Map.Entry<String, Long> entry : BudgetExpense.getSumOfEveryBudgetExpenseCategory().entrySet()) {
            new MultiYearBudgetExpense(entry.getKey(), BudgetExpense.getDescriptionWithCode(entry.getKey()), "ΕΞΟΔΑ", entry.getValue(), detectedBaseYear);
        }
    }

    /**
     * Synchronizes entity-level expenses with the multi-year model using the detected year.
     */
    public static void mergeBudgetExpensesPerEntityOfBaseYearWithMultiYearBudgetExpensesPerEntity() {
        for (Entity entity : Entity.getEntities()) {
            new MultiYearBudgetExpense(entity.getEntityCode(), entity.getEntityName(), entity.calculateRegularSum(), entity.calculatePublicInvestmentSum(), detectedBaseYear);
        }
    }
}
