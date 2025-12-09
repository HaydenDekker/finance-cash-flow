package com.hdekker.finance_cash_flow.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CategorisedTransactionDeleter;
import com.hdekker.finance_cash_flow.CategorisedTransactionReader;
import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.transaction.TransactionTestData;

@SpringBootTest
public class CategoryDatabaseTest {
	
	@Autowired
	CategoryAllocator categoryAllocator;
	
	@Autowired
	CategorisedTransactionReader categorisedTransactionReader;
	
	@Autowired
	TransactionPersister transactionPersister;
	
	@Autowired
	CategorisedTransactionDeleter categorisedTransactionDeleter;
	
	@Test
	public void canWrtieReadCategoryData() {
		
		TransactionTestData data = new TransactionTestData();
		CatorgorisedTransaction ct = new CatorgorisedTransaction(
				data.stub, 
				TransactionCategory.ENTERTAINMENT, 
				LocalDateTime.now());
		
		// need transient saved beforehand
		transactionPersister.persist(data.stub);
		
		ct = categoryAllocator.allocate(ct);
		assertThat(ct)
			.isNotNull();
		
		List<CatorgorisedTransaction> list = categorisedTransactionReader.list();
		assertThat(list)
			.hasSizeGreaterThan(0);
		
		categorisedTransactionDeleter.delete(ct);
		List<CatorgorisedTransaction> listAfterDeletion = categorisedTransactionReader.list();
		assertThat(listAfterDeletion.size())
			.isLessThan(list.size());
		
		
	}

}
