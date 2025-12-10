package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PublicInvestmentBudgetRevenue extends BudgetRevenue {

    //Constructor & Fields

    private final String type;
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenues = new ArrayList<>();
    protected static ArrayList<PublicInvestmentBudgetRevenue> publicInvestmentBudgetRevenuesFiltered = new ArrayList<>();

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long amount) {
        super(code, description, category, amount);
        this.type = type;
        publicInvestmentBudgetRevenues.add(this);
    }

    //Extra constructor that creates PublicInvestmentBudgetRevenues (filtered)
    private long nationalAmount;
    private long coFundedAmount;

    public PublicInvestmentBudgetRevenue(String code, String description, String category, String type, long nationalAmount, long coFundedAmount, long amount) {
        super(code, description, category, amount);
        this.type = type;
        this.nationalAmount = nationalAmount;
        this.coFundedAmount = coFundedAmount;
        publicInvestmentBudgetRevenuesFiltered.add(this);
    }

    //Class Methods

    //Creation of PublicInvestmentBudgetRevenuesSorted
    public static void sortPublicInvestmentBudgetRevenuesByCode() {
        Collections.sort(publicInvestmentBudgetRevenuesFiltered, new Comparator<PublicInvestmentBudgetRevenue>() {
            @Override
            public int compare(PublicInvestmentBudgetRevenue b1, PublicInvestmentBudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetRevenues() {
        return publicInvestmentBudgetRevenues;
    }

    public static void printPublicInvestmentBudgetRevenues() {
        DataOutput.printPublicInvestmentBudgetRevenueWithAsciiTable(publicInvestmentBudgetRevenues);
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getMainPublicInvestmentBudgetRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(publicInvestmentBudgetRevenuesFiltered);
    }

    public static void printMainPublicInvestmentBudgetRevenues() {
        BudgetRevenueHandling.printMainBudgetRevenues(getMainPublicInvestmentBudgetRevenues());
    }

    public static ArrayList<PublicInvestmentBudgetRevenue> getPublicInvestmentBudgetRevenuesFiltered() {
        return publicInvestmentBudgetRevenuesFiltered;
    }

    public static void printPublicInvestmentBudgetRevenuesFiltered() {
        DataOutput.printPublicInvestmentBudgetRevenuesFilteredWithAsciiTable(getPublicInvestmentBudgetRevenuesFiltered(), BudgetRevenueHandling.calculateSum(getPublicInvestmentBudgetRevenuesFiltered()));
    }

    public static PublicInvestmentBudgetRevenue findPublicInvestmentBudgetRevenueWithCode(String code) {
        return BudgetRevenueHandling.findRevenueWithCode(code, publicInvestmentBudgetRevenuesFiltered);
    }

    //Getters & Setters

    public String getType() {
        return type;
    }

    public long getNationalAmount() {
        return nationalAmount;
    }

    public long getCoFundedAmount() {
        return coFundedAmount;
    }

    private void setNationalAmount(long nationalAmount) {
        this.nationalAmount = nationalAmount;
    }

    private void setCoFundedAmount(long coFundedAmount) {
        this.coFundedAmount = coFundedAmount;
    }

    @Override
    public String toString() {
        return "Code: " + getCode() + ", Description: " + getDescription() + ", Category: " + getCategory() + ", Type: " + type + ", Amount: " + String.format("%,d", getAmount());
    }
}