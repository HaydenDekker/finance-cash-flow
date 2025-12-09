package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;

import java.time.LocalDate;
import java.util.Optional;

class TransactionCSVParserTest {
	
    @Test
    void givenCSVTransactionString_parsesCsvTransaction() {
    	
    	TransactionCSVParser parser = new TransactionCSVParser();
    	TransactionTestData testData = new TransactionTestData();
    	
        Optional<Transaction> actual = parser.importTransaction(testData.mockCSVData);
        assertTrue(actual.isPresent());
        assertEquals(testData.descriptionStub, actual.get().description());
        assertThat(actual.get().localDate())
        	.isEqualTo(LocalDate.of(2025, 12, 3));
        
        actual = parser.importTransaction(testData.mockCSVDataDoubleDigitDay);
        assertTrue(actual.isPresent());
        assertEquals(testData.descriptionStub, actual.get().description());
        assertThat(actual.get().localDate())
        	.isEqualTo(LocalDate.of(2025, 12, 24));
        
    }
    
    
}

