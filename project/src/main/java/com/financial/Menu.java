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
    
    public static void printMenu() {
        Scanner input = new Scanner(System.in); //Δημιουργία αντικειμένου της κλάσης Scanner 
        System.out.println(PURPLE + BOLD + "ΠΡΟΘΥΠΟΥΡΓΟΣ ΓΙΑ ΜΙΑ ΗΜΕΡΑ - BUDGET MANAGER 2026" + RESET + RESET_2);
        int choice;
        do {
            try {
                System.out.println(CYAN + BOLD + "=========== ΚΥΡΙΟ ΜΕΝΟΥ ===========" + RESET + RESET_2);
                System.out.println();
                System.out.println(CYAN + BOLD + "[1] " + RESET + "Φόρτωση / Ενημέρωση CSV" + RESET_2);
                System.out.println(CYAN + BOLD + "[2] " + RESET + "Προβολή Προϋπολογισμού" + RESET_2);
                System.out.println(CYAN + BOLD + "[3] " + RESET + "Εισαγωγή Αλλαγών" + RESET_2);
                System.out.println(CYAN + BOLD + "[4] " + RESET + "Έλεγχος Περιορισμών" + RESET_2);
                System.out.println(CYAN + BOLD + "[5] " + RESET + "Οικονομική Ανάλυση" + RESET_2);
                System.out.println(CYAN + BOLD + "[6] " + RESET + "Γραφήματα & Οπτικοποιήσεις" + RESET_2);
                System.out.println(CYAN + BOLD + "[7] " + RESET + "Export PDF / CSV" + RESET_2);
                System.out.println(RED + BOLD +  "[0] Έξοδος" + RESET + RESET_2);
                System.out.println();
                System.out.print("Επιλογή: ");
                choice = input.nextInt(); //Εισαγωγή αριθμού από τον χρήστη
                input.nextLine();
                if (choice == 1) {
                    System.out.println(YELLOW + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + RESET);
                    System.out.println();
                    System.out.println(YELLOW + BOLD + "[1] " + RESET + "Φόρτωση αρχείων" + RESET + RESET_2);
                    System.out.println(YELLOW + BOLD + "[2] " + RESET + "Επαναφόρτωση όλων των CSV" + RESET + RESET_2);
                    System.out.println(YELLOW + BOLD + "[0] " + RESET + "Επιστροφή στο Κύριο Μενού" + RESET + RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                    if (choice == 1) {
                        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + YELLOW + " (ΕΣΟΔΑ ΚΡΑΤΙΚΟΎ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + RESET);
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
                        System.out.println();
                        System.out.println(GREEN + "Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΠΡΑΓΜΑΤΟΠΟΙΗΘΗΚΕ ΕΠΙΤΥΧΩΣ!" + RESET);
                        System.out.println();
                    }
                } else if (choice == 2) {
                    System.out.println("=== ΠΡΟΒΟΛΗ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
                    System.out.println();
                    System.out.println("[1] Προβολή συνολικών Εσόδων");
                    System.out.println("[2] Προβολή συνολικών Εξόδων");
                    System.out.println("[3] Προβολή ανά Υπουργείο");
                    System.out.println("[4] Προβολή ανά Κωδικό");
                    System.out.println("[5] Αναζήτηση στοιχείων");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");

                } else if(choice == 3) {
                    System.out.println("=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
                    System.out.println();
                    System.out.println("[1] Τροποποίηση Ποσού Υφιστάμενου Λογαριασμού");
                    System.out.println("[2] Προσθήκη Γραμμής Εσόδων");
                    System.out.println("[3] Προσθήκη Γραμμής Εξόδων");
                    System.out.println("[4] Διαγραφή Γραμμής");
                    System.out.println("[0] Επιστροφή");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                    if (choice == 1) {
                        System.out.println("=== ΤΡΟΠΟΠΟΙΗΣΗ ΠΟΣΟΥ ΥΦΙΣΤΑΜΕΝΟΥ ΛΟΓΑΡΙΑΣΜΟΥ ===");
                        System.out.println();
                        System.out.println("[1] Ισόποση αλλαγή");
                        System.out.println("[2] Αναλογική αλλαγή");
                        System.out.println("[3] Ποσοστιαία αλλαγή");
                        System.out.println("[4] Χειροκίνητη αλλαγή ανά υποκατηγορία");
                        System.out.println("[0] Επιστροφή στο προηγούμενο μενού");
                        System.out.println();
                        System.out.print("Επιλογή: ");
                        choice = input.nextInt();
                        System.out.println();
                        System.out.println(RED + "ΟΙ ΑΛΛΑΓΕΣ ΔΕΝ ΟΛΟΚΛΗΡΩΘΗΚΑΝ ΜΕ ΕΠΙΤΥΧΙΑ.\n" + RESET + "ΕΠΙΣΤΡΟΦΗ ΣΤΟ ΚΥΡΙΟ ΜΕΝΟΥ..");
                    }
                } else if (choice == 4) {
                    System.out.println("=== ΕΛΕΓΧΟΣ ΠΕΡΙΟΡΙΣΜΩΝ ===");
                    System.out.println();
                    System.out.println("[1] Έλεγχος ισοσκέλισης προϋπολογισμού");
                    System.out.println("[2] Έλεγχος υπέρβασης εξόδων");
                    System.out.println("[3] Έλεγχος μηδενικών ή ελλιπών τιμών");
                    System.out.println("[4] Έλεγχος αντιστοίχισης κωδικών");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                } else if (choice == 5) {
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
                } else if (choice == 6) {
                    System.out.println("=== ΓΡΑΦΗΜΑΤΑ & ΟΠΤΙΚΟΠΟΙΗΣΕΙΣ ===");
                    System.out.println();
                    System.out.println("[1] Γράφημα Εσόδων");
                    System.out.println("[2] Γράφημα Εξόδων");
                    System.out.println("[3] Γράφημα ανά Υπουργείο");
                    System.out.println("[4] Σύγκριση Εσόδων - Εξόδων");
                    System.out.println("[5] Custom γραφήματα");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
                } else if (choice == 7) {
                    System.out.println("=== EXPORT PDF / CSV ===");
                    System.out.println();
                    System.out.println("[1] Export Εσόδων σε PDF");
                    System.out.println("[2] Export Εξόδων σε PDF");
                    System.out.println("[3] Export όλων των δεδομένων σε CSV");
                    System.out.println("[4] Export ανάλυσης σε PDF");
                    System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                    System.out.println();
                    System.out.print("Επιλογή: ");
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
            }
        } while (true);
    }
}
