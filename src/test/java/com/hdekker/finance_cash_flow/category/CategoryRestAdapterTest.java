package com.hdekker.finance_cash_flow.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CatorgarisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.transaction.TransactionTestData;

@SpringBootTest
class CategoryRestAdapterTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
    @Test
    void givenTransaction_ExpectCanAssignToCategory() {
    	
    	LocalDateTime dateTime = LocalDateTime.now();
    	
    	TransactionTestData data = new TransactionTestData();
    	
    	CatorgarisedTransaction tran = categoryRestAdapter.set(
    			new CatorgarisedTransaction(
    					data.stub, 
    					TransactionCategory.ENTERTAINMENT, 
    					dateTime));
    	
    	assertThat(tran)
    		.isNotNull();
    	
    	assertThat(dateTime)
    		.isEqualTo(tran.assignmentTimeStamp());
    	
    	List<CatorgarisedTransaction> listAssignements = categoryRestAdapter.list();
    	assertThat(listAssignements)
    		.hasSize(1);
    	
    }
    
}