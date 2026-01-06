package com.hdekker.finance_cash_flow.app.category;

import java.util.List;

import org.springframework.ai.util.json.schema.JsonSchemaGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdekker.finance_cash_flow.CategorisedTransaction;

public class AutoCategorisePromptTemplate {
	
	static String intro = """
			The following list of items are transactions that require categorisation.
			Each item requires the following properties,
			"required" : ["category", "expenseType", "financialFrequency", "necessity"]
			noting that it already provides the keyword and transaction.
			
			Expense type adhoc means the expense will be semi-regular but not periodical
			OneOff means the expense should be just once for many years, such as wedding ring etc.
			""";
	
	private static String getPrompt() {
		
		String schema = JsonSchemaGenerator.generateForType(CategorisedTransaction.class);
		
		return intro + "\n\r"
				+ schema;
		
	}
	
	record TransactionSummary(
			String description,
			String keyword
			) {}
	
	public static String prompt(List<CategorisedTransaction> transactions) {
		
		List<TransactionSummary> summary = transactions.stream()
				.map(ct->new TransactionSummary(ct.transaction().description(), ct.transactionDescriptionSearchKeyword()))
				.toList();
		
		ObjectMapper om = new ObjectMapper();
		try {
			String data = om.writeValueAsString(summary);
			return getPrompt() + "\n\r" + data;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Error passing data";
	}

}
