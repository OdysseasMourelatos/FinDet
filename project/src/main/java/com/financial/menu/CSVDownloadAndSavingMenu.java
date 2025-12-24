package com.financial.menu;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.services.DataInput;

import java.util.Scanner;

public class CSVDownloadAndSavingMenu {
    public static void printSubMenuOfCSVDownloadAndSaving() {
        Scanner input = new Scanner(System.in);
        
        while (true) {
            System.out.println(Colors.BLUE + Colors.BOLD + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + Colors.RESET);
            System.out.println();
            System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Φόρτωση αρχείων" + Colors.RESET + Colors.RESET_2);
            System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Επαναφόρτωση όλων των CSV" + Colors.RESET + Colors.RESET_2);
            System.out.println(Colors.BLUE + Colors.BOLD + "[3] " + Colors.RESET + "Εμφάνιση στατιστικών βάσης" + Colors.RESET + Colors.RESET_2);
            System.out.println(Colors.BLUE + Colors.BOLD + "[4] " + Colors.RESET + "Καθαρισμός βάσης δεδομένων" + Colors.RESET + Colors.RESET_2);
            System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET + Colors.RESET_2);
            System.out.println();
            System.out.print("Επιλογή: ");
            
            int choice;
            try {
                choice = input.nextInt();
                input.nextLine(); // Καθαρισμός buffer
            } catch (Exception e) {
                System.out.println("Παρακαλώ εισάγετε έγκυρο αριθμό.");
                input.nextLine(); // Καθαρισμός buffer
                continue;
            }
            
            switch (choice) {
                case 1:
                    handleFileUpload(input);
                    break;
                case 2:
                    handleReloadAllCSV(input);
                    break;
                case 3:
                    // ΝΕΟ: Εμφάνιση στατιστικών βάσης
                    DataInput.showDatabaseStatistics();
                    break;
                case 4:
                    // ΝΕΟ: Καθαρισμός βάσης
                    DataInput.clearDatabase();
                    break;
                case 0:
                    return; // Επιστροφή στο κύριο μενού
                default:
                    System.out.println("Μη έγκυρη επιλογή. Παρακαλώ επιλέξτε 0-4.");
            }
            
            System.out.println("\nΠατήστε Enter για συνέχεια...");
            input.nextLine();
        }
    }
    
    private static void handleFileUpload(Scanner input) {
        System.out.println("\n" + Colors.CYAN + "=== ΦΟΡΤΩΣΗ ΑΡΧΕΙΩΝ CSV ===" + Colors.RESET);
        
        // ΕΣΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
        System.out.print("Εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΣΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + Colors.RESET);
        String filePath = input.nextLine();
        if (!filePath.trim().isEmpty()) {
            DataInput.advancedCSVReader(filePath);
        }
        
        // ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
        System.out.print("Εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
        filePath = input.nextLine();
        if (!filePath.trim().isEmpty()) {
            DataInput.advancedCSVReader(filePath);
        }
        
        // ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ
        System.out.print("Εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): " + Colors.RESET);
        filePath = input.nextLine();
        if (!filePath.trim().isEmpty()) {
            DataInput.advancedCSVReader(filePath);
        }
        
        // ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ
        System.out.print("Εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
        filePath = input.nextLine();
        if (!filePath.trim().isEmpty()) {
            DataInput.advancedCSVReader(filePath);
        }
        
        System.out.println(Colors.GREEN + "✅ Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΟΛΟΚΛΗΡΩΘΗΚΕ!" + Colors.RESET);
        
        // Δημιουργία entities και φιλτραρίσματα
        DataInput.createEntityFromCSV();
        PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
        PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
        BudgetRevenue.sortBudgetRevenuesByCode();
        BudgetRevenue.filterBudgetRevenues();
        
        // Αυτόματη εμφάνιση στατιστικών μετά τη φόρτωση
        System.out.println("\n" + Colors.YELLOW + "=== ΣΤΑΤΙΣΤΙΚΑ ΜΕΤΑ ΤΗ ΦΟΡΤΩΣΗ ===" + Colors.RESET);
        DataInput.showDatabaseStatistics();
    }
    
    private static void handleReloadAllCSV(Scanner input) {
        System.out.println(Colors.YELLOW + "⚠️  Προσοχή: Αυτή η ενέργεια θα επαναφορτώσει όλα τα CSV αρχεία." + Colors.RESET);
        System.out.print("Θέλετε να συνεχίσετε; (Ν/Ο): ");
        String response = input.nextLine().trim().toUpperCase();
        
        if (response.equals("Ν") || response.equals("YES") || response.equals("Y")) {
            // Εδώ μπορείς να προσθέσεις λογική για επαναφόρτωση
            System.out.println("Η λειτουργία επαναφόρτωσης βρίσκεται υπό ανάπτυξη...");
        } else {
            System.out.println("Επιστροφή στο μενού.");
        }
    }
}