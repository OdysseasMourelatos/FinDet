package com.financial.entries;

import com.financial.services.expenses.BudgetExpenseLogicService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class BudgetExpense extends BudgetEntry {

    private final String entityCode;
    private final String entityName;
    private final String serviceCode;
    private final String serviceName;
    protected static ArrayList <BudgetExpense> budgetExpenses = new ArrayList<>();

    public BudgetExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(code, description, category, amount);
        this.entityCode = entityCode;
        this.entityName = entityName;
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
        budgetExpenses.add(this);
    }

    public static Map<String, Long> getSumOfEveryBudgetExpenseCategory() {
        Map<String, Long> regularMap = RegularBudgetExpense.getSumOfEveryRegularExpenseCategory();
        Map<String, Long> pibMap = PublicInvestmentBudgetExpense.getSumOfEveryPublicInvestmentExpenseCategory();

        // Δημιουργούμε ένα νέο Map ξεκινώντας με τα δεδομένα του Τακτικού
        Map<String, Long> combinedMap = new LinkedHashMap<>(regularMap);

        // Προσθέτουμε τα δεδομένα του ΠΔΕ
        pibMap.forEach((serviceCode, amount) ->
                combinedMap.merge(serviceCode, amount, Long::sum)
        );

        return combinedMap;
    }

    public static long calculateSum() {
        return RegularBudgetExpense.calculateSum() + PublicInvestmentBudgetExpense.calculateSum();
    }

    public static String getDescriptionWithCode(String code) {
        return BudgetExpenseLogicService.getDescriptionWithCode(code, budgetExpenses);
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static ArrayList <BudgetExpense> getBudgetExpenses() {
        return budgetExpenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BudgetExpense that = (BudgetExpense) o;
        // Μοναδικότητα βάσει του σύνθετου κλειδιού
        return Objects.equals(getEntityCode(), that.getEntityCode()) &&
                Objects.equals(getServiceCode(), that.getServiceCode()) &&
                Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), getEntityCode(), getServiceCode(), getCode());
    }

    @Override
    public String toString() {
        return "Entity Code: " + getEntityCode() + ", Entity Name : " + entityName + ", Service Code: " + serviceCode + ", Service Name: " + serviceName + ", Expense Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Amount: " + String.format("%,d", getAmount());
    }
}

