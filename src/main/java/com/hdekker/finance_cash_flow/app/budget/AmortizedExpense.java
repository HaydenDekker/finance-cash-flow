package com.hdekker.finance_cash_flow.app.budget;

import java.time.YearMonth;

import com.hdekker.finance_cash_flow.CategorisedTransaction;

public record AmortizedExpense(
		CategorisedTransaction categorisedTransaction,
		YearMonth applicableMonth,
		Double amortizedValue
		) {

}
