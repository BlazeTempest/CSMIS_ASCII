package com.dat.CateringService.service;

import java.time.LocalDate;

import com.dat.CateringService.entity.Headcount;

public interface HeadcountService {
	
	public void saveHeadcount(Headcount headcount);
	public Headcount getHeadcountByDate(LocalDate date);
}
