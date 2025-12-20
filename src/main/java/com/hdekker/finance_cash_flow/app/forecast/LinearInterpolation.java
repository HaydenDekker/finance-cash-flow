package com.hdekker.finance_cash_flow.app.forecast;

import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;

import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;

public record LinearInterpolation (double slope, double intercept, YearMonth startMonth) {
	    
	    public double evaluate(YearMonth targetMonth) {
	        long x = ChronoUnit.MONTHS.between(startMonth, targetMonth);
	        return (slope * x) + intercept;
	    }

	    public static LinearInterpolation fit(Map<YearMonth, SummedTransactions> data) {
	        TreeMap<YearMonth, SummedTransactions> sortedData = new TreeMap<>(data);
	        YearMonth startMonth = sortedData.firstKey();
	        int n = sortedData.size();

	        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

	        for (var entry : sortedData.entrySet()) {
	            double x = ChronoUnit.MONTHS.between(startMonth, entry.getKey());
	            double y = entry.getValue().amount();
	            sumX += x;
	            sumY += y;
	            sumXY += x * y;
	            sumX2 += x * x;
	        }

	        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
	        double intercept = (sumY - slope * sumX) / n;

	        return new LinearInterpolation(slope, intercept, startMonth);
	    }
	}
