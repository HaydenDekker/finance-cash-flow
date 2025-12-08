package com.hdekker.finance_cash_flow.transaction;

import com.hdekker.finance_cash_flow.Transaction;

public interface TransactionPersister {
	
	Transaction persist(Transaction transaction);

}
