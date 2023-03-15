package com.dat.CateringService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MonthlyInvoiceController {
	
	@GetMapping("/monthly_invoice")
	public String monthlyInvoice() {
		return "monthly-invoice";
	}

}
