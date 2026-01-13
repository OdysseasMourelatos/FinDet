# FinDet: Financial Data & Analytics Tool

**FinDet** is a specialized, high-performance Java application designed for the visualization, hierarchical analysis, and strategic modification of the **Greek State Budget**. Built to simulate real-world fiscal scenarios, it provides a robust platform for financial officers and researchers to explore revenue/expense structures and perform complex, safe budget adjustments.

---

## 1. Build & Run Instructions

The application uses **Maven** for dependency management and for building the executable artifact.

### Prerequisites

* **Java 21** or newer (utilizing modern features like Enhanced Switch)
* **Apache Maven** installed on your system

### Build Steps

From the project root directory (where the `pom.xml` is located), run:

```bash
mvn clean package

```

This command generates the `FinDet-1.0-SNAPSHOT.jar` inside the `target/` directory.

### Execution Methods

#### A. Run via Maven (Recommended)

This method ensures the correct loading of the **JavaFX modules**:

```bash
mvn javafx:run

```

#### B. Run via Executable JAR

If the executable (**Fat JAR**) has been successfully created:

```bash
java -jar target/FinDet-1.0-SNAPSHOT.jar

```

---

## 2. Comprehensive User Guide

### 2.1 Startup & Data Loading

Upon launching **FinDet**, you are presented with a crucial choice:

* **Load from CSV:** Import fresh data from the original fiscal CSV files.
* **Load from Database:** Resume from the last saved state in the **SQLite** database, preserving all previous modifications.

The **Home Screen** greets you with an overview of the budget's current state, featuring professional CSS styling and high-level summaries.

### 2.2 Navigation & Exploration

* **Revenues Menu:** Navigate through all budget types (**State, Regular, PIB, National PIB, and Co-funded PIB**).
* *Filtering:* Filter by account depth (e.g., 2-digit roots, 3-digit categories, etc.).
* *Search:* Search any account by code or description.


* **Expenses Menu:** Detailed analytical and consolidated views.
* *Filtering:* Search by Ministry code, description, or specific Entity.


* **Entities (Ministries) Menu:** View aggregated data for all ministries. Clicking **"More"** on a specific Ministry opens a submenu showing:
1. Credits by **Major Expense Category**.
2. Credits by **Special Entity**.
3. Combined view (Category & Special Entity).


* **Explorer Menu:** Visualize the revenue hierarchy clearly.
* *Example:* Searching for code `111` displays the parent category `11` (Taxes) and its children, such as `11101` (VAT).



### 2.3 The Fiscal Adjustment Engine (Changes)

This is the core of the application, designed to simulate real-world fiscal scenarios with high integrity.

#### General Rules

* Changes are permitted only for **Regular Budget, National PIB, and Co-funded PIB**.
* Impact can be monitored at the **State Budget** level.
* **Safety Mechanism (Rollback):** If any modification results in a **negative balance** for any account in the hierarchy, the entire transaction is aborted, a warning message is displayed, and the accounts revert to their previous state.

#### Revenue Adjustments

Controlled via 5 primary filters:

1. **Budget Type** (Regular, National, Co-funded).
2. **Account Code**.
3. **Change Value** (Fixed amount or Percentage).
4. **Change Type** (Adjustment, Percentage, or Target Balance).
5. **Distribution:** **Percentage Adjustment** or **Equal Distribution** to subcategories.

#### Expense Adjustments

Simulates granular budget allocations:

* **Scope:** Global (all entities), per Entity, or per Service.
* **Category:** Focus on a specific category (e.g., "Employee Benefits") or apply to all.
* **Calculation Mode:** * *Horizontal:* Every account in the scope is modified by the set value.
* *Distributed:* The total sum is modified, and the value is apportioned across the scope's categories.



### 2.4 History & Persistence

* **Undo System:** Multiple levels of Undo are supported. Users receive notifications upon successful reversal.
* **Database Sync:** Before closing, the system detects unsaved changes and prompts the user to save the session to the **SQLite** database.

---

## 3. Visualizations & Analytics

### 3.1 Dynamic Charts

* **Overview:** Pie and Bar charts for State Budget categories and Expenses per Ministry.
* **Custom Revenue Charts:** Input a code (e.g., `11`) to see the distribution of Income Taxes, VAT, and Customs.
* **Ministry Analytics:** Break down expenses by category or by sub-entities (Special Entities).

### 3.2 Multi-Year Analysis

Compare fiscal data over time using Line and Bar charts by importing up to three additional CSV files for different years.

* **Trends:** Visualize Revenue, Expense, and Deficit/Surplus evolution.
* **Custom Tracking:** Track specific codes (e.g., code `21` for payroll) across years.
* **Smoothing:** Toggle "Normalization" to remove outliers for clearer trend analysis.

### 3.3 Statistical Analysis

* **Descriptive Stats:** Mean, Median, Skewness, Kurtosis, and Outlier detection.
* **Frequency Tables:** Histograms and polygon lines based on logarithmic scales.
* **Visuals:** Boxplots and Distribution charts for Ministry expenses.

---

## 4. Technical Implementation

### 4.1 Architecture & OOP

FinDet follows strict Object-Oriented principles:

* **Inheritance:** `BudgetEntry` (abstract) is the foundation for `BudgetRevenue` and `BudgetExpense`.
* **Interfaces:** `BudgetRevenueLogic`, `BudgetRevenueChanges`, and `EntityLogic` separate business rules from data models.
* **Aggregation:** The `Entity` class manages collections of different expense types (Regular, National PIB, Co-funded PIB).

### 4.2 Design Patterns

* **Strategy Pattern:** Used for budget adjustment algorithms (`PercentageAdjustment`, `EqualDistribution`), allowing the application to switch calculation logic at runtime.
* **Service Layer:** `BudgetRevenueLogicService` and `BudgetRevenueChangesService` handle complex recursive calculations and hierarchical propagation.

### 4.3 Technologies & Quality

* **JavaFX:** Modern, responsive UI with CSS styling.
* **OpenCSV:** Robust parsing of complex financial CSV files.
* **iTextPDF:** Professional PDF report generation for budget exports.
* **Apache Commons Math:** Advanced statistical computations.
* **JUnit & Checkstyle:** High quality-assurance with **87% test coverage**.

---

## 5. Repository Structure

```text
src/main/java/com/financial/
    entries/        # Models (BudgetEntry, Entity, Revenue/Expense subtypes)
    services/       # Recursive logic and hierarchical calculations
    ui/views/       # JavaFX views and UI controllers
    strategies/     # Implementation of the Strategy Pattern for modifications
    database/       # SQLite persistence management

src/test/java/      # Comprehensive JUnit test suite
docs/               # UML Diagrams, User Documentation, and Reports
pom.xml             # Maven configuration
README.md           # This file

```

---

*Developed as part of the Semester Project for Programming II(2025-2026).*

---
