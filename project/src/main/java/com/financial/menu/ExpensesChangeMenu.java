package com.financial.menu;

import com.financial.entries.*;
import com.financial.services.expenses.BudgetExpenseHandling;
import com.financial.services.data.DataOutput;
import com.financial.strategies.*;
import java.util.ArrayList;
import java.util.Scanner;
import com.financial.strategies.filters.*;
import com.financial.strategies.operations.*;

import static com.financial.menu.Colors.*;

public class ExpensesChangeMenu {
    public static void showExpensesChangesMenu(int budgetType, int publicInvestmentType) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ΣΕ ΛΟΓΑΡΙΑΣΜΟΥΣ ΕΞΟΔΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Εισαγωγή αλλαγών σε συγκεκριμένο φορέα");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Εισαγωγή μαζικών αλλαγών σε όλους τους φορείς");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> showSpecificEntityExpensesChangesMenu(budgetType, publicInvestmentType);
            case 2 -> showAllEntitiesExpensesChangeMenu(budgetType, publicInvestmentType);
        }
    }

    public static void showSpecificEntityExpensesChangesMenu(int budgetType, int publicInvestmentType) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Εισάγετε τον κωδικό του φορέα του οποίου τα έξοδα επιθυμείτε να τροποποιήσετε: ");
        String entityCode = input.nextLine();
        System.out.println();
        Entity entity = Entity.findEntityWithEntityCode(entityCode);
        System.out.println(BLUE + BOLD + "=== " + entity.getEntityName() + " - ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Τροποποίηση συνολικών εξόδων του φορέα");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Τροποποίηση συγκεκριμένου λογαριασμού εξόδων όλων των ειδικών φορέων");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Τροποποίηση όλων των λογαριασμών εξόδων ενός ειδικού φορέα");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής των συνολικών εξόδων του φορέα: ");
                double percentage = input.nextDouble() / 100;
                showExpensesOfEntity(entity, budgetType, true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, budgetType, false);
            }
            case 2 -> {
                System.out.println();
                System.out.print("Εισάγετε τον κωδικό του λογαριασμού: ");
                String code = input.nextLine();
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής του λογαριασμού " + BOLD + BudgetExpenseHandling.findExpenseWithCode(code, entity.getRegularBudgetExpenses()).getDescription().toUpperCase()  + RESET_2 + " : ");
                double percentage = input.nextDouble() / 100;
                showExpensesOfEntity(entity, budgetType, true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, budgetType, false);
            }
            case 3 -> {
                System.out.println("\nΕΙΔΙΚΟΙ ΦΟΡΕΙΣ & ΑΘΡΟΙΣΜΑ ΕΞΟΔΩΝ");
                for (String serviceCode : entity.getRegularServiceCodes()) {
                    System.out.println(serviceCode + " " + entity.findRegularServiceNameWithCode(serviceCode) + " " + entity.getRegularSumOfServiceWithCode(serviceCode));
                    for (RegularBudgetExpense expense : entity.getRegularExpensesOfServiceWithCode(serviceCode)) {
                        System.out.println(expense.getCode() + " " + expense.getDescription() + " " + expense.getAmount());
                    }
                }
                System.out.println();
                System.out.print("Εισάγετε τον κωδικό του ειδικού φορέα: ");
                String serviceCode = input.nextLine();
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής του ειδικού φορέα " + BOLD + entity.findRegularServiceNameWithCode(serviceCode).toUpperCase() + RESET_2 + " : ");
                double percentage = input.nextDouble() / 100;
                showExpensesOfEntity(entity, budgetType, true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new ServiceFilter(serviceCode), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, budgetType, false);
            }
        }
    }

    public static void showAllEntitiesExpensesChangeMenu(int budgetType, int publicInvestmentType) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΜΑΖΙΚΩΝ ΑΛΛΑΓΩΝ ΣΕ ΟΛΟΥΣ ΤΟΥΣ ΦΟΡΕΙΣ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Τροποποίηση ποσού λογαριασμού εξόδων για όλους τους φορείς");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Αύξηση όλων των λογαριασμών εξόδων όλων των φορέων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> {
                System.out.println();
                System.out.print("Εισάγετε τον κωδικό του λογαριασμού: ");
                String code = input.nextLine();
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής του λογαριασμού " + BOLD + BudgetExpenseHandling.findExpenseWithCode(code, BudgetExpense.getExpenses()).getDescription().toUpperCase() + RESET_2 + " : ");
                double percentage = input.nextDouble() / 100;
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
                strategy.applyAdjustment(BudgetExpense.getExpenses(), percentage, 0);
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(),  false);
            }
            case 2 -> {
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής: ");
                double percentage = input.nextDouble() / 100;
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
                strategy.applyAdjustment(BudgetExpense.getExpenses(), percentage, 0);
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), false);
            }
        }
    }

    public static void showExpensesOfEntity(Entity entity, int budgetType, boolean before) {
        System.out.println();
        if (before) {
            System.out.println("************************************************************ ΠΡΙΝ ************************************************************");
        } else {
            System.out.println("************************************************************ ΜΕΤΑ ************************************************************");
        }
        System.out.println();
        DataOutput.printEntityWithAsciiTable(entity, budgetType);
    }

    public static void showGeneralExpenses(ArrayList<? extends BudgetExpense> expenses, boolean before) {
        System.out.println();
        if (before) {
            System.out.println("************************************ ΠΡΙΝ *****************************************");
        } else {
            System.out.println("************************************ ΜΕΤΑ *****************************************");
        }
        System.out.println();
        DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(expenses));
    }
}
