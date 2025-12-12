package com.hdekker.finance_cash_flow.category;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdekker.finance_cash_flow.CategorisedTransactionLister;
import com.hdekker.finance_cash_flow.CategorisedTransactionReader;
import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;
import com.hdekker.finance_cash_flow.app.actual.ExpenseFilter;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;

@RestController
public class CategoryRestAdapter {
	
	@Autowired
	CategoryAllocator allocator;
	
	@Autowired
	CategorisedTransactionLister categorisedTransactionLister;
	
	@Autowired
	MissingCategorisedTransactionReader missingCategorisedTransactionReader;
	
	@Autowired
	CategorisedTransactionReader categorisedTransactionReader;

	@PostMapping("/category")
	public CategorisedTransaction set(
			@RequestBody CategorisedTransaction transaction) {
		return allocator.allocate(transaction);
	}

	@GetMapping("/category")
	public List<CategorisedTransaction> list() {
		return categorisedTransactionLister.list();
	}

	@GetMapping("/category/incomplete")
	public List<CategorisedTransaction> listIncomplete() {
		return missingCategorisedTransactionReader.findAll()
					.stream()
					.map(t->new CategorisedTransaction(t, null, null, null, null, LocalDateTime.now()))
					.toList();
	}
	
	@GetMapping("/category/grouped-and-summed")
	public List<SummedTransactionCategory> groupedAndSummed(){
		return CategoryGroup.groupByCategoryAndByYearMonthAndSum(list());
	}
	
	@GetMapping("/category/{objectKey}")
	public CategorisedTransaction findById(
			@PathVariable("objectKey") String objectKey) {
		return categorisedTransactionReader.read(objectKey);
	}
	
	public record HistoricalOverview(
			//Map<YearMonth, SummedTransactions> monthlyIncomeTotal,
			Map<YearMonth, SummedTransactions> monthlyExpensesTotal,
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

	@GetMapping("/category/historical-overview")
	public HistoricalOverview historicalOverview() {
		
		List<CategorisedTransaction> trans = list();
		List<CategorisedTransaction> expenses = ExpenseFilter.filter(trans);
		List<SummedTransactionCategory> summed = CategoryGroup.groupByCategoryAndByYearMonthAndSum(trans);
		Map<YearMonth, SummedTransactions> monthlyExpensesTotal = HistoricalSummer.calculateTotal(
				expenses.stream().map(ct->ct.transaction()).toList()
			);
		return new HistoricalOverview(monthlyExpensesTotal, summed);
	}

}
