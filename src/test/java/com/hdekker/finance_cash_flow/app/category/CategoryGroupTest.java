package com.hdekker.finance_cash_flow.app.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class CategoryGroupTest {
	
	@Test
	public void givenCatergorisedTransactionList_ExpectCanGroupByCategoryAndByYearMonthAndSummed() {
		
		List<CategorisedTransaction> transactions = TestData.basicTestCase().transactions().stream()
				.map(ta-> ta.categorisedTransaction())
				.toList();
		
		List<SummedTransactionCategory> groupedTransactions = CategoryGroup.groupByCategoryAndByYearMonthAndSum(transactions);
		
		assertThat(groupedTransactions)
			.hasSize(3);
		
	}
	
	

}
