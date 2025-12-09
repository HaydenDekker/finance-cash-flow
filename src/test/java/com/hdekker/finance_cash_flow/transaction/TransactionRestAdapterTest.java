package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.Transaction;

@SpringBootTest
public class TransactionRestAdapterTest {
	
	@Autowired
	TransactionRestAdapter transactionRestAdapter;
	
	@Test
	public void canAddTransaction() {
		
		TransactionTestData testData = new TransactionTestData();
		Transaction saved = transactionRestAdapter.save(testData.stub);
		assertThat(saved)
			.isNotNull();
		
		List<Transaction> all = transactionRestAdapter.list();
		assertThat(all)
			.hasSize(1);
		
	}
	
	@Test
	public void canAddTransactionCSVItem() {
		
		TransactionTestData testData = new TransactionTestData();
		String csvItem = testData.mockCSVData;
		Transaction saved = transactionRestAdapter.save(csvItem);
		assertThat(saved)
			.isNotNull();
		
	}

}
