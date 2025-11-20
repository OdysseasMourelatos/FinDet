import java.util.Scanner;
public class Menu {
    public static void printmenu() {
        Scanner input = new Scanner(System.in); //Δημιουργία αντικειμένου της κλάσης Scanner 
        System.out.println("ΠΡΟΘΥΠΟΥΡΓΟΣ ΓΙΑ ΜΙΑ ΗΜΕΡΑ - BUDGET MANAGER 2026");
        do {
            System.out.println("=== ΚΥΡΙΟ ΜΕΝΟΥ ===");
            System.out.println();
            System.out.println("[1] Φόρτωση / Ενημέρωση CSV");
            System.out.println("[2] Προβολή Προϋπολογισμού");
            System.out.println("[3] Εισαγωγή Αλλαγών");
            System.out.println("[4] Έλεγχος Περιορισμών");
            System.out.println("[5] Οικονομική Ανάλυση");
            System.out.println("[6] Γραφήματα & Οπτικοποιήσεις");
            System.out.println("[7] Export PDF / CSV");
            System.out.println("[0] Έξοδος");
            System.out.println();
            System.out.print("Επιλογή: ");
            int choise = nextInt(); //Εισαγωγή αριθμού από τον χρήστη
            input.nextLine()
            if(choise == 1) {
                System.out.println("=== ΦΟΡΤΩΣΗ / ΕΝΗΜΕΡΩΣΗ CSV ===");
                System.out.println();
                System.out.println("[1] Φόρτωση αρχείων Εσόδων");
                System.out.println("[2] Φόρτωση αρχείων Εξόδων");
                System.out.println("[3] Φόρτωση αντιστοίχισης κωδικών");
                System.out.println("[4] Επαναφόρτωση όλων των CSV");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise==2) {
                System.out.println("=== ΠΡΟΒΟΛΗ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ===");
                System.out.println();
                System.out.println("[1] Προβολή συνολικών Εσόδων");
                System.out.println("[2] Προβολή συνολικών Εξόδων");
                System.out.println("[3] Προβολή ανά Υπουργείο");
                System.out.println("[4] Προβολή ανά Κωδικό");
                System.out.println("[5] Αναζήτηση στοιχείων");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");

            } else if(choise==3) {
                System.out.println("=== ΕΙΣΑΓΩΓΗ ΑΛΛΑΓΩΝ ===");
                System.out.println();
                System.out.println("[1] Προσθήκη νέας γραμμής Εσόδων");
                System.out.println("[2] Προσθήκη νέας γραμμής Εξόδων");
                System.out.println("[3] Τροποποίηση υπάρχουσας γραμμής");
                System.out.println("[4] Διαγραφή γραμμής");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise == 4) {
                System.out.println("=== ΕΛΕΓΧΟΣ ΠΕΡΙΟΡΙΣΜΩΝ ===");
                System.out.println();
                System.out.println("[1] Έλεγχος ισοσκέλισης προϋπολογισμού");
                System.out.println("[2] Έλεγχος υπέρβασης εξόδων");
                System.out.println("[3] Έλεγχος μηδενικών ή ελλιπών τιμών");
                System.out.println("[4] Έλεγχος αντιστοίχισης κωδικών");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise == 5) {
                System.out.println("=== ΟΙΚΟΝΟΜΙΚΗ ΑΝΑΛΥΣΗ ===");
                System.out.println();
                System.out.println("[1] Ανάλυση Εσόδων");
                System.out.println("[2] Ανάλυση Εξόδων");
                System.out.println("[3] Μεταβολές σε σχέση με προηγούμενο έτος");
                System.out.println("[4] Αναλογίες ανά Υπουργείο");
                System.out.println("[5] Δείκτες οικονομικής απόδοσης");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise == 6) {
                System.out.println("=== ΓΡΑΦΗΜΑΤΑ & ΟΠΤΙΚΟΠΟΙΗΣΕΙΣ ===");
                System.out.println();
                System.out.println("[1] Γράφημα Εσόδων");
                System.out.println("[2] Γράφημα Εξόδων");
                System.out.println("[3] Γράφημα ανά Υπουργείο");
                System.out.println("[4] Σύγκριση Εσόδων - Εξόδων");
                System.out.println("[5] Custom γραφήματα");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise == 7) {
                System.out.println("=== EXPORT PDF / CSV ===");
                System.out.println();
                System.out.println("[1] Export Εσόδων σε PDF");
                System.out.println("[2] Export Εξόδων σε PDF");
                System.out.println("[3] Export όλων των δεδομένων σε CSV");
                System.out.println("[4] Export ανάλυσης σε PDF");
                System.out.println("[0] Επιστροφή στο Κύριο Μενού");
                System.out.println();
                System.out.print("Επιλογή: ");
            } else if(choise == 0) {
                System.out.println("ΠΡΑΓΜΑΤΟΠΟΙΕΙΤΑΙ ΕΞΟΔΟΣ ΑΠΟ ΤΟ ΣΥΣΤΗΜΑ...");
            }
        }
    }
}
