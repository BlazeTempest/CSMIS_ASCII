package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.Headcount;

public interface HeadcountService {
	
	public void saveHeadcount(Headcount headcount);
	public Headcount getHeadcountByDate(LocalDate date);
	public List<Headcount> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    public List<Headcount> findAll();

}
