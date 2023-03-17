package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.HolidayService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class RegistrationController {
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@Autowired
	private HolidayService holidayService;
	
	@GetMapping("/registration")
	public String showRegistrationForm(Model model, Authentication authentication) {
        List<String> headers = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        List<LocalDate> holidays = new ArrayList<>();
        List<Holidays> holidayEntity = holidayService.getAll();
        
        
        for(Holidays d : holidayEntity) {
        	holidays.add(d.getHoliday_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        
       LocalDate date = LocalDate.now(); // get the current date
       // LocalDate date = LocalDate.of(2023, 4, 11);
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
    	
//    	List<Registered_list> currentMonthRegisteredList = registeredService.getRegisteredListByStaffID(authentication.getName(), firstDayOfMonth, lastDayOfMonth);
    	String status = "Register";
    	
    	List<Registered_list> currentMonthRegisteredList = registeredService.getByStaffID(authentication.getName());
    	List<LocalDate> checkedDates = new ArrayList<>();
    	
    	if(!currentMonthRegisteredList.isEmpty()) {
        	for(Registered_list registered : currentMonthRegisteredList) {
        		if(registered.getDine()==(byte)1 && registered.getDineDate().getMonthValue() == month) {
        			checkedDates.add(registered.getDineDate());
        		}
        	}
        	if(!checkedDates.isEmpty()) {
        		status = "Update";
        	}
    	}
    	System.out.println(LocalDate.now());
    	System.out.println(weeksInMonth.size());
    	List<LocalDate> disabledDates = new ArrayList<>();
    	List<LocalDate> tempDate = new ArrayList<>();
    	
    	for(List<LocalDate> week : weeksInMonth) {
    		for(LocalDate day : week) {
    			tempDate.add(day);
    			if(week.contains(LocalDate.now()) && day.getDayOfWeek()==DayOfWeek.SATURDAY) {
    				disabledDates.addAll(tempDate);
    				for(LocalDate temp : disabledDates) {
    					System.out.println("Disabled: " + temp);
    				}
    			}
    		}
    	}
    	model.addAttribute("status", status);
    	model.addAttribute("checkedDates", checkedDates);
    	model.addAttribute("holidays", holidays);
		model.addAttribute("headers", headers);
		model.addAttribute("currentMonth", date.getMonth());
		model.addAttribute("currentYear", date.getYear());
		model.addAttribute("month", weeksInMonth);
		model.addAttribute("disabledDates", disabledDates);
		return "lunch-register";
	}
	
	@PostMapping("/saveRegister")
	public String saveRegister(@RequestParam("disabledDates")List<String> disabledDates , @RequestParam("dates")List<String> dates, @RequestParam("month")List<String> month, @RequestParam("status")String status, Model model , Authentication authentication, RedirectAttributes redirAttrs) {
		List<LocalDate> checkedDates = new ArrayList<>();
		List<LocalDate> uncheckedDates = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		System.out.println(status);
		for(String date : month) {
			if(dates.contains(date)) {
				checkedDates.add(LocalDate.parse(date, formatter));
				System.out.println("Checked: " + date);
			}
			else if(!dates.contains(date) && !disabledDates.contains(date)) {
				uncheckedDates.add(LocalDate.parse(date, formatter));
				System.out.println("Unchecked: " + date);
			}
		}
		Staff staff = staffService.getStaffById(authentication.getName());
		if(status.equalsIgnoreCase("Update")) {
			for(LocalDate dineDate : checkedDates) {
				Registered_list registered = registeredService.getbyDineDate(dineDate);
				registered.setDine((byte)1);
				registered.setModify_date(LocalDate.now());
				registeredService.addRegisteredDate(registered);
				System.out.println("updated");
			}
			for(LocalDate dineDate : uncheckedDates) {
				Registered_list registered = registeredService.getbyDineDate(dineDate);
				registered.setDine((byte)0);
				registered.setModify_date(LocalDate.now());
				registeredService.addRegisteredDate(registered);
				System.out.println("updated");
			}
			redirAttrs.addFlashAttribute("message", "Your changes Lunch plan is updated successfully!");
		}else if(status.equalsIgnoreCase("Register")) {
			for(LocalDate dineDate : checkedDates) {
				Registered_list registered = new Registered_list(staff.getStaffID(), (byte)1, dineDate, LocalDate.now(), staff.getDoorLogNo());
				registeredService.addRegisteredDate(registered);
				System.out.println("Added");
			}
			for(LocalDate dineDate : uncheckedDates) {
				Registered_list registered = new Registered_list(staff.getStaffID(), (byte)0, dineDate, LocalDate.now(), staff.getDoorLogNo());
				registeredService.addRegisteredDate(registered);
				System.out.println("Added");
			}
			redirAttrs.addFlashAttribute("message", "Your Lunch plan is registered successfully!");
		}
		
		
		return "redirect:/registration";
	}
}
