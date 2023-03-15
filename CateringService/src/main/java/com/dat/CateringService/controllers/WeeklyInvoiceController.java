package com.dat.CateringService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.service.WeeklyInvoiceService;

@Controller
public class WeeklyInvoiceController {
	
	private WeeklyInvoiceService weeklyInvoiceService;
	
	public WeeklyInvoiceController(WeeklyInvoiceService weeklyInvoiceService) {
		super();
		this.weeklyInvoiceService = weeklyInvoiceService;
	}

	@GetMapping("/weekly-invoice")
	public String weeklyInvoice(Model theModel) {
		theModel.addAttribute("paymentVoucher",weeklyInvoiceService.getPaymentVoucher());
		return "weekly-invoice";
	}
	
	@GetMapping("/saveInvoice")
	public String createInvoice(@RequestParam("action") String action, @ModelAttribute("paymentVoucher") PaymentVoucher thePaymentVoucher) {
		if (action.equals("close")) {
		    // handle form submission for close button
		    return "redirect:/weekly-invoice";
		  } else if (action.equals("save")) {
		    // handle form submission for save button
			weeklyInvoiceService.addNewPaymentVoucher(thePaymentVoucher);
			
		    return "weekly-invoice";
		  } else {
		    // handle invalid action value
		    return "error";
		  }
		
	}

}
