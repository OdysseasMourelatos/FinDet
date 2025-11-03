import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DataInput {

    public static void csvReader(){
        Scanner s = new Scanner(System.in);
        System.out.print("Παρακαλούμε εισάγετε το file path του csv αρχείου: ");
        String filePath = s.nextLine();
        String line;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
