<<<<<<< HEAD
//package com.financial;

public class Main {
    public static void main(String[] args) {

   
        String csvFilePath = "budgetdata.csv"; 
        DataInput.advancedCSVReader(csvFilePath);

        
        String outputFile = "BudgetRevenue.pdf";
        BudgetRevenueConvertToPdf.createPdf(outputFile);

        System.out.println("PDF created " + outputFile);
    }
}

=======
package com.financial;

import com.financial.menu.Menu;

public class Main {
    public static void main(String[] args) {
        Menu.printMainMenu();
    }
}
>>>>>>> main
