package com.hdekker.finance_cash_flow.transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TransactionCSVFileReader {

	public static List<String> readAll(InputStream is) {
		
		List<String> csvItems = new ArrayList<String>();
		
		try (
		        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		        
		        // 2. Wrap the Reader in a BufferedReader for efficient line-by-line reading
		        BufferedReader reader = new BufferedReader(isr)
		    ) {
		        String line;
		        
		        // 3. Loop until the end of the stream is reached (line is null)
		        while ((line = reader.readLine()) != null) {
		           csvItems.add(line);
		        }

		    } catch (IOException e) {
		        System.err.println("Error reading CSV stream: " + e.getMessage());
		    }
		
		return csvItems;
	}

}
