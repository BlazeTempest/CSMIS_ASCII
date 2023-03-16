package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.ExcelImporter;
import com.dat.CateringService.service.AnnouncementService;
import com.dat.CateringService.service.StaffService;
import com.dat.CateringService.service.SuggestionService;
import com.opencsv.exceptions.CsvException;

@Controller
public class HomeController {
	@Autowired
	private StaffService staffService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// count total number of staffs
	public int countStaff(List<Staff> staffs) {
		int totalNum = 0;
		for (Staff staff : staffs) {
			totalNum++;
		}
		return totalNum;
	}

	@GetMapping("/employee-list")
	public String empList(Model model, Authentication authentication) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Staff> staffs = staffService.getActiveStaffs(1);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", countStaff(staffs));
				return "admin/employee-list";
			}
			return "404";

		} catch (NullPointerException e) {

			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/search")
	public String searchEmp(Authentication authentication, @RequestParam(name = "name", required = false) String name,@RequestParam(name = "id", required = false) String id,@RequestParam(name = "team", required = false) String team, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				if (name.trim().isEmpty() && id.trim().isEmpty() && team.trim().isEmpty()) {
					return "redirect:/employee-list";
				} else {
					List<Staff> staffs = staffService.searchBy(name, id, team);
					int totalNum = countStaff(staffs);

					model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
					model.addAttribute("divs", staffService.getDivNames());
					model.addAttribute("depts", staffService.getDeptNames());
					model.addAttribute("teams", staffService.getTeamNames());
					model.addAttribute("staffs", staffs);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("searchName", name);
					model.addAttribute("id", id);
					model.addAttribute("team", team);
					return "admin/employee-list";
				}
			}
			return "404";
		} catch (NullPointerException e) {
			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/filterDiv")
	public String filterbyDiv(Authentication authentication,@RequestParam(name = "division", required = false) String division, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();

			if (role.equals("admin")) {
				List<Staff> staffs = staffService.filterByDivision(division);
				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", countStaff(staffs));
				return "admin/employee-list";
			}
			return "404";

		} catch (NullPointerException e) {

			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/filterDept")
	public String filterByDept(Authentication authentication,@RequestParam(name = "dept", required = false) String dept, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Staff> staffs = staffService.filterByDept(dept);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", countStaff(staffs));
				return "admin/employee-list";
			}
			return "404";

		} catch (NullPointerException e) {

			return "redirect:/showMyLoginPage";
		}
	}

	@GetMapping("/filterStatus")
	public String filterbyTeam(Authentication authentication,@RequestParam(name = "enabled", required = false) int status, Model model) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Staff> staffs = staffService.getActiveStaffs(status);

				model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				model.addAttribute("divs", staffService.getDivNames());
				model.addAttribute("depts", staffService.getDeptNames());
				model.addAttribute("teams", staffService.getTeamNames());
				model.addAttribute("staffs", staffs);
				model.addAttribute("totalNum", countStaff(staffs));
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
				model.addAttribute("totalNum", countStaff(staffs));
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
					System.out.println("Inactive");
					Staff staff = staffService.getStaffById(id);
					staff.setStatus((byte) 0);
					staff.setDelete_date(LocalDateTime.now());
					staff.setDelete_by(adminName);
					staff.setRole("operator");
					staff.setPassword("$2a$04$WvPSakxEW208zFYymEfyFO90gtbmP5o.vrcEogJ0JRMLuK4Y0LxIi");
					tempStaffs.add(staff);
				}
			}
			staffService.addAllStaff(tempStaffs);
			staffs = staffService.getActiveStaffs(1);
			model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
			model.addAttribute("totalNum", countStaff(staffs));
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
