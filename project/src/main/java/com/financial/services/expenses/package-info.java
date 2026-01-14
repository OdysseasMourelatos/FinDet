/**
 * Provides specialized services and logic for managing budget expenses at the entity and service levels.
 * <p>
 * This package handles the "heavy lifting" of the expenditure side, including:
 * <ul>
 * <li><b>Hierarchical Logic:</b> Navigating and aggregating data from Services up to Entities via {@code EntityLogicService}.</li>
 * <li><b>State Management:</b> Tracking modification history and providing undo capabilities through {@code ExpensesHistory}.</li>
 * <li><b>Strategic Adjustments:</b> Executing complex budget changes using the Strategy design pattern via {@code BudgetExpenseChangesService}.</li>
 * <li><b>Abstraction:</b> Defining the core financial contracts for organizational units through the {@code EntityLogic} interface.</li>
 * </ul>
 */
package com.financial.services.expenses;