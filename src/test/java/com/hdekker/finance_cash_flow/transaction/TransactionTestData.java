package com.hdekker.finance_cash_flow.transaction;

import java.time.LocalDate;
import java.util.List;

import com.hdekker.finance_cash_flow.Transaction;

public class TransactionTestData {
	
	String stubDescription = "sweets";
	LocalDate date = LocalDate.now();
	public Transaction stub = new Transaction(
			date, 
			0.0, 
			stubDescription);
	
	String descriptionStub = "PAYMENT TO TELSTRA SERVICES 0PME5S2S";
    String mockCSVData = "\"3/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
    String mockCSVDataDoubleDigitDay = "\"24/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
    
    public List<Transaction> testTransactions(){
    	return List.of(
    		new Transaction(LocalDate.of(2024,5,15), 3.0, "Groceries"),
    		new Transaction(LocalDate.of(2024,6,15), 6.0, "Fuel"),
    		new Transaction(LocalDate.of(2024,7,15), 11.0, "Fuel"),
    		new Transaction(LocalDate.of(2024,8,15), 18.0, "Groceries"),
    		new Transaction(LocalDate.of(2024,9,15), 27.0, "Rego"),
    		new Transaction(LocalDate.of(2024,10,15), 38.0, "Groceries"),
    		new Transaction(LocalDate.of(2024,11,15), 51.0, "Fuel"),
    		new Transaction(LocalDate.of(2025,1,15), 66.0, "Groceries"),
    		new Transaction(LocalDate.of(2025,2,15), 83.0, "Fuel"),
    		new Transaction(LocalDate.of(2025,5,15), 102.0, "Rego")
    	);
    }

}
