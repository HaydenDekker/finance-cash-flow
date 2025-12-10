package com.hdekker.finance_cash_flow;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.hdekker.finance_cash_flow.historical.HistoricalInterpollationRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TransactionRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

@SpringBootTest
public class SystemIntegrationTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	@Autowired
	TransactionRestAdapter transactionRestAdapter;
	
	@Autowired
	HistoricalInterpollationRestAdapter historicalInterpollationRestAdapter;
	
	public List<TestCase> testCases(){
		
		TestData td = new TestData();
		return td.testCases();
	}
	
	@ParameterizedTest
	@MethodSource("testCases")
	public void givenCategorisedData_ExpectAverageCalculated(TestCase testCase) {
	
		testCase.transactions().forEach(t->transactionRestAdapter.save(t.transaction()));
		testCase.transactions().forEach(ta-> categoryRestAdapter.set(ta.categorisedTransaction()));
		historicalInterpollationRestAdapter.listAll();
		
	}

}
