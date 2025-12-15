package com.hdekker.finance_cash_flow.category;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CategorisedTransactionDeleter;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.TransactionDeleter;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.transaction.TestData;

@SpringBootTest
class CategoryRestAdapterTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	@Autowired
	TransactionPersister transactionPersister;
	
	@Autowired
	CategorisedTransactionDeleter categorisedTransactionDeleter;
	
	@Autowired
	TransactionDeleter transactionDeleter;
	
    @Test
    void givenTransaction_ExpectCanAssignToCategory() {
    	
    	LocalDateTime dateTime = LocalDateTime.now();
    	
    	TestData data = new TestData();
    	
    	transactionPersister.persist(data.stub);
    	
    	CategorisedTransaction tran = categoryRestAdapter.set(
    			new CategorisedTransaction(
    					data.stub, 
    					TransactionCategory.ENTERTAINMENT, 
    					Necessity.REQUIRED,
    					new ForecastGroup("Test Expense"),
    					FinancialFrequency.AD_HOC,
    					ExpenseType.FIXED,
    					dateTime));
    
    	assertThat(tran)
    		.isNotNull();
    	
    	assertThat(dateTime)
    		.isEqualTo(tran.assignmentTimeStamp());
    	
    	List<CategorisedTransaction> listAssignements = categoryRestAdapter.list();
    	assertThat(listAssignements)
    		.hasSize(1);
    	
    	categorisedTransactionDeleter.delete(tran);
    	transactionDeleter.delete(data.stub);
    	
    }
    
}