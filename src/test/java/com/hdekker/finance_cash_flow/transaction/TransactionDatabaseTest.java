package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
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
	
	TransactionTestData transactionTestData = new TransactionTestData();
	
	@Test
	public void givenTransaction_ExpectCanBeSavedAndReadFromDatabase() {
		
		Transaction transaction = transactionPersistor.persist(transactionTestData.stub);
		assertThat(transaction)
			.isNotNull();
		
		List<Transaction> allTransactions = transactionReader.list();
		
		assertThat(allTransactions.stream()
				.filter(t->t.description().contains(transactionTestData.stubDescription))
				.findFirst())
			.isPresent();
		
	}

}
