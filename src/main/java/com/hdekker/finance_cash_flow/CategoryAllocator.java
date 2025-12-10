package com.hdekker.finance_cash_flow;

public interface CategoryAllocator {
	CategorisedTransaction allocate(CategorisedTransaction transaction);
}
