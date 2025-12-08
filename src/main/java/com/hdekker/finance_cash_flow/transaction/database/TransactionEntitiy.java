package com.hdekker.finance_cash_flow.transaction.database;

import com.hdekker.finance_cash_flow.Transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * 
 */
@Entity
public class TransactionEntitiy {
	
	@Id
	String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	String date;
	
	Double amount;
	
	String description;
	
	
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private static String createId(Transaction transaction) {
		return transaction.date() + "-" + transaction.amount() + "-" + transaction.description();
	}

	public static TransactionEntitiy from(Transaction transaction) {
		
		String id = createId(transaction);
		
		TransactionEntitiy te = new TransactionEntitiy();
		te.setDate(transaction.date());
		te.setDescription(transaction.description());
		te.setAmount(transaction.amount());
		te.setId(id);
		
		return te;
	}

	public static Transaction to(TransactionEntitiy entity) {
		return new Transaction(entity.getDate(), entity.getAmount(), entity.getDescription());
	}
	
	
	

}
