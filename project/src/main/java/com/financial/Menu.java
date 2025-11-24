package com.financial;

import java.util.InputMismatchException;
import java.util.Scanner;
public class Menu {
    public static final String RESET = "\u001B[0m"; //προκαθορισμένο χρώμα χαρακτήρων
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String BOLD = "\u001B[1m";
    public static final String RESET_2 = "\u001B[0m"; //προκαθορισμένη ένταση χρώματος χαρακτήρων
    
    public static void printMainMenu() {
        Scanner input = new Scanner(System.in); //Δημιουργία αντικειμένου της κλάσης Scanner 
        System.out.println(BLUE + BOLD + "ΠΡΟΘΥΠΟΥΡΓΟΣ ΓΙΑ ΜΙΑ ΗΜΕΡΑ - BUDGET MANAGER 2026" + RESET + RESET_2);
        int choice;
        do {
            try {
                System.out.println(BLUE + BOLD + "=========== ΚΥΡΙΟ ΜΕΝΟΥ ===========" + RESET + RESET_2);
                System.out.println();
                System.out.println(BLUE + BOLD + "[1] " + RESET + "Φόρτωση / Ενημέρωση CSV" + RESET_2);
                System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή Προϋπολογισμού" + RESET_2);
                System.out.println(BLUE + BOLD + "[3] " + RESET + "Εισαγωγή Αλλαγών" + RESET_2);
                System.out.println(BLUE + BOLD + "[4] " + RESET + "Αναζήτηση Στοιχείων Προϋπολογισμού" + RESET_2);
                System.out.println(BLUE + BOLD + "[5] " + RESET + "Οικονομική Ανάλυση" + RESET_2);
                System.out.println(BLUE + BOLD + "[6] " + RESET + "Γραφήματα & Οπτικοποιήσεις" + RESET_2);
                System.out.println(BLUE + BOLD + "[7] " + RESET + "Export σε PDF " + RESET_2);
                System.out.println(BLUE + BOLD +  "[0] Έξοδος" + RESET + RESET_2);
                System.out.println();
                System.out.print("Επιλογή: ");
                choice = input.nextInt(); //Εισαγωγή αριθμού από τον χρήστη
                input.nextLine();
                if(choice == 1) {
                    printSubMenuOfChoice1();
                } else if(choice==2) {
                    printSubMenuOfChoice2();
                } else if(choice==3) {
                    printSubMenuOfChoice3();
                } else if(choice == 4) {
                    System.out.println("=== ΑΝΑΖΗΤΗΣΗ ΣΤΟΙΧΕΙΩΝ ===");
                    System.out.println();
                    System.out.println("[1] Αναζήτηση με Κωδικό");
                    System.out.println("[2] Αναζήτηση με Περιγραφή / Λέξη-κλειδί");
                    System.out.println("[3] Αναζήτηση ανά Υπουργείο");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if(choice == 5) {
                    System.out.println("=== ΟΙΚΟΝΟΜΙΚΗ ΑΝΑΛΥΣΗ ===");
                    System.out.println();
                    System.out.println("[1] Ανάλυση Εσόδων");
                    System.out.println("[2] Ανάλυση Εξόδων");
                    System.out.println("[3] Μεταβολές σε σχέση με προηγούμενο έτος");
                    System.out.println("[4] Αναλογίες ανά Υπουργείο");
                    System.out.println("[5] Δείκτες οικονομικής απόδοσης");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if(choice == 6) {
                    printSubMenuOfChoice6();
                } else if(choice == 7) {
                    System.out.println("=== ΕΞΑΓΩΓΗ ΣΕ PDF ===");
                    System.out.println();
                    System.out.println("[1] Εξαγωγή Εσόδων σε PDF");
                    System.out.println("[2] Εξαγωγή Εξόδων σε PDF");
                    System.out.println("[3] Εξαγωγή Τελικού Κρατικού Προϋπολογισμού σε PDF");
                    System.out.println("[4] Εξαγωγή Προϋπολογισμού Δημόσιων Επενδύσεων σε PDF");
                    System.out.println("[5] Εξαγωγή Ανάλυσης σε PDF");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if(choice == 0) {
                    System.out.println("ΠΡΑΓΜΑΤΟΠΟΙΕΙΤΑΙ ΕΞΟΔΟΣ ΑΠΟ ΤΟ ΣΥΣΤΗΜΑ...");
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (InputMismatchException e) {
                System.out.println(RED + "ΚΑΤΑΧΩΡΗΘΗΚΕ ΜΗ ΕΠΙΤΡΕΠΤΗ ΤΙΜΗ. ΠΑΡΑΚΑΛΩ ΕΙΣΑΓΕΤΕ ΤΙΜΗ ΕΝΤΟΣ ΤΩΝ ΟΡΙΩΝ" + RESET);
                input.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(RED + "ΚΑΤΑΧΩΡΗΘΗΚΕ ΜΗ ΕΠΙΤΡΕΠΤΗ ΤΙΜΗ. ΠΑΡΑΚΑΛΩ ΕΙΣΑΓΕΤΕ ΤΙΜΗ ΕΝΤΟΣ ΤΩΝ ΟΡΙΩΝ" + RESET);
            } catch (NullPointerException e) {
                System.out.println(RED + "ΕΣΦΑΛΜΕΝΗ ΕΚΧΩΡΗΣΗ ΚΩΔΙΚΟΥ ΛΟΓΑΡΙΑΣΜΟΥ" + RESET);
            }
        } while (true);
    }

    public static void printSubMenuOfChoice1() {
        Scanner input = new Scanner(System.in);
        System.out.println(YELLOW + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + RESET);
        System.out.println();
        System.out.println(YELLOW + BOLD + "[1] " + RESET + "Φόρτωση αρχείων" + RESET + RESET_2);
        System.out.println(YELLOW + BOLD + "[2] " + RESET + "Επαναφόρτωση όλων των CSV" + RESET + RESET_2);
        System.out.println(YELLOW + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if(choice == 1) {
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΕΣΟΔΑ ΚΡΑΤΙΚΟΎ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + RESET);
            String filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου (ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): ");
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): ");
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): ");
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου (ΚΩΔΙΚΟΣ ΚΑΙ ΟΝΟΜΑΣΙΑ ΦΟΡΕΩΝ): ");
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.println();
            System.out.println(GREEN + "Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΠΡΑΓΜΑΤΟΠΟΙΗΘΗΚΕ ΕΠΙΤΥΧΩΣ!" + RESET);
        }
    }

    public static void printSubMenuOfChoice2() {
        Scanner input = new Scanner(System.in);
        System.out.println("=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
        System.out.println();
        System.out.println("[1] Συνολικά Έσοδα");
        System.out.println("[2] Συνολικά Έξοδα");

        System.out.println("[3] Βασικές Κατηγορίες Εσόδων (2ψήφιοι)");
        System.out.println("[4] Βασικές Κατηγορίες Εξόδων (2ψήφιοι)");
        System.out.println("[5] Βασικές Κατηγορίες Εσόδων & Εξόδων + Αποτέλεσμα Κρατικού Προϋπολογισμού");

        System.out.println("[6] Προβολή ανά Υπουργείο");
        System.out.println("[7] Προβολή βάσει Κωδικού (2–10 ψηφία)");

        System.out.println("[8] Προβολή ΠΔΕ συνολικά");
        System.out.println("[9] Προβολή ΠΔΕ ανά Σκέλος (Εθνικό / Συγχρ.)");

        System.out.println("[10] Προβολή ανά Τύπο Προϋπολογισμού (Τακτικός / ΠΔΕ / Κρατικός)");
        System.out.println("[11] Προβολή ανά Κατηγορία Πόρων");

        System.out.println("[0] Επιστροφή στο Κύριο Μενού\n");

        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> showTotalRevenuesMenu();
            case 3 -> {
                BudgetRevenue.printMainBudgetRevenues();
                System.out.println("\nΣΥΝΟΛΙΚΑ ΕΣΟΔΑ: " + String.format("%,d", BudgetRevenue.calculateSum()));
            }
            case 4 -> {
                BudgetExpense.printSumOfEveryCategory();
                System.out.println("\nΣΥΝΟΛΙΚΑ ΕΞΟΔΑ: " + String.format("%,d", BudgetExpense.calculateSum()));
            }
            case 5 -> {
                BudgetRevenue.printMainBudgetRevenues();
                System.out.println();
                BudgetExpense.printSumOfEveryCategory();
                long totalRevenuesSum = BudgetRevenue.calculateSum();
                System.out.println("\nΣΥΝΟΛΙΚΑ ΕΣΟΔΑ: " + String.format("%,d", totalRevenuesSum));
                long totalExpensesSum = BudgetExpense.calculateSum();
                System.out.println("ΣΥΝΟΛΙΚΑ ΕΞΟΔΑ: " + String.format("%,d", totalExpensesSum));
                System.out.println("\nΑΠΟΤΕΛΕΣΜΑ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ (ΕΣΟΔΑ - ΕΞΟΔΑ): " + String.format("%,d", totalRevenuesSum - totalExpensesSum));

            }
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    public static void printSubMenuOfChoice3(){
        Scanner input = new Scanner(System.in);
        System.out.println("=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println("[1] Τροποποίηση Ποσού Υφιστάμενου Λογαριασμού Εσόδων");
        System.out.println("[2] Προσθήκη Γραμμής Εσόδων");
        System.out.println("[3] Προσθήκη Γραμμής Εξόδων");
        System.out.println("[4] Διαγραφή Γραμμής");
        System.out.println("[0] Επιστροφή");
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            modifyExistingRevenueAccount();
        }
    }

    public static void modifyExistingRevenueAccount() {
        Scanner input = new Scanner(System.in);
        System.out.println("=== ΤΡΟΠΟΠΟΙΗΣΗ ΠΟΣΟΥ ΥΦΙΣΤΑΜΕΝΟΥ ΛΟΓΑΡΙΑΣΜΟΥ ΕΣΟΔΩΝ ===");
        System.out.println();
        System.out.print("Εισάγετε τον κωδικό του λογαριασμού εσόδων του οποίου το ποσό επιθυμείτε να αλλάξει: ");
        String code = input.nextLine();
        BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode(code);

        if (budgetRevenue != null) {
            selectChangeMethodAndApply(budgetRevenue);
        } else {
            System.out.println(RED + "Δεν βρέθηκε λογαριασμός με κωδικό: " + code + RESET);
        }
    }

    public static void selectChangeMethodAndApply(BudgetRevenue budgetRevenue) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println("=== ΤΡΟΠΟΣ ΜΕΤΑΒΟΛΗΣ ΥΠΟΚΑΤΗΓΟΡΙΩΝ ΕΣΟΔΩΝ ===");
        System.out.println();
        System.out.println("[1] Ισόποση μεταβολή υποκατηγοριών");
        System.out.println("[2] Ποσοστιαία (Αναλογική) μεταβολή υποκατηγοριών");
        System.out.println("[3] Χειροκίνητη μεταβολή ανά υποκατηγορία");
        System.out.println();
        System.out.print("Επιλογή: ");
        int changeMethod = input.nextInt();
        System.out.println();

        selectInputMethodAndExecute(budgetRevenue, changeMethod);
    }

    public static void selectInputMethodAndExecute(BudgetRevenue budgetRevenue, int changeMethod) {
        Scanner input = new Scanner(System.in);
        System.out.println("=== ΤΡΟΠΟΣ ΕΙΣΑΓΩΓΗΣ ΜΕΤΑΒΟΛΗΣ ΛΟΓΑΡΙΑΣΜΟΥ ΕΣΟΔΩΝ ===");
        System.out.println();
        System.out.println("[1] Εισαγωγή Ποσοστού (%) Μεταβολής Λογαριασμού");
        System.out.println("[2] Εισαγωγή Ποσού (€) Μεταβολής Λογαριασμού");
        System.out.println("[3] Εισαγωγή Νέου Επιθυμητού Υπολοίπου (€) Λογαριασμού");
        System.out.println();
        System.out.print("Επιλογή: ");
        int inputMethod = input.nextInt();
        System.out.println();

        executeBudgetChanges(budgetRevenue, changeMethod, inputMethod);
    }

    public static void printSubMenuOfChoice6() {
        Scanner input = new Scanner(System.in);
        System.out.println("=== ΓΡΑΦΗΜΑΤΑ & ΟΠΤΙΚΟΠΟΙΗΣΕΙΣ ===");
        System.out.println();
        System.out.println("[1] Κατανομή Εσόδων (Pie Chart)");
        System.out.println("[2] Κατανομή Εξόδων (Pie Chart)");
        System.out.println("[3] Σύγκριση Εσόδων–Εξόδων (Bar Chart)");
        System.out.println("[4] Δαπάνες ανά Υπουργείο (Bar Chart)");
        System.out.println("[0] Επιστροφή στο Κύριο Μενού");
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            printRevenueChartsSubMenu();
        }
    }

    public static void printRevenueChartsSubMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println("=== ΚΑΤΑΝΟΜΗ ΕΣΟΔΩΝ (PIE CHART) ===");
        System.out.println();
        System.out.println("[1] Κατανομή Βασικών Λογαριασμών Εσόδων");
        System.out.println("[2] Κατανομή Φόρων");
        System.out.println("[3] Κατανομή Μεταβιβάσεων");
        System.out.println("[4] Κατανομή Πωλήσεων Αγαθών & Υπηρεσιών");
        System.out.println("[5] Κατανομή Λοιπών Τρεχόντων Εσόδων");
        System.out.println("[0] Επιστροφή στο Κύριο Μενού");
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            PieChartGenerator.generateChart(BudgetRevenue.getMainBudgetRevenues(), "Κατανομή Εσόδων");
        } else if (choice == 2) {
            printTaxChartsSubMenu();
        } else if (choice == 3) {
            BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("13");
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Μεταβιβάσεων");
        } else if (choice == 4) {
            BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("14");
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Πωλήσεων Αγαθών & Υπηρεσιών");
        } else if (choice == 5) {
            BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("15");
            PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Λοιπών Τρεχόντων Εσόδων");
        }
    }

    public static void showBeforeChanges(BudgetRevenue budgetRevenue) {
        System.out.println();
        System.out.println("********************************************************* ΠΡΙΝ *********************************************************");
        System.out.println();
        DataOutput.printEntryWithAsciiTable(budgetRevenue);
        System.out.println();
        System.out.println("ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΥΨΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n");
        budgetRevenue.printSuperCategoriesTopDown();
        System.out.println();
        System.out.println("ΚΑΤΗΓΟΡΙΕΣ ΣΕ ΧΑΜΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n");
        budgetRevenue.printAllSubCategories();
        System.out.println();
    }

    public static void showAfterChanges(BudgetRevenue budgetRevenue) {
        System.out.println();
        System.out.println("********************************************************* ΜΕΤΑ *********************************************************");
        System.out.println();
        DataOutput.printEntryWithAsciiTable(budgetRevenue);
        System.out.println();
        System.out.println("ΚΑΤΗΓΟΡΙΕΣ ΛΟΓΑΡΙΑΣΜΩΝ ΣΕ ΥΨΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n");
        budgetRevenue.printSuperCategoriesTopDown();
        System.out.println();
        System.out.println("ΚΑΤΗΓΟΡΙΕΣ ΛΟΓΑΡΙΑΣΜΩΝ ΣΕ ΧΑΜΗΛΟΤΕΡΟ ΕΠΙΠΕΔΟ:\n");
        budgetRevenue.printAllSubCategories();
        System.out.println();
        System.out.println(GREEN + "ΟΙ ΑΛΛΑΓΕΣ ΟΛΟΚΛΗΡΩΘΗΚΑΝ ΜΕ ΕΠΙΤΥΧΙΑ.\n" + RESET + "ΕΠΙΣΤΡΟΦΗ ΣΤΟ ΚΥΡΙΟ ΜΕΝΟΥ..");
    }

    private static void showTotalRevenuesMenu() {
        Scanner input = new Scanner(System.in);

        System.out.println("\n=== ΣΥΝΟΛΙΚΑ ΕΣΟΔΑ ===\n");
        System.out.println("[1] Κρατικός Προϋπολογισμός");
        System.out.println("[2] Κρατικός Προϋπολογισμός + Προϋπολογισμός Δημόσιων Επενδύσεων (ΠΔΕ)");
        System.out.println("[3] Προϋπολογισμός Δημόσιων Επενδύσεων (Εθνικό + Συγχρηματοδοτούμενο Σκέλος)");
        System.out.println("[4] Εθνικό Σκέλος Προϋπολογισμού Δημόσιων Επενδύσεων");
        System.out.println("[5] Συγχρηματοδοτούμενο Σκέλος Προϋπολογισμού Δημόσιων Επενδύσεων");
        System.out.print("\nΕπιλογή: ");

        int choice = input.nextInt();
        System.out.println();
        switch (choice) {
            case 1 -> BudgetRevenue.printAllBudgetRevenues();
            case 2 -> {
                BudgetRevenue.printAllBudgetRevenues();
                System.out.println();
                PublicInvestmentBudgetRevenue.printAllPublicInvestmentBudgetRevenues();
            }
            case 3 -> PublicInvestmentBudgetRevenue.printAllPublicInvestmentBudgetRevenues();
            case 4 -> PublicInvestmentBudgetRevenue.printPublicInvestmentBudgetNationalRevenues();
            case 5 -> PublicInvestmentBudgetRevenue.printPublicInvestmentBudgetCoFundedRevenues();
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    public static void printTaxChartsSubMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println("=== ΚΑΤΑΝΟΜΗ ΦΟΡΩΝ ===");
        System.out.println();
        System.out.println("[1] Κατανομή Βασικών Λογαριασμών Φόρων");
        System.out.println("[2] Κατανομή Φόρων επί αγαθών και υπηρεσιών");
        System.out.println("[3] Κατανομή Φόρων Εισοδήματος");
        System.out.println("[0] Επιστροφή στο Κύριο Μενού");
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 1 -> {
                BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("11");
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων");
            }
            case 2 -> {
                BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("111");
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων επί αγαθών και υπηρεσιών");
            }
            case 3 -> {
                BudgetRevenue budgetRevenue = BudgetRevenue.findRevenueWithCode("115");
                PieChartGenerator.generateChart(budgetRevenue.findNextLevelSubCategories(), "Κατανομή Φόρων Εισοδήματος");
            }
        }
    }
}
