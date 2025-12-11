package com.hdekker.finance_cash_flow.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

@Route("transaction-classifier-autocomplete")
public class TransactionClassifierAutocomplete implements AfterNavigationObserver, HasUrlParameter<String> {
	
	Logger log = LoggerFactory.getLogger(TransactionClassifierAutocomplete.class);
	
	//@Autowired
	//CategorisedTra

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
		
		if (parameter != null && !parameter.isEmpty()) {
            // The parameter is the key you passed (e.g., "objectkey")
            String objectKey = parameter;
            
            
        } else {
            log.error("No parameter found.");
        }
		
	}

}
