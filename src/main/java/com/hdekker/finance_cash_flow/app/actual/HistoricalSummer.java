package com.hdekker.finance_cash_flow.app.actual;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.Transaction;

public class HistoricalSummer {

    public static Map<YearMonth, Double> calculateTotal(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.localDate()), Collectors.summingDouble(Transaction::amount)));
    }

}
