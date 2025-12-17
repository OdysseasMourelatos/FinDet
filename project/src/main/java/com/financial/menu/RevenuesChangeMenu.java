package com.financial.menu;

import java.util.Scanner;
import com.financial.entries.*;
import com.financial.services.data.DataOutput;
import com.financial.services.revenues.BudgetRevenueLogicService;

import static com.financial.menu.Colors.*;

public class RevenuesChangeMenu {
    public static void modifyExistingRevenueAccount() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΤΡΟΠΟΠΟΙΗΣΗ ΠΟΣΟΥ ΥΦΙΣΤΑΜΕΝΟΥ ΛΟΓΑΡΙΑΣΜΟΥ ΕΣΟΔΩΝ ===" + RESET + RESET_2);
        System.out.println();
        System.out.print("Εισάγετε τον κωδικό του λογαριασμού εσόδων του οποίου το ποσό επιθυμείτε να αλλάξει: ");
        String code = input.nextLine();
        BudgetRevenue budgetRevenue = BudgetRevenueLogicService.findRevenueWithCode(code, BudgetRevenue.getAllBudgetRevenues());

        if (budgetRevenue != null) {
            selectChangeMethodAndApply(budgetRevenue);
        } else {
            System.out.println(RED + "Δεν βρέθηκε λογαριασμός με κωδικό: " + code + RESET);
        }
    }

    public static void selectChangeMethodAndApply(BudgetRevenue budgetRevenue) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΤΡΟΠΟΣ ΜΕΤΑΒΟΛΗΣ ΥΠΟΚΑΤΗΓΟΡΙΩΝ ΕΣΟΔΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Ισόποση μεταβολή υποκατηγοριών");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Ποσοστιαία (Αναλογική) μεταβολή υποκατηγοριών");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Χειροκίνητη μεταβολή ανά υποκατηγορία" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int changeMethod = input.nextInt();
        System.out.println();

        selectInputMethodAndExecute(budgetRevenue, changeMethod);
    }

    public static void selectInputMethodAndExecute(BudgetRevenue budgetRevenue, int changeMethod) {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΤΡΟΠΟΣ ΕΙΣΑΓΩΓΗΣ ΜΕΤΑΒΟΛΗΣ ΛΟΓΑΡΙΑΣΜΟΥ ΕΣΟΔΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Εισαγωγή Ποσοστού (%) Μεταβολής Λογαριασμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Εισαγωγή Ποσού (€) Μεταβολής Λογαριασμού");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Εισαγωγή Νέου Επιθυμητού Υπολοίπου (€) Λογαριασμού" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int inputMethod = input.nextInt();
        System.out.println();

        executeBudgetChanges(budgetRevenue, changeMethod, inputMethod);
    }

    public static void executeBudgetChanges(BudgetRevenue budgetRevenue, int changeMethod, int inputMethod) {
        Scanner input = new Scanner(System.in);

        switch (inputMethod) {
            case 1 -> applyPercentageChange(budgetRevenue, changeMethod);
            case 2 -> applyAbsoluteChange(budgetRevenue, changeMethod);
            case 3 -> applyTargetBalanceChange(budgetRevenue, changeMethod);
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    public static void applyPercentageChange(BudgetRevenue budgetRevenue, int changeMethod) {
        Scanner input = new Scanner(System.in);
        System.out.print(BOLD + "Εισάγετε το ποσοστό (%) μεταβολής λογαριασμού: " + RESET_2);
        double percentage = input.nextDouble() / 100;
        applyChangeWithMethod(budgetRevenue, changeMethod, percentage, 0, true);
    }

    public static void applyAbsoluteChange(BudgetRevenue budgetRevenue, int changeMethod) {
        Scanner input = new Scanner(System.in);
        System.out.print(BOLD + "Εισάγετε το ποσό (€) μεταβολής λογαριασμού: " + RESET_2);
        long change = input.nextLong();
        double percentage = ((double) change / budgetRevenue.getAmount());
        applyChangeWithMethod(budgetRevenue, changeMethod, percentage, change, false);
    }

    public static void applyTargetBalanceChange(BudgetRevenue budgetRevenue, int changeMethod) {
        Scanner input = new Scanner(System.in);
        System.out.print(BOLD + "Εισάγετε το νέο επιθυμητό υπόλοιπο (€) λογαριασμού: " + RESET_2);
        long targetBalance = input.nextLong();
        long change = targetBalance - budgetRevenue.getAmount();
        double percentage = ((double) change / budgetRevenue.getAmount());
        applyChangeWithMethod(budgetRevenue, changeMethod, percentage, change, false);
    }

    public static void applyChangeWithMethod(BudgetRevenue budgetRevenue, int changeMethod,
                                             double percentage, long changeAmount, boolean isPercentage) {
        showBeforeChanges(budgetRevenue);
        if (changeMethod == 1) {
            long change;
            if (isPercentage) {
                double baseAmount = budgetRevenue.getAmount();
                double calculated = baseAmount * percentage;   // π.χ. 0.20 = 20%
                change = (long) calculated;
            } else {
                change = changeAmount;
            }
            //budgetRevenue.implementChangesOfEqualDistribution(change);
        } else if (changeMethod == 2) {
            //budgetRevenue.implementChangesOfPercentageAdjustment(percentage);
        } else if (changeMethod == 3) {
            return;
        }
        showAfterChanges(budgetRevenue);
    }

    public static void showBeforeChanges(BudgetRevenue budgetRevenue) {
        System.out.println();
        System.out.println("********************************************************* ΠΡΙΝ *********************************************************");
        System.out.println();
        DataOutput.printEntryWithAsciiTable(budgetRevenue);
        System.out.println();
        System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΥΨΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
        budgetRevenue.printSuperCategoriesTopDown();
        System.out.println();
        System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΧΑΜΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
        budgetRevenue.printAllSubCategories();
        System.out.println();
    }

    public static void showAfterChanges(BudgetRevenue budgetRevenue) {
        System.out.println();
        System.out.println("********************************************************* ΜΕΤΑ *********************************************************");
        System.out.println();
        DataOutput.printEntryWithAsciiTable(budgetRevenue);
        System.out.println();
        System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΛΟΓΑΡΙΑΣΜΩΝ ΣΕ ΥΨΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
        budgetRevenue.printSuperCategoriesTopDown();
        System.out.println();
        System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΛΟΓΑΡΙΑΣΜΩΝ ΣΕ ΧΑΜΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
        budgetRevenue.printAllSubCategories();
        System.out.println();
        System.out.println(GREEN + "ΟΙ ΑΛΛΑΓΕΣ ΟΛΟΚΛΗΡΩΘΗΚΑΝ ΜΕ ΕΠΙΤΥΧΙΑ.\n" + RESET + "ΕΠΙΣΤΡΟΦΗ ΣΤΟ ΚΥΡΙΟ ΜΕΝΟΥ..");
    }

}
