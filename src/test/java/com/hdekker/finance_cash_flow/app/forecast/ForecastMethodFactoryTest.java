package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.app.actual.HistoricalOverviewFilter;
import com.hdekker.finance_cash_flow.app.actual.HistoricalOverviewFilter.HistoricalOverview;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;
import com.hdekker.finance_cash_flow.app.forecast.ForecastMethodFactory.Forecast;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class ForecastMethodFactoryTest {
	
	@Test
	public void givenIncomeCategorisedTransactionList_ExpectForcastedFixedMethodReturnsExpectedValue() {
		
		List<CategorisedTransaction> transactions = TestData.testCases()
															.get(0)
															.trans();
		
		HistoricalOverview overview = HistoricalOverviewFilter.calculate(transactions);
		SummedTransactionCategory income = overview.monthlyIncomeTotal();
		
		Forecast forcast = ForecastMethodFactory.fixed().forcast(
				income.categorisedTransaction(), 
				income.categorisedTransaction().get(
						income.categorisedTransaction().size()-1).getTransactionYearMonth().plusYears(1));
		assertThat(forcast.forcastedTransaction())
			.hasSize(12);
		
	}

}
