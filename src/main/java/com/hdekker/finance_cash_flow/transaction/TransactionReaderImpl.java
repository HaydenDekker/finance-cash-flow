package com.hdekker.finance_cash_flow.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntityRepository;

@Component
public class TransactionReaderImpl implements TransactionReader{
	
	@Autowired
	TransactionEntityRepository repo;

	@Override
	public List<Transaction> list() {
		return repo.findAll().stream()
				.map(te-> TransactionEntitiy.to(te))
				.toList();
	}

}
