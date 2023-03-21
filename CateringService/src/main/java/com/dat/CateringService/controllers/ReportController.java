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
import com.dat.CateringService.entity.RegisteredEat;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.DoorlogImporter;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.RegisteredEatService;
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
	
	@Autowired
	private RegisteredEatService eatService;
	
	@GetMapping("/registered-list")
	public String plannedList(Model model, RedirectAttributes redirect) {
		List<Registered_list> registeredStaffs = registeredService.getRegisteredStaffByStartDateAndEndDate(LocalDate.now(), LocalDate.now());
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("registeredStaffs", registeredStaffs);
		return "admin/registered-list";
	}
	
	@GetMapping("/searchRegistered")
	public String searchRegistered(@RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
		try {
			if(end=="") {
				end = start;
			}
			List<Registered_list> registeredList = new ArrayList<>();
			if(id!="" && team!="" && name!="" && start!="" && end!="") {
				System.out.println("Searching with all fields");
				registeredList = registeredService.getRegisteredListByNameAndStaffID(name, id, team, LocalDate.parse(start), LocalDate.parse(end));
				model.addAttribute("team", team);
				model.addAttribute("searchName", name);
				model.addAttribute("id", id);
				model.addAttribute("start", start);
				model.addAttribute("end", end);
			}
			else if(name!="" && start!="" && end!="") {
				System.out.println("Searching with name and dates!");
				registeredList = registeredService.getRegisteredListByNameAndDate(name, LocalDate.parse(start), LocalDate.parse(end));
				model.addAttribute("searchName", name);
				model.addAttribute("start", start);
				model.addAttribute("end", end);
			}
			else if(id!="" && start!="" && end!="") {
				System.out.println("Searching with Id and dates!");
				registeredList = registeredService.getRegisteredListByIdAndDate(id, LocalDate.parse(start), LocalDate.parse(end));
				model.addAttribute("id", id);
				model.addAttribute("start", start);
				model.addAttribute("end", end);
			}
			else if(id!="" || name!="" || team!="") {
				System.out.println("Name and Id searching");
				registeredList = registeredService.searchByNameAndId(name, id, team);
				model.addAttribute("team", team);
				model.addAttribute("searchName", name);
				model.addAttribute("id", id);
			}
			else if(start!="" && end!="") {
				System.out.println("Date Searching");
				registeredList = registeredService.getRegisteredStaffByStartDateAndEndDate(LocalDate.parse(start), LocalDate.parse(end));
				model.addAttribute("start", start);
				model.addAttribute("end", end);
			}
			
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("registeredStaffs", registeredList);
			model.addAttribute("totalNum", registeredList.size());
			return "admin/registered-list";
		}catch(NullPointerException e) {
			return "redirect:/registered-list";
		}
	}
	
	@GetMapping("/filter")
	public String filterByDivAndDept(@RequestParam(name="division", required=false)String div, @RequestParam(name="dept", required=false)String dept, Model model) {
		try {
			List<Registered_list> registered = new ArrayList<>();
			
			if(div!=null) {
				System.out.println("Division");
				registered = registeredService.filterByDivision(div);
			}else if(dept!=null) {
				System.out.println("Department");
				registered = registeredService.filterByDept(dept);
			}
			
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("registeredStaffs", registered);
			model.addAttribute("totalNum", registered.size());
			return "admin/registered-list";
		}catch(NullPointerException e) {
			return "redirect:/registered-list";
		}
	}
	
	@GetMapping("/register-eat-list")
	public String plannedEatList(Model model) {
		List<RegisteredEat> eatList = eatService.getEatlistByDate(LocalDate.of(2023, 3, 28), LocalDate.of(2023, 3, 28));
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/searchEat")
	public String searchEat(@RequestParam("start")String start, @RequestParam("end")String end, Model model) {
		List<RegisteredEat> eatList = eatService.getEatlistByDate(LocalDate.parse(start), LocalDate.parse(end));
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/filterEat")
	public String filterEat(@RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, Model model) {
		List<RegisteredEat> eatList = new ArrayList<>();
		if(division!="" && division!=null) {
			System.out.println("Division");
			eatList = eatService.filterByDiv(division);
		}else if(dept!=null && dept!=null) {
			System.out.println("Department");
			eatList = eatService.filterByDept(dept);
		}
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/register-not-eat-list")
	public String plannedNotEatList(Model model) {
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		return "admin/register-not-eat-list";
	}
	
	@GetMapping("/not-registereed-eat-list")
	public String unplannedEatList(Model model) {
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
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
			List<Integer> ids = new ArrayList<>();
			
			for(DailyDoorLog temp : todayImported) {
				ids.add(temp.getDoorLogNo());
			}
			for(DailyDoorLog temp : doorlogs) {
				if(ids.contains(temp.getDoorLogNo())) {
					continue;
				}else {
					System.out.println(temp.getDineDate());
					System.out.println(temp.getDoorLogNo());
					Staff staff = staffService.getByDoorlog(temp.getDoorLogNo());
					if(staff!=null) {
						temp.setStaffID(staff.getStaffID());
						temp.setImported_by(staffService.getStaffById(authentication.getName()).getName());
						temp.setImported_date(LocalDate.now());
						doorlogService.add(temp);
					}
					else {
						continue;
					}
				}
			}
			
			DailyDoorLog ddl = doorlogService.getLastInserted();
			LocalDate start = ddl.getDineDate();
			System.out.println(start);
			List<Registered_list> registered = registeredService.getRegisteredStaffByStartDateAndEndDate(start, LocalDate.of(2023, 3, 28));

			List<DailyDoorLog> doorlog = doorlogService.getDoorlogByDineDate(start, LocalDate.of(2023, 3, 28));
			List<RegisteredEat> eatList = new ArrayList<>();
			for(Registered_list register: registered) {
				for(DailyDoorLog log : doorlog) {
					if(log.getStaffID().equals(register.getStaffID())) {
//						RegisteredEat eat = new RegisteredEat(register.getStaffID(), register.getName(),register.getDivision(), register.getDept(), register.getDineDate(), register.getTeam());
//						eatList.add(eat);
						register.setStatus((byte)1);
						registeredService.addRegisteredDate(register);
						registered.remove(registered.indexOf(register));
					}
				}
			}
			for(Registered_list temp : registered) {
				temp.setStatus((byte)0);
				registeredService.addRegisteredDate(temp);
			}
//			eatService.addAll(eatList);
			
			model.addFlashAttribute("success", "Uploaded Successfully");
			return "redirect:/registered-list";
		} catch (IOException e) {
			model.addFlashAttribute("error", "An error occurred while uploading the file" + e.toString());
			return "redirect:/registered-list";
		}
	}
}
