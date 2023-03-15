package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	public String showRegistrationForm(Model model) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
		LocalDate date = LocalDate.now(); // get the current date
        int month = date.getMonthValue();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        List<List<LocalDate>> weeksInMonth = new ArrayList<>();
        
        List<String> headers = new ArrayList<>();
        
        headers.add("Sunday");
        headers.add("Monday");
        headers.add("Tuesday");
        headers.add("Wednesday");
        headers.add("Thursday");
        headers.add("Friday");
        headers.add("Saturday");
        // loop over the weeks of the month
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
        int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
        for (int weekNumber = firstWeekNumber; weekNumber <= lastWeekNumber; weekNumber++) {
            LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 1);
            LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber).with(weekFields.dayOfWeek(), 7);
            if (sundayOfWeek.getMonthValue() > month) {
                sundayOfWeek = lastDayOfMonth;
            }
            List<LocalDate> daysOfWeek = new ArrayList<>();
            for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
                if(d.getMonth() == date.getMonth()) {
                	daysOfWeek.add(d);
                }else {
                	daysOfWeek.add(null);
                }
            }
            weeksInMonth.add(daysOfWeek);
        }
        model.addAttribute("formatter", formatter);
		model.addAttribute("headers", headers);
		model.addAttribute("currentMonth", date.getMonth());
		model.addAttribute("month", weeksInMonth);
		return "lunch-register";
	}
}
