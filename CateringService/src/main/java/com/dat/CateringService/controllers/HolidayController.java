package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.importHelper.HolidayExcelImport;
import com.dat.CateringService.service.HolidayService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class HolidayController {
	
	@Autowired
	private StaffService staffService;
	
	private HolidayService holidayService;
	private RegisteredListService registeredService;
	
	public HolidayController(HolidayService holidayService, RegisteredListService registeredService) {
		super();
		this.holidayService = holidayService;
		this.registeredService = registeredService;
	}
	
	@GetMapping("/deleteholiday")
	public String deleteHoliday(@RequestParam("id")int id, RedirectAttributes attr) {
		Holidays temp = holidayService.getById(id);
		holidayService.deleteHoliday(temp);
		attr.addFlashAttribute("message", "Deleted " + temp.getHolidayName() + " ( " + temp.getHolidayDate() + " )");
		return "redirect:/holiday";
	}

	@GetMapping("/holiday")
	public String Holiday(Model theModel,Authentication authentication) {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			List<Holidays> totalHolidays = holidayService.getAllAsec();
			List<Holidays> holidays = new ArrayList<>();
			for(Holidays temp: totalHolidays) {
				if(temp.getHolidayDate().getYear()==LocalDate.now().getYear()) {
					holidays.add(temp);
				}
			}
			theModel.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
	        theModel.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
			theModel.addAttribute("totalNum", holidays.size());
			theModel.addAttribute("currentYear", LocalDate.now().getYear());
			theModel.addAttribute("totalHolidays",holidays);
			return "admin/holiday";
		} 
		return "redirect:/showMyLoginPage";
	}
	
	@PostMapping("/addHoliday")
	public String addHoliday(@RequestParam("holiday")String holiday, @RequestParam("description")String description, RedirectAttributes attr) throws ParseException {
		LocalDate date = LocalDate.parse(holiday);
		Holidays validDate = holidayService.getByDate(date);
		if(validDate == null) {
			Holidays newHoliday = new Holidays(date, description);
			holidayService.addHolidays(newHoliday);
			List<Registered_list> registered = registeredService.getRegisteredStaffByDate(LocalDate.parse(holiday));
			if(!registered.isEmpty()) {
				for(Registered_list temp : registered) {
					temp.setDine(false);
					registeredService.addRegisteredDate(temp);
				}
			}
		}else {
			attr.addFlashAttribute("message", "Selected date is already existed!");
		}
		
		return "redirect:/holiday";
	}
	
	@PostMapping("/importHoliday")
	public String importHoliday(@RequestParam("file") MultipartFile file, RedirectAttributes theModel) {
		
		if (file.isEmpty()) {
            theModel.addAttribute("message", "Please select an excel file to upload.");
            return "404";
        }
		
		try {
			InputStream inputStream=file.getInputStream();
			inputStream.available();
			List<Holidays> holidayObjects = HolidayExcelImport.readHolidayExcel(inputStream);
			List<Holidays> holidays = holidayService.getAll();
			List<LocalDate> dates = new ArrayList<>();
			for(Holidays temp : holidays) {
				dates.add(temp.getHolidayDate());
			}
			for(Holidays holidayObject : holidayObjects) {
				if(dates.contains(holidayObject.getHolidayDate())) {
					Holidays temp = holidayService.getByDate(holidayObject.getHolidayDate());
					temp.setHolidayName(holidayObject.getHolidayName());
					holidayService.addHolidays(temp);
				}else {
					holidayService.addHolidays(holidayObject);
					List<Registered_list> registered = registeredService.getRegisteredStaffByDate(holidayObject.getHolidayDate());
					if(!registered.isEmpty()) {
						for(Registered_list temp : registered) {
							temp.setDine(false);
							registeredService.addRegisteredDate(temp);
						}
					}
				}
			}
			
			theModel.addFlashAttribute("message","File uploaded successfully at " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond() + ".");
			return "redirect:/importHolidayFile";
		}
		catch (IOException e) {
			theModel.addAttribute("message","An error occurred while uploading the file: " + e.getMessage());
			return "404";
		}
	}

}
