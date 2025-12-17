package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

public class ForecasterTest {
	
	@Test
	public void givenListOfItemsWithNoForecastGroupSet_ExpectGroupNameAsCategoryANDTypeVariableUsedForAllCalculations() {
		
		TestCase testCase = TestData.noForecastGroupTestCase();
		List<CategorisedTransaction> forecastedTransactions = Forecaster.forcast(testCase.trans());
		
		assertThat(forecastedTransactions)
			.isNotEmpty();
		
		// all should be variable expenses
		assertThat(forecastedTransactions.stream()
				.filter(ct->!ct.expenseType().equals(ExpenseType.VARIABLE))
				.findAny())
			.isEmpty();
		
		
		assertThat(forecastedTransactions.stream()
				// forecast group name should category name
				.filter(ct->ct.forcastGroup().name().equals(ct.category().name()))
				.toList())
			.isNotEmpty();
		
	}

}
