package com.financial;

import java.util.ArrayList;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;

public class DataOutput {
    public static void printList (ArrayList<?> arrayList){
        for (Object object : arrayList){
            System.out.println(object);
        }
    }

    public static void printWithAsciiTable(ArrayList<? extends BudgetEntry> entries) {
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

        for (BudgetEntry entry : entries) {  // Εδώ μπορούμε να χρησιμοποιήσουμε BudgetEntry
            at.addRow(
                    entry.getCode(),
                    entry.getDescription(),
                    entry.getCategory(),
                    String.format("%,d", entry.getAmount())
            );
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
}
}

