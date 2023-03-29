package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dat.CateringService.entity.PaymentVoucher;

public interface WeeklyInvoiceRepository extends JpaRepository<PaymentVoucher, String> {

    @Query(value="SELECT to_date FROM payment_voucher ORDER BY to_date DESC LIMIT 1", nativeQuery = true)
    public String findLastInsertedToDate();
}