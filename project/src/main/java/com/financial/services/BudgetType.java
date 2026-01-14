package com.financial.services;

/**
 * Defines the classification of different budget sectors within the financial system.
 * <p>
 * This enumeration is used to distinguish between operational (Regular) and
 * development (Public Investment) resources, as well as to specify the
 * funding source (National vs. Co-funded) for investment projects.
 * </p>
 */
public enum BudgetType {
    /** Represents the consolidated view of all budget types. */
    GENERAL_BUDGET,

    /** Represents the standard operational budget for recurring expenses and revenues. */
    REGULAR_BUDGET,

    /** Represents the combined Public Investment Budget (ΠΔΕ). */
    PUBLIC_INVESTMENT_BUDGET,

    /** Represents the National (Εθνικό) component of the Public Investment Budget. */
    PUBLIC_INVESTMENT_BUDGET_NATIONAL,

    /** Represents the Co-funded (Συγχρηματοδοτούμενο) component of the Public Investment Budget. */
    PUBLIC_INVESTMENT_BUDGET_COFUNDED
}
