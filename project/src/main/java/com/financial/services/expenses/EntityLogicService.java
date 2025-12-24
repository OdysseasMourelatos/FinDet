package com.financial.services.expenses;

import com.financial.entries.BudgetExpense;
import java.util.ArrayList;

public class EntityLogicService {

    public static String findServiceNameWithCode(String serviceCode, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getServiceCode().equals(serviceCode)) {
                return expense.getServiceName();
            }
        }
        return null;
    }
}
