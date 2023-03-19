package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.DoorlogImporter;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class ReportController {
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private DoorlogService doorlogService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@GetMapping("/registered-list")
	public String plannedList(Model model, RedirectAttributes redirect) {
//		List<Registered_list> registeredStaffs = registeredService.getRegisteredStaffByDate(LocalDate.now(), LocalDate.now());
		List<Staff> registeredStaffs = new ArrayList<>();
		for(Registered_list temp:registeredService.getRegisteredStaffByDate(LocalDate.now(), LocalDate.now())) {
			registeredStaffs.add(staffService.getStaffById(temp.getStaffID()));
		}
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("registeredStaffs", registeredStaffs);
		return "admin/registered-list";
	}
	
	@GetMapping("/searchRegistered")
	public String searchRegistered(@RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, @RequestParam(name="division", required=false)String div, @RequestParam(name="dept", required=false)String dept, Model model) {
		List<Registered_list> registeredList = new ArrayList<>();
		if(start!=null && end!=null) {
			registeredList = registeredService.getRegisteredStaffByDate(LocalDate.parse(start), LocalDate.parse(end));
		}else if(id.trim().isEmpty() && name.trim().isEmpty() && div.trim().isEmpty() && dept.trim().isEmpty()) {
			return "redirect:/registered-list";
		}else if(id!=null || name!=null || div!=null || dept!=null) {
			registeredList = registeredService.getByStaffIDAndNameAndDivisionAndDept(id, name, div, dept);
		}
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("registeredStaffs", registeredList);
		return "admin/registered-list";
	}
	
	@GetMapping("/register-eat-list")
	public String plannedEatList() {
		return "admin/register-eat-list";
	}
	
	@GetMapping("/register-not-eat-list")
	public String plannedNotEatList() {
		return "admin/register-not-eat-list";
	}
	
	@GetMapping("/not-registereed-eat-list")
	public String unplannedEatList() {
		return "admin/not-registereed-eat-list";
	}
	
	@PostMapping("/importDoorlog")
	public String importEmp(@RequestParam("file") MultipartFile file, RedirectAttributes model, Authentication authentication) {
		// read excel file
		try {
			InputStream inputStream = file.getInputStream();
			inputStream.available();
			List<DailyDoorLog> objects = DoorlogImporter.readExcel(inputStream);
			List<DailyDoorLog> doorlogs = objects.stream().distinct().collect(Collectors.toList());
			List<DailyDoorLog> todayImported = doorlogService.getDoorlogByImportedDate(LocalDate.now());
			List<String> ids = new ArrayList<>();
			for(DailyDoorLog temp : todayImported) {
				ids.add(temp.getStaffID());
			}
			for(DailyDoorLog temp : doorlogs) {
				if(ids.contains(temp.getStaffID())) {
					System.out.println("Skipped " + temp.getStaffID());
					continue;
				}else {
					System.out.println(temp.getStaffID());
					temp.setImported_by(staffService.getStaffById(authentication.getName()).getName());
					temp.setImported_date(LocalDate.now());
					doorlogService.add(temp);
					System.out.println("Added");
				}
			}
			
			model.addFlashAttribute("success", "Uploaded Successfully");
			return "redirect:/registered-list";
		} catch (IOException e) {
			model.addFlashAttribute("error", "An error occurred while uploading the file" + e.toString());
			return "redirect:/registered-list";
		}
	}
}
