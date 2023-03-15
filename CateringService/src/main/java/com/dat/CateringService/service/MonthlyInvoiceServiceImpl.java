package com.dat.CateringService.service;

import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.MonthlyInvoiceRepository;
import com.dat.CateringService.entity.PaidVoucher;

@Service
public class MonthlyInvoiceServiceImpl implements MonthlyInvoiceService {

	private MonthlyInvoiceRepository monthlyInvoiceRepo;
	@Override
	public void AddNewPaidVoucher(PaidVoucher paidVoucher) {
		
		monthlyInvoiceRepo.save(paidVoucher);

	}

}
