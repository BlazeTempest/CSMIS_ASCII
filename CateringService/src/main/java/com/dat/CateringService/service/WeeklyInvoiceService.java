package com.dat.CateringService.service;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceService {
	
	public PaymentVoucher getPaymentVoucher();
	public void addNewPaymentVoucher(PaymentVoucher paymentVoucher);

}
