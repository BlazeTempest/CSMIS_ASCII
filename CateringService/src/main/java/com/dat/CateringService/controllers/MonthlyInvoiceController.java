package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        int totalAmount=0;

        List<PaymentVoucher> paymentVoucher=weeklyInvoiceService.findAll();
        for(PaymentVoucher temp: paymentVoucher)
        {
            totalAmount+=temp.getTotalCost();
        }
        model.addAttribute("totalAmount",totalAmount);
        model.addAttribute("paymentVoucher",paymentVoucher);

        return "admin/monthly-invoice";
    }
    @GetMapping("/searchPaidVoucher")
    public String searchPaidVoucher(Model model, @RequestParam("startDate")String invoiceStartDate, @RequestParam("endDate")String invoiceEndDate)
    {

        List<PaymentVoucher> paymentVoucherDateList=weeklyInvoiceService.findByPaymentDateBetween(LocalDate.parse(invoiceStartDate), LocalDate.parse(invoiceEndDate));

        List<PaymentVoucher> paymentVoucher=weeklyInvoiceService.findAll();

        model.addAttribute("start", invoiceStartDate);
        model.addAttribute("end", invoiceEndDate);
        model.addAttribute("paymentVoucher",paymentVoucherDateList);

        return "admin/monthly-invoice";
    }
}