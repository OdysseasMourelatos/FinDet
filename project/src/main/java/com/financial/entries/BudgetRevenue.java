package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;
import com.financial.services.IBudgetRevenueLogic;

import java.util.*;

public class BudgetRevenue extends BudgetEntry implements IBudgetRevenueLogic {

    //Constructors & Fields
    protected static ArrayList<BudgetRevenue> budgetRevenues = new ArrayList<>();
    protected static ArrayList <BudgetRevenue> budgetRevenuesFiltered = new ArrayList<>();

    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
        budgetRevenues.add(this);
    }

    private long regularAmount;
    private long publicInvestmentAmount;

    public BudgetRevenue(String code, String description, String category, long regularAmount, long publicInvestmentAmount, long amount) {
        super(code, description, category, amount);
        this.regularAmount = regularAmount;
        this.publicInvestmentAmount = publicInvestmentAmount;
        budgetRevenuesFiltered.add(this);
    }

    public void addBudgetRevenueToArrayList() {
        budgetRevenuesFiltered.add(this);
    }

    public static void sortBudgetRevenuesByCode() {
        Collections.sort(budgetRevenuesFiltered, new Comparator<BudgetRevenue>() {
            @Override
            public int compare(BudgetRevenue b1, BudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    //Class Methods

    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenuesFiltered;
    }

    public static void printAllBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(budgetRevenuesFiltered, BudgetRevenueHandling.calculateSum(budgetRevenuesFiltered));
    }

    public static ArrayList<BudgetRevenue> getMainBudgetRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(getAllBudgetRevenues());
    }

    public static void printMainBudgetRevenues() {
        DataOutput.printBudgetRevenuesWithAsciiTable(getMainBudgetRevenues(), BudgetRevenueHandling.calculateSum(getMainBudgetRevenues()));
    }

    public static ArrayList<BudgetRevenue> getBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueHandling.getRevenuesStartingWithCode(code, budgetRevenuesFiltered);
    }

    public static void printBudgetRevenuesStartingWithCode(String code) {
        DataOutput.printRevenueWithAsciiTable(getBudgetRevenuesStartingWithCode(code), 0);
    }

    //Method that all subclasses easily inherit

    public int getLevelOfHierarchy() {
        return switch (getCode().length()) {
            case 2 -> 1;   // "11" - top level
            case 3 -> 2;   // "111" - second level
            case 5 -> 3;   // "11101" - third level
            case 7 -> 4;   // "1110103" - fourth level
            case 10 -> 5;  // "1110103001" - fifth level
            default -> 0;  // unknown
        };
    }

    //*Implementation of methods*

    //Sum Method
    @Override
    public long calculateSum() {
        return BudgetRevenueHandling.calculateSum(budgetRevenuesFiltered);
    }
    //Supercategories methods

    @Override
    public BudgetRevenue findSuperCategory() {
        return BudgetRevenueHandling.findSuperCategory(this, budgetRevenuesFiltered);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueHandling.getSuperCategories(this, budgetRevenuesFiltered);
    }

    @Override
    public void printSuperCategoriesTopDown() {
        ArrayList<BudgetRevenue> superCategories = new ArrayList<>();
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            for (int i = getSuperCategories().size() - 1; i >= 0; i--) {
                superCategories.add(getSuperCategories().get(i));
            }
            DataOutput.printRevenueWithAsciiTable(superCategories, 0);
        }
    }

    @Override
    public void printSuperCategoriesBottomsUp() {
        if (getSuperCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν κατηγορίες σε υψηλότερη ιεραρχία");
        } else {
            DataOutput.printRevenueWithAsciiTable(getSuperCategories(), 0);
        }
    }

    //Subcategories methods

    @Override
    public ArrayList<BudgetRevenue> findAllSubCategories() {
        return BudgetRevenueHandling.findAllSubCategories(this, budgetRevenuesFiltered);
    }

    @Override
    public void printAllSubCategories() {
        DataOutput.printBudgetRevenuesWithAsciiTable(findAllSubCategories(),0);
    }

    @Override
    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        return BudgetRevenueHandling.findNextLevelSubCategories(this, budgetRevenuesFiltered);
    }

    @Override
    public void printNextLevelSubCategories() {
        DataOutput.printRevenueWithAsciiTable(findNextLevelSubCategories(), 0);
    }

    //*Change methods (soon to be moved explicitly to subclasses)*

    public void setAmountOfSuperCategories(long change) {
        ArrayList<BudgetRevenue> superCategories = getSuperCategories();

        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    public void setAmountOfNextLevelSubCategoriesWithEqualDistribution(long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        if (!nextLevelSubCategories.isEmpty()) {
            long changeOfCategory = change / nextLevelSubCategories.size();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount(subCategory.getAmount() + changeOfCategory);
            }
        }
    }

    public void setAmountOfAllSubCategoriesWithEqualDistribution(long change) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        long changeOfSubCategory = change;
        try {
            //Βάζω τα αρχικά ποσά κάθε επόμενης κατηγορίας σε Map
            Map<String, Long> map = new HashMap<>();
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                map.put(subCategory.getCode(), subCategory.getAmount());
            }
            //Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας
            this.setAmountOfNextLevelSubCategoriesWithEqualDistribution(changeOfSubCategory);
            //Αν δεν υπάρχουν άλλες υποκατηγορίες τερματίζει
            if (this.getCode().length() == 10) {
                throw new RuntimeException();
            }
            //Για κάθε υποκατηγορία σε επόμενο επίπεδο
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                changeOfSubCategory = (subCategory.getAmount() - map.get(subCategory.getCode()));
                subCategory.setAmountOfAllSubCategoriesWithEqualDistribution(changeOfSubCategory);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public void setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        if (!nextLevelSubCategories.isEmpty()) {
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmount((long) (subCategory.getAmount() * (1 + percentage)));
            }
        }
    }

    public void setAmountOfAllSubCategoriesWithPercentageAdjustment(double percentage) {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        try {
            // Ανανεώνω τα ποσά της κάθε επόμενης κατηγορίας
            this.setAmountOfNextLevelSubCategoriesWithPercentageAdjustment(percentage);
            // Αν δεν υπάρχουν άλλες υποκατηγορίες τερματίζει
            if (this.getCode().length() == 10) {
                throw new RuntimeException();
            }
            // Για κάθε υποκατηγορία σε επόμενο επίπεδο
            for (BudgetRevenue subCategory : nextLevelSubCategories) {
                subCategory.setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
            }
        } catch (RuntimeException e) {
            return;
        }
    }

    public void implementChangesOfEqualDistribution(long change) {
        setAmountOfSuperCategories(change);
        setAmountOfAllSubCategoriesWithEqualDistribution(change);
        setAmount(getAmount() + change);
    }

    public void implementChangesOfPercentageAdjustment(double percentage) {
        setAmountOfSuperCategories((long) (getAmount() * (percentage)));
        setAmountOfAllSubCategoriesWithPercentageAdjustment(percentage);
        setAmount((long) (getAmount() * (1 + percentage)));
    }

    @Override
    public String toString () {
        return super.toString();
    }
}
