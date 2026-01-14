package com.financial.entries;

import com.financial.services.BudgetType;
import com.financial.services.expenses.BudgetExpenseLogicService;
import com.financial.services.expenses.EntityLogic;
import com.financial.services.expenses.EntityLogicService;
import com.financial.services.expenses.ExpensesHistory;
import com.financial.strategies.ExpenseAdjustmentStrategy;
import com.financial.strategies.FilteredExpenseAdjustmentStrategy;
import com.financial.strategies.filters.AccountFilter;
import com.financial.strategies.filters.MatchAllFilter;
import com.financial.strategies.filters.ServiceFilter;
import com.financial.strategies.operations.FixedAmountOperation;
import com.financial.strategies.operations.PercentageOperation;

import java.util.*;

/**
 * **Represents** a high-level government Entity (such as a Ministry or Agency).
 * <p>
 * This class serves as the primary data container for an organization's financial records.
 * It implements {@link EntityLogic} and manages three distinct budget streams:
 * Regular Budget, National PIB, and Co-funded PIB. It provides extensive methods for
 * financial querying, aggregation, and strategic budget adjustments.
 */
public class Entity implements EntityLogic {
    private final String entityCode;
    private final String entityName;
    protected ArrayList<RegularBudgetExpense> regularBudgetExpenses;
    protected ArrayList<PublicInvestmentBudgetNationalExpense> publicInvestmentBudgetNationalExpenses;
    protected ArrayList<PublicInvestmentBudgetCoFundedExpense> publicInvestmentBudgetCoFundedExpenses;

    protected static ArrayList<Entity> entities = new ArrayList<>();

    /**
     * **Initializes** a new Entity and **links** it to its respective expenditure records.
     * <p>
     * Upon instantiation, the Entity automatically fetches all relevant expenses from the
     * static registries of {@link RegularBudgetExpense}, {@link PublicInvestmentBudgetNationalExpense},
     * and {@link PublicInvestmentBudgetCoFundedExpense} based on its unique entity code.
     *
     * @param entityCode **Unique identifier** for the Ministry/Agency.
     * @param entityName **Official name** of the Entity.
     */
    public Entity(String entityCode, String entityName) {
        this.entityCode = entityCode;
        this.entityName = entityName;
        regularBudgetExpenses = RegularBudgetExpense.getRegularBudgetExpensesOfEntityWithCode(entityCode);
        publicInvestmentBudgetNationalExpenses = PublicInvestmentBudgetNationalExpense.getPublicInvestmentBudgetNationalExpensesOfEntityWithCode(entityCode);
        publicInvestmentBudgetCoFundedExpenses = PublicInvestmentBudgetCoFundedExpense.getPublicInvestmentBudgetCoFundedExpensesOfEntityWithCode(entityCode);
        entities.add(this);
    }

