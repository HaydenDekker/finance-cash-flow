package com.hdekker.finance_cash_flow.app.forecast;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;

public class ForecastMethodFactory {
	
	public record Forecast(List<CategorisedTransaction> forcastedTransaction, YearMonth month) {}
	
	public interface ForecasterMethod {
		Forecast forcast(List<CategorisedTransaction> transactions, YearMonth to);
	}
	
	static ForecasterMethod fixed() {
		return (t, yearMonthTo)->{
			
			CategorisedTransaction latestTransaction = t.stream()
				.sorted((a,b)-> b.getTransactionYearMonth().compareTo(a.getTransactionYearMonth()))
				.toList()
				.get(0);
			
			YearMonth startMonth = latestTransaction.getTransactionYearMonth();
			
			long monthsDifference = startMonth.until(yearMonthTo, ChronoUnit.MONTHS);
			
			List<CategorisedTransaction> forcastedTransactions = Stream.iterate(latestTransaction.transaction().localDate(), current -> current.plusMonths(1))
	            // 3. Define the stopping condition (limiting the stream)
	            .limit(monthsDifference)
	            // 4. Collect the resulting elements into a List
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
