package com.hdekker.finance_cash_flow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Transaction(
	    LocalDate localDate,
	    double amount,
	    String description) {
	
	final public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
	
	public String createId() {
		return formatter.format(localDate) + "-" + amount() + "-" + description();
	}
	
	public String dateString() {
		return localDate.format(formatter);
	}
	
	
}