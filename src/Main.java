public class Main {
    public static void main (String[] args) {
        DataInput.csvReader();
        String formatted = String.format("%,d", Expense.calculateSum());
        System.out.println("ΕΞΟΔΑ: " + formatted);
    }
}