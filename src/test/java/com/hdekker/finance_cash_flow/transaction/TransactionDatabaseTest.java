package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.Transaction;

@SpringBootTest
public class TransactionDatabaseTest {
	
	@Autowired
	TransactionPersister transactionPersistor;
	
	@Test
	public void givenTransaction_ExpectCanBeSavedAndReadFromDatabase() {
		
		Transaction stub = new Transaction("Hello", 0.0, "sweets");
		Transaction transaction = transactionPersistor.persist(stub);
		assertThat(transaction)
			.isNotNull();
		
	}

}
