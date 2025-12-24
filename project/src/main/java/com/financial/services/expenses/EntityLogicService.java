package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;
import com.financial.entries.RegularBudgetExpense;

import java.util.ArrayList;
import java.util.List;

public class EntityLogicService {

    public static String getServiceNameWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getServiceCode().equals(serviceCode)) {
                return expense.getServiceName();
            }
        }
        return null;
    }

    public static List<String> getServiceCodes(ArrayList<? extends BudgetExpense> expenses) {
        return expenses.stream().map(BudgetExpense::getServiceCode).distinct().sorted().toList();
    }
}
