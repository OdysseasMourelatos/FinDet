package com.financial.menu;
import java.util.Scanner;
import static com.financial.menu.Colors.*;

public class ChangesMenu {

    public static void printChangesMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Εισαγωγή αλλαγών σε στοιχεία τακτικού προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Εισαγωγή αλλαγών σε στοιχεία προϋπολογισμού δημόσιων επενδύσεων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int budgetType = input.nextInt();
        input.nextLine();
        showChangesMenu(budgetType);
    }

    public static void showChangesMenu(int budgetType) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        int publicInvestmentType = 0;
        if (budgetType == 1) {
            System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ΣΕ ΣΤΟΙΧΕΙΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===");
        } else if (budgetType == 2) {
            System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ΣΕ ΣΤΟΙΧΕΙΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ===");
            System.out.println();
            System.out.println(BLUE + BOLD + "[1] " + RESET + "Εισαγωγή αλλαγών σε στοιχεία εθνικού σκέλους");
            System.out.println(BLUE + BOLD + "[2] " + RESET + "Εισαγωγή αλλαγών σε στοιχεία συγχρηματοδοτούμενου σκέλους");
            System.out.println();
            System.out.print("Επιλογή: ");
            publicInvestmentType = input.nextInt();
        } else {
            throw new IllegalArgumentException();
        }
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Εισαγωγή αλλαγών σε λογαριασμούς εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Εισαγωγή αλλαγών σε λογαριασμούς εξόδων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> RevenuesChangeMenu.modifyExistingRevenueAccount();
            case 2 -> ExpensesChangeMenu.showExpensesChangesMenu(budgetType, publicInvestmentType);
        }
    }
}
