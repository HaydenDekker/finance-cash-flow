package com.hdekker.finance_cash_flow.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionEntity;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionRepository;

@Component
public class CategoryAllocatorImpl implements CategoryAllocator{
	
	@Autowired
	CatorgarisedTransactionRepository repo;

	@Override
	public CategorisedTransaction allocate(CategorisedTransaction transaction) {
		return repo.save(CatorgarisedTransactionEntity.from(transaction))
				.toCatorgarisedTransaction();
	}

	
	
}
