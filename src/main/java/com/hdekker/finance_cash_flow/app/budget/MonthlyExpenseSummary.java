package com.hdekker.finance_cash_flow.app.budget;

import java.util.List;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview.AmortizedExpenseGroup;

public record MonthlyExpenseSummary(
		AmortizedExpenseGroup amortizedExpenseGroup,
		List<CategorisedTransaction> categorisedTransactions
		) {
	
	public Double netRealisedExpense() {
		return categorisedTransactions.stream()
				.collect(
					Collectors.summingDouble(ct->ct.transaction().amount())) 
				+ amortizedExpenseGroup.amount();
	}
	public Double netAmortizedCredit() {
		return amortizedExpenseGroup.credits();
				
	}
	
}
