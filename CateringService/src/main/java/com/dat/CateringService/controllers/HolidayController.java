package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.importHelper.HolidayExcelImport;
import com.dat.CateringService.service.HolidayService;
import com.dat.CateringService.service.RegisteredListService;

@Controller
public class HolidayController {
	
	private HolidayService holidayService;
	private RegisteredListService registeredService;
	
	public HolidayController(HolidayService holidayService, RegisteredListService registeredService) {
		super();
		this.holidayService = holidayService;
		this.registeredService = registeredService;
	}

	@GetMapping("/holiday")
	public String Holiday(Model theModel,Authentication authentication) {
		
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			List<Holidays> totalHolidays = holidayService.getAll();
			theModel.addAttribute("totalHolidays",totalHolidays);
			return "holiday";
			
		} return "redirect:/showMyLoginPage";
	}
	
	@PostMapping("/addHoliday")
	public String addHoliday(@RequestParam("holiday")String holiday, @RequestParam("description")String description) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(holiday);
		Holidays newHoliday = new Holidays(date, description);
		holidayService.addHolidays(newHoliday);
		List<Registered_list> registered = registeredService.getRegisteredStaffByDate(LocalDate.parse(holiday));
		if(!registered.isEmpty()) {
			for(Registered_list temp : registered) {
				System.out.println(temp.getName());
				temp.setDine(false);
				registeredService.addRegisteredDate(temp);
			}
			
		}
		return "redirect:/holiday";
	}
	
	@PostMapping("/importHoliday")
	public String importHoliday(@RequestParam("file") MultipartFile file, Model theModel) {
		
		if (file.isEmpty()) {
            theModel.addAttribute("message", "Please select a file to upload.");
            return "404";
        }
		
		try {
			InputStream inputStream=file.getInputStream();
			inputStream.available();
			List<Holidays> holidayObjects = HolidayExcelImport.readHolidayExcel(inputStream);
			
			for(Holidays holidayObject : holidayObjects) {
				if(Objects.isNull(holidayObject.getHoliday_ID())) {
					continue;
				}
				else {
					holidayService.addHolidays(holidayObject);
				}
			}
			theModel.addAttribute("message","File uploaded successfully.");
			theModel.addAttribute("totalHolidays",holidayService.getAll());
			return "redirect:/holiday";
		}
		catch (IOException e) {
			theModel.addAttribute("message","An error occurred while uploading the file: " + e.getMessage());
			return "404";
		}
	}

}
