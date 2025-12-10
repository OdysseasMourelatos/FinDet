package com.financial.menu;
import com.financial.pdf.BudgetRevenueConvertToPdf;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    public static void printMainMenu() {
        Scanner input = new Scanner(System.in); //Δημιουργία αντικειμένου της κλάσης Scanner 
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "ΠΡΟΘΥΠΟΥΡΓΟΣ ΓΙΑ ΜΙΑ ΗΜΕΡΑ - BUDGET MANAGER 2026" + Colors.RESET + Colors.RESET_2);
        int choice;
        do {
            try {
                System.out.println();
                System.out.println(Colors.BLUE + Colors.BOLD + "=========== ΚΥΡΙΟ ΜΕΝΟΥ ===========" + Colors.RESET + Colors.RESET_2);
                System.out.println();
                System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Φόρτωση / Ενημέρωση CSV" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Προβολή Προϋπολογισμού" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Εισαγωγή Αλλαγών" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Αναζήτηση Στοιχείων Προϋπολογισμού" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[5] " + Colors.RESET + "Οικονομική Ανάλυση" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[6] " + Colors.RESET + "Γραφήματα & Οπτικοποιήσεις" + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[7] " + Colors.RESET + "Export σε PDF " + Colors.RESET_2);
                System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Έξοδος " + Colors.RESET_2);
                System.out.println();
                System.out.print("Επιλογή: ");
                choice = input.nextInt(); //Εισαγωγή αριθμού από τον χρήστη
                input.nextLine();
                System.out.println();
                if (choice == 1) {
                    CSVDownloadAndSavingMenu.printSubMenuOfCSVDownloadAndSaving();
                } else if (choice == 2) {
                    printSubMenuOfChoice2();
                } else if (choice == 3) {
                    printSubMenuOfChoice3();
                } else if (choice == 4) {
                    System.out.println(Colors.BLUE + Colors.BOLD + "=== ΑΝΑΖΗΤΗΣΗ ΣΤΟΙΧΕΙΩΝ ===" + Colors.RESET + Colors.RESET_2);
                    System.out.println();
                    System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Αναζήτηση με Κωδικό" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Αναζήτηση με Περιγραφή / Λέξη-κλειδί" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Αναζήτηση ανά Υπουργείο" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if (choice == 5) {
                    System.out.println(Colors.BLUE + Colors.BOLD + "=== ΟΙΚΟΝΟΜΙΚΗ ΑΝΑΛΥΣΗ ===" + Colors.RESET + Colors.RESET_2);
                    System.out.println();
                    System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Ανάλυση Εσόδων" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Ανάλυση Εξόδων" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Μεταβολές σε σχέση με προηγούμενο έτος" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Αναλογίες ανά Υπουργείο" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[5] " + Colors.RESET + "Δείκτες οικονομικής απόδοσης" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
                    System.out.println();
                    System.out.print("Επιλογή: ");
                    choice = input.nextInt();
                    input.nextLine();
                } else if (choice == 6) {
                    ChartsMenu.printChartsMenu();
                } else if (choice == 7) {
                    System.out.println(Colors.BLUE + Colors.BOLD + "=== ΕΞΑΓΩΓΗ ΣΕ PDF ===" + Colors.RESET + Colors.RESET_2);
                    System.out.println();
                    System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Εξαγωγή Εσόδων σε PDF" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Εξαγωγή Εξόδων σε PDF" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Εξαγωγή Τελικού Κρατικού Προϋπολογισμού σε PDF" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Εξαγωγή Προϋπολογισμού Δημόσιων Επενδύσεων σε PDF" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[5] " + Colors.RESET + "Εξαγωγή Ανάλυσης σε PDF" + Colors.RESET_2);
                    System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET_2);
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
                System.out.println(Colors.RED + "ΚΑΤΑΧΩΡΗΘΗΚΕ ΜΗ ΕΠΙΤΡΕΠΤΗ ΤΙΜΗ. ΠΑΡΑΚΑΛΩ ΕΙΣΑΓΕΤΕ ΤΙΜΗ ΕΝΤΟΣ ΤΩΝ ΟΡΙΩΝ" + Colors.RESET);
                input.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(Colors.RED + "ΚΑΤΑΧΩΡΗΘΗΚΕ ΜΗ ΕΠΙΤΡΕΠΤΗ ΤΙΜΗ. ΠΑΡΑΚΑΛΩ ΕΙΣΑΓΕΤΕ ΤΙΜΗ ΕΝΤΟΣ ΤΩΝ ΟΡΙΩΝ" + Colors.RESET);
            } catch (NullPointerException e) {
                System.out.println(Colors.RED + "ΕΣΦΑΛΜΕΝΗ ΕΚΧΩΡΗΣΗ ΚΩΔΙΚΟΥ ΛΟΓΑΡΙΑΣΜΟΥ" + Colors.RESET);
            }
        } while (true);
    }
}

