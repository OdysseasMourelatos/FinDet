# FinDet 

## 1. Overview (What the Application Does)

**FinDet** is a specialized application for managing and analyzing the **Greek State Budget**.
It allows users to:

* import financial data,
* navigate through the revenue and expenditure hierarchy,
* apply budget modification scenarios,
* visualize results through statistical and multi-year analysis.

---

## 2. Functional Features (What It Offers)

### Data Import & Processing

* Reading and editing data from **CSV files**
* Supports:

  * Regular State Budget
  * Public Investment Program (PIP)

### Hierarchical Exploration

* Navigation through the revenue hierarchy
* Expense search by:

  * Entity
  * Service
  * Account Code

### Bulk Modification System

* Apply changes using:

  * equal distribution of amounts across subcategories
  * percentage-based adjustments
* **Undo functionality** via complete change history tracking

### Advanced Analytics

* Dynamic chart generation:

  * Pie Charts
  * Bar Charts
  * Line Charts
* Statistical analysis including:

  * outlier detection
* Multi-year trend analysis:

  * comparison of financial figures across fiscal years

### Report Export

* Export reports to **PDF**
* Write processed data into **SQL tables**

---

## 3. Technical Implementation (How It Is Implemented)

### Object-Oriented Design (OOP)

#### Inheritance

* Abstract base class:

  * `BudgetEntry`
* Derived classes:

  * `BudgetRevenue` (Revenues)
  * `BudgetExpense` (Expenses)

#### Interfaces

* `EntityLogic`
* `BudgetRevenueLogic`
* `BudgetRevenueChanges`

➡ Clear separation of business logic from data management

#### Aggregation

* The `Entity` class acts as a container for:

  * Regular Expenses
  * National PIP Expenses
  * Co-financed PIP Expenses

---

### Design Patterns

#### Strategy Pattern

* Flexible application of budget adjustment algorithms:

  * Percentage-based operations
  * Fixed amount operations

---

### Technologies & Libraries

* **JavaFX** – Interactive and modern user interface
* **OpenCSV** – Robust processing of complex CSV files
* **Apache Commons Math** – Statistical analysis and indicators
* **iTextPDF** – Automated PDF report generation

---

### Quality Assurance

* **JUnit**

  * Unit testing
  * Overall test coverage: **87%**
* **Checkstyle**

  * Code quality enforcement and style consistency

---

