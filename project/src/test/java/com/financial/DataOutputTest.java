package com.financial;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
}
