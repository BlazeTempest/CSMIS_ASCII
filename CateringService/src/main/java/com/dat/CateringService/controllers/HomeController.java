package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Holidays;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.ExcelImporter;
import com.dat.CateringService.service.AvoidMeatService;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.HolidayService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class HomeController {
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@Autowired
	private HolidayService holidayService;

	@Autowired
	private AvoidMeatService avoidMeatService;
	
	@Autowired
	private DoorlogService doorService;
	
	@GetMapping("/detailCalendar")
	public String calendarDetail(@RequestParam("id")String id, Model model, Authentication authentication) {
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
		
		List<Registered_list> currentMonthRegisteredList = registeredService.getByStaffID(authentication.getName());
		List<LocalDate> checkedDates = new ArrayList<>();
		if (!currentMonthRegisteredList.isEmpty()) {
			for (Registered_list registered : currentMonthRegisteredList) {
				if (registered.getDine() == true && registered.getDineDate().getMonthValue() == month) {
					checkedDates.add(registered.getDineDate());
				}
			}
		}
		List<DailyDoorLog> actualEatDates = doorService.getDoorlogByDineDate(firstDayOfMonth, lastDayOfMonth);
		List<LocalDate> actualDates = new ArrayList<>();
		if(!actualEatDates.isEmpty()) {
			for(DailyDoorLog temp : actualEatDates) {
				if(temp.getStaffID().equals(id)) {
					System.out.println(temp.getDineDate());
					actualDates.add(temp.getDineDate());
				}
			}
		}
		
		List<AvoidMeat> avoidMeats = avoidMeatService.findAll();
		String staffAvoidMeats = staffService.getStaffById(id).getAvoidMeatIds();
		List<AvoidMeat> checkedMeats = new ArrayList<>();
		if (staffAvoidMeats != null) {
			List<String> Meats = Arrays.asList(staffAvoidMeats.split(","));
			if (!staffAvoidMeats.equals("")) {
				for (String Id : Meats) {
					int tempId = Integer.parseInt(Id);
					checkedMeats.add(avoidMeatService.findById(tempId));
				}
			}
		}
		
		model.addAttribute("actual", actualDates);
		model.addAttribute("staff", staffService.getStaffById(id));
		model.addAttribute("checkedMeats", checkedMeats);
		model.addAttribute("avoidmeats", avoidMeats);
		model.addAttribute("checkedDates", checkedDates);
		model.addAttribute("holidays", holidays);
		model.addAttribute("headers", headers);
		model.addAttribute("currentDay", date.getDayOfMonth());
		model.addAttribute("currentMonth", date.getMonth());
		model.addAttribute("currentYear", date.getYear());
		model.addAttribute("month", weeksInMonth);
		return "calendarDetail";
	}
	
	@GetMapping("/employee-list")
	public String empList(Model model, Authentication authentication) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Staff> staffs = staffService.getActiveStaffs(1);
				
				List<Staff> toRemove = new ArrayList<>();
				for(Staff staff : staffs) {
					if(staff.getName().equals("Admin")) {
						toRemove.add(staff);
					}
				}
				staffs.removeAll(toRemove);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", staffs.size());
				return "admin/employee-list";
			}
			return "404";

		} catch (NullPointerException e) {

			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/search")
	public String searchEmp(Authentication authentication, @RequestParam(name = "dept", required = false) String dept, @RequestParam(name = "enabled", required = false) String status, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "division", required = false) String division, @RequestParam(name = "id", required = false) String id,@RequestParam(name = "team", required = false) String team, @RequestParam(name="role", required = false)String roleSearch, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				if (role.trim().isEmpty() && name.trim().isEmpty() && id.trim().isEmpty() && team.trim().isEmpty() && dept.trim().isEmpty() && division.trim().isEmpty() && status.trim().isEmpty()) {
					return "redirect:/employee-list";
				} else {
					List<Staff> staffs = staffService.searchBy(name, id, team);
					//Division
					if(division!="" && division!=null) {
						List<Staff> toRemove = new ArrayList<>();
						for(Staff temp : staffs) {
							if(!temp.getDivision().equals(division)) {
								toRemove.add(temp);
							}
						}
						staffs.removeAll(toRemove);
					}
					//Department
					else if(dept!="" && dept!=null) {
						List<Staff> toRemove = new ArrayList<>();
						for(Staff temp : staffs) {
							if(!temp.getDept().equals(dept)) {
								toRemove.add(temp);
							}
						}
						staffs.removeAll(toRemove);
					}
					//Status
					else if(status!="" && status!=null) {
						List<Staff> toRemove = new ArrayList<>();
						if(status.equals("1")) {
							for(Staff temp : staffs) {
								if(temp.getEnabled()!=(byte)1) {
									toRemove.add(temp);
								}
							}
						}else if(status.equals("0")) {
							for(Staff temp : staffs) {
								if(temp.getEnabled()!=(byte)0) {
									System.out.println("Removed " + temp.getName());
									toRemove.add(temp);
								}
							}
						}
						staffs.removeAll(toRemove);
					}
					//Role
					else if(roleSearch!="" && roleSearch!=null) {
						staffs = staffService.filterByRole(roleSearch);
					}
					
					List<Staff> toRemove = new ArrayList<>();
					for(Staff staff : staffs) {
						if(staff.getName().equals("Admin")) {
							System.out.println("Removed : " + staff.getName());
							toRemove.add(staff);
						}
					}
					staffs.removeAll(toRemove);
					
					model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
					model.addAttribute("divs", staffService.getDivNames());
					model.addAttribute("depts", staffService.getDeptNames());
					model.addAttribute("teams", staffService.getTeamNames());
					model.addAttribute("staffs", staffs);
					model.addAttribute("totalNum", staffs.size());
					model.addAttribute("searchName", name);
					model.addAttribute("id", id);
					model.addAttribute("team", team);
					return "admin/employee-list";
				}
			}
			return "404";
		} 
		catch (NullPointerException e) {
			return "404";
		}
	}

	@GetMapping("/filterStatus")
	public String filterbyTeam(Authentication authentication, @RequestParam(name = "enabled", required = false) int status, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Staff> staffs = staffService.getActiveStaffs(status);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", staffs.size());
				return "admin/employee-list";
			}
			return "404";
		} catch (IllegalStateException e) {
			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/filterRole")
	public String filterbyRole(Authentication authentication,@RequestParam(name = "role", required = false) String role, Model model) {
		try {
			String Role = authentication.getAuthorities().toArray()[0].toString();

			if (Role.equals("admin")) {
				List<Staff> staffs = staffService.filterByRole(role);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", staffs.size());
				return "admin/employee-list";
			}
			return "404";

		} catch (NullPointerException e) {

			return "redirect:/showMyLoginPage";
		}
	}

	@PostMapping("/importStaff")
	public String importEmp(@RequestParam("file") MultipartFile file, @RequestParam("adminName") String adminName,
			Model model, Authentication authentication) {
		// read excel file
		try {
			InputStream inputStream = file.getInputStream();
			inputStream.available();
			List<Staff> objects = ExcelImporter.readExcel(inputStream);
			List<Staff> staffs = staffService.getAllStaffs();
			List<String> ids = new ArrayList<String>();
			List<Staff> tempStaffs = new ArrayList<Staff>();
			for (Staff staff : staffs) {
				ids.add(staff.getStaffID());
			}
			for (Staff object : objects) {
				// Update existing operator from excel file
				if (ids.contains(object.getStaffID())) {
					System.out.println("Updating");
					Staff staff = staffService.getStaffById(object.getStaffID());
					staff.setModify_date(LocalDateTime.now());
					staff.setDivision(object.getDivision());
					staff.setDept(object.getDept());
					staff.setTeam(object.getTeam());
					staff.setModify_by(adminName);
					tempStaffs.add(staff);
					ids.remove(ids.indexOf(object.getStaffID()));

				}
				// Add new operators
				else if (!(ids.contains(object.getStaffID()))) {
					System.out.println("Adding");
					object.setCreated_date(LocalDateTime.now());
					object.setCreated_by(adminName);
					object.setEmail_noti((byte) 0);
					object.setPassword("$2a$04$WvPSakxEW208zFYymEfyFO90gtbmP5o.vrcEogJ0JRMLuK4Y0LxIi");
					object.setRole("operator");
					object.setStatus((byte) 1);
					tempStaffs.add(object);
				}
			}
			// Change enabled to 0 (resigned operators)
			if (ids != null) {
				for (String id : ids) {
					Staff staff = staffService.getStaffById(id);
					if(staff.getStaffID()=="1" || staff.getName()=="Admin") {
						continue;
					}else {
						System.out.println("Inactive");
						List<Registered_list> registered = registeredService.getRegisteredStaffByDateAfter(LocalDate.now());
						for(Registered_list temp : registered) {
							temp.setDine(false);
						}
						staff.setStatus((byte) 0);
						staff.setDelete_date(LocalDateTime.now());
						staff.setDelete_by(adminName);
						staff.setRole("operator");
						staff.setPassword("$2a$04$WvPSakxEW208zFYymEfyFO90gtbmP5o.vrcEogJ0JRMLuK4Y0LxIi");
						tempStaffs.add(staff);
					}
				}
			}
			staffService.addAllStaff(tempStaffs);
			staffs = staffService.getActiveStaffs(1);
			model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
			model.addAttribute("totalNum", staffs.size());
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("staffs", staffService.getAllStaffs());
			model.addAttribute("success", "Uploaded Successfully");
			return "admin/employee-list";
		} catch (IOException e) {
			model.addAttribute("error", "An error occurred while uploading the file" + e.toString());
			return "admin/employee-list";
		}
	}

	@GetMapping("/editStaffForm")
	public String editStaff(@RequestParam(name = "id", required = false) String id, Model model,Authentication authentication) {
		try {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
		if (id == null) {
			return "redirect:/showMyLoginPage";
		} else {
			Staff staff = staffService.getStaffById(id);
			model.addAttribute("staff", staff);
			return "admin/editStaff";	
			}
		}return "404";
			
		}catch(NullPointerException e) {
			return "redirect:/showMyLoginPage";
		}
		
	}

	@GetMapping("/saveEdit")
	public String saveEditStaff(@ModelAttribute("staff") Staff tmpStaff, Authentication authentication) {
		try{
			Staff staff = staffService.getStaffById(tmpStaff.getStaffID());
		String name = staffService.getStaffById(authentication.getName()).getName();
		staff.setRole(tmpStaff.getRole());
		staff.setModify_by(name);
		staff.setModify_date(LocalDateTime.now());
		staffService.addStaff(staff);
		return "redirect:/employee-list";

		}catch(InvalidDataAccessApiUsageException e) {
			return"404";
		}
		
	}

}
