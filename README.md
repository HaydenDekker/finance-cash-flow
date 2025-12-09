# Finance App (Cash Flow / Budgeting)

A personal finance management tool for tracking expenses, analyzing historical data, and forecasting future budgets using local file processing and AI.

## Core Features
- **Local File Processing**: Import Bank CSV (Complete), PDF receipts, photos (via OCR), and database aggregations.
- **7 Core Expense Categories**:
  - Housing, Utilities & Services, Food & Groceries, Transportation
  - Personal Care & Health, Entertainment, Savings & Debt
- **Category Attribute***
  - Frequency: Expenses have a transaction frequency associated
  - Type: transactions expenses have a type of truly fixed, known variable, truly variable
  - Necessity: Required, Discretionary

- **Forecasting System**:
  - Historical averaging (3, 6, 12 months)
  - Zero-Based Budget Check
  - Sinking Fund Allocation for annual costs
- **Scenario Modeling**: Base, Worst-Case, Best-Case financial projections.
- **Controls**: Manipulate scenarios to gauge where to tighten.

## Testing Strategy
Initial tests for the `historical averaging` feature:
- (complete) **Data Import**: Validates CSV import from bank transactions list. Validate CSV files can be split into a list of CSV lines as string.
- (complete) **Category Allocation** Tests categorization. Inputs: Transactions without categories (e.g., 'groceries'); outputs: Correctly assigned to Food & Groceries.
- (complete) **Average Calculation** Interpolate historical spend for set of transactions. Check that a reasonable estimate is produced given set of test data.
- (In progress) **System Test - Historical Averaging** importing data, then assessing the average over a time.
- (In progress) View Data Import - Display raw transaction data in UI grid. Upload CSV files.

## Future Goals
- Enhanced LLM-powered expense categorization
- Web-based dashboard with interactive graphs