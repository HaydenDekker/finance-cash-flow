package com.hdekker.finance_cash_flow.category.database;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hdekker.finance_cash_flow.CatorgarisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class CatorgarisedTransactionEntity {
	
    
	@Id
    private String id;

    public CatorgarisedTransactionEntity() {}
    
    @OneToOne
    TransactionEntitiy transaction;
    TransactionCategory category;
    LocalDateTime categoryTimeStamp;
    
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TransactionEntitiy getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionEntitiy transaction) {
		this.transaction = transaction;
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
	    e.setId(ct.transaction().createId());
	    e.setTransaction(TransactionEntitiy.from(ct.transaction()));
	    e.setCategoryTimeStamp(ct.assignmentTimeStamp());
	    e.setCategory(ct.category());
	    return e;
	    
	}
	
	public CatorgarisedTransaction toCatorgarisedTransaction() {
		
	    return new CatorgarisedTransaction(
	    		getTransaction().toTransaction(), 
	    		getCategory(), 
	    		getCategoryTimeStamp());
	    
	}

}