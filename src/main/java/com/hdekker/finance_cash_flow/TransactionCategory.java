package com.hdekker.finance_cash_flow;

public enum TransactionCategory {
	
	INCOME("income"),
	HOUSING("Housing"),
	UTILITIES_SERVICES("Utilities & Services"), 
	FOOD_GROCERIES("Food & Groceries"), 
	TRANSPORTATION("Transportation"), 
	PERSONAL_CARE_HEALTH("Personal Care & Health"), 
	ENTERTAINMENT("Entertainment & Discretionary"),
	GIFTS_AND_DONATIONS("Gifts and donations"),
	CHILDCARE_AND_SCHOOLING("Childcare and Schooling"),
	SAVINGS_AND_DEBT("Savings & Debt"),
	PROFESSIONAL("Professional"),
	CLOTHING("Clothing"),
	UNKNOWN("Unknown");
	
	String displayName;
	
	TransactionCategory(String displayName){
		this.displayName = displayName;
	}
	
}
