package com.hdekker.finance_cash_flow.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.CategorisedTransaction;
import com.hdekker.finance_cash_flow.Transaction;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ExpenseType;
import com.hdekker.finance_cash_flow.CategorisedTransaction.FinancialFrequency;
import com.hdekker.finance_cash_flow.CategorisedTransaction.ForecastGroup;
import com.hdekker.finance_cash_flow.CategorisedTransaction.Necessity;
import com.hdekker.finance_cash_flow.app.actual.AutoCompletePromptTemplate;
import com.hdekker.finance_cash_flow.app.actual.AutoCompletePromptTemplate.SearchKeyword;
import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;

@Route(value = "transaction-classifier", layout = MainLayout.class)
public class TransactionClassifier extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver{

		Logger log = LoggerFactory.getLogger(TransactionClassifier.class);

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
		
		public class CategorisedTransactionPropertyDisplay extends FormLayout {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -7247949776137434927L;
			ComboBox<TransactionCategory> tcField;
			Checkbox discretionaryField; 
			TextField forecastGroupField;
			TextField keywordField;
			ComboBox<ExpenseType> etField;
			ComboBox<FinancialFrequency> ffField;
			
			CategorisedTransactionPropertyDisplay(){
				
				keywordField = new TextField("Search keyword");
				add(keywordField);
				
				tcField = new ComboBox<TransactionCategory>("Transaction Category");
				add(tcField);
				tcField.setItems(Arrays.asList(TransactionCategory.values()));
				
				discretionaryField = new Checkbox("Discretionary");
				add(discretionaryField);
				
				forecastGroupField = new TextField("Forecast Group");
				add(forecastGroupField);
				
				
				etField = new ComboBox<>("Expense Type");
				add(etField);
				etField.setItems(Arrays.asList(ExpenseType.values()));
				
				ffField = new ComboBox<>("Financial Frequency");
				add(ffField);
				ffField.setItems(Arrays.asList(FinancialFrequency.values()));
				
			}
			
			public void set(CategorisedTransaction ct) {
				
				if(ct.category()!=null) tcField.setValue(ct.category());
				
				if(Necessity.DISCRETIONARY.equals(ct.necessity())) discretionaryField.setValue(true);
				
				if(ct.forcastGroup()!=null) forecastGroupField.setValue(ct.forcastGroup().name());
				
				if(ct.expenseType()!=null) etField.setValue(ct.expenseType());
				
				if(ct.financialFrequency()!=null) ffField.setValue(ct.financialFrequency());
			
				if(ct.transactionDescriptionSearchKeyword()!=null) keywordField.setValue(ct.transactionDescriptionSearchKeyword());
				
			}
			
			public CategorisedTransaction get() {
				return new CategorisedTransaction(
						null, 
						keywordField.getValue(),
						tcField.getValue(),
						discretionaryField.getValue()==true? Necessity.DISCRETIONARY: Necessity.REQUIRED,
						new ForecastGroup(forecastGroupField.getValue()),
						ffField.getValue(),
						etField.getValue()
						);
				
			}
			
		}
		
