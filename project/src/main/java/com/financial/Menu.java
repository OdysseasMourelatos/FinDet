import java.util.Scanner;

public class Menu{
    public static void printmenu(){
        Scanner input = new Scanner(System.in); //Δημιουργία αντικειμένου της κλάσης Scanner 
        System.out.println("ΚΑΛΩΣ ΗΡΘΑΤΕ ΣΤΗΝ ΕΦΑΡΜΟΓΗ ΕΠΕΞΕΡΓΑΣΙΑΣ ΤΟΥ ΚΡΑΤΙΚΟΥ ΠΡΟΥΠΟΛΟΓΙΣΜΟΥ");
        do{
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ ΕΝΑΝ ΑΡΙΘΜΟ ΑΠΟ ΤΟ 1(ΕΝΑ) ΕΩΣ ΤΟ 6(ΕΞΙ) ΓΙΑ ΕΜΦΑΝΙΣΗ ΤΗΣ ΑΝΤΙΣΤΟΙΧΗΣ ΕΝΕΡΓΕΙΑΣ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 1 ΓΙΑ ΕΙΣΑΓΩΓΗ ΔΕΔΟΜΕΝΩΝ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 2 ΓΙΑ ΠΡΟΕΠΙΣΚΟΠΗΣΗ ΤΟΥ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 3 ΓΙΑ ΑΛΛΑΓΗ ΚΟΝΔΥΛΙΟΥ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 4 ΓΙΑ ΠΑΡΟΧΗ ΟΙΚΟΝΟΜΙΚΗΣ ΑΝΑΛΥΣΗΣ ΚΑΙ ΓΙΑ ΔΙΑΓΡΑΜΜΑΤΙΚΗ ΑΠΕΙΚΟΝΙΣΗ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 5 ΓΙΑ ΠΛΗΡΗΣ ΟΙΚΟΝΟΜΙΚΗ ΑΝΑΛΥΣΗ");
            System.out.println("ΠΛΗΚΤΡΟΛΟΓΙΣΤΕ 6 ΓΙΑ ΕΞΟΔΟ ΑΠΟ ΤΗΝ ΕΦΑΡΜΟΓΗ");
            int choise = nextInt(); //Εισαγωγή αριθμού από τον χρήστη
            input.nextLine()
            if(choise == 1){
                System.out.println("ΠΑΡΑΚΑΛΩ ΕΙΣΑΓΕΤΕ CSV PATH");
                String csv_path = input.nextLine();
            } else if(choise==2){

            } else if(choise==3){
                System.out.println("ΕΙΣΑΓΕΤΕ ΤΟΝ ΚΩΔΙΚΟ ΑΠΟ ΤΟ ΚΟΝΔΥΛΙΟ");
                String code = input.nextLine();


            } else if(choise == 4){

            } else if(choise == 5){

            } else if(choise == 6){
                System.out.println("ΠΡΑΓΜΑΤΟΠΟΙΕΙΤΑΙ ΕΞΟΔΟΣ ΑΠΟ ΤΗΝ ΕΦΑΡΜΟΓΗ ΚΡΑΤΙΚΟΥ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ");
                break;
            } else{
                System.out.println("ΕΧΕΤΕ ΕΙΣΑΓΕΙ ΑΡΙΘΜΟ ΕΚΤΟΣ ΤΩΝ ΕΠΙΤΡΕΠΟΜΕΝΩΝ ΟΡΙΩΝ. "); //choise<1 ή choise>6
                continue;
            }

            

        }



    }
}
