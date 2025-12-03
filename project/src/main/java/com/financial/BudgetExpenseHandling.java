package com.financial;

import java.util.ArrayList;

public class BudgetExpenseHandling {

    public static BudgetExpense findExpenseWithCode (String code, ArrayList<? extends BudgetExpense> expenses) {
        for (BudgetExpense expense : expenses) {
            if (expense.getCode().equals(code)) {
                return expense;
            }
        }
        return null;
    }

}
