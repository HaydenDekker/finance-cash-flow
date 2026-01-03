package com.hdekker.finance_cash_flow.ui;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hdekker.finance_cash_flow.TransactionCategory;
import com.hdekker.finance_cash_flow.app.actual.HistoricalSummer.SummedTransactions;
import com.hdekker.finance_cash_flow.app.budget.BudgetOverview;
import com.hdekker.finance_cash_flow.app.budget.HasAmount;
import com.hdekker.finance_cash_flow.app.budget.MonthlyExpenseSummary;
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
	
	public record TransactionSummaryDisplay(
			String rowName,
			Function<YearMonth, String> displayString,
			List<TransactionCategory> categoriesIncluded) {}
	
	
	Grid<TransactionSummaryDisplay> grid = new Grid<>(TransactionSummaryDisplay.class, false);
	
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
	
	private String tryFormat(HasAmount hasAmount) {
		return Optional.ofNullable(hasAmount)
				.map(st->st.amount())
				.map(d->formatDouble(d))
				.orElse("0.0");
	}
	
	private String formatDouble(Double value) {
		return String.format("%.2f", value);	
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		
		BudgetOverview budgetOverview = adapter.budgetOverview();
		Set<YearMonth> yearMonths = budgetOverview.yearMonths();
		log.info("" + yearMonths.size() + " months in dataset.");
		
		grid.removeAllColumns();
		
		grid.addColumn(TransactionSummaryDisplay::rowName)
		    .setHeader("Category")
		    .setSortable(true)
		    .setKey("categoryName")
		    .setWidth("300px");
		
		for (YearMonth month : yearMonths) {
		    grid.addColumn(category -> category.displayString().apply(month))
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
		
		Stream<TransactionSummaryDisplay> incomeTotal = Stream.of(
				new TransactionSummaryDisplay(
						"Income Total",
				(ym)-> tryFormat(
						budgetOverview.monthlyIncomeTotal()
							.summedMonths()
							.get(ym)
						),
				List.of(TransactionCategory.INCOME)
				));
		
		Stream<TransactionSummaryDisplay> expenseTotal = Stream.of(
				new TransactionSummaryDisplay(
						"Expense Total",
				(ym)-> tryFormat(
						budgetOverview.monthlyExpensesTotal()
							.get(ym)),
				Arrays.asList(TransactionCategory.values())
					.stream()
					.filter(tc->!tc.equals(TransactionCategory.INCOME))
					.toList())
				);
		
		Stream<TransactionSummaryDisplay> netTotal = Stream.of(
				new TransactionSummaryDisplay(
						"Net flow",
				(ym)-> tryFormat(
						budgetOverview.netFlow().get(ym)
					),
				Arrays.asList(TransactionCategory.values()))
				);
	
		Map<YearMonth, ? extends HasAmount> amortizedExpenses = budgetOverview.netAmortizedExpenses();
		
		Stream<TransactionSummaryDisplay> amortized = Stream.of(
				new TransactionSummaryDisplay(
						"Amortized Expense",
				(ym) -> tryFormat(amortizedExpenses.get(ym)),
				Arrays.asList(TransactionCategory.values()))
				);
		
		Map<TransactionCategory,
			Map<YearMonth, MonthlyExpenseSummary>> monthlyExpenseSummary = budgetOverview.monthlyExpenseSummary();
	
	
		Stream<TransactionSummaryDisplay> items = budgetOverview.summedTransactionsByCategory().stream()
			.map(st-> new TransactionSummaryDisplay(
					st.category().name(), 
					(ym)-> {
						Map<YearMonth, MonthlyExpenseSummary> data = monthlyExpenseSummary.get(st.category());
						MonthlyExpenseSummary summary = data.get(ym);
						if(summary==null) return "0.0";
						return formatDouble(summary.netRealisedExpense()) + "/" + formatDouble(summary.netAmortizedCredit());
					},
					List.of(st.category())
					))
			.sorted((a,b) -> a.rowName().compareTo(b.rowName()));
		
		
		List<TransactionSummaryDisplay> combined = List.of(
					incomeTotal, 
					expenseTotal, 
					netTotal, 
					amortized,
					items
				)
				.stream()
				.flatMap(s->s)
				.toList();
		
		grid.setItems(combined);
		

	}
	

}
