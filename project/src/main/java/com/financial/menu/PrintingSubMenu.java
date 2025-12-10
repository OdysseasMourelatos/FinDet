package com.financial.menu;

import java.io.DataOutput;
import java.util.Scanner;
import static com.financial.menu.Colors.*;

import com.financial.services.*;
import com.financial.entries.*;

public class PrintingSubMenu {
    
    public static void showSubMenuOfPrinting() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===" );
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

    private static void showExpensesMenu(String budgetType) {
        Scanner input = new Scanner(System.in);

        System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΞΟΔΩΝ " + budgetType.toUpperCase() + " ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή πιστώσεων κατά μείζονα κατηγορία δαπάνης");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή πιστώσεων συνολικά κατά φορέα");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή πιστώσεων κατά ειδικό φορέα & μείζονα κατηγορία δαπάνης");
        System.out.print("\nΕπιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.println();
        switch (choice) {
            case 1 -> {
                if (budgetType.equals("ΚΡΑΤΙΚΟΥ")) {
                    DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(BudgetExpense.getExpenses()));
                } else if (budgetType.equals("ΤΑΚΤΙΚΟΥ")) {
                    DataOutput.printExpenseWithAsciiTable(BudgetExpenseHandling.getSumOfEveryCategory(RegularBudgetExpense.getAllRegularBudgetExpenses()));
                } else if (budgetType.equals("ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ")) {
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
        private static void showRegularRevenuesMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===\n");
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή των βασικών λογαριασμών εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή όλων των λογαριασμών εσόδων");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή συγκεκριμένων λογαριασμών εσόδων");
        System.out.print("\nΕπιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.println();
        switch (choice) {
            case 1 -> BudgetRevenueHandling.printMainBudgetRevenues(RegularBudgetRevenue.getAllRegularBudgetRevenues());
            case 2 -> RegularBudgetRevenue.printAllRegularBudgetRevenues();
            case 3 -> {
                //ΦΤΙΑΞΤΟ ΒΓΑΖΕΙ ΛΑΘΟΣ ΣΤΑ budgetRevenue.printSuperCategoriesTopDown();
                System.out.print("Παρακαλούμε εισάγετε τον κωδικό του επιθυμητού λογαριασμού εσόδων: ");
                String code = input.nextLine();
                System.out.println();
                RegularBudgetRevenue budgetRevenue = BudgetRevenueHandling.findRevenueWithCode(code, RegularBudgetRevenue.getAllRegularBudgetRevenues());
                DataOutput.printEntryWithAsciiTable(budgetRevenue);
                System.out.println();
                System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΥΨΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
                budgetRevenue.printSuperCategoriesTopDown();
                System.out.println();
                System.out.println(BOLD + "ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΧΑΜΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n" + RESET_2);
                budgetRevenue.printAllSubCategories();
                System.out.println();
            }
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    private static void showPublicInvestmentBudgetRevenuesMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println(BLUE + BOLD + "\n=== ΠΡΟΒΟΛΗ ΕΣΟΔΩΝ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ ===\n");
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή λογαριασμών εσόδων (ΕΘΝΙΚΟ ΣΚΕΛΟΣ)");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή λογαριασμών εσόδων (ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ)");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή λογαριασμών εσόδων (ΕΘΝΙΚΟ + ΣΥΓΧΡΗΜΑΤΟΔΟΤΟΥΜΕΝΟ ΣΚΕΛΟΣ)");
        System.out.println(BLUE + BOLD + "[4] " + RESET + "Προβολή συγκεκριμένου λογαριασμού εσόδων");
        System.out.print("\nΕπιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        System.out.println();
        switch (choice) {
            case 1 -> PublicInvestmentBudgetRevenue.printPublicInvestmentBudgetNationalRevenues();
            case 2 -> PublicInvestmentBudgetRevenue.printPublicInvestmentBudgetCoFundedRevenues();
            case 3 -> PublicInvestmentBudgetRevenue.printAllPublicInvestmentBudgetRevenues();
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }
    
}
