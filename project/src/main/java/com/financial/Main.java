package com.financial;

public class Main {
    public static void main (String[] args) {
        DataInput.csvReader();
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
