package com.hdekker.finance_cash_flow.historical;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;

public class HistoricalSummariserTest {
    
    List<Transaction> testTransactions(){
        return List.of(
            new Transaction(LocalDate.of(2025,3,5), 100.0, "Coffee"),
            new Transaction(LocalDate.of(2025,4,12), -50.0, "Groceries"),
            new Transaction(LocalDate.of(2025,4,20), 200.0, "Salary"),
            new Transaction(LocalDate.of(2025,5,1), -25.0, "Utilities")
        );
    }

    @Test
    public void givenListOfTransactions_ExpectMapProduced() {
        Map<YearMonth, Double> result = HistoricalSummariser.calculateTotal(testTransactions());
        assertThat(result.containsKey(YearMonth.of(2025, 4)));
        assertThat(result.get(YearMonth.of(2025, 4)))
        	.isEqualTo(150.0);
    }
}
