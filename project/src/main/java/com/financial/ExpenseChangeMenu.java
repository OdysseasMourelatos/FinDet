import com.financial.entries.*;
import com.financial.strategies.*;
import java.util.ArrayList;
import java.util.Scanner;
import static com.financial.util.Colors.*;

public class ExpenseChangeMenu {
    public static void showExpensesChangesMenu() {
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
            case 1 -> showSpecificEntityExpensesChangesMenu();
            case 2 -> showAllEntitiesExpensesChangeMenu();
        }
    }

    public static void showSpecificEntityExpensesChangesMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Εισάγετε τον κωδικό του φορέα του οποίου τα έξοδα επιθυμείτε να τροποποιήσετε: ");
        String entityCode = input.nextLine();
        System.out.println();
        Entity entity = Entity.findEntityWithEntityCode(entityCode);
        System.out.println(BLUE + BOLD + "=== " + entity.getEntityName() + " - ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Τροποποίηση συνολικών εξόδων του φορέα");
        System.out.println(BLUE + BOLD + "[2] " + RESET
        + "Τροποποίηση συγκεκριμένου λογαριασμού εξόδων όλων των ειδικών φορέων");
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
                double percentage = input.nextDouble()/100;
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", false);
            }
            case 2 -> {
                System.out.println();
                System.out.print("Εισάγετε τον κωδικό του λογαριασμού: ");
                String code = input.nextLine();
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής του λογαριασμού " + BOLD + BudgetExpenseHandling.findExpenseWithCode(code, entity.getRegularBudgetExpenses()).getDescription().toUpperCase()  + RESET_2 + " : ");
                double percentage = input.nextDouble()/100;
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", false);
            }
            case 3 -> {
                System.out.println("\nΕΙΔΙΚΟΙ ΦΟΡΕΙΣ & ΑΘΡΟΙΣΜΑ ΕΞΟΔΩΝ");
                for (String serviceCode : entity.getRegularServiceCodes()) {
                    System.out.println(serviceCode + " " + entity.findRegularServiceNameWithCode(serviceCode)
                    + " " + entity.getRegularSumOfServiceWithCode(serviceCode));
                    for (RegularBudgetExpense expense : entity.getRegularExpensesOfServiceWithCode(serviceCode)) {
                        System.out.println(expense.getCode() + " " + expense.getDescription() + " " + expense.getAmount());
                    }
                }
                System.out.println();
                System.out.print("Εισάγετε τον κωδικό του ειδικού φορέα: ");
                String serviceCode = input.nextLine();
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής του ειδικού φορέα " + BOLD
                + entity.findRegularServiceNameWithCode(serviceCode).toUpperCase() + RESET_2 + " : ");
                double percentage = input.nextDouble()/100;
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new ServiceFilter(serviceCode), new PercentageOperation());
                strategy.applyAdjustment(entity.getRegularBudgetExpenses(), percentage, 0);
                showExpensesOfEntity(entity, "ΤΑΚΤΙΚΟΥ", false);
            }
        }
    }

    public static void showAllEntitiesExpensesChangeMenu() {
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
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), "ΤΑΚΤΙΚΟΥ", true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new AccountFilter(code), new PercentageOperation());
                strategy.applyAdjustment(BudgetExpense.getExpenses(), percentage, 0);
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), "ΤΑΚΤΙΚΟΥ", false);
            }
            case 2 -> {
                System.out.println();
                System.out.print("Εισάγετε το ποσοστό (%) μεταβολής: ");
                double percentage = input.nextDouble() / 100;
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), "ΤΑΚΤΙΚΟΥ", true);
                IExpenseAdjustmentStrategy strategy = new PercentageAllocationAdjustmentStrategy(new MatchAllFilter(), new PercentageOperation());
                strategy.applyAdjustment(BudgetExpense.getExpenses(), percentage, 0);
                showGeneralExpenses(RegularBudgetExpense.getAllRegularBudgetExpenses(), "ΤΑΚΤΙΚΟΥ", false);
            }
        }
    }

    public static void showExpensesOfEntity(Entity entity, String budgetType, boolean before) {
        System.out.println();
        if (before) {
            System.out.println("************************************************************ ΠΡΙΝ ************************************************************");
        } else {
            System.out.println("************************************************************ ΜΕΤΑ ************************************************************");
        }
        System.out.println();
        DataOutput.printEntityWithAsciiTable(entity, budgetType);
    }

    public static void showGeneralExpenses(ArrayList<? extends BudgetExpense> expenses, String budgetType, boolean before) {
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
