package com.hdekker.finance_cash_flow.transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;

public class TestData {
	
	String stubDescription = "sweets";
	LocalDate date = LocalDate.now();
	public Transaction stub = new Transaction(
			date, 
			0.0, 
			stubDescription);
	
	public Transaction stub2 = new Transaction(
			date.plusDays(1), 
			10.0, 
			stubDescription + " second.");
	
	String descriptionStub = "PAYMENT TO TELSTRA SERVICES 0PME5S2S";
    String mockCSVData = "\"3/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
    String mockCSVDataDoubleDigitDay = "\"24/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
    
    private static LocalDate startingDate = LocalDate.now().minusYears(1);
    public static YearMonth yearMonthOfStartingDate = YearMonth.from(startingDate);
    
    public static List<Transaction> testTransactions(){
    	return List.of(
    		new Transaction(startingDate, 300.0, "Income"),
    		new Transaction(startingDate.plusDays(1), 3.0, "Groceries"),
    		new Transaction(startingDate.plusMonths(1), 6.0, "Fuel"),
    		new Transaction(startingDate.plusMonths(2), 11.0, "Fuel"),
    		new Transaction(startingDate.plusMonths(3), 18.0, "Groceries"),
    		new Transaction(startingDate.plusMonths(3), 30.0, "Groceries"),
    		new Transaction(startingDate.plusMonths(4), 27.0, "Rego"),
    		new Transaction(startingDate.plusMonths(5), 38.0, "Groceries"),
    		new Transaction(startingDate.plusMonths(6), 51.0, "Fuel"),
    		new Transaction(startingDate.plusMonths(7), 66.0, "Groceries"),
    		new Transaction(startingDate.plusMonths(8), 83.0, "Fuel"),
    		new Transaction(startingDate.plusMonths(9), 102.0, "Rego")
    	);
    }
    
	public record ExpectedResults() {}
	
	public record ExpectedOutput(YearMonth yearMonth, Double amount, Double difference) {}
	
	public record TestCase(List<CategorisedTransaction> transactions) {
		
		public List<Transaction> transactionExpenses() {
			return ExpenseFilter.breakdown(
					transactions)
					.expense()
					.stream()
						.map(ct->ct.transaction())
						.toList();
		}
		
	}
	
	
	
	static Map<String, TransactionCategory> categoryMap = Map.of(
			"Groceries", TransactionCategory.FOOD_GROCERIES,
			"Fuel", TransactionCategory.TRANSPORTATION,
			"Rego", TransactionCategory.TRANSPORTATION,
			"Income", TransactionCategory.INCOME);
	
	static Map<String, ExpenseType> forecastMethodMap = Map.of(
			"Groceries", ExpenseType.VARIABLE,
			"Fuel", ExpenseType.VARIABLE,
			"Rego", ExpenseType.KNOWN_VARIABLE,
			"Income", ExpenseType.FIXED);
	
	static CategorisedTransaction assign(Transaction transaction, Boolean mockForcastGroup) {
		return 
				new CategorisedTransaction(
						transaction,
						"",
						categoryMap.get(transaction.description()), 
						Necessity.DISCRETIONARY,
						// just use description as group for test
						mockForcastGroup ? new ForecastGroup(transaction.description() + "_forecast_group") : new ForecastGroup(""),
						FinancialFrequency.AD_HOC,
						forecastMethodMap.get(transaction.description()),
						LocalDateTime.now());
	}

	public static TestCase basicTestCase() {
		return new TestCase(
						testTransactions().stream().map(t->assign(t, true)).toList()
						);
	}
	
	public static TestCase noForecastGroupTestCase() {
		return new TestCase(
						testTransactions().stream().map(t->assign(t, false)).toList()
						);
	}
	
	public static TestCase annualisedExpenseTestCase() {
		
		return new TestCase(List.of(
				new CategorisedTransaction(
						new Transaction(startingDate, 120, "Annual expense stub"), 
						"House Insurance", 
						TransactionCategory.HOUSING, 
						Necessity.REQUIRED, 
						new ForecastGroup("House Insurance"), 
						FinancialFrequency.ANNUALLY, 
						ExpenseType.KNOWN_VARIABLE, 
						LocalDateTime.now())));
		
	}


}
