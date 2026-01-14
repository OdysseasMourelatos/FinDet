/**
 * The central service layer of the application, orchestrating financial logic across all budget sectors.
 * <p>
 * This package serves as the integration point for:
 * <ul>
 * <li><b>Budget Classification:</b> Categorizing data through the {@link com.financial.services.BudgetType} enumeration.</li>
 * <li><b>Revenue Services:</b> (Sub-package) Logic for tracking and adjusting budget inflows.</li>
 * <li><b>Expense Services:</b> (Sub-package) Logic for managing entity expenditures and organizational hierarchies.</li>
 * </ul>
 * It decouples the raw data entries from the business rules, ensuring that financial
 * calculations remain consistent and reversible across the entire system.
 */
package com.financial.services;