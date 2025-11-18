package com.financial;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου (ΕΣΟΔΑ ΚΡΑΤΙΚΟΎ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): ");
        String filePath = s.nextLine();
        DataInput.advancedCSVReader(filePath);
        s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): ");
        filePath = s.nextLine();
        DataInput.advancedCSVReader(filePath);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): ");
        filePath = s.nextLine();
        DataInput.advancedCSVReader(filePath);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: (ΚΩΔΙΚΟΣ ΚΑΙ ΟΝΟΜΑΣΙΑ ΦΟΡΕΩΝ): ");
        filePath = s.nextLine();
        DataInput.advancedCSVReader(filePath);
        long totalRegularBudgetExpenses = RegularBudgetExpense.getSumOfRegularBudgetExpenses();
        System.out.println("ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ: " + String.format("%,d", totalRegularBudgetExpenses));
        long totalPublicInvestmentBudgetExpenses = PublicInvestmentBudgetExpense.getSumOfPublicInvestmentBudgetExpenses();
        System.out.println("ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ: " + String.format("%,d", totalPublicInvestmentBudgetExpenses));
        long totalBudgetExpenses = totalRegularBudgetExpenses + totalPublicInvestmentBudgetExpenses;
        System.out.println("ΣΥΝΟΛΙΚΑ ΕΞΟΔΑ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ: " + String.format("%,d", totalBudgetExpenses));

        Entity.printEntitiesWithTheirTotalRegularExpenses();
        DataUpdate.csvUpdate();
    }
}