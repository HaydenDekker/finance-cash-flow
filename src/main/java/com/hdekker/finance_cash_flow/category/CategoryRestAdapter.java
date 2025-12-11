package com.hdekker.finance_cash_flow.category;

import java.time.LocalDateTime;
import java.util.List;
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

}
