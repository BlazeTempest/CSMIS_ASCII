package com.dat.CateringService.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.service.WeeklyInvoiceService;

@Controller
public class MonthlyInvoiceController {

    private WeeklyInvoiceService weeklyInvoiceService;

    @Autowired
    public MonthlyInvoiceController(WeeklyInvoiceService weeklyInvoiceService) {
        super();
        this.weeklyInvoiceService = weeklyInvoiceService;
    }

    @GetMapping("/monthly_invoice")
    public String monthlyInvoice(Model model) {

        List<PaymentVoucher> paymentVoucher=weeklyInvoiceService.findAll();

        System.out.println("payment voucher is ........... " + paymentVoucher);

        model.addAttribute("paymentVoucher",paymentVoucher);

        return "admin/monthly-invoice";
    }
}