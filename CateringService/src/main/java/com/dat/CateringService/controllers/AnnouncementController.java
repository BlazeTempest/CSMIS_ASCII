package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.service.AnnouncementService;
import com.dat.CateringService.service.StaffService;

@Controller
public class AnnouncementController {

	private AnnouncementService announcementService;
	private StaffService staffService;
	public AnnouncementController(AnnouncementService theAnnouncementService, StaffService theStaffService) {
		announcementService = theAnnouncementService;
		staffService = theStaffService;
	}

	@PostMapping("/announcement")
	public String announce(@RequestParam("description") String description,@ModelAttribute("announcement") Announcement theAnnouncement, Model model, Authentication authentication) {
		
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			theAnnouncement.setCreated_date(LocalDateTime.now());
			theAnnouncement.setCreated_by(staffService.getStaffById(authentication.getName()).getName());
			theAnnouncement.setDeleted_date(LocalDate.now().plusDays(7));
			announcementService.save(theAnnouncement);
			return "redirect:/dashboard";
		}
		return "404";
	}
}