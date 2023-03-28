package com.dat.CateringService.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.HolidayRepository;
import com.dat.CateringService.entity.Holidays;

@Service
public class HolidayServiceImpl implements HolidayService {

	@Autowired
	private HolidayRepository holidayRepo;
	
	@Override
	public List<Holidays> getAll() {
		return holidayRepo.findAll();
	}

	@Override
	public void addHolidays(Holidays theHoliday) {
		
	    holidayRepo.save(theHoliday);
	}
}
