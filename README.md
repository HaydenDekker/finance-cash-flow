# Finance App (Cash Flow / Budgeting)

A personal finance management tool for tracking expenses, analyzing historical data, and forecasting future budgets using local file processing and AI.

## Core Features
- **Data Imports**: Import Bank CSV (Complete), PDF receipts, photos (via OCR), and database aggregations.
- **Categories**
  - Necessity: Required, Discretionary
  - Housing, Utilities & Services, Food & Groceries, Transportation, Personal Care & Health, Entertainment, Savings & Debt
  
- **Forecasts** 
  - Groups: Control of how transactions affect future funds.
  - Frequency: Expenses have a transaction frequency associated
  - Type: Transactions expenses have a type of truly fixed, known variable, truly variable
  - Sinking Fund Allocation for annual costs giving oversight to monthly budget requirements

- **Forecasting System**:
  - Historical averaging (3, 6, 12 months)
  - Zero-Based Budget Check
  
- **Scenario Modeling**: Base, Worst-Case, Best-Case financial projections.
- **Controls**: Manipulate scenarios to gauge where to tighten.

## Testing Strategy
Initial tests for the `historical summary` feature:
- (complete) **Data Import**: Validates CSV import from bank transactions list. Validate CSV files can be split into a list of CSV lines as string.
- (complete) **Category Allocation** Tests categorization. Inputs: Transactions without categories (e.g., 'groceries'); outputs: Correctly assigned to Food & Groceries.
- (complete) **Average Calculation** Interpolate historical spend for set of transactions. Check that a reasonable estimate is produced given set of test data.
- (Complete) View Data Import - Display raw transaction data in UI grid. Upload CSV files.
- (Complete) View - Transaction Classification - Classify transactions to category, frequency, necessity.
- (In progress) **System Test - Historical Averaging** importing data, then assessing the average over a time.
  Calculate category actuals, calculate forecasts. Forecasts depend on expense type. Need to produce, Set of all Months in actuals and forecasts.  
  List<Category, Map<Month, MonthActual | MonthForcast> two lists. MonthForcasts have 3 components, Fixed forecast (summed), Known Variable (summed, Variable (linear interpolation)
- (Complete) - Display actuals - Category (row) by YearMonth (column) and cost is the value. Display historical averages, filtered by category. Graph should be cost by month. 
- (done) Display total monthly spend across all categories
- (done) Display total monthly income
- (complete) Display monthly net position
- (complete) View - Historical summary - Initially a table will do. Stacked Bar chart for individual categories, line chart for total expense.

Tests for `forecasts`
-  (completed) Can assign categorised transactions to a forecast group.
-  (completed) Can detect latest expense in a group, can carry that forward by month.
-  (complete) If no forecast group is assigned, use method variable for entire category group and assign the category as the group name. This ensures forcasts will still be relevant by group.
-  (complete) Collect all like forecast groups and sum by month before calculating a forecast. (i.e may have Phone group and two phones each month, 1 $60 and other $80, equaling $140 for that group per month for the forecast)
-  (complete) If forecast group is assigned, use method provided.
- show annual/multi-month expense's monthly allocation in actual expenses, i.e an annual rego payment, divide by number of months based on the forecast method... understand that last months net didn't factor in x,y,z annualised payments. Amortized payments. Each category should include the amortized value by taking the total monthly expense, - any annual expense + the amortised expense. This shows the total known/required expense for the month. 
- A second category value should show the total annual monthly expense component for the month. This way you can see a big payment occurred, but know it was accounted for in the amortised value.
- the total expense for the month should exclude amortised components.
- the amortised expense for the month should include the annual offset and amortised component against the total expense.
- the netflow is the income - the total expense - the amortized expense.

Test for UI review
- (completed) show list of categorised transactions for a given month when clicking the grid cell on the budgeter view.
- (completed) Show CSS coloured/highlighted line for the current day.

Test for Autocompletion
- (complete) expect button copies to clipboard, prompt to extract payee search keywords.
 

## Future Goals
- Use a grouping attribute to collect or differentiate like expenses. e.g All SE W payee's can be South East Water group. When searching by group all expenses can be displayed.
- Enhanced LLM-powered expense categorization
- Existing transaction check before replacing existing transaction.