package com.hdekker.finance_cash_flow.category.database;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hdekker.finance_cash_flow.CatorgarisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionCategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CatorgarisedTransactionEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public CatorgarisedTransactionEntity() {}
    
    String transactionId;
    LocalDate date;
    Double amount;
    String description;
    TransactionCategory category;
    LocalDateTime categoryTimeStamp;
    
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
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

	public TransactionCategory getCategory() {
		return category;
	}

	public void setCategory(TransactionCategory category) {
		this.category = category;
	}

	public LocalDateTime getCategoryTimeStamp() {
		return categoryTimeStamp;
	}

	public void setCategoryTimeStamp(LocalDateTime categoryTimeStamp) {
		this.categoryTimeStamp = categoryTimeStamp;
	}

	public static CatorgarisedTransactionEntity from(CatorgarisedTransaction ct) {
	    CatorgarisedTransactionEntity e = new CatorgarisedTransactionEntity();
	    String transactionId = ct.transaction().createId();
	    e.setTransactionId(transactionId);
	    e.setCategoryTimeStamp(ct.assignmentTimeStamp());
	    e.setDescription(ct.transaction().description());
	    e.setAmount(ct.transaction().amount());
	    e.setDate(ct.transaction().localDate());
	    e.setCategory(ct.category());
	    return e;
	}
	
	public static CatorgarisedTransaction to(CatorgarisedTransactionEntity entity) {
	    Transaction tx = new Transaction(
	            entity.getDate(),
	            entity.getAmount(),
	            entity.getDescription());
	    return new CatorgarisedTransaction(
	    		tx, 
	    		entity.getCategory(), 
	    		entity.getCategoryTimeStamp());
	}

}