package com.hdekker.finance_cash_flow.app.budget;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview.AmortizedExpenseGroup;

public class MonthlyExpenseSummaryTest {
	
	@Test
	public void calculateMonthlyExpenseSummary() {
		
		AmortizedExpense ae = new AmortizedExpense(null, null, 10.0);
		AmortizedExpenseGroup g = new AmortizedExpenseGroup(List.of(ae));
		Transaction t = new Transaction(LocalDate.now(), 20.0, "Just a transaction");
		CategorisedTransaction ct = new CategorisedTransaction(
				t,
				"just",
				TransactionCategory.CHILDCARE_AND_SCHOOLING,
				Necessity.REQUIRED,
				new ForecastGroup("school"),
				FinancialFrequency.AD_HOC,
				ExpenseType.VARIABLE
				);
		
		MonthlyExpenseSummary s = new MonthlyExpenseSummary(g, List.of(ct));
		
		assertThat(s.netMonthlyExpense())
			.isEqualTo(20.0);
			
		assertThat(s.netAmortizedCredit())
			.isEqualTo(10.0);
		
		assertThat(s.netRealisedExpense())
			.isEqualTo(20.0 + 10.0);
		
		
	}


}
