public class Main {
    public static void main (String[] args) {
        DataInput.csvReader();
        String formatted = String.format("%,d", Income.calculateSum());
        System.out.println("ΕΣΟΔΑ" + formatted);
    }
}