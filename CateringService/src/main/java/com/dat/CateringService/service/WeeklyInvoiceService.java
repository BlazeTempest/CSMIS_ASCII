package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceService {

    public List<PaymentVoucher> findAll();
    public void save(PaymentVoucher thePaymentVoucher);

    public String findLastInsertedToDate();
}