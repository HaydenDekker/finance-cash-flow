# Finance App (Cash Flow / Budgeting)

A personal finance management tool for tracking expenses, analyzing historical data, and forecasting future budgets using local file processing and AI.

## Core Features
- **Local File Processing**: Import CSV/PDF receipts, photos (via OCR), and database aggregations.
- **7 Core Expense Categories**:
  - Housing, Utilities & Services, Food & Groceries, Transportation
  - Personal Care & Health, Entertainment, Savings & Debt
- **Forecasting System**:
  - Historical averaging (3, 6, 12 months)
  - Zero-Based Budget Check
  - Sinking Fund Allocation for annual costs
- **Scenario Modeling**: Base, Worst-Case, Best-Case financial projections.

## Testing Strategy
Initial tests for the `historical averaging` feature:
- **Data Import** (`TransactionImporterTest`): Validates CSV/PDF parsing. Inputs: `test_data/transactions.csv` (dates/amounts); checks extraction of missing values.
- **Category Allocation** (`CategoryAllocatorTest`): Tests auto-categorization. Inputs: Transactions without categories (e.g., 'groceries'); outputs: Correctly assigned to Food & Groceries.
- **Average Calculation** (`HistoricalAveragerTest`): Confirms 1-month average. Inputs: 30 days of mock transactions; validates sum ÷ 30 = daily average.

## Future Goals
- Auto-import from bank apps (ANZ, etc.)
- Enhanced LLM-powered expense categorization
- Web-based dashboard with interactive graphs