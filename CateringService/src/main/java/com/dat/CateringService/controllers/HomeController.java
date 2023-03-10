package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.ExcelImporter;
import com.dat.CateringService.service.StaffService;

@Controller
public class HomeController {
	@Autowired
	private StaffService staffService;
	
	//count total number of staffs
	public int countStaff(List<Staff> staffs) {
		int totalNum = 0;
		for(Staff staff:staffs) {
			totalNum++;
		}
		return totalNum;
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employee-list")
	public String empList(Model model) {
		List<Staff> staffs  = staffService.getActiveStaffs("active");
		
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "employee-list";
	}
	
	@GetMapping("/search")
	public String searchEmp(@RequestParam("name")String name, @RequestParam("id")String id,@RequestParam("team")String team , Model model) {
		if(name.trim().isEmpty() && id.trim().isEmpty() && team.trim().isEmpty()) {
			return "redirect:/employee-list";
		}else {
			List<Staff> staffs = staffService.searchBy(name, id, team);
			int totalNum = countStaff(staffs);
			
			model.addAttribute("divs", staffService.getDivNames());
			model.addAttribute("depts", staffService.getDeptNames());
			model.addAttribute("teams", staffService.getTeamNames());
			model.addAttribute("staffs", staffs);
			model.addAttribute("totalNum", totalNum);
			return "employee-list";
		}
	}
	
	@GetMapping("/filterDiv")
	public String filterbyDiv(@RequestParam("division")String division , Model model) {
		List<Staff> staffs = staffService.filterByDivision(division);
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "employee-list";
	}
	
	@GetMapping("/filterDept")
	public String filterByDept(@RequestParam("dept")String dept , Model model) {
		List<Staff> staffs = staffService.filterByDept(dept);
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "employee-list";
	}
	
	@GetMapping("/filterStatus")
	public String filterbyTeam(@RequestParam("status")String status , Model model) {
		List<Staff> staffs = staffService.getActiveStaffs(status);
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "employee-list";
	}
	
	@GetMapping("/filterRole")
	public String filterbyRole(@RequestParam("role")String role , Model model) {
		List<Staff> staffs = staffService.filterByRole(role);
		
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "employee-list";
	}
	
	@PostMapping("/importStaff")
	public String importEmp(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "404";
        }

        try {
            InputStream inputStream = file.getInputStream();
            inputStream.available();
            List<Staff> objects = ExcelImporter.readExcel(inputStream);
            List<Staff> staffs = staffService.getAllStaffs();
            List<String> ids = new ArrayList<String>();
            for(Staff staff:staffs) {
            	ids.add(staff.getStaffID());
            }
            for(Staff object : objects) {
            	if(object.getStaffID()==null || object.getDoorLogNo()==0) {
            		continue;
            	}else {
            		
            			if(ids.contains(object.getStaffID())) {
            				Staff staff = staffService.getStaffById(object.getStaffID());
            				staff.setModify_date(LocalDateTime.now());
            				staff.setDoorLogNo(object.getDoorLogNo());
            				staff.setDivision(object.getDivision());
            				staff.setDept(object.getDept());
            				staff.setEmail(object.getEmail());
            				staff.setTeam(object.getTeam());
            				staff.setName(object.getName());
            				staff.setStatus("active");
            				staffService.addStaff(staff);
            				
            				int index = ids.indexOf(object.getStaffID());
            				ids.remove(index);
            			}else if(!(ids.contains(object.getStaffID()))) {
            				object.setCreated_date(LocalDateTime.now());
                        	object.setEmail_noti("on");
                       		object.setPassword("gogodat");
                       		object.setRole("operator");
                       		object.setStatus("active");
                       		staffService.addStaff(object);
                       		System.out.println("Added new staff");
            			}
            		}
            	}
            if(ids!=null) {
    			for(String id:ids) {
    				Staff staff = staffService.getStaffById(id);
    				staff.setStatus("inactive");
    				staff.setDelete_date(LocalDateTime.now());
    				staffService.addStaff(staff);
   					System.out.println("Change status to inactive");
    			}
			}
            staffs = staffService.getActiveStaffs("active");
            model.addAttribute("totalNum", countStaff(staffs));
            model.addAttribute("divs", staffService.getDivNames());
    		model.addAttribute("depts", staffService.getDeptNames());
    		model.addAttribute("teams", staffService.getTeamNames());
            model.addAttribute("message", "File uploaded successfully.");
            model.addAttribute("staffs", staffService.getAllStaffs());
            model.addAttribute("success", "Uploaded Successfully!");
            return "employee-list";
        } catch (IOException e) {
            model.addAttribute("message", "An error occurred while uploading the file: " + e.getMessage());
            return "404";
        }
	}
	
	@GetMapping("/editStaffForm")
	public String editStaff(@RequestParam("id")String id, Model model) {
		if(id==null) {
			return "redirect:/";
		}else {
			Staff staff = staffService.getStaffById(id);
			model.addAttribute("staff", staff);
			return "editStaff";
		}
	}
	
	@GetMapping("/saveEdit")
	public String saveEditStaff(@ModelAttribute("staff")Staff tmpStaff) {
		Staff staff = staffService.getStaffById(tmpStaff.getStaffID());
		staff.setRole(tmpStaff.getRole());
		staffService.addStaff(staff);
		return "redirect:/employee-list";
		
	}
}
