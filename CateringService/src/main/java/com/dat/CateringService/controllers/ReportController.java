package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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

import com.dat.CateringService.DTO.ReportDTO;
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
		List<Registered_list> registeredStaffs = registeredService.getRegisteredStaffByStartDateAndEndDate(LocalDate.now(), LocalDate.now());
		System.out.println("Total registered list >>>>>>>>>>>"+registeredStaffs);
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("registeredStaffs", registeredStaffs);
		model.addAttribute("totalNum", registeredStaffs.size());
		return "admin/registered-list";
	}
	
	@GetMapping("/searchRegistered")
	public String searchRegistered(@RequestParam(name="division", required=false)String div, @RequestParam(name="dept", required=false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
			
			if(end=="") end = start;
			List<Registered_list> registeredList = new ArrayList<>();
			//Searching
			if(name!="" || id!="" || team!="") {
				registeredList = registeredService.searchByNameAndId(name, id, team);
				if(registeredList!=null && start!="") {
					List<Registered_list> toRemove = new ArrayList<>();
					for(Registered_list temp:registeredList) {
						if(temp.getDineDate().isBefore(LocalDate.parse(start)) || temp.getDineDate().isAfter(LocalDate.parse(end))) {
							toRemove.add(temp);
						}
					}
					registeredList.removeAll(toRemove);
				}
				model.addAttribute("start", start);
				model.addAttribute("end", end);
				model.addAttribute("team", team);
				model.addAttribute("searchName", name);
				model.addAttribute("id", id);
			} else if(start!="") {
				registeredList = registeredService.getRegisteredStaffByStartDateAndEndDate(LocalDate.parse(start), LocalDate.parse(end));
				
				model.addAttribute("start", start);
				model.addAttribute("end", end);
			}
			if(div!="" && div!=null) {
				List<Registered_list> toRemove = new ArrayList<>();
				for(Registered_list temp:registeredList) {
					if(!temp.getDivision().equals(div)) {
						toRemove.add(temp);
					}
				}
				registeredList.removeAll(toRemove);
			} else if(dept!=null && dept!=null) {
				List<Registered_list> toRemove = new ArrayList<>();
				for(Registered_list temp:registeredList) {
					if(!temp.getDept().equals(dept)) {
						toRemove.add(temp);
					}
				}
				registeredList.removeAll(toRemove);
			}
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("registeredStaffs", registeredList);
			model.addAttribute("totalNum", registeredList.size());
			return "admin/registered-list";
	}
	
	@GetMapping("/register-eat-list")
	public String plannedEatList(Model model) {
		List<Registered_list> eatList = registeredService.getRegisteredStaffByStatusAndDineAndDate(true, true, LocalDate.now(), LocalDate.now());
		
		model.addAttribute("start", LocalDate.now().minusDays(5));
		model.addAttribute("end", LocalDate.now());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/searchEat")
	public String searchEat(@RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, @RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, Model model) {		
		
		if(end=="") end = start;
		List<Registered_list> registeredList = new ArrayList<>();
		//Searching
		if(name!="" || id!="" || team!="") {
			registeredList = registeredService.getRegisteredStaffByStatusAndNameAndID(true, name, id, team);
			if(registeredList!=null && start!="") {
				List<Registered_list> toRemove = new ArrayList<>();
				for(Registered_list temp:registeredList) {
					if(temp.getDineDate().isBefore(LocalDate.parse(start)) || temp.getDineDate().isAfter(LocalDate.parse(end))) {
						toRemove.add(temp);
					}
				}
				registeredList.removeAll(toRemove);
			}
			model.addAttribute("start", start);
			model.addAttribute("end", end);
			model.addAttribute("team", team);
			model.addAttribute("searchName", name);
			model.addAttribute("id", id);
		} else if(start!="") {
			registeredList = registeredService.getRegisteredStaffByStatusAndDate(true, LocalDate.parse(start), LocalDate.parse(end));
			
			model.addAttribute("start", start);
			model.addAttribute("end", end);
		}
		
		if(division!="" && division!=null) {
			List<Registered_list> toRemove = new ArrayList<>();
			for(Registered_list temp:registeredList) {
				if(!temp.getDivision().equals(division)) {
					toRemove.add(temp);
				}
			}
			registeredList.removeAll(toRemove);
		} else if(dept!=null && dept!=null) {
			List<Registered_list> toRemove = new ArrayList<>();
			for(Registered_list temp:registeredList) {
				if(!temp.getDept().equals(dept)) {
					toRemove.add(temp);
				}
			}
			registeredList.removeAll(toRemove);
		}
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", registeredList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", registeredList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/register-not-eat-list")
	public String plannedNotEatList(Model model) {
		List<Registered_list> eatList = registeredService.getRegisteredStaffByStatusAndDineAndDate(false, true, LocalDate.now(), LocalDate.now());
		
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-not-eat-list";
	}
	
	@GetMapping("/searchCancel")
	public String searchCancel(@RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
		if(end=="") end = start;
		List<Registered_list> registeredList = new ArrayList<>();
		
		if(name!="" || id!="" || team!="") {
			registeredList = registeredService.getRegisteredStaffByStatusAndNameAndID(false, name, id, team);
			if(registeredList!=null && start!="") {
				List<Registered_list> toRemove = new ArrayList<>();
				for(Registered_list temp:registeredList) {
					if(temp.getDineDate().isBefore(LocalDate.parse(start)) || temp.getDineDate().isAfter(LocalDate.parse(end))) {
						toRemove.add(temp);
					}
				}
				registeredList.removeAll(toRemove);
			}
		}
		else if(start!="" && end !="") {
			registeredList = registeredService.getRegisteredStaffByStatusAndDate(false, LocalDate.parse(start), LocalDate.parse(end));
		}
		
		if(division!="" && division!=null) {
			List<Registered_list> toRemove = new ArrayList<>();
			for(Registered_list temp:registeredList) {
				if(!temp.getDivision().equals(division)) {
					System.out.println(temp.getDivision());
					toRemove.add(temp);
				}
			}
			registeredList.removeAll(toRemove);
		}else if(dept!="" && dept!=null) {
			List<Registered_list> toRemove = new ArrayList<>();
			for(Registered_list temp:registeredList) {
				if(!temp.getDept().equals(dept)) {
					toRemove.add(temp);
				}
			}
			registeredList.removeAll(toRemove);
		}
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", registeredList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("team", team);
		model.addAttribute("searchName", name);
		model.addAttribute("id", id);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("totalNum", registeredList.size());
		return "admin/register-not-eat-list";
	}

	@GetMapping("/not-registereed-eat-list")
	public String unplannedEatList(Model model) {
		List<DailyDoorLog> doorlogs = doorlogService.getByRegisteredAndStaffID(false, LocalDate.now(), LocalDate.of(2023,3,28));
		
		model.addAttribute("totalNum", doorlogs.size());
		model.addAttribute("staffs", doorlogs);
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		return "admin/not-registereed-eat-list";
	}
	
	@GetMapping("searchUnregisteredEat")
	public String searchUnregisteredEat(@RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
		if(end=="") end = start;
		List<DailyDoorLog> doorlogs = new ArrayList<>();
		if(name!="" || id!="" || team!="") {
			doorlogs = doorlogService.getByAll(false, name, id, team);
			if(doorlogs!=null && start!="") {
				List<DailyDoorLog> toRemove = new ArrayList<>();
				for(DailyDoorLog temp:doorlogs) {
					if(temp.getDineDate().isBefore(LocalDate.parse(start)) || temp.getDineDate().isAfter(LocalDate.parse(end))) {
						toRemove.add(temp);
					}
				}
				doorlogs.removeAll(toRemove);
			}
		}else if(start!="" && end !="") {
			doorlogs = doorlogService.getByRegisteredAndStaffID(false, LocalDate.parse(start), LocalDate.parse(end));
		}
		
		if(division!="" && division!=null) {
			List<DailyDoorLog> toRemove = new ArrayList<>();
			for(DailyDoorLog temp:doorlogs) {
				if(!temp.getDivision().equals(division)) {
					toRemove.add(temp);
				}
			}
			doorlogs.removeAll(toRemove);
		}else if(dept!="" && dept!=null) {
			List<DailyDoorLog> toRemove = new ArrayList<>();
			for(DailyDoorLog temp:doorlogs) {
				if(!temp.getDept().equals(dept)) {
					toRemove.add(temp);
				}
			}
			doorlogs.removeAll(toRemove);
		}
		
		
		model.addAttribute("team", team);
		model.addAttribute("searchName", name);
		model.addAttribute("id", id);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("totalNum", doorlogs.size());
		model.addAttribute("staffs", doorlogs);
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
					Staff staff = staffService.getByDoorlog(temp.getDoorLogNo());
					
					if(staff!=null) {
						Registered_list registered = registeredService.getbyStaffIDAndDineDate(staff.getStaffID(), temp.getDineDate());
						temp.setStaffID(staff.getStaffID());
						temp.setImported_by(staffService.getStaffById(authentication.getName()).getName());
						temp.setImported_date(LocalDateTime.now());
						temp.setName(staff.getName());
						temp.setTeam(staff.getTeam());
						temp.setDivision(staff.getDivision());
						temp.setDept(staff.getDept());
						if(registered!=null) {
							temp.setRegistered(true);
						}else {
							temp.setRegistered(false);
						}
						
						doorlogService.add(temp);
					}
					else {
						continue;
					}
				}
			}
			
			DailyDoorLog ddl = doorlogService.getLastInserted();
			LocalDate start = ddl.getDineDate();
			List<Registered_list> registered = registeredService.getRegisteredStaffByStartDateAndEndDate(start, LocalDate.of(2023,3,28));
			List<DailyDoorLog> doorlog = doorlogService.getDoorlogByDineDate(start, LocalDate.of(2023,3,28));
			List<Registered_list> toRemove = new ArrayList<>();
			for(Registered_list register: registered) {
				for(DailyDoorLog log : doorlog) {
					if(log.getStaffID().equals(register.getStaffID())) {
						register.setStatus(true);
						registeredService.addRegisteredDate(register);
						int index = registered.indexOf(register);
						if(index != -1) {
							toRemove.add(register);
						}
					}
				}
			}
			registered.removeAll(toRemove);
			for(Registered_list temp : registered) {
				temp.setStatus(false);
				registeredService.addRegisteredDate(temp);
			}
			
			model.addFlashAttribute("success", "Uploaded Successfully");
			return "redirect:/registered-list";
		} catch (IOException e) {
			model.addFlashAttribute("error", "An error occurred while uploading the file" + e.toString());
			return "redirect:/404";
		}
	}
	
	public List<ReportDTO> getUnregisteredEatList(){
		List<Registered_list> registered = registeredService.getRegisteredStaffByDate(LocalDate.now());
		List<String> doorlogIds = doorlogService.getStaffIDByDineDate(LocalDate.now());
		List<ReportDTO> staffs = new ArrayList<>();
		List<String> toRemove = new ArrayList<>();
		for(Registered_list register : registered) {
			if(doorlogIds.contains(register.getStaffID())) {
				toRemove.add(register.getStaffID());
			}
		}
		doorlogIds.removeAll(toRemove);
		for(String id : doorlogIds) {
			Staff staff = staffService.getStaffById(id);
			staffs.add(new ReportDTO(staff.getStaffID(), LocalDate.now(), staff.getDoorLogNo(), staff.getName(), staff.getDivision(), staff.getDept(), staff.getTeam()));
		}
		return staffs;
	}
}
