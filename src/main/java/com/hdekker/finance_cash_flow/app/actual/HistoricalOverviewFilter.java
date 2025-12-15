package com.hdekker.finance_cash_flow.app.actual;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter.ExpenseIncomeBreakdown;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;

public class HistoricalOverviewFilter {
	
	public record HistoricalOverview (
			SummedTransactionCategory monthlyIncomeTotal,
			Map<YearMonth, SummedTransactions> monthlyExpensesTotal,
			Map<YearMonth, SummedTransactions> difference,
			List<SummedTransactionCategory> summedTransactionsByCategory) {
		
		public Set<YearMonth> yearMonths(){
			
			// 1. Collect all unique month keys
			Set<YearMonth> uniqueMonths = new TreeSet<>();

			for (SummedTransactionCategory category : summedTransactionsByCategory) {
			    uniqueMonths.addAll(category.summedMonths().keySet());
			}
			
			return uniqueMonths;
		}
		
	}
	
	public static HistoricalOverview calculate(List<CategorisedTransaction> trans) {
		
		ExpenseIncomeBreakdown breakdown = ExpenseFilter.breakdown(trans);
		
		List<SummedTransactionCategory> summedExpense = CategoryGroup.groupByCategoryAndByYearMonthAndSum(breakdown.expense());
		List<SummedTransactionCategory> income = CategoryGroup.groupByCategoryAndByYearMonthAndSum(breakdown.income());
		
		List<Transaction> expenseTransactions = breakdown.expense().stream().map(ct->ct.transaction()).toList();
		Stream<Transaction> incomeTransactions = breakdown.income().stream().map(ct->ct.transaction());
		
		Map<YearMonth, SummedTransactions> monthlyExpensesTotal = HistoricalSummer.calculateTotal(
				expenseTransactions
			);
		
		Map<YearMonth, SummedTransactions> netCashFlow = HistoricalSummer.calculateTotal(
				Stream.concat(incomeTransactions, expenseTransactions.stream()).toList()
			);
		
		return new HistoricalOverview(income.get(0), 
				monthlyExpensesTotal, 
				netCashFlow,
				summedExpense);
		
	}

}
