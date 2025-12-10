package com.hdekker.finance_cash_flow;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CatorgorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.hdekker.finance_cash_flow.historical.HistoricalInterpollationRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TransactionRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TransactionTestData;

@SpringBootTest
public class SystemIntegrationTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	@Autowired
	TransactionRestAdapter transactionRestAdapter;
	
	@Autowired
	HistoricalInterpollationRestAdapter historicalInterpollationRestAdapter;
	
	public record TransactionAssignement(
			Transaction transaction, 
			CatorgorisedTransaction categorisedTransaction) {}
	
	public record ExpectedResults() {}
	
	public record ExpectedOutput(YearMonth yearMonth, Double amount, Double difference) {}
	
	public record TestCase(List<TransactionAssignement> transactions, ExpectedOutput expectedOutput) {}
	
	Map<String, TransactionCategory> categoryMap = Map.of(
			"Groceries", TransactionCategory.FOOD_GROCERIES,
			"Fuel", TransactionCategory.TRANSPORTATION,
			"Rego", TransactionCategory.TRANSPORTATION);
	
	public TransactionAssignement assign(Transaction transaction) {
		return new TransactionAssignement(
				transaction, 
				new CatorgorisedTransaction(
						transaction, 
						categoryMap.get(transaction.description()), 
						Necessity.DISCRETIONARY,
						FinancialFrequency.AD_HOC,
						ExpenseType.FIXED,
						LocalDateTime.now()));
	}
	
	public List<TestCase> testCases(){
		
		TransactionTestData td = new TransactionTestData();
		
		return List.of(
				new TestCase(
						td.testTransactions().stream().map(this::assign).toList(),
						new ExpectedOutput(YearMonth.of(2025, 07), 127.0, 5.0)
						)
				);
	}
	
	@ParameterizedTest
	@MethodSource("testCases")
	public void givenCategorisedData_ExpectAverageCalculated(TestCase testCase) {
	
		testCase.transactions.forEach(t->transactionRestAdapter.save(t.transaction()));
		testCase.transactions.forEach(ta-> categoryRestAdapter.set(ta.categorisedTransaction()));
		historicalInterpollationRestAdapter.listAll();
		
	}

}
