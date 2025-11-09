package com.financial;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {

        Scanner s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: ");
        String filePath = s.nextLine();
        DataInput.csvReader(filePath);
        BudgetExpense.printExpenses();

        long totalExpenses = BudgetExpense.calculateSum();
        String totalExpensesFormatted = String.format("%,d", totalExpenses);
        System.out.println("ΕΞΟΔΑ: " + totalExpensesFormatted);
        long totalRevenues = BudgetRevenue.calculateSum();
        String totalRevenuesFormatted = String.format("%,d", totalRevenues);
        System.out.println("ΕΣΟΔΑ: " + totalRevenuesFormatted);
        long totalResult = totalRevenues - totalExpenses;
        String totalResultFormatted= String.format("%,d", totalResult);
        System.out.println("ΑΠΟΤΕΛΕΣΜΑ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ (ΕΣΟΔΑ - ΕΞΟΔΑ): " + totalResultFormatted);

        DataUpdate.csvUpdate();
    }
}
