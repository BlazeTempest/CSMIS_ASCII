package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceRepository extends JpaRepository<PaymentVoucher, String> {
	public List<PaymentVoucher> findByPaymentDateBetween(LocalDate invoiceStartDate, LocalDate invoiceEndDate);

    @Query(value="SELECT to_date FROM payment_voucher ORDER BY to_date DESC LIMIT 1", nativeQuery = true)
    public String findLastInsertedToDate();
}