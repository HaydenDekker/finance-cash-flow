package com.hdekker.finance_cash_flow.app.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.transaction.TestData;

public class AutoCategorisePromptTemplateTest {
	
	Logger log = LoggerFactory.getLogger(AutoCategorisePromptTemplateTest.class);
	
	@Test
	public void givenCT_ExpectBuildsPrompt() {
		
		CategorisedTransaction tran = TestData.basicTestCase().transactions().get(0);
		String prompt = AutoCategorisePromptTemplate.prompt(List.of(tran));
		
		assertThat(prompt)
			.contains("FIXED");
		
		assertThat(prompt)
			.contains("DISCRETIONARY");
		
		log.info(prompt);
		
	}

}
