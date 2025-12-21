package com.hdekker.finance_cash_flow.app.budget;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

public class BudgetOverviewTest {
	
	@Test
	public void givenExpenseAndIncome_ExpectNetCalculated() {
		
		TestCase tc = TestData.basicTestCase();
		BudgetOverview bo = BudgetOverview.calculate(tc.transactions());
		
		LocalDate targetDate = TestData.startingDate.plusMonths(3);
		Double expenseTotalAtTargetMonth = 48.0;
		
		SummedTransactions net = bo.netFlow().get(YearMonth.from(targetDate));
		
		assertThat(net.amount())
			.isEqualTo(expenseTotalAtTargetMonth);
		
	}
	
	@Test
	public void givenAnualisedExpense_ExpectSinkCalculatedOnCategory() {
		
		TestCase tc = TestData.basicTestCase();
		BudgetOverview bo = BudgetOverview.calculate(tc.transactions());
		
	}

}
