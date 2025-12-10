package com.hdekker.finance_cash_flow.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CatorgorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.hdekker.finance_cash_flow.transaction.TransactionRestAdapter;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "transaction-calssifier", layout = MainLayout.class)
public class TransactionClassifier extends VerticalLayout implements AfterNavigationObserver{

	Grid<CatorgorisedTransaction> catorgorisedTransaction = new Grid<CatorgorisedTransaction>();
	
	@Autowired
	CategoryRestAdapter categoryRestAdapter;
	
	@Autowired
	TransactionRestAdapter transactionRestAdapter;
	
	public TransactionClassifier() {
		add(new H2("Transaction Classifier"));
		add(catorgorisedTransaction);	
		setHeightFull();
		
		catorgorisedTransaction.addColumn(ct->{
			return ct.transaction().dateString() + " " + ct.transaction().amount() + " " + ct.transaction().description();
		}).setHeader("Transaction");
		
		catorgorisedTransaction.setHeightFull();
	}
	
	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		List<CatorgorisedTransaction> list = categoryRestAdapter.list();
		List<Transaction> listTransactions = transactionRestAdapter.list();
		
		catorgorisedTransaction.setItems(list);
		
	}

}
