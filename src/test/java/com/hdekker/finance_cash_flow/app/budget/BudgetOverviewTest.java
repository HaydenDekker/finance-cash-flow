package com.hdekker.finance_cash_flow.app.budget;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

public class BudgetOverviewTest {
	
	@Test
	public void givenExpenseAndIncome_ExpectNetCalculated() {
		
		TestCase tc = TestData.basicTestCase();
		BudgetOverview bo = BudgetOverview.calculate(tc.transactions());
		
		YearMonth targetDate = TestData.yearMonthOfStartingDate.plusMonths(3);
		Double expenseTotalAtTargetMonth = -48.0;
		
		SummedTransactions net = bo.netFlow().get(targetDate);
		
		assertThat(net.amount())
			.isEqualTo(expenseTotalAtTargetMonth);
		
	}
	
	@Test
	public void givenAnualisedExpense_ExpectSinkCalculatedOnCategory() {
		
		TestCase tc = TestData.annualisedExpenseTestCase();
		
		BudgetOverview bo = BudgetOverview.calculate(tc.transactions());
		
		double amount = bo.netAmortizedExpenses()
			.get(TestData.yearMonthOfStartingDate)
			.amount();
		
		assertThat(amount)
			.isEqualTo(110.0);
		
	}
	
	/**
	 * Want to show how much my actual expenses were
	 * along with any amortised expenses (money I'm saving for an annual expense)
	 * plus applied amortised offsets (when the annual/non-monthly expense has occured)
	 * 
	 */
	@Test
	public void givenAnnualAndMonthlyExpense_ExpectCombinedInMonthSummary() {
		
		TestCase tc = TestData.mixedFrequencyExpensesForSingleMonth();
		BudgetOverview bo = BudgetOverview.calculate(tc.transactions());
		
		MonthlyExpenseSummary summary = bo.monthlyExpenseSummary()
			.get(TransactionCategory.HOUSING)
			.get(TestData.yearMonthOfStartingDate);
			
		assertThat(summary.netRealisedExpense())
			.isEqualTo(-35.0);
		
		assertThat(summary.netAmortizedCredit())
			.isEqualTo(120.0);
		
		MonthlyExpenseSummary summaryEntertainment = bo.monthlyExpenseSummary()
				.get(TransactionCategory.ENTERTAINMENT)
				.get(TestData.yearMonthOfStartingDate);
		
		assertThat(summaryEntertainment.netRealisedExpense())
		.isEqualTo(-56.0);
	
		assertThat(summaryEntertainment.netAmortizedCredit())
			.isEqualTo(0.0);
		
	}

}
