package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PublicInvestmentBudgetExpense extends BudgetExpense {
    private final String type;
    protected static ArrayList<PublicInvestmentBudgetExpense> publicInvestmentBudgetExpenses = new ArrayList<>();

    public PublicInvestmentBudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetExpenses.add(this);
    }

    public static ArrayList<PublicInvestmentBudgetExpense> getAllPublicInvestmentBudgetExpenses() {
        return publicInvestmentBudgetExpenses;
    }

    public static Map<String, Long> getSumOfEveryPublicInvestmentExpenseCategory() {
        Map<String, Long> nationalMap = PublicInvestmentBudgetNationalExpense.getSumOfEveryPublicInvestmentNationalExpenseCategory();
        Map<String, Long> coFundedMap = PublicInvestmentBudgetCoFundedExpense.getSumOfEveryPublicInvestmentCoFundedExpenseCategory();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Εθνικού σκέλους
        Map<String, Long> combinedMap = new LinkedHashMap<>(nationalMap);

        // Προσθέτουμε τα δεδομένα του Συγχρηματοδοτούμενου
        coFundedMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    public static long calculateSum() {
        return PublicInvestmentBudgetNationalExpense.calculateSum() + PublicInvestmentBudgetCoFundedExpense.calculateSum();
    }

    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, publicInvestmentBudgetExpenses);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + ", Σκέλος : " + type;
    }
}