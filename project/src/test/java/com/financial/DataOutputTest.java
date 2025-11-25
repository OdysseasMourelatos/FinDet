package com.financial;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataOutputTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        BudgetRevenue.getAllBudgetRevenues().clear();
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    protected String getOutput() {
        return outContent.toString();
    }

    @Test
    void testPrintList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");

        DataOutput.printList(list);

        String output = getOutput();
        assertTrue(output.contains("A"));
        assertTrue(output.contains("B"));
    }

    @Test
    void testPrintWithAsciiTableMultipleRows() {
        BudgetRevenue br1 = new BudgetRevenue("11", "Revenue A", "ΕΣΟΔΑ", 500);
        BudgetRevenue br2 = new BudgetRevenue("12", "Revenue B", "ΕΣΟΔΑ", 3500);

        ArrayList<BudgetRevenue> list = new ArrayList<>();
        list.add(br1);
        list.add(br2);

        DataOutput.printWithAsciiTable(list);

        String output = getOutput();

        // Validate contents
        assertTrue(output.contains("Revenue A"));
        assertTrue(output.contains("Revenue B"));

        // Check numeric formatting with commas
        assertTrue(output.contains("3,500"));
    }

    @Test
    void testPrintWithAsciiTableEmptyList() {
        ArrayList<BudgetRevenue> list = new ArrayList<>();

        DataOutput.printWithAsciiTable(list);

        String output = getOutput();

        // Should contain header but no data rows except closing rule
        assertTrue(output.contains("Κωδικός Ταξινόμησης"));
        assertFalse(output.contains("ΕΣΟΔΑ"));  // no entries
    }

    @Test
    void testGreekCharactersOutput() {
        BudgetRevenue br = new BudgetRevenue("11", "Έσοδα Δοκιμή", "ΕΣΟΔΑ", 1200);

        DataOutput.printEntryWithAsciiTable(br);

        String output = getOutput();

        assertTrue(output.contains("Έσοδα"));
        assertTrue(output.contains("ΕΣΟΔΑ"));
    }
}
