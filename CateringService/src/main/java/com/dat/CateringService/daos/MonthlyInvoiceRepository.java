package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.PaidVoucher;

public interface MonthlyInvoiceRepository extends JpaRepository<PaidVoucher, Integer>{
	
	

}
