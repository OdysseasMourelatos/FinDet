package com.financial.multi_year_analysis.entries;

import java.util.ArrayList;
import java.util.List;

public class MultiYearBudgetExpense extends MultiYearBudgetEntry {
    protected static List<MultiYearBudgetExpense> multiYearBudgetExpenses = new ArrayList<>();

    public MultiYearBudgetExpense(String code, String description, String category, long amount, int year) {
        super(code, description, category, amount, year);
        multiYearBudgetExpenses.add(this);
    }

    public static List<MultiYearBudgetExpense> getMultiYearBudgetExpenses() {
        return multiYearBudgetExpenses;
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
