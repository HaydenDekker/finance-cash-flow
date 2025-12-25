package com.hdekker.finance_cash_flow.ui;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview;
import com.hdekker.finance_cash_flow.app.budget.HasAmount;
import com.hdekker.finance_cash_flow.category.CategoryRestAdapter;
import com.vaadin.flow.component.UI;
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
	
	public record DisplaySummedTransactionCategory(String rowName, Map<YearMonth, ? extends HasAmount> summedMonths, List<TransactionCategory> categoriesIncluded) {}
	
	
	Grid<DisplaySummedTransactionCategory> grid = new Grid<>(DisplaySummedTransactionCategory.class, false);
	
	public BudgeterView() {
		
		setHeightFull();
		
		add(new H2("Budgeter"));
		add(grid);
		
		grid.setHeightFull();
		
		grid.addItemClickListener(e->{
			
			String headerText = e.getColumn().getHeaderText();
			String categoriesList = e.getItem().categoriesIncluded()
					.stream()
					.map(tc->"category=" + tc.name())
					.collect(Collectors.joining("&"));
			
			log.info("Clicked on column " + headerText + " for categories " + categoriesList);
		
			UI.getCurrent().navigate("transaction-classifier?" + categoriesList + "&" + "date=" + headerText);
			
		});
		
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		BudgetOverview budgetOverview = adapter.budgetOverview();
		Set<YearMonth> yearMonths = budgetOverview.yearMonths();
		log.info("" + yearMonths.size() + " months in dataset.");
		
		grid.removeAllColumns();
		
		grid.addColumn(DisplaySummedTransactionCategory::rowName)
		    .setHeader("Category")
		    .setSortable(true)
		    .setKey("categoryName")
		    .setWidth("300px");
		
		for (YearMonth month : yearMonths) {
		    grid.addColumn(category -> Optional.ofNullable(category.summedMonths().get(month))
		    		.map(st->st.amount())
		    		.map(d->String.format("%.2f", d))
		    		.orElse("0.0"))
		        .setHeader(month.toString())
		        .setKey(month.toString()) // Use the month name as the key
		        .setTextAlign(ColumnTextAlign.END)
		        .setPartNameGenerator(item -> {
		            if (month.equals(YearMonth.now())) {
		                return "today-cell";
		            }
		            return null;
		        })
		        .setWidth("150px");
		}
		
		Stream<DisplaySummedTransactionCategory> incomeTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Income Total",
				budgetOverview.monthlyIncomeTotal().summedMonths(),
				List.of(TransactionCategory.INCOME)
				));
		
		Stream<DisplaySummedTransactionCategory> expenseTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Expense Total",
				budgetOverview.monthlyExpensesTotal(),
				Arrays.asList(TransactionCategory.values())
					.stream()
					.filter(tc->!tc.equals(TransactionCategory.INCOME))
					.toList())
				);
		
		Stream<DisplaySummedTransactionCategory> netTotal = Stream.of(
				new DisplaySummedTransactionCategory(
						"Net flow",
				budgetOverview.netFlow(),
				Arrays.asList(TransactionCategory.values()))
				);
		
		Stream<DisplaySummedTransactionCategory> amortized = Stream.of(
				new DisplaySummedTransactionCategory(
						"Amortized Expense",
				budgetOverview.netAmortizedExpenses(),
				Arrays.asList(TransactionCategory.values()))
				);
		
		
		Stream<DisplaySummedTransactionCategory> items = budgetOverview.summedTransactionsByCategory().stream()
			.map(st-> new DisplaySummedTransactionCategory(
					st.category().name(), 
					st.summedMonths(),
					List.of(st.category())
					))
			.sorted((a,b) -> a.rowName().compareTo(b.rowName()));
			
		
		List<DisplaySummedTransactionCategory> combined = List.of(incomeTotal, expenseTotal, netTotal, amortized, items)
				.stream()
				.flatMap(s->s)
				.toList();
		
		grid.setItems(combined);
		

	}
	

}
