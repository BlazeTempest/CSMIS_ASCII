package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.HeadcountRepository;
import com.dat.CateringService.entity.Headcount;
@Service
public class HeadcountServiceImpl implements HeadcountService {
	
	@Autowired
	private HeadcountRepository headcountRepository;

	@Override
	public void saveHeadcount(Headcount headcount) {
		headcountRepository.save(headcount);
	}

	@Override
	public Headcount getHeadcountByDate(LocalDate date) {
		return headcountRepository.findByDate(date);
	}

	@Override
	public List<Headcount> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate) {
		return headcountRepository.findByInvoiceDateBetween(startDate, endDate);
	}

	@Override
	public List<Headcount> findAll() {
		return headcountRepository.findAll();
	}

}
