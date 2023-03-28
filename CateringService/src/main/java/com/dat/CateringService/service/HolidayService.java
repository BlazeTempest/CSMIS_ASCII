package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.Holidays;

public interface HolidayService {
	
	public List<Holidays> getAll();
	public void addHolidays(Holidays theHoliday);
}
