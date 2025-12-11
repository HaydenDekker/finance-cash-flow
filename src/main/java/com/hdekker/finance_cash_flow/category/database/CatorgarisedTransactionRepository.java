package com.hdekker.finance_cash_flow.category.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hdekker.finance_cash_flow.transaction.database.TransactionEntitiy;

public interface CatorgarisedTransactionRepository extends JpaRepository<CatorgarisedTransactionEntity, String> {
    
	/**
     * Finds all TransactionEntitiy records that do not have an associated
     * CatorgarisedTransactionEntity (i.e., they are uncategorized).
     * * This uses a LEFT JOIN from TransactionEntitiy to CatorgarisedTransactionEntity
     * and filters for cases where the CatorgarisedTransactionEntity's transaction
     * association is NULL.
     * * @return A list of uncategorized TransactionEntitiy objects.
     */
    @Query("SELECT t FROM TransactionEntitiy t LEFT JOIN CatorgarisedTransactionEntity ct ON t.id = ct.transaction.id WHERE ct.transaction.id IS NULL")
    List<TransactionEntitiy> findUncategorisedTransactions();
    
}