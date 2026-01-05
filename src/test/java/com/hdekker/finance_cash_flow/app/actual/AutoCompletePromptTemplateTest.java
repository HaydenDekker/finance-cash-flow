package com.hdekker.finance_cash_flow.app.actual;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.hdekker.finance_cash_flow.app.actual.AutoCompletePromptTemplate.SearchKeyword;

public class AutoCompletePromptTemplateTest {
	
	
	@Test
	public void givenKeyword_ExpectMatchesTransaction() {
		
		String keywordResponse = """
		[{
		    "payeeDescription": "MICROSOFT#G100327188      MSBILL.INFO",
		    "bestSearchTerm": "MICROSOFT MSBILL INFO"
		}]
						""";
		
		List<SearchKeyword> keywords = AutoCompletePromptTemplate.parserAIResponse(keywordResponse);
		
		assertThat(keywords)
			.hasSize(1);
		
		
		
		
	}

}
