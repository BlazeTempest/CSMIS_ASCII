package com.dat.CateringService.controllers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dat.CateringService.DTO.ReportDTO;
import com.dat.CateringService.daos.PriceRepository;
import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.entity.Price;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.AnnouncementService;
import com.dat.CateringService.service.MenuPdfService;
import com.dat.CateringService.service.PriceService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class LoginController {
	@Autowired
	private PriceService priceService;
	@Autowired
	private MenuPdfService menuPdfService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private AnnouncementService announcementService;
	@Autowired
	private RegisteredListService registeredService;

	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {

		return "signin";

	}

	@GetMapping("/access-denied")
	public String showAccessDenied() {

		return "access-denied";

	}

	@GetMapping("/dashboard")
	public String showDashboard(Model theModel, Authentication authentication) throws IOException {

		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Announcement> announcements = announcementService.getAllAnnouncements();
				for(Announcement temp : announcements) {
					if(temp.getDeleted_date()==LocalDate.now()) {
						announcementService.delete(temp);
					}
				}
				theModel.addAttribute("announcement", new Announcement());
				//dashboard overview counts
				List<String> teams = staffService.getTeamNames();
				List<String> depts = staffService.getDeptNames();
				List<Staff> staffs = new ArrayList<>();
				List<ReportDTO> deptCounts = new ArrayList<>();
				List<ReportDTO> teamCounts = new ArrayList<>();
				List<Staff> admin = new ArrayList<>();
				
				for(Staff temp : staffService.getActiveStaffs(1)) {
					staffs.add(temp);
					if(temp.getRole().equals("admin") && !temp.getName().equals("Admin")) {
						admin.add(temp);
					}
				}
				
				LocalDate today = LocalDate.now();
				LocalDate firstDay = today.withDayOfMonth(1);
				LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());
				
				for(String dept : depts) {
					ReportDTO dto = new ReportDTO(registeredService.getDeptCount(dept, true), registeredService.getDeptCount(dept, false));
					dto.setDept(dept);
					if(!(registeredService.getDeptCount(dept, true)==0 && registeredService.getDeptCount(dept, false)==0)) {
						deptCounts.add(dto);
					}
				}
				
				for(String team : teams) {
					ReportDTO dto = new ReportDTO(registeredService.getTeamCount(team, true), registeredService.getTeamCount(team, false));
					dto.setTeam(team);
					if(!(registeredService.getTeamCount(team, true)==0 && registeredService.getTeamCount(team, false)==0)) {
						teamCounts.add(dto);
					}
				}
				Price activePrice = priceService.findActivePrice();
				Byte status = activePrice.getStatus();
				if (status != null || status.equals((byte)1)) {
					
					// perform actions when status is equal to myByteObject
					theModel.addAttribute("totalPrice", activePrice.getTotal_price());
					theModel.addAttribute("datPrice", activePrice.getDATprice());
					theModel.addAttribute("staffPrice", activePrice.getStaff_price());
				}
				theModel.addAttribute("admins", admin);
				theModel.addAttribute("teamCounts", teamCounts);
				theModel.addAttribute("deptCounts", deptCounts);
				theModel.addAttribute("totalRegistered", registeredService.getRegisteredStaffByStartDateAndEndDate(firstDay, lastDay).size());
				theModel.addAttribute("totalTeam", teams.size());
				theModel.addAttribute("totalDept", depts.size());
				theModel.addAttribute("TeamNames", teams.size());
				theModel.addAttribute("DeptNames", depts.size());
				theModel.addAttribute("totalStaff", staffs.size());
				return "admin/adminDashboard";

			} else if (role.equals("operator")) {

				Staff staff = staffService.getStaffById(authentication.getName());
				theModel.addAttribute("name", staff.getName());
				theModel.addAttribute("noti", staff.getEmail_noti());
				List<Announcement> announcements = announcementService.getAllAnnouncements();
				theModel.addAttribute("announcements", announcements);
				
					String pdfFileName = "currentweek.pdf";
					System.out.println(pdfFileName);
					if (pdfFileName != null) {

						String encodedPdf = menuPdfService.getPdfAsByteString(pdfFileName);
						theModel.addAttribute("pdf", encodedPdf);

					}

					String pdfFileName2 = "nextweek.pdf";
					if (pdfFileName2 != null) {

						String encodedPdf1 = menuPdfService.getPdfAsByteString(pdfFileName2);
						theModel.addAttribute("pdf1", encodedPdf1);
					}
					Price activePrice = priceService.findActivePrice();
					Byte status = activePrice.getStatus();
					if (status != null || status.equals((byte)1)) {
						
						// perform actions when status is equal to myByteObject
						theModel.addAttribute("totalPrice", activePrice.getTotal_price());
						theModel.addAttribute("datPrice", activePrice.getDATprice());
						theModel.addAttribute("staffPrice", activePrice.getStaff_price());
					}
				return "employee/employeeDashboard";
			} else {

				return "redirect:/access-denied";
			}
		} catch (NullPointerException e) {
			return "redirect:/showMyLoginPage";

		}

	}
	
	@RequestMapping("/toggle-switch")
	@ResponseBody
	public void handleToggleSwitch(@RequestParam("isChecked") boolean isChecked ,Authentication authentication) {

		String staffId=authentication.getName();
		System.out.println(staffId);
		System.out.println("Toggle switch is " + (isChecked ? "on" : "off"));
		// JDBC or an ORM to save the value of the toggle switch to the database
		// Example using JDBC:
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/csmis_service", "csmisadmin", "csmisdat")) {
			
			String sql = "UPDATE staff SET email_noti = ? WHERE staffID=?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			 statement.setBoolean(1, isChecked);
			 statement.setString(2, staffId);
			 
			 int rowsUpdated = statement.executeUpdate();
			 System.out.println(rowsUpdated + " rows updated successfully.");

			
		} catch (SQLException e) {
			
			System.out.println("error");
			e.printStackTrace();
		}
	}
}
