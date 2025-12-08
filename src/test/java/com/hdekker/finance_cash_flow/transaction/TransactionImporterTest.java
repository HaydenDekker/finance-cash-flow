package com.hdekker.finance_cash_flow.transaction;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.Transaction;

import java.util.Optional;

class TransactionImporterTest {
	
    @Test
    void givenCSVTransactionString_parsesCsvTransaction() {
    	
    	TransactionCSVParser parser = new TransactionCSVParser();
    	String descriptionStub = "PAYMENT TO TELSTRA SERVICES 0PME5S2S";
        String mockData = "\"3/12/2025\",\"-80.2\",\"" + descriptionStub + "\"";
        Optional<Transaction> actual = parser.importTransaction(mockData);
        assertTrue(actual.isPresent());
        assertEquals(descriptionStub, actual.get().description());
        
    }
    
    
}

