package com.hdekker.finance_cash_flow.category;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hdekker.finance_cash_flow.CategorisedTransactionReader;
import com.hdekker.finance_cash_flow.CategoryAllocator;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.MissingCategorisedTransactionReader;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup;

@RestController
public class CategoryRestAdapter {
	
	@Autowired
	CategoryAllocator allocator;
	
	@Autowired
	CategorisedTransactionReader categorisedTransactionReader;
	
	@Autowired
	MissingCategorisedTransactionReader missingCategorisedTransactionReader;

	@PostMapping("/category")
	public CategorisedTransaction set(
			@RequestBody CategorisedTransaction transaction) {
		return allocator.allocate(transaction);
	}

	@GetMapping("/category")
	public List<CategorisedTransaction> list() {
		return categorisedTransactionReader.list();
	}

	@GetMapping("/category/incomplete")
	public List<CategorisedTransaction> listIncomplete() {
		return missingCategorisedTransactionReader.findAll()
					.stream()
					.map(t->new CategorisedTransaction(t, null, null, null, null, LocalDateTime.now()))
					.toList();
	}
	
	@GetMapping("/category/grouped")
	public Map<TransactionCategory, Map<YearMonth, List<CategorisedTransaction>>> grouped(){
		return CategoryGroup.groupByCategoryAndByYearMonth(list());
	}
	
	@GetMapping("/category/grouped-and-summed")
	public Map<TransactionCategory, Map<YearMonth, SummedTransactions>> groupedAndSummed(){
		return CategoryGroup.groupByCategoryAndByYearMonthAndSum(list());
	}

}
