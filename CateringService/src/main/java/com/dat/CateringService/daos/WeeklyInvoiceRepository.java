package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceRepository extends JpaRepository<PaymentVoucher, Integer> {

}
