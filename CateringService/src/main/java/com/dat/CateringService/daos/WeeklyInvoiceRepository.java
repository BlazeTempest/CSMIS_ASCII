package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.PaymentVoucher;
import com.dat.CateringService.entity.Restaurant;

public interface WeeklyInvoiceRepository extends JpaRepository<PaymentVoucher, String> {
	
}
