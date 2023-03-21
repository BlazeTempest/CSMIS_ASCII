package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.service.RestaurantService;
import com.dat.CateringService.service.WeeklyInvoiceService;


@Controller
public class WeeklyInvoiceController {

	private RestaurantService restaurantService;

	private WeeklyInvoiceService weeklyInvoiceService;

	public WeeklyInvoiceController(WeeklyInvoiceService weeklyInvoiceService, RestaurantService restaurantService) {
		super();
		this.weeklyInvoiceService = weeklyInvoiceService;
		this.restaurantService = restaurantService;
	}

	/* generate voucher_ID like CS001-currentdate */
	private static int counter = 1;
	private String generateSuffix() {
		
		System.out.println("generateSuffix() called, counter = " + counter);
		String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return String.format("%03d-%s", counter, dateStr);
	}

	@GetMapping("/weekly-invoice")
	public String weeklyInvoice(Model model) {

		PaymentVoucher weeklyInvoice = new PaymentVoucher();
		
		model.addAttribute("restaurantName", restaurantService.findActiveRestaurantName());
		model.addAttribute("weeklyInvoice", weeklyInvoice);

		return "weekly-invoice";
	}

	@PostMapping("/saveWeeklyInvoice")
	public String saveWeeklyInvoice(@ModelAttribute("weeklyInvoice") PaymentVoucher paymentVoucher, Model model) {

		paymentVoucher.setVoucher_ID("CS" + generateSuffix());
		System.out.println("CS" + generateSuffix());
	

		paymentVoucher.setCreated_date(LocalDate.now());
		paymentVoucher.setRestaurant_name(restaurantService.findActiveRestaurantName());
		weeklyInvoiceService.save(paymentVoucher);
		counter++;
		
		return "redirect:/weekly-invoice";
	}
}
