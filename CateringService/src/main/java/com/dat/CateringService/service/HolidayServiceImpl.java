package com.dat.CateringService.service;

import java.time.LocalDate;
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

	@Override
	public List<Holidays> getAllAsec() {
		return holidayRepo.findAllByOrderByHolidayDateAsc();
	}

	@Override
	public Holidays getByDate(LocalDate date) {
		return holidayRepo.findByHolidayDate(date);
	}

	@Override
	public Holidays getByName(String name) {
		return holidayRepo.findByHolidayName(name);
	}

	@Override
	public void deleteHoliday(Holidays holiday) {
		holidayRepo.delete(holiday);
	}

	@Override
	public Holidays getById(int id) {
		return holidayRepo.getById(id);
	}
}
