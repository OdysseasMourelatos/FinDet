import java.util.Scanner;
import static Colors.*;


public class Choise3 {
    public static void printSubMenuOfChoice3() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET
        + "Εισαγωγή αλλαγών σε στοιχεία κρατικού προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET
        + "Εισαγωγή αλλαγών σε στοιχεία τακτικού προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[3] " + RESET
        + "Εισαγωγή αλλαγών σε στοιχεία προϋπολογισμού δημόσιων επενδύσεων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> RevenueChangeMenu.modifyExistingRevenueAccount();
            case 2 -> showRegularChangesMenu();
        }
    }

    public static void showRegularChangesMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD
        + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ΣΕ ΣΤΟΙΧΕΙΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET
        + "Εισαγωγή αλλαγών σε λογαριασμούς εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET
        + "Εισαγωγή αλλαγών σε λογαριασμούς εξόδων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> RevenueChangeMenu.modifyExistingRevenueAccount();
            case 2 -> showExpensesChangesMenu();
        }
    }
}
