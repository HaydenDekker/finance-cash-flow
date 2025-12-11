package com.hdekker.finance_cash_flow.transaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.TransactionPersister;
import com.hdekker.finance_cash_flow.TransactionLister;

@RestController
public class TransactionRestAdapter {
	
	@Autowired
	TransactionPersister transactionPersister;
	
	@Autowired
	TransactionLister transactionLister;
	
	TransactionCSVParser transactionCSVParser = new TransactionCSVParser();

	@PostMapping(path = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Transaction save(
			@RequestBody Transaction transaction) {
		return transactionPersister.persist(transaction);
	}

	@GetMapping(path = "/transaction")
	public List<Transaction> list() {
		return transactionLister.list();
	}
	
	@PostMapping(path = "/transaction", consumes = "text/csv")
	public Transaction save(
			@RequestBody String transactionCSVItem) {
		Optional<Transaction> transaction = transactionCSVParser.importTransaction(transactionCSVItem);
		if(transaction.isEmpty()) {	
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"The provided CSV item could not be parsed into a valid Transaction. Item: " + transactionCSVItem
				);
		}
		return transactionPersister.persist(transaction.get());
	}

	public void upload(File file) throws FileNotFoundException {
		List<String> csvItems = TransactionCSVFileReader.readAll(new FileInputStream(file));
		csvItems.forEach(item-> save(item));
	}
	
}
