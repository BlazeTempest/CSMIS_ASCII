package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.service.StaffService;
import com.dat.CateringService.service.WeeklyInvoiceService;

@Controller
public class MonthlyInvoiceController {
	
	@Autowired
	private StaffService staffService;

    private WeeklyInvoiceService weeklyInvoiceService;

    @Autowired
    public MonthlyInvoiceController(WeeklyInvoiceService weeklyInvoiceService) {
        super();
        this.weeklyInvoiceService = weeklyInvoiceService;
    }

    @GetMapping("/monthly_invoice")
    public String monthlyInvoice(Model model,Authentication authentication) {
    	try {
    		String role = authentication.getAuthorities().toArray()[0].toString();
    		if (role.equals("admin")) {
    			 int totalAmount=0;

    		        List<PaymentVoucher> paymentVoucher=weeklyInvoiceService.findAll();
    		        for(PaymentVoucher temp: paymentVoucher)
    		        {
    		            totalAmount+=temp.getTotalCost();
    		        }
    		        model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
    		        model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
    		        model.addAttribute("totalAmount",totalAmount);
    		        model.addAttribute("paymentVoucher",paymentVoucher);

    		        return "admin/monthly-invoice";
    		} return"404";
    	}catch(NullPointerException e) {
			
			return "redirect:/showMyLoginPage";
		}

    }
    @GetMapping("/searchPaidVoucher")
    public String searchPaidVoucher(Authentication authentication,Model model, @RequestParam("startDate")String invoiceStartDate, @RequestParam("endDate")String invoiceEndDate)
    {

        List<PaymentVoucher> paymentVoucherDateList=weeklyInvoiceService.findByPaymentDateBetween(LocalDate.parse(invoiceStartDate), LocalDate.parse(invoiceEndDate));

        List<PaymentVoucher> paymentVoucher=weeklyInvoiceService.findAll();
        model.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
        model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
        model.addAttribute("start", invoiceStartDate);
        model.addAttribute("end", invoiceEndDate);
        model.addAttribute("paymentVoucher",paymentVoucherDateList);

        return "admin/monthly-invoice";
    }
}