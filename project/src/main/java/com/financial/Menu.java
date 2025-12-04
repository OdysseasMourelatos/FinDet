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
        System.out.println();
        System.out.println(BLUE + BOLD + "ΠΡΟΘΥΠΟΥΡΓΟΣ ΓΙΑ ΜΙΑ ΗΜΕΡΑ - BUDGET MANAGER 2026" + RESET + RESET_2);
        int choice;
        do {
            try {
                System.out.println();
                System.out.println(BLUE + BOLD + "=========== ΚΥΡΙΟ ΜΕΝΟΥ ===========" + RESET + RESET_2);
                System.out.println();
                System.out.println(BLUE + BOLD + "[1] " + RESET + "Φόρτωση / Ενημέρωση CSV" + RESET_2);
                System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή Προϋπολογισμού" + RESET_2);
                System.out.println(BLUE + BOLD + "[3] " + RESET + "Εισαγωγή Αλλαγών" + RESET_2);
                System.out.println(BLUE + BOLD + "[4] " + RESET + "Αναζήτηση Στοιχείων Προϋπολογισμού" + RESET_2);
                System.out.println(BLUE + BOLD + "[5] " + RESET + "Οικονομική Ανάλυση" + RESET_2);
                System.out.println(BLUE + BOLD + "[6] " + RESET + "Γραφήματα & Οπτικοποιήσεις" + RESET_2);
                System.out.println(BLUE + BOLD + "[7] " + RESET + "Export σε PDF " + RESET_2);
                System.out.println(BLUE + BOLD + "[0] " + RESET + "Έξοδος " + RESET_2);
                System.out.println();
                System.out.print("Επιλογή: ");
                choice = input.nextInt(); //Εισαγωγή αριθμού από τον χρήστη
                input.nextLine();
                System.out.println();
                if (choice == 1) {
                    printSubMenuOfChoice1();
                } else if (choice == 2) {
                    printSubMenuOfChoice2();
                } else if (choice == 3) {
                    printSubMenuOfChoice3();
                } else if (choice == 4) {
                    System.out.println(BLUE + BOLD + "=== ΑΝΑΖΗΤΗΣΗ ΣΤΟΙΧΕΙΩΝ ===" + RESET + RESET_2);
                    System.out.println();
                    System.out.println(BLUE + BOLD + "[1] " + RESET + "Αναζήτηση με Κωδικό" + RESET_2);
                    System.out.println(BLUE + BOLD + "[2] " + RESET + "Αναζήτηση με Περιγραφή / Λέξη-κλειδί" + RESET_2);
                    System.out.println(BLUE + BOLD + "[3] " + RESET + "Αναζήτηση ανά Υπουργείο" + RESET_2);
                    System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if (choice == 5) {
                    System.out.println(BLUE + BOLD + "=== ΟΙΚΟΝΟΜΙΚΗ ΑΝΑΛΥΣΗ ===" + RESET + RESET_2);
                    System.out.println();
                    System.out.println(BLUE + BOLD + "[1] " + RESET + "Ανάλυση Εσόδων" + RESET_2);
                    System.out.println(BLUE + BOLD + "[2] " + RESET + "Ανάλυση Εξόδων" + RESET_2);
                    System.out.println(BLUE + BOLD + "[3] " + RESET + "Μεταβολές σε σχέση με προηγούμενο έτος" + RESET_2);
                    System.out.println(BLUE + BOLD + "[4] " + RESET + "Αναλογίες ανά Υπουργείο" + RESET_2);
                    System.out.println(BLUE + BOLD + "[5] " + RESET + "Δείκτες οικονομικής απόδοσης" + RESET_2);
                    System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if (choice == 6) {
                    printSubMenuOfChoice6();
                } else if (choice == 7) {
                    System.out.println(BLUE + BOLD + "=== ΕΞΑΓΩΓΗ ΣΕ PDF ===" + RESET + RESET_2);
                    System.out.println();
                    System.out.println(BLUE + BOLD + "[1] " + RESET + "Εξαγωγή Εσόδων σε PDF" + RESET_2);
                    System.out.println(BLUE + BOLD + "[2] " + RESET + "Εξαγωγή Εξόδων σε PDF" + RESET_2);
                    System.out.println(BLUE + BOLD + "[3] " + RESET + "Εξαγωγή Τελικού Κρατικού Προϋπολογισμού σε PDF" + RESET_2);
                    System.out.println(BLUE + BOLD + "[4] " + RESET + "Εξαγωγή Προϋπολογισμού Δημόσιων Επενδύσεων σε PDF" + RESET_2);
                    System.out.println(BLUE + BOLD + "[5] " + RESET + "Εξαγωγή Ανάλυσης σε PDF" + RESET_2);
                    System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                    switch (choice) {
                        case 1: BudgetRevenueConvertToPdf.createPdf("ΕΣΟΔΑ_ΚΡΑΤΙΚΟΥ_ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ.pdf");
                    }
                } else if (choice == 0) {
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
        System.out.println(BLUE + BOLD + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + RESET);
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Φόρτωση αρχείων" + RESET + RESET_2);
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Επαναφόρτωση όλων των CSV" + RESET + RESET_2);
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΕΣΟΔΑ ΚΡΑΤΙΚΟΎ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + RESET);
            String filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): " + RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + BLUE + " (ΚΩΔΙΚΟΣ ΚΑΙ ΟΝΟΜΑΣΙΑ ΦΟΡΕΩΝ): " + RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.println();
            System.out.println(GREEN + "Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΠΡΑΓΜΑΤΟΠΟΙΗΘΗΚΕ ΕΠΙΤΥΧΩΣ!" + RESET);
            DataInput.createEntityFromCSV();
            DataInput.createRegularBudgetRevenueFromCSV();
        }
    }

    public static void printSubMenuOfChoice2() {
        Scanner input = new Scanner(System.in);
        System.out.println();
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
            case 1 -> showBudgetMenu();
            case 2 -> showRegularBudgetMenu();
            case 3 -> showPublicInvestmentBudgetMenu();
            default -> System.out.println(RED + "Μη έγκυρη επιλογή" + RESET);
        }
    }

    public static void showBudgetMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΠΡΟΒΟΛΗ ΣΤΟΙΧΕΙΩΝ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===" );
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Προβολή Εσόδων Κρατικού Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προβολή Εξόδων Κρατικού Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προβολή Εσόδων & Εξόδων Κρατικού Προϋπολογισμού");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        switch (choice) {
            case 1 -> showTotalRevenuesMenu();
            case 2 -> showExpensesMenu("ΚΡΑΤΙΚΟΥ");
        }
    }

    public static void printSubMenuOfChoice3() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Τροποποίηση Ποσού Υφιστάμενου Λογαριασμού Εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Προσθήκη Γραμμής Εσόδων");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Προσθήκη Γραμμής Εξόδων");
        System.out.println(BLUE + BOLD + "[4] " + RESET + "Διαγραφή Γραμμής");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή" + RESET_2);
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
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΤΡΟΠΟΠΟΙΗΣΗ ΠΟΣΟΥ ΥΦΙΣΤΑΜΕΝΟΥ ΛΟΓΑΡΙΑΣΜΟΥ ΕΣΟΔΩΝ ===" + RESET + RESET_2);
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
            budgetRevenue.implementChangesOfEqualDistribution(change);
        } else if (changeMethod == 2) {
            budgetRevenue.implementChangesOfPercentageAdjustment(percentage);
        } else if (changeMethod == 3) {
            return;
        }

        showAfterChanges(budgetRevenue);
    }

    public static void printSubMenuOfChoice6() {
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE + BOLD + "=== ΓΡΑΦΗΜΑΤΑ & ΟΠΤΙΚΟΠΟΙΗΣΕΙΣ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Κατανομή Εσόδων (Pie Chart)");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Κατανομή Εξόδων (Pie Chart)");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Σύγκριση Εσόδων–Εξόδων (Bar Chart)");
        System.out.println(BLUE + BOLD + "[4] " + RESET + "Δαπάνες ανά Υπουργείο (Bar Chart)");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
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
        System.out.println(BLUE + BOLD + "=== ΚΑΤΑΝΟΜΗ ΕΣΟΔΩΝ (PIE CHART) ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Κατανομή Βασικών Λογαριασμών Εσόδων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Κατανομή Φόρων");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Κατανομή Μεταβιβάσεων");
        System.out.println(BLUE + BOLD + "[4] " + RESET + "Κατανομή Πωλήσεων Αγαθών & Υπηρεσιών");
        System.out.println(BLUE + BOLD + "[5] " + RESET + "Κατανομή Λοιπών Τρεχόντων Εσόδων");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
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

    public static void printTaxChartsSubMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.println(BLUE + BOLD + "=== ΚΑΤΑΝΟΜΗ ΦΟΡΩΝ ===");
        System.out.println();
        System.out.println(BLUE + BOLD + "[1] " + RESET + "Κατανομή Βασικών Λογαριασμών Φόρων");
        System.out.println(BLUE + BOLD + "[2] " + RESET + "Κατανομή Φόρων επί αγαθών και υπηρεσιών");
        System.out.println(BLUE + BOLD + "[3] " + RESET + "Κατανομή Φόρων Εισοδήματος");
        System.out.println(BLUE + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET_2);
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
