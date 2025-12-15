package com.hdekker.finance_cash_flow.ui;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview;
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
	
	public record DisplaySummedTransactionCategory(String rowName, Map<YearMonth, SummedTransactions> summedMonths) {}
	
	
	Grid<DisplaySummedTransactionCategory> grid = new Grid<>(DisplaySummedTransactionCategory.class, false);
	
	public BudgeterView() {
		
		setHeightFull();
		
		add(new H2("Budgeter"));
		add(grid);
		
		grid.setHeightFull();
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		BudgetOverview historicalOverview = adapter.historicalOverview();
		Set<YearMonth> yearMonths = historicalOverview.yearMonths();
		log.info("" + yearMonths.size() + " months in dataset.");
		
		grid.removeAllColumns();
		
		grid.addColumn(DisplaySummedTransactionCategory::rowName)
		    .setHeader("Category")
		    .setSortable(true)
		    .setKey("categoryName");
		
		for (YearMonth month : yearMonths) {
		    grid.addColumn(category -> Optional.ofNullable(category.summedMonths().get(month)).map(st->st.amount()).orElse(0.0))
		        .setHeader(month.toString())
		        .setKey(month.toString()) // Use the month name as the key
		        .setTextAlign(ColumnTextAlign.END); // Align values nicely
		}
		
		Stream<DisplaySummedTransactionCategory> incomeTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Income Total",
				historicalOverview.monthlyIncomeTotal().summedMonths())
				);
		
		Stream<DisplaySummedTransactionCategory> expenseTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Expense Total",
				historicalOverview.monthlyExpensesTotal())
				);
		
		Stream<DisplaySummedTransactionCategory> netTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Net flow",
				historicalOverview.difference())
				);
		
		Stream<DisplaySummedTransactionCategory> items = historicalOverview.summedTransactionsByCategory().stream()
			.map(st-> new DisplaySummedTransactionCategory(st.category().name(), st.summedMonths()))
			.sorted((a,b) -> a.rowName().compareTo(b.rowName()));
			
		
		List<DisplaySummedTransactionCategory> combined = List.of(incomeTotal, expenseTotal, netTotal, items)
				.stream()
				.flatMap(s->s)
				.toList();
		
		grid.setItems(combined);
		

	}
	

}
