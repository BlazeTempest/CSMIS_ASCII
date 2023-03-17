package com.dat.CateringService.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
