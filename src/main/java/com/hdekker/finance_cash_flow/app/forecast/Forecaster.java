package com.hdekker.finance_cash_flow.app.forecast;

import java.time.YearMonth;
import java.util.List;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;

public class Forecaster {
	
	private static CategorisedTransaction setForecastGroupAsCategoryName(CategorisedTransaction tran) {
		return new CategorisedTransaction(
				tran.transaction(),
				tran.transactionDescriptionSearchKeyword(),
				tran.category(), 
				tran.necessity(), 
				new ForecastGroup(tran.category().name()),
				tran.financialFrequency(),
				ExpenseType.VARIABLE, 
				tran.assignmentTimeStamp());
	}
	
	public static List<CategorisedTransaction> forcast(List<CategorisedTransaction> transactions){
		
		List<CategorisedTransaction> autoAssignForecastGroupName = transactions.stream()
			.map(tran-> {
			if(tran.forcastGroup().name().equals("")) {
				return setForecastGroupAsCategoryName(tran);
			}
			return tran;
			})
			.toList();
		
		return ForecastGroupMapper
				.map(autoAssignForecastGroupName)
				.values()
				.stream()
				.map(es-> ForecastMethodFactory.buildFor(
								es.get(0).expenseType())
							.forcast(es, YearMonth.now().plusYears(1)))
				.flatMap(forecast->forecast.forcastedTransaction().stream())
				.toList();
	}

}
