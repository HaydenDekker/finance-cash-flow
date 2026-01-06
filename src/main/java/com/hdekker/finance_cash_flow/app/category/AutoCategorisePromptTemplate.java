package com.hdekker.finance_cash_flow.app.category;

import java.util.List;

import org.springframework.ai.util.json.schema.JsonSchemaGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;

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
		
		String schema = JsonSchemaGenerator.generateForType(ResponseType.class);
		
		return intro + "\n\r"
				+ schema;
		
	}
	
	record TransactionSummary(
			String description,
			String keyword
			) {}
	
	record ResponseType(String keyword, TransactionCategory category,
		Necessity necessity,
		ForecastGroup forcastGroup,
		FinancialFrequency financialFrequency,
		ExpenseType expenseType ) {}
	
	static ObjectMapper om = new ObjectMapper();
	
	public static String prompt(List<CategorisedTransaction> transactions) {
		
		List<TransactionSummary> summary = transactions.stream()
				.map(ct->new TransactionSummary(ct.transaction().description(), ct.transactionDescriptionSearchKeyword()))
				.toList();
		
		
		try {
			String data = om.writeValueAsString(summary);
			return getPrompt() + "\n\r" + data;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Error passing data";
	}

	public static List<CategorisedTransaction> parseLLMResponse(List<CategorisedTransaction> items, String value) {
		
		try {
			List<ResponseType> values = om.readValue(value, new TypeReference<List<ResponseType>>() {});
			
			return values.stream()
				.flatMap(rt-> items.stream()
						.filter(ct->ct.transactionDescriptionSearchKeyword().equals(rt.keyword()))
						.filter(ct->!ct.isComplete())
						.map(ct-> new CategorisedTransaction(
								ct.transaction(), 
								ct.transactionDescriptionSearchKeyword(), 
								rt.category(), 
								rt.necessity(), 
								rt.forcastGroup(), 
								rt.financialFrequency(), 
								rt.expenseType()))
				)
				.toList();
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return List.of();
	}

}
