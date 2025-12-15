package com.hdekker.finance_cash_flow.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CategorisedTransactionDeleter;
import com.hdekker.finance_cash_flow.CategorisedTransactionLister;
import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.TransactionDeleter;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.transaction.TestData;

@SpringBootTest
public class CategoryDatabaseTest {
	
	@Autowired
	CategoryAllocator categoryAllocator;
	
	@Autowired
	CategorisedTransactionLister categorisedTransactionLister;
	
	@Autowired
	TransactionPersister transactionPersister;
	
	@Autowired
	TransactionDeleter transactionDeleter;
	
	@Autowired
	CategorisedTransactionDeleter categorisedTransactionDeleter;
	
	@Autowired
	MissingCategorisedTransactionReader missingCategorisedTransactionReader;
	
	@Test
	public void canWrtieReadCategoryData() {
		
		TestData data = new TestData();
		CategorisedTransaction ct = new CategorisedTransaction(
				data.stub,
				TransactionCategory.ENTERTAINMENT, 
				Necessity.REQUIRED,
				"Test Expense",
				FinancialFrequency.AD_HOC,
				ExpenseType.FIXED,
				LocalDateTime.now());
		
		// need transient saved beforehand
		transactionPersister.persist(data.stub);
		
		ct = categoryAllocator.allocate(ct);
		assertThat(ct)
			.isNotNull();
		
		List<CategorisedTransaction> list = categorisedTransactionLister.list();
		assertThat(list)
			.hasSizeGreaterThan(0);
		
		categorisedTransactionDeleter.delete(ct);
		List<CategorisedTransaction> listAfterDeletion = categorisedTransactionLister.list();
		assertThat(listAfterDeletion.size())
			.isLessThan(list.size());
		
		transactionDeleter.delete(data.stub);
		
	}
	
	@Test
	public void canGetTransactionsWithoutACategoryAllocation() {
		
		TestData data = new TestData();
		CategorisedTransaction ct = new CategorisedTransaction(
				data.stub, 
				TransactionCategory.ENTERTAINMENT, 
				Necessity.REQUIRED,
				"Test Expense",
				FinancialFrequency.AD_HOC,
				ExpenseType.FIXED,
				LocalDateTime.now()); 
		
		// need transient saved beforehand
		transactionPersister.persist(data.stub);
		transactionPersister.persist(data.stub2); // will have no associated CT
		ct = categoryAllocator.allocate(ct);
		
		List<CategorisedTransaction> listMissing = missingCategorisedTransactionReader.findAll();
		
		assertThat(listMissing)
			.hasSize(1);
		assertThat(listMissing.get(0)
				.transaction()
				.description())
			.isEqualTo(data.stub2.description());
		
		categorisedTransactionDeleter.delete(ct);
		transactionDeleter.delete(data.stub);
		transactionDeleter.delete(data.stub2);
		
	}

}
