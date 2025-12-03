package com.financial;

import java.util.ArrayList;
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

        at.getRenderer().setCWC(new CWC_FixedWidth()
                .add(20)
                .add(60)
                .add(15)
                .add(20)
        );

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
        if (sum != 0){
            at.addRow("", "", "", String.format("%,d", sum));
            at.addRule();
        }

        System.out.println(at.render());
    }

    public static <T extends BudgetEntry> void printEntryWithAsciiTable(T entry) {
        AsciiTable at = new AsciiTable();

        at.getRenderer().setCWC(new CWC_FixedWidth()
                .add(20)
                .add(60)
                .add(15)
                .add(20)
        );

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
        at.getRenderer().setCWC(new CWC_FixedWidth()
                .add(20)
                .add(70)
                .add(20)
        );
        at.addRule();
        at.addRow("Κωδικός Φορέα", "Ονομασία", "Ποσό");
        at.addRule();
        long sum = 0;
        for (Entity entity : Entity.entities) {
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
        at.getRenderer().setCWC(new CWC_FixedWidth()
                .add(125)
        );
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

}

