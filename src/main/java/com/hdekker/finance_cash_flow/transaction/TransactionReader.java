package com.hdekker.finance_cash_flow.transaction;

import java.util.List;

import com.hdekker.finance_cash_flow.Transaction;

public interface TransactionReader {
	
	List<Transaction> list();

}
