package com.hdekker.finance_cash_flow.ui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
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

	Grid<CategorisedTransaction> categorisedTransaction = new Grid<CategorisedTransaction>();
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	public TransactionClassifier() {
		add(new H2("Transaction Classifier"));
		add(categorisedTransaction);	
		setHeightFull();
		
		categorisedTransaction.addColumn(ct->{
			return ct.transaction().dateString() + " " + ct.transaction().amount() + " " + ct.transaction().description();
		}).setHeader("Transaction");
		
		categorisedTransaction.addColumn(ct->{
			if(ct.category()==null) return "";
			return ct.category().name();
		}).setHeader("Category").setSortable(true);
		
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
		
		categorisedTransaction.setItemDetailsRenderer(new ComponentRenderer<FormLayout, CategorisedTransaction>(FormLayout::new, (div, ct)->{
			
			NativeLabel transactionInfo = new NativeLabel(ct.transaction().toString());
			div.add(transactionInfo);
			
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
				
				CategorisedTransaction newCT = new CategorisedTransaction(
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
			
			Button autoComplete = new Button("Autocomplete");
			div.add(autoComplete);
			
			autoComplete.addClickListener(e->{
				UI.getCurrent()
					.navigate("transaction-classifier-autocomplete/" + ct.transaction().createId());
			});
			
		}));
	}
	
	private void refreshItems(Duration ofSeconds) {
		
		Mono.delay(ofSeconds)
			.subscribe(l->{
				categorisedTransaction.getUI().ifPresent(ui->{
					ui.access(()->{
						setTransactions();
						ui.push();
					});
				});
			});
		
	}

	public void saveCategoryTransaction(CategorisedTransaction categorisedTransaction) {
		categoryRestAdapter.set(categorisedTransaction);
	}
	
	void setTransactions() {
		
		List<CategorisedTransaction> list = categoryRestAdapter.list();
		List<CategorisedTransaction> withoutAllocation = categoryRestAdapter.listIncomplete();
		
		categorisedTransaction.setItems(Stream.concat(list.stream(), withoutAllocation.stream()).toList());
		
		if(withoutAllocation.size()>0) {
			categorisedTransaction.scrollToItem(withoutAllocation.get(0));
		}
		
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		setTransactions();
		
	}

}
