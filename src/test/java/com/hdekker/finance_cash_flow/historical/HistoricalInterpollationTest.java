package com.hdekker.finance_cash_flow.historical;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.offset;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;

public class HistoricalInterpollationTest {
	
List<Transaction> testTransactions(){
	return List.of(
		new Transaction(LocalDate.of(2024,5,15), 3.0, "Test"),
		new Transaction(LocalDate.of(2024,6,15), 6.0, "Test"),
		new Transaction(LocalDate.of(2024,7,15), 11.0, "Test"),
		new Transaction(LocalDate.of(2024,8,15), 18.0, "Test"),
		new Transaction(LocalDate.of(2024,9,15), 27.0, "Test"),
		new Transaction(LocalDate.of(2024,10,15), 38.0, "Test"),
		new Transaction(LocalDate.of(2024,11,15), 51.0, "Test"),
		new Transaction(LocalDate.of(2025,1,15), 66.0, "Test"),
		new Transaction(LocalDate.of(2025,2,15), 83.0, "Test"),
		new Transaction(LocalDate.of(2025,5,15), 102.0, "Test")
	);
}
	
	@Test
	public void givenListOfTransactions_ExpectPredictsNextValue() {
		
		HistoricalInterpollationResult result = HistoricalInterpollation.interpollate(testTransactions());
		double estimate = result.quadraticResult().evaluate(YearMonth.of(2025, 07));
		assertThat(estimate)
				.isCloseTo(127, offset(2.0));
	}

}
