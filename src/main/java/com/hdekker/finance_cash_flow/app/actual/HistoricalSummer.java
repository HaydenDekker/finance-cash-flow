package com.hdekker.finance_cash_flow.app.actual;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hdekker.finance_cash_flow.Transaction;

public class HistoricalSummer {
	
	public record SummedTransactions(List<Transaction> transactions, Double amount) {}
  
    public static Map<YearMonth, SummedTransactions> calculateTotal(List<Transaction> transactions){
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.localDate()), 
                		Collectors.teeing(
                                // 1. Collector: Collect all Transactions into a List
                                Collectors.toList(),
                                
                                // 2. Collector: Sum the amount of all Transactions
                                Collectors.summingDouble(Transaction::amount),
                                
                                // 3. Merger: Combine the List and the Double into the final record
                                (list, sum) -> new SummedTransactions(list, sum)
                       )));
    }

}
