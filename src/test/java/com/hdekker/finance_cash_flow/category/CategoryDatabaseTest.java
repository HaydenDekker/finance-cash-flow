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
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;
import com.hdekker.finance_cash_flow.Transaction;
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
	
	@Autowired
	MissingCategorisedTransactionReader missingCategorisedTransactionReader;
	
	@Test
	public void canWrtieReadCategoryData() {
		
		TransactionTestData data = new TransactionTestData();
		CategorisedTransaction ct = new CategorisedTransaction(
				data.stub,
				TransactionCategory.ENTERTAINMENT, 
				Necessity.REQUIRED,
				FinancialFrequency.AD_HOC,
				ExpenseType.FIXED,
				LocalDateTime.now());
		
		// need transient saved beforehand
		transactionPersister.persist(data.stub);
		
		ct = categoryAllocator.allocate(ct);
		assertThat(ct)
			.isNotNull();
		
		List<CategorisedTransaction> list = categorisedTransactionReader.list();
		assertThat(list)
			.hasSizeGreaterThan(0);
		
		categorisedTransactionDeleter.delete(ct);
		List<CategorisedTransaction> listAfterDeletion = categorisedTransactionReader.list();
		assertThat(listAfterDeletion.size())
			.isLessThan(list.size());
		
		
	}
	
	@Test
	public void canGetTransactionsWithoutACategoryAllocation() {
		
		TransactionTestData data = new TransactionTestData();
		CategorisedTransaction ct = new CategorisedTransaction(
				data.stub, 
				TransactionCategory.ENTERTAINMENT, 
				Necessity.REQUIRED,
				FinancialFrequency.AD_HOC,
				ExpenseType.FIXED,
				LocalDateTime.now());
		
		// need transient saved beforehand
		transactionPersister.persist(data.stub);
		transactionPersister.persist(data.stub2); // will have no associated CT
		ct = categoryAllocator.allocate(ct);
		
		List<Transaction> listMissing = missingCategorisedTransactionReader.findAll();
		
		assertThat(listMissing)
			.hasSize(1);
		assertThat(listMissing.get(0)
				.description())
			.isEqualTo(data.stub2.description());
		
	}

}
