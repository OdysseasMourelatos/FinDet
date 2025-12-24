package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseChangesService;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.ExpensesHistory;
import java.util.*;

public class PublicInvestmentBudgetCoFundedExpense extends PublicInvestmentBudgetExpense {

    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpenses = new ArrayList<>();

    public PublicInvestmentBudgetCoFundedExpense(String entityCode, String entityName, String serviceCode, String serviceName, String code, String description, String type, String category, long amount) {
        super(entityCode, entityName, serviceCode, serviceName, code, description, type, category, amount);
        pibCoFundedExpenses.add(this);
    }

    protected static ArrayList<PublicInvestmentBudgetCoFundedExpense> pibCoFundedExpensesPerCategory = new ArrayList<>();

    private PublicInvestmentBudgetCoFundedExpense(String code, String description, String type, String category, long amount) {
        super(code, description, type, category, amount);
        pibCoFundedExpensesPerCategory.add(this);
    }
}