import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataInput {

    public static void csvReader(){
        Scanner s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: ");
        String filePath = s.nextLine();
        String line;
        int lineNumber=0;
        ArrayList<BudgetEntry> budgetEntries = new ArrayList<>();
        BudgetEntry budgetEntry = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }
                String[] values = line.split(",");
                int code = Integer.parseInt(values[0]);
                String description = values[1];
                String category = values[2];
                long amount = Long.parseLong(values[3]);
                if (values[2].equals("ΕΞΟΔΑ")) {
                    budgetEntry = new Expense(code, description, category, amount);
                }
                budgetEntries.add(budgetEntry);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (BudgetEntry entry : budgetEntries) {
            System.out.println(entry);
        }
    }
}
