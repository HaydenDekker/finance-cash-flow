package com.hdekker.finance_cash_flow.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TransactionCSVFileReaderTest {
	
	@Test
	public void givenCSVFile_CanExtractInputs() {
		
		String csv = "\"hello\",\"cool\"\r\n" +
				"\"goodbye\",\"sad\"\r\n";
		
		byte[] bytes = csv.getBytes();
		InputStream is = new ByteArrayInputStream(bytes);
		List<String> csvItems = TransactionCSVFileReader.readAll(is);
		assertThat(csvItems)
			.hasSize(2);
		
	}

}
