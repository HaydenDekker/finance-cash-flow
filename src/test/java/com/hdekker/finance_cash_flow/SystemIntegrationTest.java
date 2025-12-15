package com.hdekker.finance_cash_flow;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.app.actual.HistoricalOverviewFilter.HistoricalOverview;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.hdekker.finance_cash_flow.historical.HistoricalInterpollationRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TransactionRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;
import com.hdekker.finance_cash_flow.transaction.TestData.TransactionAssignement;

@SpringBootTest
public class SystemIntegrationTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	@Autowired
	TransactionRestAdapter transactionRestAdapter;
	
	@Autowired
	HistoricalInterpollationRestAdapter historicalInterpollationRestAdapter;
	
	@Autowired
	TransactionDeleter transactionDeleter;
	
	@Autowired
	CategorisedTransactionDeleter categorisedTransactionDeleter;
	
	public static List<TestCase> testCases(){
		return TestData.testCases();
	}
	
	List<TransactionAssignement> transactions;
	
	@ParameterizedTest
	@MethodSource("testCases")
	public void givenCategorisedData_ExpectAverageCalculated(TestCase testCase) {
	
		transactions = testCase.transactions();
		testCase.transactions().forEach(t->transactionRestAdapter.save(t.transaction()));
		testCase.transactions().forEach(ta-> categoryRestAdapter.set(ta.categorisedTransaction()));
		historicalInterpollationRestAdapter.listAll();
		
		HistoricalOverview ho = categoryRestAdapter.historicalOverview();
		
		Set<YearMonth> allMonths = testCase.trans().stream()
			.map(ct->ct.getTransactionYearMonth())
			.collect(Collectors.toSet());
		
		assertThat(ho.monthlyExpensesTotal().keySet())
			.hasSizeGreaterThan(0);
		
		assertThat(ho.monthlyExpensesTotal().keySet())
			.hasSize(allMonths.size());
		
	}
	
	@AfterEach
	public void cleanUpDatabase() {
		transactions.forEach(t->{
			categorisedTransactionDeleter.delete(t.categorisedTransaction());
			transactionDeleter.delete(t.transaction());
		});
	}

}
