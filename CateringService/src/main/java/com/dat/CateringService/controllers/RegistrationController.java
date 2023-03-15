package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dat.CateringService.service.StaffService;

@Controller
public class RegistrationController {
	@Autowired
	private StaffService staffService;
	
	@GetMapping("/registration")
	public String showRegistrationForm(Authentication authentication, Model model) {
		List<LocalDate> mondays = new ArrayList<>();
		List<LocalDate> tuesdays = new ArrayList<>();
		List<LocalDate> wednesdays = new ArrayList<>();
		List<LocalDate> thursdays = new ArrayList<>();
		List<LocalDate> fridays = new ArrayList<>();
		List<LocalDate> saturdays = new ArrayList<>();
		List<LocalDate> sundays = new ArrayList<>();
		
		LocalDate currentDate = LocalDate.now();
		 
		LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);

		LocalDate date = firstDayOfMonth;
		while (date.getMonth() == currentDate.getMonth()) {
		    DayOfWeek dayOfWeek = date.getDayOfWeek();
		    
		    if(dayOfWeek == DayOfWeek.MONDAY) mondays.add(date);
		    if(dayOfWeek == DayOfWeek.TUESDAY) tuesdays.add(date);
		    if(dayOfWeek == DayOfWeek.WEDNESDAY) wednesdays.add(date);
		    if(dayOfWeek == DayOfWeek.THURSDAY) thursdays.add(date);
		    if(dayOfWeek == DayOfWeek.FRIDAY) fridays.add(date);
		    if(dayOfWeek == DayOfWeek.SATURDAY) saturdays.add(date);
		    if(dayOfWeek == DayOfWeek.SUNDAY) sundays.add(date);
		    
		    date = date.plusDays(1);
		}
		
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		return "lunch-register";
	}
}
