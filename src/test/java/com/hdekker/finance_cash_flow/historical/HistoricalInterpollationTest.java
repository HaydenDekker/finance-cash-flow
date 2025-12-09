package com.hdekker.finance_cash_flow.historical;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.offset;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.transaction.TransactionTestData;

public class HistoricalInterpollationTest {
	
	
	@Test
	public void givenListOfTransactions_ExpectPredictsNextValue() {
		
		TransactionTestData transactionTestData = new TransactionTestData();
		HistoricalInterpollationResult result = HistoricalInterpollation.interpollate(transactionTestData.testTransactions());
		double estimate = result.quadraticResult().evaluate(YearMonth.of(2025, 07));
		assertThat(estimate)
				.isCloseTo(127, offset(2.0));
	}

}
