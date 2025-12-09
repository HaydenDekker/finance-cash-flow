package com.hdekker.finance_cash_flow.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainLayout() {
        add(new H1("Cash Flow Budgeter"));
    }

}
