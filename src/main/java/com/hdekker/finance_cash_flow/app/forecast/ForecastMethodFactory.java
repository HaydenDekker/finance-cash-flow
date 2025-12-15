package com.hdekker.finance_cash_flow.app.forecast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.Transaction;

public class ForecastMethodFactory {
	
	public record Forecast(List<CategorisedTransaction> forcastedTransaction, YearMonth month) {}
	
	public interface ForecasterMethod {
		Forecast forcast(List<CategorisedTransaction> transactions, YearMonth to);
	}
	
	public static ForecasterMethod buildFor(ExpenseType forecastType) {
		switch(forecastType) {
			case FIXED: return fixed();
			case KNOWN_VARIABLE: return knownVariable();
			case VARIABLE: return variable();
		}
		return null;
	}
	
	static ForecasterMethod variable() {
		return (t, y)-> new Forecast(List.of(), y);
	}
	
	static ForecasterMethod knownVariable() {
		return (t, y)-> new Forecast(List.of(), y);
	}
	
	static CategorisedTransaction getLatestTransactionInGroup(List<CategorisedTransaction> t) {
		return t.stream()
		.sorted((a,b)-> b.getTransactionYearMonth().compareTo(a.getTransactionYearMonth()))
		.toList()
		.get(0);
	}
	
	static ForecasterMethod fixed() {
		return (t, yearMonthTo)->{
			
			CategorisedTransaction latestTransaction = getLatestTransactionInGroup(t);
			
			YearMonth startMonth = latestTransaction.getTransactionYearMonth();
			
			long monthsDifference = startMonth.until(yearMonthTo, ChronoUnit.MONTHS);
			
			LocalDate firstMonthForForecast = latestTransaction.transaction().localDate().plusMonths(1);
			List<CategorisedTransaction> forcastedTransactions = Stream.iterate(firstMonthForForecast, current -> current.plusMonths(1))
	            .limit(monthsDifference)
	            .map(ld-> new CategorisedTransaction(
	            		new Transaction(ld, latestTransaction.transaction().amount(), latestTransaction.transaction().description()),
	            		latestTransaction.category(),
	            		latestTransaction.necessity(),
	            		latestTransaction.forcastGroup(),
	            		latestTransaction.financialFrequency(),
	            		latestTransaction.expenseType(),
	            		latestTransaction.assignmentTimeStamp()
	            		))
	            .toList();
			
			return new Forecast(forcastedTransactions, yearMonthTo);
		};
	}

}
