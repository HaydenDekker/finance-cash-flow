package com.hdekker.finance_cash_flow.app.forecast;

import java.time.YearMonth;
import java.util.List;

import com.hdekker.finance_cash_flow.CategorisedTransaction;

public class Forecaster {
	
	public static List<CategorisedTransaction> forcast(List<CategorisedTransaction> transactions){
		return ForecastGroupMapper.map(transactions)
				.entrySet()
				.stream()
				.filter(es->!es.getKey().name().trim().equals(""))
				.map(es-> ForecastMethodFactory.buildFor(
								es.getValue().get(0).expenseType())
							.forcast(es.getValue(), YearMonth.now().plusYears(1)))
				.flatMap(forecast->forecast.forcastedTransaction().stream())
				.toList();
	}

}
