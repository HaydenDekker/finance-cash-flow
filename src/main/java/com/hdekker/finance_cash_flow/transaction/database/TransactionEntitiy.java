package com.hdekker.finance_cash_flow.transaction.database;

import java.time.LocalDate;

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
	
	LocalDate localDate;
	
	Double amount;
	
	String description;
	
	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

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

	public static TransactionEntitiy from(Transaction transaction) {
		
		String id = transaction.createId();
		
		TransactionEntitiy te = new TransactionEntitiy();
		te.setLocalDate(transaction.localDate());
		te.setDescription(transaction.description());
		te.setAmount(transaction.amount());
		te.setId(id);
		
		return te;
	}

	public Transaction toTransaction() {
		return new Transaction(
				getLocalDate(),
				getAmount(), 
				getDescription());
	}
	
	
	

}
