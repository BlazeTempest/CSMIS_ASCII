package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.jfree.data.time.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class RegistrationController {
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
        
        List<String> headers = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));

        LocalDate date = LocalDate.now(); // get the current date
    	int month = date.getMonthValue();
    	WeekFields weekFields = WeekFields.of(Locale.getDefault());
    	List<List<LocalDate>> weeksInMonth = new ArrayList<>();
    	// loop over the weeks of the month
    	LocalDate firstDayOfMonth = date.withDayOfMonth(1);
    	LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
    	int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
    	int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());

    	// add days from previous month to first week
    	List<LocalDate> daysOfFirstWeek = new ArrayList<>();
    	LocalDate prevMonthLastDay = firstDayOfMonth.minusDays(1);
    	int prevMonthDaysToAdd = firstDayOfMonth.getDayOfWeek().getValue() % 7;
    	for (int i = prevMonthDaysToAdd - 1; i >= 0; i--) {
    	    daysOfFirstWeek.add(prevMonthLastDay.minusDays(i));
    	}

    	// add days of current month to first week
    	for (int i = 1; i <= 7 - prevMonthDaysToAdd; i++) {
    	    daysOfFirstWeek.add(firstDayOfMonth.plusDays(i - 1));
    	}
    	weeksInMonth.add(daysOfFirstWeek);

    	// loop over the remaining weeks of the month
    	for (int weekNumber = firstWeekNumber + 1; weekNumber <= lastWeekNumber; weekNumber++) {
    	    LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 1);
    	    LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 7);
    	    if (sundayOfWeek.getMonthValue() > month) {
    	        sundayOfWeek = lastDayOfMonth;
    	    }
    	    List<LocalDate> daysOfWeek = new ArrayList<>();
    	    for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
    	        daysOfWeek.add(d);
    	    }
    	    weeksInMonth.add(daysOfWeek);
    	}
    	
    	// add days from next month to the last week
    	List<LocalDate> daysOfLastWeek = weeksInMonth.get(weeksInMonth.size() - 1);
    	LocalDate nextMonthFirstDay = lastDayOfMonth.plusDays(1);
    	int nextMonthDaysToAdd = 7 - daysOfLastWeek.size();
    	for (int i = 1; i <= nextMonthDaysToAdd; i++) {
    	    daysOfLastWeek.add(nextMonthFirstDay.plusDays(i - 1));
    	}
        
		model.addAttribute("headers", headers);
		model.addAttribute("currentMonth", date.getMonth());
		model.addAttribute("month", weeksInMonth);
		return "lunch-register";
	}
	
	@PostMapping("/saveRegister")
	public String saveRegister(@RequestParam("dates")List<String> dates) {
		for(String date:dates) {
			System.out.println(date);
		}
		return "redirect:/registration";
	}
}
