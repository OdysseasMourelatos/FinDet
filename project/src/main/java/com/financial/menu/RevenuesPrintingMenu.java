package com.financial.menu;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.RegularBudgetRevenue;
import com.financial.services.DataOutput;

import static com.financial.menu.Colors.*;
import java.util.Scanner;

public class RevenuesPrintingMenu {
    protected static void showRevenuesMenu(int budgetType, int publicInvestmentType) {
        Scanner input = new Scanner(System.in);
        if (budgetType == 1) {
            System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        } else if (budgetType == 2) {
            System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        } else if (budgetType == 3) {
            if (publicInvestmentType == 1) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ ΣΚΕΛΟΣ) ===\n");
            } else  if (publicInvestmentType == 2) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===\n");
            } else  if (publicInvestmentType == 3) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ + ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===\n");
            }
        }
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή των βασικών λογαριασμών εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή όλων των λογαριασμών εσόδων");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή συγκεκριμένων λογαριασμών εσόδων");
        System.out.print("\nΕπιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.println();
        switch (budgetType) {
            case 1 -> printTotalRevenues(choice);
            case 2 -> printRegularRevenues(choice);
            case 3 -> printPublicInvestmentBudgetRevenues(choice, publicInvestmentType);
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    private static void printTotalRevenues(int choice) {
        Scanner input = new Scanner(System.in);
        switch (choice) {
            case 1 -> BudgetRevenue.printMainBudgetRevenues();
            case 2 -> BudgetRevenue.printAllBudgetRevenues();
            case 3 -> {
                System.out.print("Παρακαλούμε εισάγετε τον κωδικό του επιθυμητού λογαριασμού εσόδων: ");
                String code = input.nextLine();
                System.out.println();
                BudgetRevenue budgetRevenue = BudgetRevenue.findBudgetRevenueWithCode(code);
                DataOutput.printSingleBudgetRevenueWithAsciiTable(budgetRevenue);
                printRevenue(budgetRevenue);
            }
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    private static void printRegularRevenues(int choice) {
        Scanner input = new Scanner(System.in);
        switch (choice) {
            case 1 -> RegularBudgetRevenue.printMainRegularBudgetRevenues();
            case 2 -> RegularBudgetRevenue.printAllRegularBudgetRevenues();
            case 3 -> {
                System.out.print("Παρακαλούμε εισάγετε τον κωδικό του επιθυμητού λογαριασμού εσόδων: ");
                String code = input.nextLine();
                System.out.println();
                RegularBudgetRevenue budgetRevenue = RegularBudgetRevenue.findRegularBudgetRevenueWithCode(code);
                DataOutput.printEntryWithAsciiTable(budgetRevenue);
                printRevenue(budgetRevenue);
            }
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }
}
