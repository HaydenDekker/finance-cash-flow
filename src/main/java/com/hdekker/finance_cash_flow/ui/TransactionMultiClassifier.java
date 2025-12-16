package com.hdekker.finance_cash_flow.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;

@Route(value = "multi-classifier", layout = MainLayout.class)
public class TransactionMultiClassifier extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver{

		

		/**
		 * 
		 */
		private static final long serialVersionUID = -774997510488454028L;

		Grid<CategorisedTransaction> categorisedTransaction = new Grid<CategorisedTransaction>();
		
		@Autowired
		CategoryRestAdapter categoryRestAdapter;
		
		Comparator<CategorisedTransaction> dateStringComparator = (dateString1, dateString2) -> {
		    try {
		        LocalDate date1 = LocalDate.parse(dateString1.transaction().dateString(), Transaction.formatter);
		        LocalDate date2 = LocalDate.parse(dateString2.transaction().dateString(), Transaction.formatter);
		        return date1.compareTo(date2); // Compare the LocalDate objects
		    } catch (Exception e) {
		        // Handle potential parsing errors (e.g., empty or malformed strings)
		        return 0;
		    }
		};
		
		List<String> category = List.of();
		Optional<String> date = Optional.empty();
		List<CategorisedTransaction> items;
		TextField transactionFilter;
		
		public TransactionMultiClassifier() {
			
			add(new H2("Transaction Classifier"));
			
			transactionFilter = new TextField("Transaction filter");
			add(transactionFilter);
			transactionFilter.addValueChangeListener(vc->{
				filterOnExistingSearchTerm();
			});
			
			FormLayout div = new FormLayout();
			add(div);
			
			ComboBox<TransactionCategory> tc = new ComboBox<TransactionCategory>("Transaction Category");
			div.add(tc);
			tc.setItems(Arrays.asList(TransactionCategory.values()));
			
			Checkbox discretionary = new Checkbox("Discretionary");
			div.add(discretionary);
			
			TextField forecastGroup = new TextField("Forecast Group");
			div.add(forecastGroup);
			
			ComboBox<ExpenseType> et = new ComboBox<>("Expense Type");
			div.add(et);
			et.setItems(Arrays.asList(ExpenseType.values()));
			
			ComboBox<FinancialFrequency> ff = new ComboBox<>("Financial Frequency");
			div.add(ff);
			ff.setItems(Arrays.asList(FinancialFrequency.values()));
			
			Button save = new Button("save");
			div.add(save);
			
			save.addClickListener(e->{
				
				categorisedTransaction.getSelectedItems()
					.forEach(ct->{
						
						CategorisedTransaction newCT = new CategorisedTransaction(
						ct.transaction(), 
						tc.getValue(),
						discretionary.getValue()==true? Necessity.DISCRETIONARY: Necessity.REQUIRED,
						new ForecastGroup(forecastGroup.getValue()),
						ff.getValue(),
						et.getValue(),
						LocalDateTime.now()
						);
				
						saveCategoryTransaction(newCT);
						
					});

				refreshItems(Duration.ofSeconds(1));
				
			});
			
			add(categorisedTransaction);	
			setHeightFull();
			
			categorisedTransaction.addColumn(ct-> ct.transaction().dateString())
				.setHeader("Date").setSortable(true).setComparator(dateStringComparator);
			
			categorisedTransaction.addColumn(ct->{
				return ct.transaction().amount() + " " + ct.transaction().description();
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
				if(ct.forcastGroup()==null) return "";
				return ct.forcastGroup().name();
			}).setHeader("Forecast Group");
			
			categorisedTransaction.addColumn(ct->{
				if(ct.expenseType()==null) return "";
				return ct.expenseType().name();
			}).setHeader("Expense Type");
			
			categorisedTransaction.addColumn(ct->{
				if(ct.financialFrequency()==null) return "";
				return ct.financialFrequency().name();
			}).setHeader("Financial Frequency");
			
			categorisedTransaction.setHeightFull();
			categorisedTransaction.setSelectionMode(SelectionMode.MULTI);
			
			categorisedTransaction.setItemDetailsRenderer(new ComponentRenderer<FormLayout, CategorisedTransaction>(FormLayout::new, (divs, ct)->{
				
				NativeLabel transactionInfo = new NativeLabel(ct.transaction().toString());
				divs.add(transactionInfo);
				
			}));
			
		}
		
		private void refreshItems(Duration ofSeconds) {
			
			Mono.delay(ofSeconds)
				.subscribe(l->{
					categorisedTransaction.getUI().ifPresent(ui->{
						ui.access(()->{
							setTransactions(false);
							ui.push();
						});
					});
				});
			
		}

		public void saveCategoryTransaction(CategorisedTransaction categorisedTransaction) {
			
			categoryRestAdapter.set(categorisedTransaction);
			
		}
		
		
		
		void setTransactions(boolean scrollToFirstWithoutAllocation) {
			
			List<CategorisedTransaction> list = categoryRestAdapter.list();
			List<CategorisedTransaction> withoutAllocation = categoryRestAdapter.listIncomplete();
			
			items = Stream.concat(list.stream(), withoutAllocation.stream()).toList();
			
			if(date.isPresent()&&category.size()>0) {
				
				YearMonth dateFilter = YearMonth.parse(date.get());
				items = items.stream()
						.filter(ct->ct.category()!=null)
						.filter(ct->category.contains(ct.category().name()))
						.filter(ct->ct.getTransactionYearMonth().equals(dateFilter))
						.toList();
			}
			
			filterOnExistingSearchTerm();
			
			categorisedTransaction.setItems(items);
			
			if(scrollToFirstWithoutAllocation && category.size()==0 && withoutAllocation.size()>0) {
				categorisedTransaction.scrollToItem(withoutAllocation.get(0));
			}
			
		}
		
		private void filterOnExistingSearchTerm() {
	
			if(transactionFilter.getValue().equals("")) {
				categorisedTransaction.setItems(items);
				return;
			}
			
			categorisedTransaction.setItems(
					items.stream()
						.filter(ct->ct.transaction()
								.description()
								.contains(transactionFilter.getValue()))
						.toList()
				);
			
		}

		@Override
		public void afterNavigation(AfterNavigationEvent event) {
			
			setTransactions(true);
			
		}

		@Override
		public void beforeEnter(BeforeEnterEvent event) {
			
			QueryParameters param = event.getLocation().getQueryParameters();
			category = param.getParameters("category");
			date = param.getSingleParameter("date");
			
			
		}

}

