package com.hdekker.finance_cash_flow.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "budgeter", layout = MainLayout.class)
public class BudgeterView extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4492720600728917877L;
	
	
	//Grid<T>
	
	public BudgeterView() {
		
		add(new H2("Budgeter"));
	}

}
