package com.hdekker.finance_cash_flow.app.category;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;

public class CategoryGroup {

	public static Map<TransactionCategory, Map<YearMonth, List<CategorisedTransaction>>> groupByCategoryAndByYearMonth(List<CategorisedTransaction> transactions){
		return transactions.stream()
	            .collect(
	                    Collectors.groupingBy(
	                        // First level grouping: by Category
	                    	CategorisedTransaction::category,
	                        
	                        // Second level grouping: by YearMonth
	                        Collectors.groupingBy(
	                        	CategorisedTransaction::getTransactionYearMonth,
	                            Collectors.toList() // Final collector: list of transactions
	                        )
	                    )
	                );
	}
	
}
