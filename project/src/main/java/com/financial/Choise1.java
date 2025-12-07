public class Choise1 {
    public static void printSubMenuOfChoice1() {
        Scanner input = new Scanner(System.in);
        System.out.println(Colors.BLUE + Colors.BOLD + "=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===" + Colors.RESET);
        System.out.println();
        System.out.println(Colors.BLUE + Colors.BOLD + "[1] " + Colors.RESET + "Φόρτωση αρχείων" + Colors.RESET + Colors.RESET_2);
        System.out.println(Colors.BLUE + Colors.BOLD + "[2] " + Colors.RESET +
        "Επαναφόρτωση όλων των CSV" + Colors.RESET + Colors.RESET_2);
        System.out.println(Colors.BLUE + Colors.BOLD + "[0] " + Colors.RESET +
        "Επιστροφή στο Κύριο Μενού" + Colors.RESET + Colors.RESET_2);
        System.out.println();
        System.out.print("Επιλογή: ");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE +
            " (ΕΣΟΔΑ ΚΡΑΤΙΚΟΎ ΠΡΟΥΠΟΛΟΓΙΣΜΟΎ): " + Colors.RESET);
            String filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE +
            " (ΕΣΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE + " (ΕΞΟΔΑ ΤΑΚΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ): " + RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου" + Colors.BLUE +
            " (ΕΞΟΔΑ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ ΔΗΜΟΣΙΩΝ ΕΠΕΝΔΥΣΕΩΝ): " + Colors.RESET);
            filePath = input.nextLine();
            DataInput.advancedCSVReader(filePath);
            System.out.println(Colors.GREEN + "Η ΦΟΡΤΩΣΗ ΤΩΝ ΑΡΧΕΙΩΝ ΠΡΑΓΜΑΤΟΠΟΙΗΘΗΚΕ ΕΠΙΤΥΧΩΣ!" + Colors.RESET);
            DataInput.createEntityFromCSV();
            DataInput.createBudgetRevenueFromCSV();
        }
    }
}
