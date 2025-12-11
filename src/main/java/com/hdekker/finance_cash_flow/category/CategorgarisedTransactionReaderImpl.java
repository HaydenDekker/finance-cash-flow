package com.hdekker.finance_cash_flow.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.finance_cash_flow.CategorisedTransactionLister;
import com.hdekker.finance_cash_flow.CategorisedTransactionReader;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.category.database.CatorgarisedTransactionRepository;

@Component
public class CategorgarisedTransactionReaderImpl implements CategorisedTransactionLister, CategorisedTransactionReader {

	@Autowired
	CatorgarisedTransactionRepository repo;
	
	@Override
	public List<CategorisedTransaction> list() {
		return repo.findAll().stream()
					.map(e-> e.toCatorgarisedTransaction())
					.toList();
	}

	@Override
	public CategorisedTransaction read(String id) {
		return repo.findById(id).map(e->e.toCatorgarisedTransaction()).orElseThrow();
	}

}
