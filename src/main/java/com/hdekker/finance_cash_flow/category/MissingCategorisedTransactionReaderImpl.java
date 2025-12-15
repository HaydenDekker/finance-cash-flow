package com.hdekker.finance_cash_flow.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionRepository;

@Component
public class MissingCategorisedTransactionReaderImpl implements MissingCategorisedTransactionReader{

	@Autowired
	CatorgarisedTransactionRepository repo;
	
	@Override
	public List<CategorisedTransaction> findAll() {
		return repo.findUncategorisedTransactions().stream()
					.map(e->e.toTransaction())
					.map(t->new CategorisedTransaction(t, null, null, null, null, null, LocalDateTime.now()))
					.toList();
	}

}
