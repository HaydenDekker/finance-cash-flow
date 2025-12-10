package com.hdekker.finance_cash_flow.ui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CatorgorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import reactor.core.publisher.Mono;

@Route(value = "transaction-calssifier", layout = MainLayout.class)
public class TransactionClassifier extends VerticalLayout implements AfterNavigationObserver{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -774997510488454028L;

	Grid<CatorgorisedTransaction> catorgorisedTransaction = new Grid<CatorgorisedTransaction>();
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	public TransactionClassifier() {
		add(new H2("Transaction Classifier"));
		add(catorgorisedTransaction);	
		setHeightFull();
		
		catorgorisedTransaction.addColumn(ct->{
			return ct.transaction().dateString() + " " + ct.transaction().amount() + " " + ct.transaction().description();
		}).setHeader("Transaction");
		
		catorgorisedTransaction.addColumn(ct->{
			if(ct.category()==null) return "";
			return ct.category().name();
		}).setHeader("Category");
		
		catorgorisedTransaction.addColumn(ct->{
			if(ct.necessity()==null) return "";
			return ct.necessity().name();
		}).setHeader("Necessity");
		
		catorgorisedTransaction.addColumn(ct->{
			if(ct.expenseType()==null) return "";
			return ct.expenseType().name();
		}).setHeader("Expense Type");
		
		catorgorisedTransaction.addColumn(ct->{
			if(ct.financialFrequency()==null) return "";
			return ct.financialFrequency().name();
		}).setHeader("Financial Frequency");
		
		catorgorisedTransaction.setHeightFull();
		
		catorgorisedTransaction.setItemDetailsRenderer(new ComponentRenderer<FormLayout, CatorgorisedTransaction>(FormLayout::new, (div, ct)->{
			
			ComboBox<TransactionCategory> tc = new ComboBox<TransactionCategory>("Transaction Category");
			div.add(tc);
			tc.setItems(Arrays.asList(TransactionCategory.values()));
			if(ct.category()!=null) tc.setValue(ct.category());
			
			Checkbox discretionary = new Checkbox("Discretionary");
			div.add(discretionary);
			if(Necessity.DISCRETIONARY.equals(ct.necessity())) discretionary.setValue(true);
			
			ComboBox<ExpenseType> et = new ComboBox<>("Expense Type");
			div.add(et);
			et.setItems(Arrays.asList(ExpenseType.values()));
			if(ct.expenseType()!=null) et.setValue(ct.expenseType());
			
			ComboBox<FinancialFrequency> ff = new ComboBox<>("Financial Frequency");
			div.add(ff);
			ff.setItems(Arrays.asList(FinancialFrequency.values()));
			if(ct.financialFrequency()!=null) ff.setValue(ct.financialFrequency());
			
			Button save = new Button("save");
			div.add(save);
			
			save.addClickListener(e->{
				
				CatorgorisedTransaction newCT = new CatorgorisedTransaction(
						ct.transaction(), 
						tc.getValue(),
						discretionary.getValue()==true? Necessity.DISCRETIONARY: Necessity.REQUIRED,
						ff.getValue(),
						et.getValue(),
						LocalDateTime.now()
						);
				
				saveCategoryTransaction(newCT);
				refreshItems(Duration.ofSeconds(1));
				
			});
			
		}));
	}
	
	private void refreshItems(Duration ofSeconds) {
		
		Mono.delay(ofSeconds)
			.subscribe(l->{
				catorgorisedTransaction.getUI().ifPresent(ui->{
					ui.access(()->{
						setTransactions();
						ui.push();
					});
				});
			});
		
	}

	public void saveCategoryTransaction(CatorgorisedTransaction categorisedTransaction) {
		categoryRestAdapter.set(categorisedTransaction);
	}
	
	void setTransactions() {
		
		List<CatorgorisedTransaction> list = categoryRestAdapter.list();
		List<CatorgorisedTransaction> withoutAllocation = categoryRestAdapter.listIncomplete();
		
		catorgorisedTransaction.setItems(Stream.concat(list.stream(), withoutAllocation.stream()).toList());
		
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		setTransactions();
		
	}

}
