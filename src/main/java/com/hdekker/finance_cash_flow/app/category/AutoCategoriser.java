package com.hdekker.finance_cash_flow.app.category;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;

public class AutoCategoriser {

	public static List<CategorisedTransaction> categorise(List<CategorisedTransaction> transactions) {
		
		Map<ForecastGroup, List<CategorisedTransaction>> forcastGroups = transactions.stream()
				.filter(ct->!(ct.forcastGroup()==null || ct.forcastGroup().name().equals("")))
				.collect(Collectors.groupingBy(CategorisedTransaction::forcastGroup));
		
		if(forcastGroups.size()>1) return List.of();
		
		Optional<CategorisedTransaction> toCopyOpt = transactions.stream()
				.filter(ct->ct.isComplete())
				.findAny();
		
		if(toCopyOpt.isEmpty()) return List.of();
		
		CategorisedTransaction toCopy = toCopyOpt.get();
		
		return transactions.stream()
				.filter(ct-> !ct.isComplete())
				.map(ct-> new CategorisedTransaction(
								ct.transaction(), 
								ct.transactionDescriptionSearchKeyword(), 
								toCopy.category(), 
								toCopy.necessity(), 
								toCopy.forcastGroup(), 
								toCopy.financialFrequency(), 
								toCopy.expenseType())
				)
				.toList();
			
		
	}

}
