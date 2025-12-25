package com.hdekker.finance_cash_flow.app.budget;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter.ExpenseIncomeBreakdown;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;

public record BudgetOverview (
			SummedTransactionCategory monthlyIncomeTotal,
			Map<YearMonth, SummedTransactions> monthlyExpensesTotal,
			Map<YearMonth, SummedTransactions> netFlow,
			List<AmortizedExpense> amortizedExpense,
			List<SummedTransactionCategory> summedTransactionsByCategory) {
	
	public record AmortizedExpenseGroup(List<AmortizedExpense> items) {
		
		public Double amount() {
			return items.stream()
				.collect(Collectors.summingDouble(AmortizedExpense::amortizedValue));
		}
	}
	
	public Map<YearMonth, AmortizedExpenseGroup> netAmortizedExpenses(){
		
		Map<YearMonth, AmortizedExpenseGroup> byYearMonth = amortizedExpense().stream()
			.collect(
				Collectors.groupingBy(
						ae->ae.applicableMonth(),
						Collectors.collectingAndThen(
							Collectors.toList(),
							(l)-> new AmortizedExpenseGroup(l))
						)
				);
		
		return byYearMonth;
		
	}
	
	
	public Set<YearMonth> yearMonths(){
		
		// 1. Collect all unique month keys
		Set<YearMonth> uniqueMonths = new TreeSet<>();

		for (SummedTransactionCategory category : summedTransactionsByCategory) {
		    uniqueMonths.addAll(category.summedMonths().keySet());
		}
		
		uniqueMonths.addAll(monthlyIncomeTotal.summedMonths().keySet());
		
		return uniqueMonths;
	}
	
	public static List<AmortizedExpense> splitOutAmortizedValues(List<CategorisedTransaction> expenses) {
		
		return expenses.stream()
			.filter(ct-> ct.financialFrequency().equals(FinancialFrequency.ANNUALLY))
			.flatMap(ct-> {
			
					Double amortizedValue = ct.transaction().amount() / 12;
			
					return Stream.iterate(
							ct.transaction().localDate(), (d)-> d.plusMonths(1))
							.limit(12)
							.map(ld-> YearMonth.from(ld))
							.map(ym-> new AmortizedExpense(ct, ym, amortizedValue));
							
			})
			.toList();
					
	}
		
	private static final SummedTransactionCategory EMPTY_INCOME_RESULT = new SummedTransactionCategory(TransactionCategory.INCOME, List.of(), Map.of());
	
	public static BudgetOverview calculate(List<CategorisedTransaction> trans) {
		
		ExpenseIncomeBreakdown breakdown = ExpenseFilter.breakdown(trans);
		
		List<AmortizedExpense> amortized = splitOutAmortizedValues(breakdown.expense());
		
		List<SummedTransactionCategory> summedExpense = CategoryGroup.groupByCategoryAndByYearMonthAndSum(breakdown.expense());
		
		Optional<SummedTransactionCategory> income = Optional.empty();
		
		if(breakdown.income().size()>0) {
			income = Optional.of(
						CategoryGroup.groupByCategoryAndByYearMonthAndSum(
								breakdown.income())
							.get(0)
					);
		}
		
		List<Transaction> expenseTransactions = breakdown.expense().stream().map(ct->ct.transaction()).toList();
		Stream<Transaction> incomeTransactions = breakdown.income().stream().map(ct->ct.transaction());
		
		Map<YearMonth, SummedTransactions> monthlyExpensesTotal = HistoricalSummer.calculateTotal(
				expenseTransactions
			);
		
		Map<YearMonth, SummedTransactions> netCashFlow = HistoricalSummer.calculateTotal(
				Stream.concat(incomeTransactions, expenseTransactions.stream()).toList()
			);
		
		return new BudgetOverview(income.orElse(EMPTY_INCOME_RESULT), 
				monthlyExpensesTotal, 
				netCashFlow,
				amortized,
				summedExpense);
		
	}

}
