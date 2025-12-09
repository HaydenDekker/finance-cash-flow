package com.hdekker.finance_cash_flow.transaction;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.hdekker.finance_cash_flow.Transaction;


public class TransactionCSVParser {
	
	final CSVFormat csvFormat;
	
	public TransactionCSVParser(){
		
		csvFormat = CSVFormat.DEFAULT.builder()
	            .setSkipHeaderRecord(false) // Skips the header row when iterating records
	            .setTrim(true) // Trims leading/trailing whitespace from fields
	            .build();
		
	}
	

	public Optional<Transaction> importTransaction(String csvItem) {
		
		StringReader sr = new StringReader(csvItem);
		
		try (CSVParser csvParser = new CSVParser(sr, csvFormat)) {
            
            // A single line will usually result in a single CSVRecord.
            // We use .getRecords() to parse all data, and then take the first one.
			List<CSVRecord> records = csvParser.getRecords();
            if (!records.isEmpty()) {
                CSVRecord record = records.get(0);
                
                return Optional.of(
                		new Transaction( 
                				LocalDate.from(Transaction.formatter.parse(record.get(0))),
                				Double.valueOf(record.get(1)), 
                				record.get(2)));
              
            }
            
        } catch (IOException e) {
        	
            e.printStackTrace();
            
        }
		
		return Optional.empty();
		
	}

}
