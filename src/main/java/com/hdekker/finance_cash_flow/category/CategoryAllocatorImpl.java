package com.hdekker.finance_cash_flow.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionEntity;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionRepository;

@Component
public class CategoryAllocatorImpl implements CategoryAllocator{
	
	@Autowired
	CatorgarisedTransactionRepository repo;

	@Override
	public CatorgorisedTransaction allocate(CatorgorisedTransaction transaction) {
		return repo.save(CatorgarisedTransactionEntity.from(transaction))
				.toCatorgarisedTransaction();
	}

	
	
}
