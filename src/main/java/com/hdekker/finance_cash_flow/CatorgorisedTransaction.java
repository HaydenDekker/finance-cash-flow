package com.hdekker.finance_cash_flow;

import java.time.LocalDateTime;

public record CatorgorisedTransaction(
		Transaction transaction,
		TransactionCategory category,
		LocalDateTime assignmentTimeStamp) {
}
