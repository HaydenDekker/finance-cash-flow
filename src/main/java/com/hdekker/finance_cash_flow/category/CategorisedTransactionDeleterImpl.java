package com.hdekker.finance_cash_flow.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.CategorisedTransactionDeleter;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionEntity;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionRepository;

@Component
public class CategorisedTransactionDeleterImpl implements CategorisedTransactionDeleter{

	@Autowired
	CatorgarisedTransactionRepository repo;
	
	@Override
	public void delete(CategorisedTransaction transaction) {
		repo.delete(CatorgarisedTransactionEntity.from(transaction));
	}

}
