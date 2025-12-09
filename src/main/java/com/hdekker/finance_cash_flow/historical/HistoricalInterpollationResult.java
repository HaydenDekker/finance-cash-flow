package com.hdekker.finance_cash_flow.historical;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.hdekker.finance_cash_flow.Transaction;

public record HistoricalInterpollationResult(
		QuadraticResult quadraticResult,
		List<Transaction> transactions
		) {
	
	public record QuadraticCoefficients(double a, double b, double c) {}
	
	/**
	 * A Java record to represent a quadratic function f(x) = a*x^2 + b*x + c.
	 * Records are perfect for carrying immutable data and providing utility methods.
	 */
	public record QuadraticResult(QuadraticCoefficients coefficients, YearMonth startMonth) {

	    /**
	     * Calculates the value of the quadratic function for a given input x.
	     * f(x) = a*x^2 + b*x + c
	     *
	     * @param x The input value.
	     * @return The resulting value of the function.
	     */
		public double evaluate(YearMonth targetMonth) {
	        // Step 1: Calculate the time index 'x'
	        // x is the number of months between the startMonth and the targetMonth.
	        long x_long = ChronoUnit.MONTHS.between(startMonth, targetMonth);
	        double x = (double) x_long; // Convert to double for math operations

	        // Retrieve coefficients
	        double a = coefficients.a();
	        double b = coefficients.b();
	        double c = coefficients.c();

	        // Step 2: Evaluate the quadratic equation
	        // y = a*x^2 + b*x + c
	        double termA = a * x * x; 
	        double termB = b * x;
	        double termC = c;
	        
	        return termA + termB + termC;
	    }

	
	}

}
