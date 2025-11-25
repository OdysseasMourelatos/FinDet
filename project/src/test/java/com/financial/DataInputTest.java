package com.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;


public class DataInputTest {

    @BeforeEach
    void resetState() {
        BudgetRevenue.getAllBudgetRevenues().clear();
    }

    @Test
    void testCreateBudgetRevenueFromCSV() throws Exception {
        String[] csvLine = {"11", "Main Revenue", "1000"};

        Method m = DataInput.class.getDeclaredMethod("createBudgetRevenueFromCSV", String[].class);
        m.setAccessible(true);
        m.invoke(null, (Object) csvLine);

        assertEquals(1, BudgetRevenue.getAllBudgetRevenues().size());
        BudgetRevenue rev = BudgetRevenue.getAllBudgetRevenues().get(0);

        assertEquals("11", rev.getCode());
        assertEquals("Main Revenue", rev.getDescription());
        assertEquals("ΕΣΟΔΑ", rev.getCategory());
        assertEquals(1000, rev.getAmount());
    }

    @Test
    void testSimpleCSVReader(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("test.csv");
        Files.write(csv, (
                "code,description,amount\n" +
                        "11,RevenueA,500\n" +
                        "12,RevenueB,300\n"
        ).getBytes());

        DataInput.simpleCSVReader(csv.toString());

        assertEquals(2, BudgetRevenue.getAllBudgetRevenues().size());
    }

    @Test
    void testAdvancedCSVReader(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("test2.csv");
        Files.write(csv, (
                "header1,header2,header3\n" +
                        "11,RevenueA,1000\n" +                   // BudgetRevenue
                        "001,EntityName\n" +                   // Entity
                        "1111,200,DescA,500\n" +               // RegularBudgetExpense
                        "2201,DescB,TYPE,300\n" +              // PublicInvestmentBudgetRevenue
                        "2201,330,DescC,TYPE,700\n"            // PublicInvestmentBudgetExpense
        ).getBytes());

        DataInput.advancedCSVReader(csv.toString());

        assertEquals(1, BudgetRevenue.getAllBudgetRevenues().size());
    }

}
