package com.hdekker.finance_cash_flow;

public record Transaction(
	    String date,
	    double amount,
	    String description) {}