package com.hdekker.finance_cash_flow.app.actual;

import java.util.List;

import java.util.function.Predicate;
import static java.util.stream.Collectors.*;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;


public class ExpenseFilter {
	
	final static Predicate<CategorisedTransaction> IS_INCOME = (ct)-> TransactionCategory.INCOME.equals(ct.category());
	
	public record ExpenseIncomeBreakdown(List<CategorisedTransaction> income, List<CategorisedTransaction> expense) {}
	
	public static ExpenseIncomeBreakdown breakdown(List<CategorisedTransaction> transactions) {
	        
	        return transactions.stream().collect(teeing(
	            
	            // 1. First Collector (to get Income)
	            filtering(IS_INCOME, toList()),
	            
	            // 2. Second Collector (to get Expense)
	            filtering(IS_INCOME.negate(), toList()),
	            
	            // 3. Merger Function (to combine the two lists into the final record)
	            ExpenseIncomeBreakdown::new // (list1, list2) -> new ExpenseIncomeBreakdown(list1, list2)
	        ));
	    }
	

}
