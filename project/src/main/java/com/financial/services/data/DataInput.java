package com.financial.services.data;

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
        BudgetRevenue regularBudgetRevenue = new RegularBudgetRevenue(code, description, category, amount);
    }

    //Activated when all PublicInvestmentBudgetRevenues are filtered
    public static void createBudgetRevenueFilteredFromPublicInvestmentBudgetRevenue() {
        for (PublicInvestmentBudgetRevenue revenue : PublicInvestmentBudgetRevenue.getAllPublicInvestmentBudgetRevenues()) {
            BudgetRevenue budgetRevenue = new BudgetRevenue(revenue.getCode(), revenue.getDescription(), revenue.getCategory(), 0, revenue.getAmount(), revenue.getAmount());
        }
    }

    private static void createPublicInvestmentBudgetRevenueFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΣΟΔΑ";
        String type = values[2];
        long amount = Long.parseLong(values[3]);
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = new PublicInvestmentBudgetNationalRevenue(code, description, category, type, amount);
        } else if (type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
            PublicInvestmentBudgetRevenue publicInvestmentBudgetRevenue = new PublicInvestmentBudgetCoFundedRevenue(code, description, category, type, amount);
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
        if (type.equals("ΕΘΝΙΚΟ") || type.equals("ΕΘΝΙΚΟ ΣΚΕΛΟΣ")) {
            BudgetExpense publicInvestmentBudgetExpense = new PublicInvestmentBudgetNationalExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
        } else if (type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ") || type.equals("ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ") ) {
            BudgetExpense publicInvestmentBudgetExpense = new PublicInvestmentBudgetCoFundedExpense(entityCode, entityName, serviceCode, serviceName, expenseCode, description, type, category, amount);
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
        MultiYearBudgetRevenue multiYearBudgetRevenue = new MultiYearBudgetRevenue(code, description, category, amount, year);
    }

    private static void createHistoricalBudgetExpenseFromCSV(String [] values) {
        String code = values[0];
        String description = values[1];
        String category = "ΕΞΟΔΑ";
        long amount = Long.parseLong(values[2]);
        int year = Integer.parseInt(values[3]);
        MultiYearBudgetExpense multiYearBudgetExpense = new MultiYearBudgetExpense(code, description, category, amount, year);
    }

    private static void createHistoricalBudgetExpensePerEntityFromCSV(String [] values) {
        String entityCode = values[0];
        String entityName = values[1];
        long regularAmount = Long.parseLong(values[2]);
        long publicInvestmentAmount = Long.parseLong(values[3]);
        int year = Integer.parseInt(values[4]);
        MultiYearBudgetExpense multiYearBudgetExpense = new MultiYearBudgetExpense(entityCode, entityName, regularAmount, publicInvestmentAmount, year);
    }

    public static void mergeBudgetRevenuesOfBaseYearWithMultiYearBudgetRevenues(int baseYear) {
        for (BudgetRevenue br : BudgetRevenue.getMainBudgetRevenues()) {
            MultiYearBudgetRevenue multiYearBudgetRevenue = new MultiYearBudgetRevenue(br.getCode(), br.getDescription(), br.getCategory(), br.getAmount(), baseYear);
        }
    }

    public static void mergeBudgetExpensesPerEntityOfBaseYearWithMultiYearBudgetExpensesPerEntity(int baseYear) {
        for (Entity entity : Entity.getEntities()) {
            MultiYearBudgetExpense multiYearBudgetExpense = new MultiYearBudgetExpense(entity.getEntityCode(), entity.getEntityName(), entity.calculateRegularSum(), entity.calculatePublicInvestmentSum(), baseYear);
        }
    }
}
