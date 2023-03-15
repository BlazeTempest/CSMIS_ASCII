package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.importHelper.HolidayExcelImport;
import com.dat.CateringService.service.HolidayService;

@Controller
public class HolidayController {
	
	private HolidayService holidayService;
	
	public HolidayController(HolidayService holidayService) {
		super();
		this.holidayService = holidayService;
	}

	@GetMapping("/holiday")
	public String Holiday(Model theModel) {
		List<Holidays> totalHolidays = holidayService.getAll();
		theModel.addAttribute("totalHolidays",totalHolidays);
		return "holiday";
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
