package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Headcount;

public interface HeadcountRepository extends JpaRepository<Headcount, Integer> {
	@Query(value="SELECT * FROM headcount WHERE invoice_date=:date", nativeQuery = true)
	public Headcount findByDate(@Param("date")LocalDate date);
	public List<Headcount> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
}
