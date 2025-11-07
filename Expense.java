import java.util.ArrayList;

public class Expense extends BudgetEntry{

    private static ArrayList <Long> totalExpenses = new ArrayList<>();

    public Expense(int code, String description, String category, long amount) {
        super(code, description, category, amount);
        totalExpenses.add(amount);
    }

    public static long calculateSum(){
        long sum = 0;
        for (long expense : totalExpenses) {
            sum += expense;
        }
        return sum;
    }
}
