package com.financial;

import com.financial.entries.BudgetExpense;
import com.financial.services.DataOutput;

import java.util.ArrayList;

public class RegularBudgetExpense extends BudgetExpense {
    protected static ArrayList<RegularBudgetExpense> regularBudgetExpenses = new ArrayList<>();

    public RegularBudgetExpense (String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, category, amount);
        regularBudgetExpenses.add(this);
    }

    public RegularBudgetExpense (String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    public static ArrayList<RegularBudgetExpense> getAllRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    public static void printAllRegularBudgetExpenses() {
        DataOutput.printBudgetExpenseWithAsciiTable(regularBudgetExpenses);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
