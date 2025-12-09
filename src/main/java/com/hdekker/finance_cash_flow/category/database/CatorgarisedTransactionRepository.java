package com.hdekker.finance_cash_flow.category.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatorgarisedTransactionRepository extends JpaRepository<CatorgarisedTransactionEntity, Long> {
    // Custom query methods
}