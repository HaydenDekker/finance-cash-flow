package com.hdekker.finance_cash_flow.transaction;

import java.time.LocalDate;

import com.hdekker.finance_cash_flow.Transaction;

public class TransactionTestData {
	
	String stubDescription = "sweets";
	LocalDate date = LocalDate.now();
	Transaction stub = new Transaction(
			date, 
			0.0, 
			stubDescription);
	
	String descriptionStub = "PAYMENT TO TELSTRA SERVICES 0PME5S2S";
    String mockCSVData = "\"3/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
    String mockCSVDataDoubleDigitDay = "\"24/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";

}
