package com.financial.menu;

import java.io.DataOutput;
import java.util.Scanner;
import static com.financial.menu.Colors.*;

import com.financial.services.*;
import com.financial.entries.*;

public class PrintingSubMenu {

    public static void showSubMenuOfPrinting() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή Στοιχείων Κρατικού Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή Στοιχείων Τακτικού Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή Στοιχείων Προϋπολογισμού Δημόσιων Επενδύσεων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);

        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        switch (choice) {
            case 1, 2, 3 -> showBudgetMenu(choice);
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    private static void showBudgetMenu(int budgetType) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        int publicInvestmentType = 0;
        if (budgetType == 1) {
            System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
        } else if (budgetType == 2) {
            System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
        } else if (budgetType == 3) {
            System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ===");
            System.out.println();
            System.out.println(BLUE + BOLD + "[1] " + RESET + "Εθνικό Σκέλος");
            System.out.println(BLUE + BOLD + "[2] " + RESET + "Συγχρηματοδοτούμενο Σκέλος");
            System.out.println(BLUE + BOLD + "[3] " + RESET + "Εθνικό + Συγχρηματοδοτούμενο Σκέλος");
            System.out.print("\nΕπιλογή: ");
            publicInvestmentType = input.nextInt();
            input.nextLine();
            if (publicInvestmentType == 1) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ ΣΚΕΛΟΣ) ===");
            } else if (publicInvestmentType == 2) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===");
            } else if (publicInvestmentType == 3) {
                System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ (ΕΘΝΙΚΟ + ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ) ===");
            } else {
                throw new IllegalArgumentException();
            }
        }
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή Εσόδων Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή Εξόδων Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή Εσόδων & Εξόδων Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> RevenuesPrintingSubMenu.showRevenuesMenu(budgetType, publicInvestmentType);
            case 2 -> ExpensesPrintingSubMenu.showExpensesMenu(budgetType - 1, publicInvestmentType);
        }
    }
}
