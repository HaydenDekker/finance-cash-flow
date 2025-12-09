package com.hdekker.finance_cash_flow;

public enum TransactionCategory {
	
	INCOME("income"),
	HOUSING("Housing"),
	UTILITIES_SERVICES("Utilities & Services"), 
	FOOD_GROCERIES("Food & Groceries"), 
	TRANSPORTATION("Transportation"), 
	PERSONAL_CARE_HEALTH("Personal Care & Health"), 
	ENTERTAINMENT("Entertainment & Discretionary"), 
	SAVINGS_AND_DEBT("Savings & Debt");
	
	String displayName;
	
	TransactionCategory(String displayName){
		this.displayName = displayName;
	}
	
}
