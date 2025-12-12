package com.hdekker.finance_cash_flow.ui;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter.HistoricalOverview;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

@Route(value = "budgeter", layout = MainLayout.class)
public class BudgeterView extends VerticalLayout implements AfterNavigationObserver{
	
	Logger log = LoggerFactory.getLogger(BudgeterView.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -4492720600728917877L;
	
	@Autowired
	CategoryRestAdapter adapter;
	
	public record DisplaySummedTransactionCategory(String rowName, SummedTransactionCategory categoryItem) {}
	
	
	Grid<DisplaySummedTransactionCategory> grid = new Grid<>(DisplaySummedTransactionCategory.class, false);
	
	public BudgeterView() {
		
		setHeightFull();
		
		add(new H2("Budgeter"));
		add(grid);
		
		grid.addColumn(DisplaySummedTransactionCategory::rowName)
		    .setHeader("Category")
		    .setSortable(true)
		    .setKey("categoryName");
		
		grid.setHeightFull();
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		HistoricalOverview historicalOverview = adapter.historicalOverview();
		Set<YearMonth> yearMonths = historicalOverview.yearMonths();
		log.info("" + yearMonths.size() + " months in dataset.");
		
		for (YearMonth month : yearMonths) {
		    grid.addColumn(category -> Optional.ofNullable(category.categoryItem().summedMonths().get(month)).map(st->st.amount()).orElse(0.0))
		        .setHeader(month.toString())
		        .setKey(month.toString()) // Use the month name as the key
		        .setTextAlign(ColumnTextAlign.END); // Align values nicely
		}
		
		Stream<DisplaySummedTransactionCategory> expenseTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Expense Total",
				new SummedTransactionCategory(TransactionCategory.INCOME, historicalOverview.monthlyExpensesTotal()))
				);
		
		Stream<DisplaySummedTransactionCategory> items = historicalOverview.summedTransactionsByCategory().stream()
			.map(st-> new DisplaySummedTransactionCategory(st.category().name(), st));
			
		
		List<DisplaySummedTransactionCategory> combined = Stream.concat(
				 expenseTotal,
				 items
				)
				.toList();
		
		grid.setItems(combined);

	}
	

}
