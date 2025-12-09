package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.Transaction;

@SpringBootTest
public class TransactionDatabaseTest {
	
	@Autowired
	TransactionPersister transactionPersistor;
	
	@Autowired
	TransactionReader transactionReader;
	
	String stubDescription = "sweets";
	LocalDate date = LocalDate.now();
	Transaction stub = new Transaction(
			date, 
			0.0, 
			stubDescription);
	
	@Test
	public void givenTransaction_ExpectCanBeSavedAndReadFromDatabase() {
		
		Transaction transaction = transactionPersistor.persist(stub);
		assertThat(transaction)
			.isNotNull();
		
		List<Transaction> allTransactions = transactionReader.list();
		
		assertThat(allTransactions.stream()
				.filter(t->t.description().contains(stubDescription))
				.findFirst())
			.isPresent();
		
	}

}
