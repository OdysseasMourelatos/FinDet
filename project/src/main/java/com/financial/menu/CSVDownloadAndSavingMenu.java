package com.financial.menu;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.services.DataInput;

import java.util.Scanner;

public class CSVDownloadAndSavingMenu {
    public static void printSubMenuOfCSVDownloadAndSaving() {
        Scanner input = new Scanner(System.in);
        System.out.println(Colors.BLUE + Colors.BOLD + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + Colors.RESET);
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Φόρτωση αρχείων" + Colors.RESET + Colors.RESET_2);
        System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET + "Επαναφόρτωση όλων των CSV" + Colors.RESET + Colors.RESET_2);
        System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET + "Επιστροφή στο Κύριο Μενού" + Colors.RESET + Colors.RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΣΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + Colors.RESET);
            String filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): " + Colors.RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.println(Colors.GREEN + "Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΠΡΑΓΜΑΤΟΠΟΙΗΘΗΚΕ ΕΠΙΤΥΧΩΣ!" + Colors.RESET);
            DataInput.createEntityFromCSV();
            PublicInvestmentBudgetRevenue.sortPublicInvestmentBudgetRevenuesByCode();
            PublicInvestmentBudgetRevenue.filterPublicInvestmentBudgetRevenues();
            BudgetRevenue.sortBudgetRevenuesByCode();
            BudgetRevenue.filterBudgetRevenues();
        }
    }
}