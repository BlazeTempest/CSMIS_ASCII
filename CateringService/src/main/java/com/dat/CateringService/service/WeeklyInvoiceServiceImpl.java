package com.dat.CateringService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.WeeklyInvoiceRepository;
import com.dat.CateringService.entity.PaymentVoucher;

@Service
public class WeeklyInvoiceServiceImpl implements WeeklyInvoiceService {

    @Autowired
    private WeeklyInvoiceRepository weeklyInvoiceRepository;

    @Override
    public void save(PaymentVoucher thePaymentVoucher) {
        weeklyInvoiceRepository.save(thePaymentVoucher);
    }

    @Override
    public List<PaymentVoucher> findAll() {
        return weeklyInvoiceRepository.findAll();
    }

    @Override
    public String findLastInsertedToDate() {
        return weeklyInvoiceRepository.findLastInsertedToDate();
    }
}