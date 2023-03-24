package com.dat.CateringService.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.AnnouncementService;
import com.dat.CateringService.service.MenuPdfService;
import com.dat.CateringService.service.StaffService;

@Controller
public class LoginController {

	@Autowired
	private MenuPdfService menuPdfService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private AnnouncementService announcementService;

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
				List<Announcement> announces = announcementService.getAllAnnouncements();

				Staff staff = staffService.getStaffById(authentication.getName());
				theModel.addAttribute("name", staff.getName());
				if (announces.size() == 0) {
					theModel.addAttribute("announcement", new Announcement());
				} else {
					theModel.addAttribute("announcement", announces.get(0));
				}

				System.out.println("======> ADMIN DASHBOARD!!!!");
				return "admin/adminDashboard";

			} else if (role.equals("operator")) {

				Staff staff = staffService.getStaffById(authentication.getName());
				theModel.addAttribute("name", staff.getName());
				theModel.addAttribute("noti", staff.getEmail_noti());
				List<Announcement> announcements = announcementService.getAllAnnouncements();
				theModel.addAttribute("announcements", announcements);
				System.out.println("---------> HELLO EMPLOYEE DASHBOARD !!!");
				
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
