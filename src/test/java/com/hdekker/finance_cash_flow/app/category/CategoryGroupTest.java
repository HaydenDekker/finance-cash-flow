package com.hdekker.finance_cash_flow.app.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class CategoryGroupTest {
	
	@Test
	public void givenCatergorisedTransactionList_ExpectCanGroupByCategoryAndByYearMonth() {
		
		TestData td = new TestData();
		List<CategorisedTransaction> transactions = td.testCases().get(0).transactions().stream()
				.map(ta-> ta.categorisedTransaction())
				.toList();
		
		Map<TransactionCategory, Map<YearMonth, List<CategorisedTransaction>>> groupedTransactions = CategoryGroup.groupByCategoryAndByYearMonth(transactions);
		
		assertThat(groupedTransactions.keySet())
			.hasSize(2);
		
	}
	
	@Test
	public void givenCatergorisedTransactionList_ExpectCanGroupByCategoryAndByYearMonthAndSummed() {
		
		TestData td = new TestData();
		List<CategorisedTransaction> transactions = td.testCases().get(0).transactions().stream()
				.map(ta-> ta.categorisedTransaction())
				.toList();
		
		Map<TransactionCategory, Map<YearMonth, SummedTransactions>> groupedTransactions = CategoryGroup.groupByCategoryAndByYearMonthAndSum(transactions);
		
		assertThat(groupedTransactions.keySet())
			.hasSize(2);
		
	}
	
	

}
