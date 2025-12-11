package com.hdekker.finance_cash_flow.app.category;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;

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
	
	public record SummedTransactionCategory (TransactionCategory category, Map<YearMonth, SummedTransactions> summedMonths) {}

	public static List<SummedTransactionCategory> groupByCategoryAndByYearMonthAndSum(
			List<CategorisedTransaction> transactions) {
		return transactions.stream()
	            .collect(
	                    Collectors.groupingBy(
	                        // First level grouping: by Category
	                    	CategorisedTransaction::category,
	                        
	                    	Collectors.collectingAndThen(
                                // 2a. First, collect all CategorisedTransaction objects for the category into a List.
                                Collectors.toList(),
                                
                                // 2b. Then, apply a transformation function to that List.
                                (List<CategorisedTransaction> categoryTransactions) -> {
                                    
                                    // Map the CategorisedTransaction list to a Transaction list 
                                    // as required by HistoricalSummer::calculateTotal
                                    List<com.hdekker.finance_cash_flow.Transaction> simpleTransactions = 
                                        categoryTransactions.stream()
                                            .map(CategorisedTransaction::transaction)
                                            .collect(Collectors.toList());
                                            
                                    // Use the calculateTotal method to perform the second level grouping and summing
                                    return HistoricalSummer.calculateTotal(simpleTransactions);
                                }
                            )
	                    )
	                )
	            .entrySet()
	            .stream()
	            .map(es->new SummedTransactionCategory(es.getKey(), es.getValue()))
	            .toList();
	}
	
}
