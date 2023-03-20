package com.dat.CateringService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.RestaurantRepository;
import com.dat.CateringService.daos.WeeklyInvoiceRepository;
import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.entity.Restaurant;

@Service
public class WeeklyInvoiceServiceImpl implements WeeklyInvoiceService {

	@Autowired
	private WeeklyInvoiceRepository weeklyInvoiceRepository;


	@Override
	public void save(PaymentVoucher thePaymentVoucher) {
		// TODO Auto-generated method stub
		weeklyInvoiceRepository.save(thePaymentVoucher);
	}

	
}
