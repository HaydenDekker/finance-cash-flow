package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.TransactionReader;

@SpringBootTest
public class TransactionDatabaseTest {
	
	@Autowired
	TransactionPersister transactionPersistor;
	
	@Autowired
	TransactionReader transactionReader;
	
	TestData testData = new TestData();
	
	@Test
	public void givenTransaction_ExpectCanBeSavedAndReadFromDatabase() {
		
		Transaction transaction = transactionPersistor.persist(testData.stub);
		assertThat(transaction)
			.isNotNull();
		
		List<Transaction> allTransactions = transactionReader.list();
		
		assertThat(allTransactions.stream()
				.filter(t->t.description().contains(testData.stubDescription))
				.findFirst())
			.isPresent();
		
	}

}
