# FinDet 


## 1. Build & Run Instructions

The application uses **Maven** for dependency management and for building the executable artifact.

### Prerequisites

* **Java 17** or newer
* **Apache Maven** installed on the system

---

### Build Steps

From the project root directory (where the `pom.xml` is located), run:

```bash
mvn clean package
```

This command will generate the file
`FinDet-1.0-SNAPSHOT.jar`
inside the `target/` directory.

---

### Execution Methods

#### A. Run via Maven (Recommended)

This is the safest execution method, as Maven ensures the correct loading of the **JavaFX modules**:

```bash
mvn javafx:run
```

---

#### B. Run via Executable JAR

If the executable (**Fat JAR**) has been successfully created, the application can be run directly using:

```bash
java -jar target/FinDet-1.0-SNAPSHOT.jar
```

---

### Note

If the following error occurs:

```text
JavaFX runtime components are missing
```

please prefer running the application via **Maven** (Method A).

---

## 2. Overview (What the Application Does)

**FinDet** is a specialized application for managing and analyzing the **Greek State Budget**.
It allows users to:

* import financial data,
* navigate through the revenue and expenditure hierarchy,
* apply budget modification scenarios,
* visualize results through statistical and multi-year analysis.

---

## 2.1 Functional Features (What It Offers)

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

## 2.2 Technical Implementation (How It Is Implemented)

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

