package com.financial;

import java.util.ArrayList;


public class Income extends BudgetEntry{

    private static ArrayList <Long> totalIncome = new ArrayList <>();

    public Income(int code, String description, String category, long amount) {
        super(code, description, category, amount);
        totalIncome.add(amount);
    }
    public static long calculateSum(){
        long sum = 0;
        for (long income : totalIncome) {
            sum += income;
        }
        return sum;
    }
}
