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
- (Complete) View Data Import - Display raw transaction data in UI grid. Upload CSV files.
- (Complete) View - Transaction Classification - Classify transactions to category, frequency, necessity.
- (In progress) **System Test - Historical Averaging** importing data, then assessing the average over a time.
  Calculate category actuals, calculate forecasts. Forecasts depend on expense type. Need to produce, Set of all Months in actuals and forecasts.  
  List<Category, Map<Month, MonthActual | MonthForcast> two lists. MonthForcasts have 3 components, Fixed forecast (summed), Known Variable (summed, Variable (linear interpolation)
- (Complete) - Display actuals - Category (row) by YearMonth (column) and cost is the value. Display historical averages, filtered by category. Graph should be cost by month. 
- Display total monthly spend across all categories
- Display total monthly income
- Display monthly net position
- (In Progress) View - Historical Average - Initially a table will do. Stacked Bar chart for individual categories, line chart for total expense. 


## Future Goals
- Enhanced LLM-powered expense categorization
- Web-based dashboard with interactive graphs