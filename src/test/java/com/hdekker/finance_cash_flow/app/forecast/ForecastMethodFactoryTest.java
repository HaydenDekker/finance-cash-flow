package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;
import com.hdekker.finance_cash_flow.app.forecast.ForecastMethodFactory.Forecast;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class ForecastMethodFactoryTest {
	
	@Test
	public void givenIncomeCategorisedTransactionList_ExpectForcastedFixedMethodReturnsExpectedValue() {
		
		List<CategorisedTransaction> transactions = TestData.basicTestCase()
															.trans();
		
		BudgetOverview overview = BudgetOverview.calculate(transactions);
		SummedTransactionCategory income = overview.monthlyIncomeTotal();
		
		Forecast forecast = ForecastMethodFactory.fixed().forcast(
				income.categorisedTransaction(), 
				income.categorisedTransaction().get(
						income.categorisedTransaction().size()-1).getTransactionYearMonth().plusYears(1));
		assertThat(forecast.forcastedTransaction())
			.hasSize(12);
		
		CategorisedTransaction latest = ForecastMethodFactory.getLatestTransactionInGroup(income.categorisedTransaction());
		
		assertThat(latest.getTransactionYearMonth().plusMonths(1))
			.isEqualTo(forecast.forcastedTransaction()
					.get(0)
					.getTransactionYearMonth()
			);
			
		
	}
	
	@Test
	public void givenVariableCategorisedTransactionList_ExpectForecastedAveragesAccrossAllValues() {
		
		List<CategorisedTransaction> transactions = TestData.basicTestCase()
				.trans()
				.stream()
				.filter(ct-> ct.expenseType().equals(ExpenseType.VARIABLE))
				.toList();
		
		assertThat(transactions)
			.hasSize(8);
		
		Forecast forecast = ForecastMethodFactory.buildFor(ExpenseType.VARIABLE)
			.forcast(transactions, YearMonth.from(TestData.startingDate.plusMonths(12)));
		
		assertThat(forecast.forcastedTransaction())
			.isNotEmpty();
		
		
	}
	

}
