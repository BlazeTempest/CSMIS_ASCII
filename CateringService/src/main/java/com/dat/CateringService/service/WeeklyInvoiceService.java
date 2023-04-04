package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceService {

    public List<PaymentVoucher> findAll();
    public void save(PaymentVoucher thePaymentVoucher);
    public List<PaymentVoucher> findByPaymentDateBetween(LocalDate start, LocalDate end);
    public String findLastInsertedToDate();
}