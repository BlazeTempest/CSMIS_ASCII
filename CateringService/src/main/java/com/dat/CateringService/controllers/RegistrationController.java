package com.dat.CateringService.controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.AvoidMeatService;
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

	@Autowired
	private AvoidMeatService avoidMeatService;

	@GetMapping("/registration")
	public String showRegistrationForm(Model model, Authentication authentication) {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if(role.equals("admin")) {
			List<String> headers = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
			List<LocalDate> holidays = new ArrayList<>();
			List<Holidays> holidayEntity = holidayService.getAll();
			for (Holidays d : holidayEntity) {
				holidays.add(d.getHoliday_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
	
			LocalDate date = LocalDate.now(); // get the current date
			int month = date.getMonthValue();
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			List<List<LocalDate>> weeksInMonth = new ArrayList<>();
	
			// loop over the weeks of the month
			LocalDate firstDayOfMonth = date.withDayOfMonth(1);
			LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
			int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
			int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
			int currentWeeknumber = date.get(weekFields.weekOfWeekBasedYear());
	
			String status = "Register";
	
			if (currentWeeknumber == lastWeekNumber) {
				LocalDate TempDate = date.plusMonths(1);
				date = LocalDate.of(date.getYear(), TempDate.getMonth(), 1);
				firstDayOfMonth = date.withDayOfMonth(1);
				lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
				firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
				lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
				currentWeeknumber = date.get(weekFields.weekOfWeekBasedYear());
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
					LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 1);
					LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 7);
					if (sundayOfWeek.getMonthValue() > month) {
						sundayOfWeek = lastDayOfMonth;
					}
					int weekSize = 0;
					List<LocalDate> daysOfWeek = new ArrayList<>();
					for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
						if (weekSize < 7) {
							daysOfWeek.add(d);
							weekSize++;
						}
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
				status = "Register";
				month += 1;
			} else {
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
					LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 1);
					LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 7);
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
				status = "Register";
			}
			List<Registered_list> currentMonthRegisteredList = registeredService.getByStaffID(authentication.getName());
			List<LocalDate> checkedDates = new ArrayList<>();
			if (!currentMonthRegisteredList.isEmpty()) {
				for (Registered_list registered : currentMonthRegisteredList) {
					if (registered.getDine() == true && registered.getDineDate().getMonthValue() == month) {
						checkedDates.add(registered.getDineDate());
					}
				}
				if (!checkedDates.isEmpty()) {
					status = "Update";
				}
			}
	
			List<LocalDate> disabledDates = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfMonth2 = currentDate.with(TemporalAdjusters.firstDayOfMonth());
			LocalDate lastDayOfMonth2 = currentDate.with(TemporalAdjusters.lastDayOfMonth());
			LocalDate firstMonday = firstDayOfMonth2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
			LocalDate lastFriday = lastDayOfMonth2.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
			LocalDate previousMonday = firstMonday.minusDays(7);
			LocalDate previousFriday = lastFriday.minusDays(7);
			disabledDates.addAll(previousMonday.datesUntil(previousFriday.plusDays(1)).collect(Collectors.toList()));
			disabledDates.addAll(firstMonday.datesUntil(lastFriday.plusDays(1)).collect(Collectors.toList()));
			LocalTime currentTime = LocalTime.now();
			if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY && currentTime.isAfter(LocalTime.of(13, 0))) {
				LocalDate nextMonday = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
				LocalDate nextFriday = currentDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
				disabledDates.addAll(nextMonday.datesUntil(nextFriday.plusDays(1)).collect(Collectors.toList()));
			} else if (currentDate.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) > 0) {
				LocalDate nextMonday = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
				LocalDate nextFriday = currentDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
				disabledDates.addAll(nextMonday.datesUntil(nextFriday.plusDays(1)).collect(Collectors.toList()));
			}
			
			List<AvoidMeat> avoidMeats = avoidMeatService.findAll();
			String staffAvoidMeats = staffService.getStaffById(authentication.getName()).getAvoidMeatIds();
			List<AvoidMeat> checkedMeats = new ArrayList<>();
			if (staffAvoidMeats != null) {
				List<String> Meats = Arrays.asList(staffAvoidMeats.split(","));
				if (!staffAvoidMeats.equals("")) {
					for (String id : Meats) {
						int tempId = Integer.parseInt(id);
						checkedMeats.add(avoidMeatService.findById(tempId));
					}
	
				}
			}
			model.addAttribute("currentDay", date.getDayOfMonth());
			model.addAttribute("checkedMeats", checkedMeats);
			model.addAttribute("avoidmeats", avoidMeats);
			model.addAttribute("status", status);
			model.addAttribute("checkedDates", checkedDates);
			model.addAttribute("holidays", holidays);
			model.addAttribute("headers", headers);
			model.addAttribute("currentMonth", date.getMonth());
			model.addAttribute("currentYear", date.getYear());
			model.addAttribute("month", weeksInMonth);
			model.addAttribute("disabledDates", disabledDates);
			return "lunch-register";
		}else {
			List<String> headers = new ArrayList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
			List<LocalDate> holidays = new ArrayList<>();
			List<Holidays> holidayEntity = holidayService.getAll();
			for (Holidays d : holidayEntity) {
				holidays.add(d.getHoliday_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			}
	
			LocalDate date = LocalDate.now(); // get the current date
			int month = date.getMonthValue();
			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			List<List<LocalDate>> weeksInMonth = new ArrayList<>();
	
			// loop over the weeks of the month
			LocalDate firstDayOfMonth = date.withDayOfMonth(1);
			LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
			int firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
			int lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
			int currentWeeknumber = date.get(weekFields.weekOfWeekBasedYear());
	
			String status = "Register";
	
			if (currentWeeknumber == lastWeekNumber) {
				LocalDate TempDate = date.plusMonths(1);
				date = LocalDate.of(date.getYear(), TempDate.getMonth(), 1);
				firstDayOfMonth = date.withDayOfMonth(1);
				lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());
				firstWeekNumber = firstDayOfMonth.get(weekFields.weekOfWeekBasedYear());
				lastWeekNumber = lastDayOfMonth.get(weekFields.weekOfWeekBasedYear());
				currentWeeknumber = date.get(weekFields.weekOfWeekBasedYear());
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
					LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 1);
					LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 7);
					if (sundayOfWeek.getMonthValue() > month) {
						sundayOfWeek = lastDayOfMonth;
					}
					int weekSize = 0;
					List<LocalDate> daysOfWeek = new ArrayList<>();
					for (LocalDate d = mondayOfWeek; !d.isAfter(sundayOfWeek); d = d.plusDays(1)) {
						if (weekSize < 7) {
							daysOfWeek.add(d);
							weekSize++;
						}
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
				status = "Register";
				month += 1;
			} else {
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
					LocalDate mondayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 1);
					LocalDate sundayOfWeek = firstDayOfMonth.with(weekFields.weekOfWeekBasedYear(), weekNumber)
							.with(weekFields.dayOfWeek(), 7);
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
				status = "Register";
			}
			List<Registered_list> currentMonthRegisteredList = registeredService.getByStaffID(authentication.getName());
			List<LocalDate> checkedDates = new ArrayList<>();
			if (!currentMonthRegisteredList.isEmpty()) {
				for (Registered_list registered : currentMonthRegisteredList) {
					if (registered.getDine() == true && registered.getDineDate().getMonthValue() == month) {
						checkedDates.add(registered.getDineDate());
					}
				}
				if (!checkedDates.isEmpty()) {
					status = "Update";
				}
			}
	
			List<LocalDate> disabledDates = new ArrayList<>();
			LocalDate currentDate = LocalDate.now();
			LocalDate firstDayOfMonth2 = currentDate.with(TemporalAdjusters.firstDayOfMonth());
			LocalDate lastDayOfMonth2 = currentDate.with(TemporalAdjusters.lastDayOfMonth());
			LocalDate firstMonday = firstDayOfMonth2.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
			LocalDate lastFriday = lastDayOfMonth2.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
			LocalDate previousMonday = firstMonday.minusDays(7);
			LocalDate previousFriday = lastFriday.minusDays(7);
			disabledDates.addAll(previousMonday.datesUntil(previousFriday.plusDays(1)).collect(Collectors.toList()));
			disabledDates.addAll(firstMonday.datesUntil(lastFriday.plusDays(1)).collect(Collectors.toList()));
			LocalTime currentTime = LocalTime.now();
			if (currentDate.getDayOfWeek() == DayOfWeek.FRIDAY && currentTime.isAfter(LocalTime.of(13, 0))) {
				LocalDate nextMonday = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
				LocalDate nextFriday = currentDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
				disabledDates.addAll(nextMonday.datesUntil(nextFriday.plusDays(1)).collect(Collectors.toList()));
			} else if (currentDate.getDayOfWeek().compareTo(DayOfWeek.FRIDAY) > 0) {
				LocalDate nextMonday = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
				LocalDate nextFriday = currentDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
				disabledDates.addAll(nextMonday.datesUntil(nextFriday.plusDays(1)).collect(Collectors.toList()));
			}
			
			
	
			List<AvoidMeat> avoidMeats = avoidMeatService.findAll();
			String staffAvoidMeats = staffService.getStaffById(authentication.getName()).getAvoidMeatIds();
			List<AvoidMeat> checkedMeats = new ArrayList<>();
			if (staffAvoidMeats != null) {
				List<String> Meats = Arrays.asList(staffAvoidMeats.split(","));
				if (!staffAvoidMeats.equals("")) {
					for (String id : Meats) {
						int tempId = Integer.parseInt(id);
						checkedMeats.add(avoidMeatService.findById(tempId));
					}
	
				}
			}
			model.addAttribute("currentDay", date.getDayOfMonth());
			model.addAttribute("checkedMeats", checkedMeats);
			model.addAttribute("avoidmeats", avoidMeats);
			model.addAttribute("status", status);
			model.addAttribute("checkedDates", checkedDates);
			model.addAttribute("holidays", holidays);
			model.addAttribute("headers", headers);
			model.addAttribute("currentMonth", date.getMonth());
			model.addAttribute("currentYear", date.getYear());
			model.addAttribute("month", weeksInMonth);
			model.addAttribute("disabledDates", disabledDates);
			return "employee-lunch-register";
		}
	}

	@PostMapping("/saveRegister")
	public String saveRegister(@RequestParam(value = "avoidMeats", required = false) List<String> avoidMeats,
			@RequestParam("disabledDates") List<String> disabledDates, @RequestParam("dates") List<String> dates,
			@RequestParam("month") List<String> month, @RequestParam("status") String status, Model model,
			Authentication authentication, RedirectAttributes redirAttrs) {
		List<LocalDate> checkedDates = new ArrayList<>();
		List<LocalDate> uncheckedDates = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (String date : month) {
			if (dates.contains(date)) {
				checkedDates.add(LocalDate.parse(date, formatter));
			} else if (!dates.contains(date) && !disabledDates.contains(date)) {
				uncheckedDates.add(LocalDate.parse(date, formatter));
			}
		}
		Staff staff = staffService.getStaffById(authentication.getName());
		if (status.equalsIgnoreCase("Update")) {
			for (LocalDate dineDate : checkedDates) {
				Registered_list registered = registeredService.getbyStaffIDAndDineDate(staff.getStaffID(), dineDate);
				registered.setDine(true);
				registered.setModify_date(LocalDateTime.now());
				registeredService.addRegisteredDate(registered);
				System.out.println("updated");
			}
			for (LocalDate dineDate : uncheckedDates) {
				Registered_list registered = registeredService.getbyStaffIDAndDineDate(staff.getStaffID(), dineDate);
				registered.setDine(false);
				registered.setModify_date(LocalDateTime.now());
				registeredService.addRegisteredDate(registered);
				System.out.println("updated");
			}
			redirAttrs.addFlashAttribute("message", "Your changes Lunch plan is updated successfully!");
		} else if (status.equalsIgnoreCase("Register")) {
			for (LocalDate dineDate : checkedDates) {
				Registered_list registered = new Registered_list(staff.getStaffID(), true, dineDate,
						LocalDateTime.now(), staff.getDoorLogNo(), staff.getName(), staff.getDivision(),
						staff.getDept(), staff.getTeam());
				registeredService.addRegisteredDate(registered);
				System.out.println("Added");
			}
			for (LocalDate dineDate : uncheckedDates) {
				Registered_list registered = new Registered_list(staff.getStaffID(), false, dineDate,
						LocalDateTime.now(), staff.getDoorLogNo(), staff.getName(), staff.getDivision(),
						staff.getDept(), staff.getTeam());
				registeredService.addRegisteredDate(registered);
				System.out.println("Added");
			}
			redirAttrs.addFlashAttribute("message", "Your Lunch plan is registered successfully!");
		}
		String avoidMeatList = "";
		if (avoidMeats != null) {

			for (String avoidMeat : avoidMeats) {
				System.out.println(avoidMeat);
				if (avoidMeatList.isEmpty() || avoidMeatList.isEmpty()) {
					avoidMeatList = avoidMeat;
				} else {
					avoidMeatList = avoidMeatList + "," + avoidMeat;
				}
			}

		}

		staff.setAvoidMeatIds(avoidMeatList);
		staffService.addStaff(staff);

		return "redirect:/registration";
	}
}
