package com.hdekker.finance_cash_flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;

@SpringBootApplication
@Push
public class FinanceCashFlowApplication implements AppShellConfigurator{
	
	private static final long serialVersionUID = 4747759585237212767L;

	public static void main(String[] args) {
		SpringApplication.run(FinanceCashFlowApplication.class, args);
		
	}

}
