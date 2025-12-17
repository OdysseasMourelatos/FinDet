package com.financial.menu;

import java.util.Scanner;
import com.financial.entries.*;
import com.financial.services.expenses.BudgetExpenseHandling;
import com.financial.services.data.DataOutput;

import static com.financial.menu.Colors.*;

public class ExpensesPrintingMenu {
    protected static void showExpensesMenu(int budgetType, int publicInvestmentType) {
        Scanner input = new Scanner(System.in);
        if (budgetType == 0) {
            System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        } else if (budgetType == 1) {
            System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        } else if (budgetType == 2) {
            if (publicInvestmentType == 1) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ ΣΚΕΛΟΣ) ===\n");
            } else if (publicInvestmentType == 2) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===\n");
            } else if (publicInvestmentType == 3) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ + ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===\n");
            }
        }
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή πιστώσεων κατά μείζονα κατηγορία δαπάνης");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή πιστώσεων συνολικά κατά φορέα");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή πιστώσεων κατά ειδικό φορέα & μείζονα κατηγορία δαπάνης");
        System.out.print("\nΕπιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.println();
        switch (choice) {
            case 1 -> {
                if (budgetType == 0) {
                    DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(BudgetExpense.getExpenses()));
                } else if (budgetType == 1) {
                    DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(RegularBudgetExpense.getAllRegularBudgetExpenses()));
                } else if (budgetType == 2) {
                    DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(PublicInvestmentBudgetExpense.getAllPublicInvestmentBudgetExpenses()));
                }
            }
            case 2 -> DataOutput.printEntitiesWithAsciiTable(budgetType);
            case 3 -> {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΠΙΣΤΩΣΕΩΝ ΚΑΤΑ ΕΙΔΙΚΟ ΦΟΡΕΑ & ΜΕΙΖΟΝΑ ΚΑΤΗΓΟΡΙΑ ΔΑΠΑΝΗΣ ===\n");
                System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή πιστώσεων όλων των φορέων");
                System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή πιστώσεων συγκεκριμένου φορέα");
                System.out.print("\nΕπιλογή: ");
                int choice2 = input.nextInt();
                input.nextLine();
                System.out.println();
                switch (choice2) {
                    case 1 -> {
                        for (Entity entity : Entity.getEntities()) {
                            DataOutput.printEntityWithAsciiTable(entity, budgetType);
                        }
                        System.out.println();
                    }
                    case 2 -> {
                        System.out.print("Παρακαλούμε εισάγετε τον κωδικό του επιθυμητού φορέα: ");
                        String entityCode = input.nextLine();
                        System.out.println();
                        Entity entity = Entity.findEntityWithEntityCode(entityCode);
                        DataOutput.printEntityWithAsciiTable(entity, budgetType);
                    }
                }
            }

            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }
}
