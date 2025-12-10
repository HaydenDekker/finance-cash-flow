package com.hdekker.finance_cash_flow.category.database;

import java.time.LocalDateTime;

import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;

import jakarta.persistence.Entity;
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
    Necessity necessity;
    FinancialFrequency financialFrequency;
	ExpenseType expenseType;
    LocalDateTime categoryTimeStamp;
    
    
    
    
	public FinancialFrequency getFinancialFrequency() {
		return financialFrequency;
	}

	public void setFinancialFrequency(FinancialFrequency financialFrequency) {
		this.financialFrequency = financialFrequency;
	}

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public Necessity getNecessity() {
		return necessity;
	}

	public void setNecessity(Necessity necessity) {
		this.necessity = necessity;
	}

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

	public static CatorgarisedTransactionEntity from(CatorgorisedTransaction ct) {
		
	    CatorgarisedTransactionEntity e = new CatorgarisedTransactionEntity();
	    e.setId(ct.transaction().createId());
	    e.setTransaction(TransactionEntitiy.from(ct.transaction()));
	    e.setCategoryTimeStamp(ct.assignmentTimeStamp());
	    e.setCategory(ct.category());
	    e.setNecessity(ct.necessity());
	    e.setFinancialFrequency(ct.financialFrequency());
	    e.setExpenseType(ct.expenseType());
	    return e;
	    
	}
	
	public CatorgorisedTransaction toCatorgarisedTransaction() {
		
	    return new CatorgorisedTransaction(
	    		getTransaction().toTransaction(), 
	    		getCategory(), 
	    		getNecessity(),
	    		getFinancialFrequency(),
	    		getExpenseType(),
	    		getCategoryTimeStamp());
	    
	}

}