package com.hdekker.finance_cash_flow;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;

public record CategorisedTransaction(
		Transaction transaction,
		TransactionCategory category,
		Necessity necessity,
		String forcastGroup,
		FinancialFrequency financialFrequency,
		ExpenseType expenseType,
		LocalDateTime assignmentTimeStamp) { // TODO remove timestamp
	
	public enum Necessity {
		REQUIRED,
		DISCRETIONARY
	}
	
	public enum FinancialFrequency {
		// Standard frequencies, often backed by a Period object
	    MONTHLY(Period.ofMonths(1)),
	    QUARTERLY(Period.ofMonths(3)),
	    ANNUALLY(Period.ofYears(1)),

	    // Special financial types that don't need a specific Period
	    ONCE_OFF(null), // A single, non-recurring event
	    AD_HOC(null);   // Unplanned, random occurrences used for calculating averages

	    private final Period period;

	    FinancialFrequency(Period period) {
	        this.period = period;
	    }

	    public Period getPeriod() {
	        return period;
	    }
	}
	
	public enum ExpenseType {
		FIXED,
		KNOWN_VARIABLE,
		VARIABLE
	}
	
	public YearMonth getTransactionYearMonth() {
	    return YearMonth.from(this.transaction().localDate()); 
	}
	
	
}