    /**
     * **Performs** a global search to locate a specific Entity instance by its code.
     * @param entityCode The code to search for.
     * @return The matching {@link Entity} object or null if not found.
     */
    public static Entity findEntityWithEntityCode(String entityCode) {
        for (Entity entity : entities) {
            if (entity.getEntityCode().equals(entityCode)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * **Calculates** the total sum of all **Regular Budget** expenditures for this entity.
     * @return Total regular budget sum as a long.
     */
    @Override
    public long calculateRegularSum() {
        return BudgetExpenseLogicService.calculateSum(regularBudgetExpenses);
    }

    /**
     * **Calculates** the total sum of all **National PIB** expenditures for this entity.
     * @return Total national investment sum.
     */
    @Override
    public long calculatePublicInvestmentNationalSum() {
        return BudgetExpenseLogicService.calculateSum(publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Calculates** the total sum of all **Co-funded PIB** expenditures for this entity.
     * @return Total co-funded investment sum.
     */
    @Override
    public long calculatePublicInvestmentCoFundedSum() {
        return BudgetExpenseLogicService.calculateSum(publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Retrieves** the name of a specific **Regular Budget Service** using its code.
     * @param serviceCode The **code** of the service.
     * @return The **display name** of the service.
     */
    @Override
    public String getRegularServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, regularBudgetExpenses);
    }

    /**
     * **Retrieves** the name of a specific **Public Investment National Budget Service** using its code.
     * @param serviceCode The **code** of the service.
     * @return The **display name** of the service.
     */
    @Override
    public String getPublicInvestmentNationalServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Retrieves** the name of a specific **Public Investment CoFundedBudget Service** using its code.
     * @param serviceCode The **code** of the service.
     * @return The **display name** of the service.
     */
    @Override
    public String getPublicInvestmentCoFundedServiceNameWithCode(String serviceCode) {
        return EntityLogicService.getServiceNameWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Retrieves** a list of all unique **Service Codes** present in the Regular Budget.
     * @return A {@link List} of **unique codes**.
     */
    @Override
    public List<String> getAllRegularServiceCodes() {
        return EntityLogicService.getAllServiceCodes(regularBudgetExpenses);
    }

    /**
     * **Retrieves** a list of all unique **Service Codes** present in the PIB National Budget.
     * @return A {@link List} of **unique codes**.
     */
    @Override
    public List<String> getAllPublicInvestmentNationalServiceCodes() {
        return EntityLogicService.getAllServiceCodes(publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Retrieves** a list of all unique **Service Codes** present in the PIB CoFunded Budget.
     * @return A {@link List} of **unique codes**.
     */
    @Override
    public List<String> getAllPublicInvestmentCoFundedServiceCodes() {
        return EntityLogicService.getAllServiceCodes(publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Computes** the financial sum for a **target service** in the Regular Budget.
     * @param serviceCode The service to aggregate.
     * @return The **total sum** for that specific service.
     */
    @Override
    public long getRegularSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, regularBudgetExpenses);
    }

    /**
     * **Computes** the financial sum for a **target service** in the PIB National Budget.
     * @param serviceCode The service to aggregate.
     * @return The **total sum** for that specific service.
     */
    @Override
    public long getPublicInvestmentNationalSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Computes** the financial sum for a **target service** in the PIB CoFunded Budget.
     * @param serviceCode The service to aggregate.
     * @return The **total sum** for that specific service.
     */
    @Override
    public long getPublicInvestmentCoFundedSumOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getSumOfServiceWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Fetches** the full list of **Regular Budget Expense Objects** for a specific service.
     * @param serviceCode The target service code.
     * @return An {@link ArrayList} of **BudgetExpense** objects.
     */
    @Override
    public ArrayList<BudgetExpense> getRegularExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, regularBudgetExpenses);
    }

    /**
     * **Fetches** the full list of **Public Investment National Expense Objects** for a specific service.
     * @param serviceCode The target service code.
     * @return An {@link ArrayList} of **BudgetExpense** objects.
     */
    @Override
    public ArrayList<BudgetExpense> getPublicInvestmentNationalExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Fetches** the full list of **Public Investment Co Funded Expense Objects** for a specific service.
     * @param serviceCode The target service code.
     * @return An {@link ArrayList} of **BudgetExpense** objects.
     */
    @Override
    public ArrayList<BudgetExpense> getPublicInvestmentCoFundedExpensesOfServiceWithCode(String serviceCode) {
        return EntityLogicService.getExpensesOfServiceWithCode(serviceCode, publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Generates** a map of **every Regular service** and its corresponding total sum.
     * @return A {@link Map} where keys are **service codes** and values are **sums**.
     */
    @Override
    public Map<String, Long> getRegularSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(regularBudgetExpenses);
    }

    /**
     * **Generates** a map of **every PIB National service** and its corresponding total sum.
     * @return A {@link Map} where keys are **service codes** and values are **sums**.
     */
    @Override
    public Map<String, Long> getPublicInvestmentNationalSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Generates** a map of **every PIB CoFunded service** and its corresponding total sum.
     * @return A {@link Map} where keys are **service codes** and values are **sums**.
     */
    @Override
    public Map<String, Long> getPublicInvestmentCoFundedSumOfEveryService() {
        return EntityLogicService.getSumOfEveryService(publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Calculates** the sum of a **specific regular expense category** (e.g., Salaries) across the entity.
     * @param code The **classification code** of the category.
     * @return The **total amount** for that category.
     */
    @Override
    public long getRegularSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, regularBudgetExpenses);
    }

    /**
     * **Calculates** the sum of a **specific PIB National expense category** across the entity.
     * @param code The **classification code** of the category.
     * @return The **total amount** for that category.
     */
    @Override
    public long getPublicInvestmentNationalSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Calculates** the sum of a **specific PIB CoFunded expense category** across the entity.
     * @param code The **classification code** of the category.
     * @return The **total amount** for that category.
     */
    @Override
    public long getPublicInvestmentCoFundedSumOfExpenseCategoryWithCode(String code) {
        return EntityLogicService.getSumOfExpenseCategoryWithCode(code, publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Provides** a complete breakdown of **all Regular expense categories** and their total sums.
     * @return A {@link Map} of **category codes** to **total expenditures**.
     */
    @Override
    public Map<String, Long> getRegularSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(regularBudgetExpenses);
    }

    /**
     * **Provides** a complete breakdown of **all PIB National expense categories** and their total sums.
     * @return A {@link Map} of **category codes** to **total expenditures**.
     */
    @Override
    public Map<String, Long> getPublicInvestmentNationalSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(publicInvestmentBudgetNationalExpenses);
    }

    /**
     * **Provides** a complete breakdown of **all PIB CoFunded expense categories** and their total sums.
     * @return A {@link Map} of **category codes** to **total expenditures**.
     */
    @Override
    public Map<String, Long> getPublicInvestmentCoFundedSumOfEveryExpenseCategory() {
        return EntityLogicService.getSumOfEveryExpenseCategory(publicInvestmentBudgetCoFundedExpenses);
    }

    /**
     * **Implements** financial changes across **all categories and services** for a specific budget type.
     * @param percentage The **percentage shift** (e.g., 0.05 for 5%).
     * @param fixedAmount The **fixed amount** to add/subtract.
     * @param budgetType The **target budget stream** (Regular, National, Co-funded).
     */
    public void implementChangesInAllExpenseCategoriesOfAllServices(double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new FilteredExpenseAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
        } else {
            strategy = new FilteredExpenseAdjustmentStrategy(new MatchAllFilter(), new FixedAmountOperation());
        }
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    /**
     * **Implements** changes targeting a **specific expense category** across all services.
     * @param expenseCode The **category** to filter by.
     */
    public void implementChangesInSpecificExpenseCategoryOfAllServices(String expenseCode, double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new FilteredExpenseAdjustmentStrategy(new AccountFilter(expenseCode), new PercentageOperation());
        } else {
            strategy = new FilteredExpenseAdjustmentStrategy(new AccountFilter(expenseCode), new FixedAmountOperation());
        }
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    /**
     * **Targeted** update for a **specific service**, affecting all categories within that service.
     */
    public void implementChangesInAllExpenseCategoriesOfSpecificService(String serviceCode, double percentage, long fixedAmount, BudgetType budgetType) {
        ExpenseAdjustmentStrategy strategy;
        if (fixedAmount == 0) {
            strategy = new FilteredExpenseAdjustmentStrategy(new ServiceFilter(serviceCode), new PercentageOperation());
        } else {
            strategy = new FilteredExpenseAdjustmentStrategy(new ServiceFilter(serviceCode), new FixedAmountOperation());
        }
        applyChangesAndKeepHistory(strategy, percentage, fixedAmount, budgetType);
    }

    /**
     * **Granular update** for one **specific category within one specific service**.
     * <p>
     * Includes **error handling** to revert to previous states via {@link ExpensesHistory}
     * if the update fails.
     */
    public void implementChangesInSpecificExpenseCategoryOfSpecificService(String serviceCode, String categoryCode, double percentage, long fixedAmount, BudgetType budgetType) {
        try {
            BudgetExpense expense;
            if (budgetType == BudgetType.REGULAR_BUDGET) {
                expense = RegularBudgetExpense.findRegularBudgetExpenseWithCodes(entityCode, serviceCode, categoryCode);
            } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL) {
                expense = PublicInvestmentBudgetNationalExpense.findPublicInvestmentBudgetNationalExpenseWithCodes(entityCode, serviceCode, categoryCode);
            } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED) {
                expense = PublicInvestmentBudgetCoFundedExpense.findPublicInvestmentBudgetCoFundedExpenseWithCodes(entityCode, serviceCode, categoryCode);
            } else {
                return;
            }

            ArrayList<BudgetExpense> expenses = new ArrayList<>();
            expenses.add(expense);
            ExpensesHistory.keepHistory(expenses, budgetType);

            if (fixedAmount != 0) {
                expense.setAmount(expense.getAmount() + fixedAmount);
            } else {
                expense.setAmount( (long) (expense.getAmount() * (1 + percentage)));
            }

        } catch (IllegalArgumentException e) {
            ExpensesHistory.returnToPreviousState();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * **Internal helper** to apply strategies while **preserving data history**.
     * @throws IllegalStateException If an **unsupported budget type** is provided.
     */
    private void applyChangesAndKeepHistory(ExpenseAdjustmentStrategy strategy, double percentage, long fixedAmount, BudgetType budgetType) {
        if (budgetType == BudgetType.REGULAR_BUDGET) {
            ExpensesHistory.keepHistory(regularBudgetExpenses, budgetType);
            strategy.applyAdjustment(regularBudgetExpenses, percentage, fixedAmount);
        } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_NATIONAL) {
            ExpensesHistory.keepHistory(publicInvestmentBudgetNationalExpenses, budgetType);
            strategy.applyAdjustment(publicInvestmentBudgetNationalExpenses, percentage, fixedAmount);
        } else if (budgetType == BudgetType.PUBLIC_INVESTMENT_BUDGET_COFUNDED) {
            ExpensesHistory.keepHistory(publicInvestmentBudgetCoFundedExpenses, budgetType);
            strategy.applyAdjustment(publicInvestmentBudgetCoFundedExpenses, percentage, fixedAmount);
        } else {
            throw new IllegalStateException("BUDGET_TYPE NOT SUPPORTED");
        }
    }

    /** @return The **unique entity code**. */
    public String getEntityCode() {
        return entityCode;
    }

    /** @return The **official entity name**. */
    public String getEntityName() {
        return entityName;
    }

    /** @return The list of **Regular Budget** expenses. */
    public ArrayList<RegularBudgetExpense> getRegularBudgetExpenses() {
        return regularBudgetExpenses;
    }

    /** @return The list of **Public Investment Budget National** expenses. */
    public ArrayList<PublicInvestmentBudgetNationalExpense> getPublicInvestmentBudgetNationalExpenses() {
        return publicInvestmentBudgetNationalExpenses;
    }

    /** @return The list of **Public Investment Budget Co Funded** expenses. */
    public ArrayList<PublicInvestmentBudgetCoFundedExpense> getPublicInvestmentBudgetCoFundedExpenses() {
        return publicInvestmentBudgetCoFundedExpenses;
    }

    /** @return The global registry of **all Entity instances**. */
    public static ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * **Returns** a string representation of the Entity.
     * @return A formatted string with **Code and Name**.
     */
    @Override
    public String toString() {
        return "Entity Code: " + entityCode + ", Name: " + entityName;
    }
}
