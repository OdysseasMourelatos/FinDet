package com.financial;
import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: ");
        String filePath = s.nextLine();
        DataInput.csvReader(filePath);

        long totalExpenses = Expense.calculateSum();
        String totalExpensesFormatted = String.format("%,d", totalExpenses);
        System.out.println("ΕΞΟΔΑ: " + totalExpensesFormatted);
        long totalIncome = Income.calculateSum();
        String totalIncomeFormatted = String.format("%,d", totalIncome);
        System.out.println("ΕΣΟΔΑ: " + totalIncomeFormatted);
        long totalResult = totalIncome - totalExpenses;
        String totalResultFormatted= String.format("%,d", totalResult);
        System.out.println("ΑΠΟΤΕΛΕΣΜΑ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ (ΕΣΟΔΑ - ΕΞΟΔΑ): " + totalResultFormatted);
    }
}
