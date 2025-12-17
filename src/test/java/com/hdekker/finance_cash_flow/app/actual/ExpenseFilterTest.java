package com.hdekker.finance_cash_flow.app.actual;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter.ExpenseIncomeBreakdown;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class ExpenseFilterTest {
	
	@Test
	public void givenListOfTransactions_ExpectExpensesFiltered() {
		

		List<CategorisedTransaction> transactions = TestData.basicTestCase().trans();
		
		assertThat(transactions.stream().filter(ct-> ct.category().equals(TransactionCategory.INCOME)).findAny())
			.isPresent();
			
		ExpenseIncomeBreakdown breakdown = ExpenseFilter.breakdown(transactions);
		
		assertThat(breakdown.expense().stream().filter(ct-> ct.category().equals(TransactionCategory.INCOME)).findAny())
			.isEmpty();
		
	}

}
