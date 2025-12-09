package com.hdekker.finance_cash_flow.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CatorgarisedTransaction;

@RestController
public class CategoryRestAdapter {
	
	@Autowired
	CategoryAllocator allocator;

	@PostMapping("/category")
	public CatorgarisedTransaction set(
			@RequestBody CatorgarisedTransaction transaction) {
		return allocator.allocate(transaction);
	}

}
