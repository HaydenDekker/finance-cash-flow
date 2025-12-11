package com.hdekker.finance_cash_flow.app.actual;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class ExpenseFilterTest {
	
	@Test
	public void givenListOfTransactions_ExpectExpensesFiltered() {
		

		List<CategorisedTransaction> transactions = TestData.testCases().get(0).trans();
		
		assertThat(transactions.stream().filter(ct-> ct.category().equals(TransactionCategory.INCOME)).findAny())
			.isPresent();
			
		List<CategorisedTransaction> expenses = ExpenseFilter.filter(transactions);
		
		assertThat(expenses.stream().filter(ct-> ct.category().equals(TransactionCategory.INCOME)).findAny())
			.isEmpty();
		
	}

}
