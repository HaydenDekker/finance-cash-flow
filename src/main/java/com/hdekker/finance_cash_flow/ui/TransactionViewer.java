package com.hdekker.finance_cash_flow.ui;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionReader;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;

@Route(value = "transaction-view", layout = MainLayout.class)
public class TransactionViewer extends VerticalLayout implements AfterNavigationObserver {
	
	Logger log = LoggerFactory.getLogger(TransactionViewer.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -8101501973560695910L;
	
	@Autowired
	TransactionReader transactionReader;
	
	Grid<Transaction> transactionGrid = new Grid<Transaction>();
	
	
	Upload upload = new Upload(UploadHandler.toTempFile(
			(UploadMetadata metadata, File file) -> {
				log.info("Uploaded document.");
			}
		));


	public TransactionViewer() {
		add(new H2("Transactions"));
		add(upload);
		add(transactionGrid);
		
		transactionGrid.addColumn(Transaction::dateString).setHeader("Date");
		transactionGrid.addColumn(Transaction::amount).setHeader("Amount");
		transactionGrid.addColumn(Transaction::description).setHeader("Payee Description");
		
	}


	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		List<Transaction> transactions = transactionReader.list();
		transactionGrid.setItems(transactions);
		
	}
	
}
