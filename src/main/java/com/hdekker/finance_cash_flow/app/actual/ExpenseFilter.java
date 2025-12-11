package com.hdekker.finance_cash_flow.app.actual;

import java.util.List;
import java.util.function.Predicate;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;

public class ExpenseFilter {
	
	final static Predicate<CategorisedTransaction> expenseFilter = (ct)-> !TransactionCategory.INCOME.equals(ct.category());

	public static List<CategorisedTransaction> filter(List<CategorisedTransaction> transactions) {
		return transactions.stream()
					.filter(expenseFilter)
					.toList();
	}

}
