package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

public class ForecasterTest {
	
	@Test
	public void givenListOfItemsWithNoForecastGroupSet_ExpectTypeVariableUsedForAllCalculations() {
		
		TestCase testCase = TestData.noForecastGroupTestCase();
		List<CategorisedTransaction> forcastedTransactions = Forecaster.forcast(testCase.trans());
		assertThat(forcastedTransactions)
			.isNotEmpty();
		
	}

}