		public TransactionClassifier() {
			
			add(new H2("Transaction Classifier"));
			
			HorizontalLayout controls = new HorizontalLayout();
			transactionFilter = new TextField("Transaction filter");
			
			controls.setAlignItems(Alignment.BASELINE);
			add(controls);
			controls.add(transactionFilter);
			
			transactionFilter.addValueChangeListener(vc->{
				filterOnExistingSearchTerm();
			});
			
			Dialog searchPromptDialog = new Dialog("search prompt");
			searchPromptDialog.setHeaderTitle("Search keyword auto creation prompt");
			searchPromptDialog.setWidth("90%");
			searchPromptDialog.setHeight("80%");
			
			TextArea textArea = new TextArea("prompt response input");
			searchPromptDialog.add(textArea);
			textArea.setWidth("100%");
			textArea.setHeightFull();
			
			textArea.addValueChangeListener(vc->{
				
				List<SearchKeyword> result = AutoCompletePromptTemplate.parserAIResponse(vc.getValue());
				log.info("" + result.size() + " in response.");
				
				Map<SearchKeyword, List<CategorisedTransaction>> resultMap = 
					result.stream()
				            .collect(Collectors.toMap(
				                keyword -> keyword, // Key: The string from the first list
				                keyword -> items.stream()
				                    .filter(item -> item.transaction()
				                    		.descriptionContainsAllKeywords(
				                    				Arrays.asList(keyword.bestSearchTerm().split(" "))
				                    		)
				                    )
				                    .collect(Collectors.toList())
				            ));
				
				resultMap.forEach((s,tL)->{
					
					tL.forEach(tran->{
						categoryRestAdapter.set(
								new CategorisedTransaction(
										tran.transaction(),
										s.bestSearchTerm(),
										tran.category(),
										tran.necessity(),
										tran.forcastGroup()==null? new ForecastGroup(""): tran.forcastGroup(),
										tran.financialFrequency(),
										tran.expenseType())
								);
					});
	
				});
				
			});
			
			Button getAutoSearchTermPrompt = new Button("Auto search prompt");
			
			getAutoSearchTermPrompt.addClickListener(event -> {
				
				Map<String, List<CategorisedTransaction>> descMap = items.stream()
					.filter(CategorisedTransaction::hasTransactionKeyword)
					.collect(Collectors.groupingBy(t->t.transaction().description()));
				
				log.info("" + descMap.size() + " unique descriptions from " + items.size() + " transactions");
				
				String descriptions = descMap.keySet()
					.stream()
					.collect(Collectors.joining(","));
			    // Execute JS to write to the clipboard
				getAutoSearchTermPrompt.getElement().executeJs(
			        "navigator.clipboard.writeText($0)", AutoCompletePromptTemplate.appendItems(descriptions)
			    );
			    
			    Notification.show("Copied " + descMap.size() + "items to clipboard!");
			    
			    searchPromptDialog.open();
			    
			});
			
			controls.add(getAutoSearchTermPrompt);
			
			Button autoCategoriseButton = new Button("Auto Categorise");
			controls.add(autoCategoriseButton);
			
			autoCategoriseButton.addClickListener(e->{
				items = categoryRestAdapter.autoCategorise();
				categorisedTransaction.setItems(items);
			});
			
			CategorisedTransactionPropertyDisplay div = new CategorisedTransactionPropertyDisplay();
			add(div);
		
			Button saveButton = new Button("save all");
			div.add(saveButton);
			
			saveButton.addClickListener(e->{
				
				CategorisedTransaction properties = div.get();
				
				categorisedTransaction.getSelectedItems()
					.forEach(ct->{
						
						CategorisedTransaction newCT = new CategorisedTransaction(
						ct.transaction(),
						properties.transactionDescriptionSearchKeyword(),
						properties.category(),
						properties.necessity(),
						properties.forcastGroup(),
						properties.financialFrequency(),
						properties.expenseType()
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
				return ct.transactionDescriptionSearchKeyword();
			}).setHeader("Search Keyword")
			.setSortable(true);
			
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
			
			categorisedTransaction.setItemDetailsRenderer(new ComponentRenderer<Div, CategorisedTransaction>(Div::new, (divs, ct)->{
				
				NativeLabel transactionInfo = new NativeLabel(ct.transaction().toString());
				divs.add(transactionInfo);
				
				CategorisedTransactionPropertyDisplay propForm = new CategorisedTransactionPropertyDisplay();
				divs.add(propForm);
				propForm.set(ct);

				Button save = new Button("save item");
				divs.add(save);
				
				save.addClickListener(e->{
					
					CategorisedTransaction properties = propForm.get();
					
					CategorisedTransaction newCT = new CategorisedTransaction(
								ct.transaction(), 
								properties.transactionDescriptionSearchKeyword(),
								properties.category(),
								properties.necessity(),
								properties.forcastGroup(),
								properties.financialFrequency(),
								properties.expenseType()
						);
					
					saveCategoryTransaction(newCT);
					refreshItems(Duration.ofSeconds(1));
					
				});
				
				Button autoComplete = new Button("Autocomplete");
				divs.add(autoComplete);
				
				autoComplete.addClickListener(e-> div.set(ct));
				
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

