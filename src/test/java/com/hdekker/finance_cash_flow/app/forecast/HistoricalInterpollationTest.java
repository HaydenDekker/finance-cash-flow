package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.offset;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.historical.HistoricalInterpollation;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class HistoricalInterpollationTest {
	
	
	@Test
	public void givenListOfTransactions_ExpectPredictsNextValue() {
		
		HistoricalInterpollationResult result = HistoricalInterpollation.interpollate(
				TestData.testCases()
					.get(0)
					.transactionExpenses());
		
		double estimate = result.quadraticResult().evaluate(
				YearMonth.from(
						TestData.startingDate.plusMonths(10)));
		assertThat(estimate)
				.isCloseTo(124, offset(2.0));
	}

}
