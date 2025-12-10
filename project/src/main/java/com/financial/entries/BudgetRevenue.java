package com.financial.entries;

import com.financial.services.BudgetRevenueHandling;
import com.financial.services.DataOutput;
import com.financial.services.IBudgetRevenueLogic;

import java.util.*;

public class BudgetRevenue extends BudgetEntry implements IBudgetRevenueLogic {

    protected static ArrayList <BudgetRevenue> budgetRevenues = new ArrayList<>();

    public BudgetRevenue(String code, String description, String category, long amount) {
        super(code, description, category, amount);
    }

    public void addBudgetRevenueToArrayList() {
        budgetRevenues.add(this);
    }

    public static void sortBudgetRevenuesByCode() {
        Collections.sort(budgetRevenues, new Comparator<BudgetRevenue>() {
            @Override
            public int compare(BudgetRevenue b1, BudgetRevenue b2) {
                return b1.getCode().compareTo(b2.getCode());
            }
        });
    }

    //Class Methods

    public static ArrayList<BudgetRevenue> getAllBudgetRevenues() {
        return budgetRevenues;
    }

    public static void printAllBudgetRevenues() {
        DataOutput.printRevenueWithAsciiTable(budgetRevenues, BudgetRevenueHandling.calculateSum(budgetRevenues));
    }

    public static ArrayList<BudgetRevenue> getMainBudgetRevenues() {
        return BudgetRevenueHandling.getMainBudgetRevenues(getAllBudgetRevenues());
    }

    public static void printMainBudgetRevenues() {
        DataOutput.printBudgetRevenuesWithAsciiTable(getMainBudgetRevenues(), BudgetRevenueHandling.calculateSum(getMainBudgetRevenues()));
    }

    public static ArrayList<BudgetRevenue> getBudgetRevenuesStartingWithCode(String code) {
        return BudgetRevenueHandling.getRevenuesStartingWithCode(code, budgetRevenues);
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
        return BudgetRevenueHandling.calculateSum(budgetRevenues);
    }
    //Supercategories methods

    @Override
    public BudgetRevenue findSuperCategory() {
        return BudgetRevenueHandling.findSuperCategory(this, budgetRevenues);
    }

    @Override
    public ArrayList<BudgetRevenue> getSuperCategories() {
        return BudgetRevenueHandling.getSuperCategories(this, budgetRevenues);
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

    public void setAmountOfSuperCategories(long change) {
        ArrayList<BudgetRevenue> superCategories = getSuperCategories();

        for (BudgetRevenue superCategory : superCategories) {
            superCategory.setAmount(superCategory.getAmount() + change);
        }
    }

    public ArrayList<BudgetRevenue> findAllSubCategories() {
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().startsWith(this.getCode()) && !(budgetRevenue.equals(this))) {
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }

    public void printAllSubCategories() {
        ArrayList<BudgetRevenue> allSubCategories = findAllSubCategories();
        DataOutput.printRevenueWithAsciiTable(allSubCategories, 0);
    }

    public ArrayList<BudgetRevenue> findNextLevelSubCategories() {
        int level = getLevelOfHierarchy();
        int subCategoryCodeLength;
        switch (level) {
            case 1 -> subCategoryCodeLength = 3;
            case 2 -> subCategoryCodeLength = 5;
            case 3 -> subCategoryCodeLength = 7;
            case 4 -> subCategoryCodeLength = 10;
            default -> subCategoryCodeLength = 0;
        }
        ArrayList<BudgetRevenue> subCategories = new ArrayList<>();
        for (BudgetRevenue budgetRevenue : budgetRevenues) {
            if (budgetRevenue.getCode().startsWith(this.getCode()) && (budgetRevenue.getCode().length() == subCategoryCodeLength)) {
                subCategories.add(budgetRevenue);
            }
        }
        return subCategories;
    }

    public void printNextLevelSubCategories() {
        ArrayList<BudgetRevenue> nextLevelSubCategories = findNextLevelSubCategories();
        DataOutput.printRevenueWithAsciiTable(findNextLevelSubCategories(), 0);
    }

    //*Change methods (soon to be moved explicitly to subclasses)*

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
