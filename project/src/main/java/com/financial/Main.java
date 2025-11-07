package com.financial;

public class Main {
    public static void main (String[] args) {
        DataInput.csvReader();
        String formatted1 = String.format("%,d", Expense.calculateSum());
        System.out.println("ΕΞΟΔΑ: " + formatted1);
        String formatted2 = String.format("%,d", Income.calculateSum());
        System.out.println("ΕΣΟΔΑ: " + formatted2);
    }
}
