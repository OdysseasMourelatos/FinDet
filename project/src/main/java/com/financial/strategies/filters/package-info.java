/**
 * Defines filtering strategies for selecting specific {@link com.financial.entries.BudgetExpense} objects.
 * <p>
 * This package employs the Strategy design pattern to decouple the criteria for selecting budget
 * records from the actual financial modification logic. It supports:
 * <ul>
 * <li><b>Targeted Filtering:</b> Selection by specific account or service codes.</li>
 * <li><b>Broad Filtering:</b> Universal selection for global or entity-wide budget adjustments.</li>
 * </ul>
 */
package com.financial.strategies.filters;