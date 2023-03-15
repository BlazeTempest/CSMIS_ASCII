package com.dat.CateringService.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.importHelper.ExcelImporter;
import com.dat.CateringService.service.StaffService;
import com.opencsv.exceptions.CsvException;

@Controller
public class HomeController {
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//count total number of staffs
	public int countStaff(List<Staff> staffs) {
		int totalNum = 0;
		for(Staff staff:staffs) {
			totalNum++;
		}
		return totalNum;
	}
	
	@GetMapping("/employee-list")
	public String empList(Model model, Authentication authentication) {
		List<Staff> staffs  = staffService.getActiveStaffs(1);
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "admin/employee-list";
	}
	
	@GetMapping("/search")
	public String searchEmp(Authentication authentication, @RequestParam("name")String name, @RequestParam("id")String id,@RequestParam("team")String team , Model model) {
		if(name.trim().isEmpty() && id.trim().isEmpty() && team.trim().isEmpty()) {
			return "redirect:/employee-list";
		}else {
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
	
	@GetMapping("/filterDiv")
	public String filterbyDiv(Authentication authentication, @RequestParam("division")String division , Model model) {
		List<Staff> staffs = staffService.filterByDivision(division);
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "admin/employee-list";
	}
	
	@GetMapping("/filterDept")
	public String filterByDept(Authentication authentication, @RequestParam("dept")String dept , Model model) {
		List<Staff> staffs = staffService.filterByDept(dept);
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "admin/employee-list";
	}
	
	@GetMapping("/filterStatus")
	public String filterbyTeam(Authentication authentication, @RequestParam("enabled")int status , Model model) {
		List<Staff> staffs = staffService.getActiveStaffs(status);
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "admin/employee-list";
	}
	
	@GetMapping("/filterRole")
	public String filterbyRole(Authentication authentication, @RequestParam("role")String role , Model model) {
		List<Staff> staffs = staffService.filterByRole(role);
		
		model.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
		model.addAttribute("divs", staffService.getDivNames());
		model.addAttribute("depts", staffService.getDeptNames());
		model.addAttribute("teams", staffService.getTeamNames());
		model.addAttribute("staffs", staffs);
		model.addAttribute("totalNum", countStaff(staffs));
		return "admin/employee-list";
	}
	
//	@PostMapping("/importStaff")
//	public String importEmp(@RequestParam("file") MultipartFile file, Model model) throws NumberFormatException, CsvException {
//	    try {
//	        //read CSV file
//	        Reader reader = new InputStreamReader(file.getInputStream());
//	        List<Staff> records = ExcelImporter.readCSV(file.getInputStream());
//	        
//	        List<Staff> staffs = staffService.getAllStaffs();
//	        List<String> ids = new ArrayList<String>();
//	        List<Staff> tempStaffs = new ArrayList<Staff>();
//	        for (Staff staff : staffs) {
//	            ids.add(staff.getStaffID());
//	        }
//	        for (Staff staff : records) {
//	            
//	            //Update existing operator from CSV file
//	            if (ids.contains(staff.getStaffID())) {
//	                staff = staffService.getStaffById(staff.getStaffID());
//	                staff.setModify_date(LocalDateTime.now());
//	                staff.setDoorLogNo(staff.getDoorLogNo());
//	                staff.setDivision(staff.getDivision());
//	                staff.setDept(staff.getDept());
//	                staff.setEmail(staff.getEmail());
//	                staff.setTeam(staff.getTeam());
//	                staff.setName(staff.getName());
//	                staff.setStatus((byte) 1);
//	                staff.setEmail_noti((byte) 0);
//	                tempStaffs.add(staff);
////	                staffService.addStaff(staff);
//	                ids.remove(ids.indexOf(staff.getStaffID()));
//
//	            } else { //Add new operators
//	                staff.setCreated_date(LocalDateTime.now());
//	                staff.setEmail_noti((byte) 0);
//	                String encodedPassword = passwordEncoder.encode("gogodat");
//	                staff.setPassword(encodedPassword);
//	                staff.setRole("operator");
//	                staff.setStatus((byte) 1);
////	                staffService.addStaff(staff);
//	                tempStaffs.add(staff);
//	            }
//	        }
//
//	        //Change enabled to 0 (resigned operators)
//	        if (ids != null) {
//	            for (String id : ids) {
//	                Staff staff = staffService.getStaffById(id);
//	                staff.setStatus((byte) 0);
//	                staff.setDelete_date(LocalDateTime.now());
//	                staff.setRole("operator");
//	                String encodedPassword = passwordEncoder.encode("gogodat");
//	                staff.setPassword(encodedPassword);
////	                staffService.addStaff(staff);
//	                tempStaffs.add(staff);
//	            }
//	        }
//	        staffService.addAllStaff(tempStaffs);
//	        staffs = staffService.getActiveStaffs(1);
//	        model.addAttribute("totalNum", countStaff(staffs));
//	        model.addAttribute("divs", staffService.getDivNames());
//	        model.addAttribute("depts", staffService.getDeptNames());
//	        model.addAttribute("teams", staffService.getTeamNames());
//	        model.addAttribute("staffs", staffService.getAllStaffs());
//	        model.addAttribute("success", "Uploaded Successfully!");
//	        return "admin/employee-list";
//	    } catch (IOException e) {
//	        model.addAttribute("error", "An error occurred while uploading the file" + e.toString());
//	        return "admin/employee-list";
//	    }
//	}


	
	@PostMapping("/importStaff")
	public String importEmp(@RequestParam("file") MultipartFile file, @RequestParam("adminName")String adminName, Model model, Authentication authentication) {
		//read excel file
        try {
            InputStream inputStream = file.getInputStream();
            inputStream.available();
            List<Staff> objects = ExcelImporter.readExcel(inputStream);
            List<Staff> staffs = staffService.getAllStaffs();
            List<String> ids = new ArrayList<String>();
            List<Staff> tempStaffs = new ArrayList<Staff>();
            for(Staff staff:staffs) {
            	ids.add(staff.getStaffID());
            }
            for(Staff object : objects) {
            			//Update existing operator from excel file
            			if(ids.contains(object.getStaffID())) {
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
            			//Add new operators
            			else if(!(ids.contains(object.getStaffID()))) {
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
            //Change enabled to 0 (resigned operators)
            if(ids!=null) {
    			for(String id:ids) {
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
	public String editStaff(@RequestParam("id")String id, Model model) {
		if(id==null) {
			return "redirect:/";
		}else {
			Staff staff = staffService.getStaffById(id);
			model.addAttribute("staff", staff);
			return "admin/editStaff";
		}
	}
	
	@GetMapping("/saveEdit")
	public String saveEditStaff(@ModelAttribute("staff")Staff tmpStaff, Authentication authentication) {
		Staff staff = staffService.getStaffById(tmpStaff.getStaffID());
		String name = staffService.getStaffById(authentication.getName()).getName();
		staff.setRole(tmpStaff.getRole());
		staff.setModify_by(name);
		staff.setModify_date(LocalDateTime.now());
		staffService.addStaff(staff);
		return "redirect:/employee-list";
		
	}
}
