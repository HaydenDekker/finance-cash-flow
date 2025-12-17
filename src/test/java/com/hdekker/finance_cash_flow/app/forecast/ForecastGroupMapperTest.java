package com.hdekker.finance_cash_flow.app.forecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class ForecastGroupMapperTest {
	
	@Test
	public void givenCategorisedTransactions_ExpectMappedByForcastGroup() {
		
		List<CategorisedTransaction> transactions = TestData.basicTestCase().trans();
		Map<ForecastGroup, List<CategorisedTransaction>> groups = ForecastGroupMapper.map(transactions);
		assertThat(groups.keySet())
			.hasSize(4);
		
	}

}
