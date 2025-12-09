package com.hdekker.finance_cash_flow.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntityRepository;

@Component
public class TransactionPersisterImpl implements TransactionPersister {
	
	@Autowired
	TransactionEntityRepository repo;

	@Override
	public Transaction persist(Transaction transaction) {
		
		return TransactionEntitiy.to(
				repo.save(
					TransactionEntitiy.from(transaction)));
	}
	
	

}
