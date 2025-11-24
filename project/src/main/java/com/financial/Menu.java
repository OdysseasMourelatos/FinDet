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
            }
        } while (true);
    }
}
