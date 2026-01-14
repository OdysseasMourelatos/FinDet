# FinDet: Financial Data & Analytics Tool

**FinDet** is a specialized, high-performance Java application designed for the visualization, hierarchical analysis, and strategic modification of the Greek State Budget. Built to simulate real-world fiscal scenarios, it provides a robust platform for financial officers and researchers to explore revenue/expense structures and perform complex, safe budget adjustments.

---

## 1. Intended Use & Target Audience
The application is designed for **single-user operation**, tailored specifically for high-level decision-makers such as the **Prime Minister**, the **Minister of Finance**, or specialized **Ministry of Finance staff**.

Once an initial draft of the next year's budget is formed, the user can:
* **Import raw data** via CSV files to initialize the system.
* **Uncover hidden insights** and aggregate figures not visible to the naked eye.
* **Visualize trends** and the evolution of fiscal magnitudes over time.
* **Execute "What-if" scenarios** by performing real-time budget modifications and observing their impact on the total balance.

---

## 2. Data Specifications & Requirements

### 2.1 Mandatory Input Files
For the program to function correctly, **4 specific CSV files** must be provided in the `input` folder:
1.  **Regular Budget Revenue** (Έσοδα Τακτικού Προϋπολογισμού)
2.  **Public Investment Budget (PIB) Revenue** (Έσοδα ΠΔΕ)
3.  **Regular Budget Expenses** (Έξοδα Τακτικού Προϋπολογισμού)
4.  **Public Investment Budget (PIB) Expenses** (Έξοδα ΠΔΕ)

### 2.2 Strict Formatting Rules
To ensure system integrity, users must adhere to the following:
* **Template Consistency:** Files must strictly match the structure of the templates found in the `input` folder (identical columns, specific order, no additions).
* **Year-Based Column Headers:** In the amount column, the header must state the specific year (e.g., **"2025"**) instead of a generic term like "Amount".
* **File Maintenance:** When importing new data, the old CSV files in the `input` folder must be deleted to ensure the program reads the current year's data correctly.

### 2.3 Multi-Year Evolution
To analyze budget trends over time, the user can:
* Provide **3 additional CSV files** following the same strict templates.
* **Alternatively**, enrich the existing CSV files with data columns for as many years as desired, without adding new files.

---

## 3. Usage Risks & Limitations
* **Single User Restriction:** The application does **not** support concurrent multi-user access. Since the system writes directly to the database, the last user to save will overwrite previous changes.
* **Data Overwrite Warning:** If a user chooses to start the program by loading from **CSV files** while the **SQLite Database** already contains modified data, the database changes will be permanently overwritten.
* **Strategic Focus:** To prioritize complex financial logic and hierarchical propagation over database concurrency management (which would fall under a Database course scope), the current version is optimized for single-user analytical depth.

---

## 4. Comprehensive User Guide

### 4.1 Startup & Data Loading
Upon launching FinDet, you are presented with a crucial choice:
* **Load from CSV:** Import fresh data from the original fiscal CSV files.
* **Load from Database:** Resume from the last saved state in the **SQLite** database, preserving all previous modifications.

### 4.2 Navigation & Exploration
* **Revenues Menu:** Navigate through all budget types (State, Regular, PIB). Includes filtering by account depth and searching by code/description.
* **Expenses Menu:** Detailed analytical views by Ministry, Category, or Entity.
* **Explorer Menu:** Visualize the revenue hierarchy (e.g., searching for code 111 displays parent category 11 "Taxes" and its children like 11101 "VAT").

### 4.3 The Fiscal Adjustment Engine (Changes)
* **Safety Mechanism (Rollback):** If any modification results in a negative balance for any account in the hierarchy, the entire transaction is aborted and a warning is displayed.
* **Revenue Adjustments:** Supports Fixed amounts, Percentages, or Target Balances with hierarchical distribution (Percentage Adjustment or Equal Distribution).
* **Expense Adjustments:** Apply changes horizontally across all accounts in scope or distribute a total sum across categories.

---

## 5. Visualizations & Analytics
* **Dynamic Charts:** Pie and Bar charts for State Budget categories and Expenses per Ministry.
* **Multi-Year Analysis:** Line and Bar charts for comparing Revenue, Expense, and Deficit/Surplus evolution across different years.
* **Statistical Analysis:** Descriptive stats (Mean, Median, Skewness, Kurtosis) and Outlier detection. Includes Histograms and Boxplots for Ministry expenses.

---

## 6. Technical Implementation & Quality

### 6.1 System Documentation (UML Diagrams)
The system's architecture is documented via three specialized diagrams in the `docs/` folder:
* **`generalUML.png`**: Provides a clear view of the core hierarchy and the fundamental structure of the program.
* **`detailedUML.png`**: Displays the basic hierarchy along with complex class relationships, interfaces, and logic flows.
* **`MultiYearAnalysisUML.png`**: Illustrates the specific hierarchy and logic used for processing and comparing data across multiple fiscal years.

### 6.2 Technologies & Quality Assurance
* **Java 21:** Utilizing modern features like Enhanced Switch.
* **SQLite Persistence:** Integrated database for local data persistence and session recovery.
* **99% Backend Test Coverage:** High quality-assurance with a comprehensive JUnit test suite covering all core financial logic.
* **Design Patterns:** Implementation of the **Strategy Pattern** for budget adjustment algorithms and a **Service Layer** for recursive calculations.

---

## 7. Repository Structure
```text
src/main/java/com/financial/
    entries/        # Models (BudgetEntry, Entity, Revenue/Expense subtypes)
    services/       # Recursive logic and hierarchical calculations
    ui/views/       # JavaFX views and UI controllers
    strategies/     # Strategy Pattern for budget modifications
    database/       # SQLite persistence management

src/test/java/      # JUnit test suite (99% Backend Coverage)
docs/               
    generalUML.png              # Basic program hierarchy
    detailedUML.png             # Complex relationships & interfaces
    MultiYearAnalysisUML.png    # Multi-year analysis logic
input/              # Folder for CSV templates and data import
pom.xml             # Maven configuration

---

## 8 . Build & Run Instructions

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
