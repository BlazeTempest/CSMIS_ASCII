package com.dat.CateringService.service;

import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.WeeklyInvoiceRepository;
import com.dat.CateringService.entity.PaymentVoucher;

@Service
public class WeeklyInvoiceServiceImpl implements WeeklyInvoiceService {

	private WeeklyInvoiceRepository weeklyInvoiceRepo;
	@Override
	public void addNewPaymentVoucher(PaymentVoucher paymentVoucher) {
		weeklyInvoiceRepo.save(paymentVoucher);
	}
	@Override
	public PaymentVoucher getPaymentVoucher() {
		return (PaymentVoucher) weeklyInvoiceRepo.findAll();
	}

}
