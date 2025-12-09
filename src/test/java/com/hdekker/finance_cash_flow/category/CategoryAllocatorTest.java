package com.hdekker.finance_cash_flow.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.finance_cash_flow.CatorgarisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.transaction.TransactionTestData;

@SpringBootTest
class CategoryAllocatorTest {
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
    @Test
    void givenTransaction_ExpectCanAssignToCategory() {
    	
    	TransactionTestData data = new TransactionTestData();
    	CatorgarisedTransaction tran = categoryRestAdapter.set(
    			new CatorgarisedTransaction(data.stub, TransactionCategory.ENTERTAINMENT));
    	assertThat(tran)
    		.isNotNull();
    	
    }
}