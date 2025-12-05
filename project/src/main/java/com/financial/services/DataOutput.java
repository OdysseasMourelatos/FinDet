package com.financial.services;

import java.util.ArrayList;

import com.financial.entries.BudgetRevenue;
import com.financial.entries.Entity;
import com.financial.entries.PublicInvestmentBudgetExpense;
import com.financial.entries.PublicInvestmentBudgetRevenue;
import com.financial.entries.BudgetEntry;
import com.financial.entries.BudgetExpense;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;

public class DataOutput {
    public static void printList (ArrayList<?> arrayList) {
        for (Object object : arrayList) {
            System.out.println(object);
        }
    }

    public static void printRevenueWithAsciiTable(ArrayList<? extends BudgetRevenue> entries, long sum) {
        AsciiTable at = new AsciiTable();

        at.getRenderer().setCWC(new CWC_FixedWidth().add(20).add(60).add(15).add(20));

        at.addRule();
        at.addRow("Κωδικός Ταξινόμησης", "Ονομασία", "Κατηγορία", "Ποσό");
        at.addRule();

        for (BudgetEntry entry : entries) {
            at.addRow(
                    entry.getCode(),
                    entry.getDescription(),
                    entry.getCategory(),
                    String.format("%,d", entry.getAmount())
            );
            at.addRule();
        }
        if (sum != 0) {
            at.addRow("", "", "", String.format("%,d", sum));
            at.addRule();
        }

        System.out.println(at.render());
    }

    public static <T extends BudgetEntry> void printEntryWithAsciiTable(T entry) {
        AsciiTable at = new AsciiTable();

        at.getRenderer().setCWC(new CWC_FixedWidth().add(20).add(60).add(15).add(20));

        at.addRule();
        at.addRow("Κωδικός Ταξινόμησης", "Ονομασία", "Κατηγορία", "Ποσό");
        at.addRule();

        at.addRow(
                entry.getCode(),
                entry.getDescription(),
                entry.getCategory(),
                String.format("%,d", entry.getAmount())
        );
        at.addRule();

        System.out.println(at.render());
    }

    public static void printEntitiesWithAsciiTable(String budgetType) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(20).add(70).add(20));
        at.addRule();
        at.addRow("Κωδικός Φορέα", "Ονομασία", "Ποσό");
        at.addRule();
        long sum = 0;
        for (Entity entity : Entity.getEntities()) {
            at.addRow(
                    entity.getEntityCode(),
                    entity.getEntityName(),
                    String.format("%,d", entity.getSum(budgetType))
            );
            at.addRule();
            sum += entity.getSum(budgetType);
        }

        at.addRow("", "", String.format("%,d", sum));
        at.addRule();
        System.out.println(at.render());
    }

    public static void printEntityWithAsciiTable(Entity entity, String budgetType) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(125));
        at.addRule();
        at.addRow(entity.getEntityName());
        at.addRule();
        System.out.println(at.render());

        if (budgetType.equals("ΚΡΑΤΙΚΟΥ")) {
            //printBudgetExpenseWithAsciiTable(entity.getAllBudgetExpenses()); // Αν υλοποιηθεί
        } else if (budgetType.equals("ΤΑΚΤΙΚΟΥ")) {
            printBudgetExpenseWithAsciiTable(entity.regularBudgetExpenses);
        } else if (budgetType.equals("ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ")) {
            printPublicInvestmentBudgetExpenseWithAsciiTable(entity.publicInvestmentBudgetExpenses);
        }
    }

    public static void printExpenseWithAsciiTable(ArrayList<? extends BudgetExpense> expenses) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(20).add(40).add(20));

        at.addRule();
        at.addRow("Μείζονα Κατηγορία", "Ονομασία", "Ποσό");
        at.addRule();

        long sum = 0;

        for (BudgetExpense expense : expenses) {
            at.addRow(
                    expense.getCode(),
                    expense.getDescription(),
                    String.format("%,d", expense.getAmount())
            );
            at.addRule();
            sum += expense.getAmount();
        }

        at.addRow("", "", String.format("%,d", sum));
        at.addRule();
        System.out.println(at.render());
    }

    public static void printBudgetExpenseWithAsciiTable(ArrayList<? extends BudgetExpense> expenses) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(10).add(61).add(10).add(25).add(15));

        at.addRule();
        at.addRow("Κωδικός Ειδικού Φορέα", "Ονομασία Ειδικού Φορέα", "Κωδικός Δαπάνης", "Ονομασία Δαπάνης", "Ποσό");
        at.addRule();
        long sum = 0;

        for (BudgetExpense expense : expenses) {
            at.addRow(
                    expense.getServiceCode(),
                    expense.getServiceName(),
                    expense.getCode(),
                    expense.getDescription(),
                    String.format("%,d", expense.getAmount())
            );
            at.addRule();
            sum += expense.getAmount();
        }

        at.addRow("", "", "", "", String.format("%,d", sum));
        at.addRule();
        System.out.println(at.render());
    }

    public static void printPublicInvestmentBudgetExpenseWithAsciiTable(ArrayList<? extends PublicInvestmentBudgetExpense> expenses) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(10).add(40).add(10).add(25).add(20).add(15));

        at.addRule();
        at.addRow("Κωδικός Ειδικού Φορέα", "Ονομασία Ειδικού Φορέα", "Κωδικός Δαπάνης", "Ονομασία Δαπάνης",  "Σκέλος", "Ποσό");
        at.addRule();
        long sum = 0;

        for (PublicInvestmentBudgetExpense expense : expenses) {
            at.addRow(
                    expense.getServiceCode(),
                    expense.getServiceName(),
                    expense.getCode(),
                    expense.getDescription(),
                    expense.getType(),
                    String.format("%,d", expense.getAmount())
            );
            sum += expense.getAmount();
            at.addRule();
        }
        at.addRow("", "", "", "", "", String.format("%,d", sum));
        at.addRule();
        System.out.println(at.render());
    }

    public static void printPublicInvestmentBudgetRevenueWithAsciiTable(ArrayList<? extends PublicInvestmentBudgetRevenue> revenues) {
        AsciiTable at = new AsciiTable();
        at.getRenderer().setCWC(new CWC_FixedWidth().add(20).add(40).add(20).add(15).add(20));

        at.addRule();
        at.addRow("Κωδικός Ταξινόμησης", "Ονομασία",  "Σκέλος", "Κατηγορία", "Ποσό");
        at.addRule();
        long sum = 0;

        for (PublicInvestmentBudgetRevenue revenue : revenues) {
            at.addRow(
                    revenue.getCode(),
                    revenue.getDescription(),
                    revenue.getType(),
                    revenue.getCategory(),
                    String.format("%,d", revenue.getAmount())
            );
            if (revenue.getCode().length() == 2) { // Logic to sum only main categories
                sum += revenue.getAmount();
            }
            at.addRule();
        }
        at.addRow("", "", "", "", String.format("%,d", sum));
        at.addRule();
        System.out.println(at.render());
    }

}

