package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.dat.CateringService.DTO.ReportDTO;
import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Headcount;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.DoorlogImporter;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.HeadcountService;
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
	private HeadcountService headcountService;
	
	@PostMapping("/addHeadcount")
	public String addHeadcountManually(@RequestParam("invoiceDate")String invoiceDate, @RequestParam("actual")int actualCount, RedirectAttributes attr) {
		Headcount temp = new Headcount();
		temp.setActualCount(actualCount);
		temp.setInvoiceDate(LocalDate.parse(invoiceDate));
		temp.setRegisteredCount(registeredService.getRegisteredStaffByDate(LocalDate.now()).size());
		temp.setDifference(registeredService.getRegisteredStaffByDate(LocalDate.now()).size() - actualCount);
		headcountService.saveHeadcount(temp);
		attr.addFlashAttribute("successHeadcount", "Imported headcount for " + invoiceDate + " successfully");
		return "redirect:/importFiles";
	}
	
	@GetMapping("/registered-list")
	public String plannedList(Authentication authentication , Model model, RedirectAttributes redirect) {
		List<Registered_list> registeredStaffs = registeredService.getRegisteredStaffByDate(LocalDate.now());
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("registeredStaffs", registeredStaffs);
		model.addAttribute("totalNum", registeredStaffs.size());
		return "admin/registered-list";
	}
	
	@GetMapping("/searchRegistered")
	public String searchRegistered(Authentication authentication, @RequestParam(name="division", required=false)String div, @RequestParam(name="dept", required=false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
			
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
			
			model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("registeredStaffs", registeredList);
			model.addAttribute("totalNum", registeredList.size());
			return "admin/registered-list";
	}
	
	@GetMapping("/register-eat-list")
	public String plannedEatList(Authentication authentication, Model model) {
		List<Registered_list> eatList = registeredService.getRegisteredStaffByStatusAndDineAndDate(true, true, LocalDate.now(), LocalDate.now());
		   
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
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
	public String searchEat(Authentication authentication , @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, @RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, Model model) {		
		
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
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("eatStaffs", registeredList);
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", registeredList.size());
		return "admin/register-eat-list";
	}
	
	@GetMapping("/register-not-eat-list")
	public String plannedNotEatList(Authentication authentication , Model model) {
		List<Registered_list> eatList = registeredService.getRegisteredStaffByStatusAndDineAndDate(false, true, LocalDate.now(), LocalDate.now());
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("eatStaffs", eatList);
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("totalNum", eatList.size());
		return "admin/register-not-eat-list";
	}
	
	@GetMapping("/searchCancel")
	public String searchCancel(Authentication authentication, @RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
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
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
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
	public String unplannedEatList(Authentication authentication, Model model) {
		List<DailyDoorLog> doorlogs = doorlogService.getByRegisteredAndStaffID(false, LocalDate.now(), LocalDate.of(2023,3,28));
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("totalNum", doorlogs.size());
		model.addAttribute("staffs", doorlogs);
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		return "admin/not-registereed-eat-list";
	}
	
	@GetMapping("searchUnregisteredEat")
	public String searchUnregisteredEat(Authentication authentication, @RequestParam(name = "division", required = false)String division, @RequestParam(name = "dept", required = false)String dept, @RequestParam(name="team", required=false)String team, @RequestParam(name="start", required=false)String start, @RequestParam(name="end", required=false)String end, @RequestParam(name="name", required=false)String name, @RequestParam(name="id", required=false)String id, Model model) {
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
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
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
	
	@GetMapping("/summary")
	public String summary(Authentication authentication, Model model, @RequestParam(name = "start", required = false)String start, @RequestParam(name = "end", required = false)String end) {
		if((start == "" || start == null) && (end == "" || end == null)) {
			start = end = LocalDate.now().toString();
		}
		if(end == "" || end == null) end = start; 
		if(start == "" || start == null) start = end;
		
		List<Registered_list> registered = registeredService.getRegisteredStaffByStartDateAndEndDate(LocalDate.parse(start), LocalDate.parse(end));
		List<DailyDoorLog> actual = doorlogService.getDoorlogByDineDate(LocalDate.parse(start), LocalDate.parse(end));
		List<ReportDTO> staffs = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		int type1 = 0;
		int type2 = 0;
		int type3 = 0;
		
		for(Registered_list temp:registered) {
			if(temp.getStatus() == null) {
				continue;
			} else {
				ReportDTO report = new ReportDTO();
				report.setName(temp.getName());
				report.setTeam(temp.getTeam());
				report.setStaffID(temp.getStaffID());
				report.setDineDate(temp.getDineDate());
				//staffs who don't planned and skipped lunch
				if(temp.getDine()==false && temp.getStatus()==false) continue;
				//staffs who planned and finished lunch
				if(temp.getDine()==true && temp.getStatus()==true) {
					report.setRegisteredStatus(1);
					type1++;
				}
				//staffs who planned but skipped lunch
				if(temp.getDine()==true && temp.getStatus()==false) {
					report.setRegisteredStatus(2);
					type2++;
				}
				//staffs who didn't plan but finished lunch
				if(temp.getDine()==false && temp.getStatus()==true) {
					report.setRegisteredStatus(3);
					type3++;
				}
				
				ids.add(temp.getStaffID());
				staffs.add(report);
			}
		}
		
		//staffs who didn't plan but finished lunch
		for(DailyDoorLog temp:actual) {
			ReportDTO report = new ReportDTO();
			report.setName(temp.getName());
			report.setTeam(temp.getTeam());
			report.setStaffID(temp.getStaffID());
			report.setDineDate(temp.getDineDate());
			if(ids.contains(temp.getStaffID())) {
				continue;
			}else {
				if(temp.getRegistered()==false) report.setRegisteredStatus(3);
				type3++;
			}
			staffs.add(report);
		}
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("type1", type1);
		model.addAttribute("type2", type2);
		model.addAttribute("type3", type3);
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("totalNum", staffs.size());
		model.addAttribute("staffs", staffs);
		return "admin/summary";
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
			List<Registered_list> registered = registeredService.getRegisteredStaffByStartDateAndEndDate(start, LocalDate.now());
			List<DailyDoorLog> doorlog = doorlogService.getDoorlogByDineDate(start, LocalDate.now());
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
			List<Registered_list> notEatList = registeredService.getRegisteredStaffByStartDateAndEndDate(start, LocalDate.now());
			for(Registered_list temp : notEatList) {
				if(temp.getStatus()==null) {
					temp.setStatus(false);
					registeredService.addRegisteredDate(temp);
				}
			}
			
			
			model.addFlashAttribute("success", "Uploaded Successfully");
			return "redirect:/importDoorFile";
		} catch (IOException e) {
			model.addFlashAttribute("error", "An error occurred while uploading the file" + e.toString());
			return "redirect:/404";
		}
	}
}
