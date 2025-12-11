package com.hdekker.finance_cash_flow.ui;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.app.category.CategoryGroup.SummedTransactionCategory;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
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
	
	
	Grid<SummedTransactionCategory> grid = new Grid<>(SummedTransactionCategory.class, false);
	
	public BudgeterView() {
		
		setHeightFull();
		
		add(new H2("Budgeter"));
		add(grid);
		
		grid.addColumn(SummedTransactionCategory::category)
		    .setHeader("Category")
		    .setSortable(true)
		    .setKey("categoryName");
		
		grid.setHeightFull();
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		List<SummedTransactionCategory> summedTransactions = adapter.groupedAndSummed();
		Set<YearMonth> yearMonths = yearMonths(summedTransactions);
		log.info("" + yearMonths.size() + " months in dataset.");
		
		for (YearMonth month : yearMonths) {
		    grid.addColumn(category -> Optional.ofNullable(category.summedMonths().get(month)).map(st->st.amount()).orElse(0.0))
		        .setHeader(month.toString())
		        .setKey(month.toString()) // Use the month name as the key
		        .setTextAlign(ColumnTextAlign.END); // Align values nicely
		}
		
		grid.setItems(summedTransactions);
		
	}
	
	Set<YearMonth> yearMonths(List<SummedTransactionCategory> data){
	
		// 1. Collect all unique month keys
		Set<YearMonth> uniqueMonths = new TreeSet<>();

		for (SummedTransactionCategory category : data) {
		    uniqueMonths.addAll(category.summedMonths().keySet());
		}
		
		return uniqueMonths;
	}

}
