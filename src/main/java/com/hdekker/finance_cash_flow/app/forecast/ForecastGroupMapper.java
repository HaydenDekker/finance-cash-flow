package com.hdekker.finance_cash_flow.app.forecast;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;

public class ForecastGroupMapper {
	
	public static Map<ForecastGroup, List<CategorisedTransaction>> map(List<CategorisedTransaction> transactions){
		return transactions.stream()
					.collect(Collectors.groupingBy(CategorisedTransaction::forcastGroup));
	}

}
