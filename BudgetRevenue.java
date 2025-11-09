package com.financial;

import java.util.ArrayList;


public class BudgetRevenue extends BudgetEntry{

    private static ArrayList <BudgetRevenue> revenues = new ArrayList<>();

    public BudgetRevenue(int code, String description, String category, long amount) {
        super(code, description, category, amount);
        revenues.add(this);
    }
    public static long calculateSum(){
        long sum = 0;
        for (BudgetRevenue revenue : revenues) {
            sum += revenue.getAmmount();
        }
        return sum;
    }

    public static void printRevenues() {
        for (BudgetRevenue revenue : revenues) {
            System.out.println(revenue);
        }
    }
    public static BudgetRevenue findRevenueWithCode (int code) {
        for (BudgetRevenue revenue : revenues) {
            if (revenue.getCode() == code) {
                return revenue;
            }
        }
        return null;
    }
    @Override
    public String toString () {
        return super.toString();
    }
}

