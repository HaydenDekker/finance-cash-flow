package com.hdekker.finance_cash_flow.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdekker.finance_cash_flow.CategorisedTransactionReader;
import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;

@RestController
public class CategoryRestAdapter {
	
	@Autowired
	CategoryAllocator allocator;
	
	@Autowired
	CategorisedTransactionReader categorisedTransactionReader;
	
	@Autowired
	MissingCategorisedTransactionReader missingCategorisedTransactionReader;

	@PostMapping("/category")
	public CatorgorisedTransaction set(
			@RequestBody CatorgorisedTransaction transaction) {
		return allocator.allocate(transaction);
	}

	@GetMapping("/category")
	public List<CatorgorisedTransaction> list() {
		return categorisedTransactionReader.list();
	}

	@GetMapping("/category/incomplete")
	public List<CatorgorisedTransaction> listIncomplete() {
		return missingCategorisedTransactionReader.findAll()
					.stream()
					.map(t->new CatorgorisedTransaction(t, null, null, null, null, LocalDateTime.now()))
					.toList();
	}

}
