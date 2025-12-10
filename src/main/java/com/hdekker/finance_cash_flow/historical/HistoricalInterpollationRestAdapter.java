package com.hdekker.finance_cash_flow.historical;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdekker.finance_cash_flow.app.forecast.HistoricalInterpollationResult;


@RestController
public class HistoricalInterpollationRestAdapter {
	
	@GetMapping("/interpolate/list")
	public HistoricalInterpollationResult listAll() {
		return null;
	}

}
