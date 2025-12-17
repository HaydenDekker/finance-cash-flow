package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.offset;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.forecast.HistoricalInterpollationResult.QuadraticCalculation;
import com.hdekker.finance_cash_flow.historical.HistoricalInterpollation;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class HistoricalInterpollationTest {
	
	
	@Test
	public void givenListOfTransactions_ExpectPredictsNextValue() {
		
		List<Transaction> testData = TestData.basicTestCase()
			.transactionExpenses();
		
		Map<YearMonth, SummedTransactions> data = HistoricalSummer.calculateTotal(testData);
		
		QuadraticCalculation result = HistoricalInterpollation.interpollate(data);
		
		double estimate = result.evaluate(
				YearMonth.from(
						TestData.startingDate.plusMonths(10)));
		assertThat(estimate)
				.isCloseTo(124, offset(2.0));
	}

}
