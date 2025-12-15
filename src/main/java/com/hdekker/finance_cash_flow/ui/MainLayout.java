package com.hdekker.finance_cash_flow.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends VerticalLayout implements RouterLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainLayout() {
		setHeightFull();
		H1 header = new H1("Cash Flow Kabana");
		HorizontalLayout controls = new HorizontalLayout();
		Anchor aTransactions = new Anchor("transaction-view", "transactions");
		Anchor aClassification = new Anchor("transaction-classifier", "classifier");
		Anchor aBudgeter = new Anchor("budgeter", "budgeter");
		controls.add(header, aTransactions, aClassification, aBudgeter);
		controls.setAlignItems(Alignment.BASELINE);
        add(controls);
        header.addClickListener(e->{
        	UI.getCurrent().navigate("budgeter");
        });
    }

}
