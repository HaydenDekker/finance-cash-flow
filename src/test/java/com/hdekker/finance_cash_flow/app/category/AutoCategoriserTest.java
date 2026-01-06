package com.hdekker.finance_cash_flow.app.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.transaction.TestData;
import com.hdekker.finance_cash_flow.transaction.TestData.TestCase;

public class AutoCategoriserTest {
	
	@Test
	public void givenMixedListOfTransactions_ExpectAutoCategoriseMatchesLikeKeywordTransactions() {
		
		TestCase tc = TestData.repeatedTransactionSingleProivder();
		
		List<CategorisedTransaction> categorisedTransactions = AutoCategoriser.categorise(tc.transactions());
	
		assertThat(categorisedTransactions)
			.hasSize(1);
		
		boolean allHaveBeenCategorised = categorisedTransactions.stream()
				.filter(ct->ct.category()==null)
				.findAny()
				.isEmpty();
		
		assertThat(allHaveBeenCategorised)
			.isTrue();
		
	}
	
	@Test
	public void givenMixedListOfTransactionsWithNoForcastGroup_ExpectAutoCategoriseMatchesLikeKeywordTransactions() {
		
		TestCase tc = TestData.repeatedTransactionSingleProivderEmptyForcastGroup();
		
		List<CategorisedTransaction> categorisedTransactions = AutoCategoriser.categorise(tc.transactions());
	
		assertThat(categorisedTransactions)
			.hasSize(1);
		
		boolean allHaveBeenCategorised = categorisedTransactions.stream()
				.filter(ct->ct.category()==null)
				.findAny()
				.isEmpty();
		
		assertThat(allHaveBeenCategorised)
			.isTrue();
		
	}
	
	@Test
	public void givenRepeatTransactionWithMultipleForcastGroupAssigned_ExpectNewTransactionNotAutoCategorised() {
		
		TestCase tc = TestData.repeatedTransactionSingleProivderWithMultipleForcastGroup();
		
		List<CategorisedTransaction> categorisedTransactions = AutoCategoriser.categorise(tc.transactions());
	
		assertThat(categorisedTransactions)
			.hasSize(0);
		
	}
	
	@Test
	public void givenCompletedTransactions_ExpectReturnsExactList() {
		
		TestCase tc = TestData.repeatedTransactionSingleProivderAllComplete();
		
		List<CategorisedTransaction> categorisedTransactions = AutoCategoriser.categorise(tc.transactions());
	
		assertThat(categorisedTransactions)
			.hasSize(0);
		
	}
	
	@Test
	public void givenNoCompletedCategorisations_ExpectReturnsExactList() {
		
		TestCase tc = TestData.repeatedTransactionSingleProivderNoCategorisations();
		
		List<CategorisedTransaction> categorisedTransactions = AutoCategoriser.categorise(tc.transactions());
	
		assertThat(categorisedTransactions)
			.hasSize(0);
		
	}
	

}
