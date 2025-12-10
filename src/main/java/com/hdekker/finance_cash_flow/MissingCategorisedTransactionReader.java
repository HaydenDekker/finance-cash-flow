package com.hdekker.finance_cash_flow;

import java.util.List;

public interface MissingCategorisedTransactionReader {
	List<Transaction> findAll();
}
