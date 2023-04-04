package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.Holidays;

public interface HolidayRepository extends JpaRepository<Holidays, Integer>{
	public List<Holidays> findAllByOrderByHolidayDateAsc();
	public Holidays findByHolidayDate(LocalDate holidayDate);
	public Holidays findByHolidayName(String holidayName);
}
