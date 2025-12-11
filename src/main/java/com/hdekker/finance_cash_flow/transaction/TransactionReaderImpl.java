package com.hdekker.finance_cash_flow.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionLister;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntityRepository;

@Component
public class TransactionReaderImpl implements TransactionLister{
	
	@Autowired
	TransactionEntityRepository repo;

	@Override
	public List<Transaction> list() {
		return repo.findAll().stream()
				.map(TransactionEntitiy::toTransaction)
				.toList();
	}

}
