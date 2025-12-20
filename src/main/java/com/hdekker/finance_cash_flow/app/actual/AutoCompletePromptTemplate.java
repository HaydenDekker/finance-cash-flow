package com.hdekker.finance_cash_flow.app.actual;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AutoCompletePromptTemplate {

	public static String promptHeader = """
			The list provided are payee desciptions of bank transaction items. 
			For each payee description given provide the best search terms that would uniquely identify the payee business name. 
			Payee descriptions generally have a geographical location included at the end of the description, 
			exclude geographical locations from the answers. 
			All answers shall only consist of words present in the provided string. 
			Numerals must be discarded from the answer. 
			First explain your understanding of each point of this prompt then,
			Output each result in the following json format, 
			[
				{
					"payeeDescription":String,
					"bestSearchTerm":String
				}
			]
			
			""";
	
	public static String appendItems(String itemString) {
		return promptHeader + "\n\r" + itemString;
	}
	
	public record SearchKeyword(String payeeDescription, String bestSearchTerm) {}
	
	public static List<SearchKeyword> parserAIResponse(String responseJson){
		ObjectMapper om = new ObjectMapper();
		
		try {	
			List<SearchKeyword> results = om.readValue(responseJson, new TypeReference<List<SearchKeyword>>() {});
			return results;
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
