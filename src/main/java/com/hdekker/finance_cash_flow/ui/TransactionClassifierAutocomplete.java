package com.hdekker.finance_cash_flow.ui;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;

@Route("transaction-classifier-autocomplete")
public class TransactionClassifierAutocomplete extends VerticalLayout implements AfterNavigationObserver, HasUrlParameter<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8205446985281792544L;

	Logger log = LoggerFactory.getLogger(TransactionClassifierAutocomplete.class);
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	CategorisedTransaction trans;
	
	NativeLabel nativeLabel = new NativeLabel();
	TextField keyword = new TextField("payee keyword matcher");
	Grid<CategorisedTransaction> categorisedTransaction = new Grid<CategorisedTransaction>();
	Button button = new Button("set all");
	
	List<CategorisedTransaction> matchingPayees;
	
	TransactionClassifierAutocomplete(){
		
		add(nativeLabel, keyword, categorisedTransaction, button);
		setHeightFull();
		
		categorisedTransaction.addColumn(ct->{
			return ct.transaction().dateString() + " " + ct.transaction().amount() + " " + ct.transaction().description();
		}).setHeader("Transaction");
		
		categorisedTransaction.addColumn(ct->{
			if(ct.category()==null) return "";
			return ct.category().name();
		}).setHeader("Category");
		
		categorisedTransaction.addColumn(ct->{
			if(ct.necessity()==null) return "";
			return ct.necessity().name();
		}).setHeader("Necessity");
		
		categorisedTransaction.addColumn(ct->{
			if(ct.expenseType()==null) return "";
			return ct.expenseType().name();
		}).setHeader("Expense Type");
		
		categorisedTransaction.addColumn(ct->{
			if(ct.financialFrequency()==null) return "";
			return ct.financialFrequency().name();
		}).setHeader("Financial Frequency");
		
		categorisedTransaction.setHeightFull();
		
		keyword.addValueChangeListener(vc->{
			
			matchingPayees = categoryRestAdapter.listIncomplete()
				.stream()
				.filter(ct->ct.transaction().description().contains(vc.getValue()))
				.toList();
			
			categorisedTransaction.setItems(matchingPayees);
			
		});
		
		button.addClickListener(e->{
			
			matchingPayees.forEach(ct-> {
				categoryRestAdapter.set(new CategorisedTransaction(
						ct.transaction(),
						trans.category(),
						trans.necessity(),
						trans.financialFrequency(),
						trans.expenseType(),
						LocalDateTime.now()));
			});
			
			UI.getCurrent().navigate("transaction-calssifier");
			
		});
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		nativeLabel.setText(trans.toString());
		
	}

	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
		
		if (parameter != null && !parameter.isEmpty()) {
            // The parameter is the key you passed (e.g., "objectkey")
            String objectKey = parameter;
            trans = categoryRestAdapter.findById(objectKey);
            
        } else {
            log.error("No parameter found.");
        }
		
	}

}
