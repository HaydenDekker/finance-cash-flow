package com.hdekker.finance_cash_flow.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionDeleter;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntityRepository;

@Component
public class TransactionDeleterImpl implements TransactionDeleter {

	@Autowired
	TransactionEntityRepository repo;
	
	@Override
	public void delete(Transaction transaction) {
		repo.delete(TransactionEntitiy.from(transaction));
	}

}
