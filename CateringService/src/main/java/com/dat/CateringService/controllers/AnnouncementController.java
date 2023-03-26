package com.dat.CateringService.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webmvc.SpringWebMvcThymeleafRequestContext;

import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.service.AnnouncementService;

@Controller
public class AnnouncementController {

	private AnnouncementService announcementService;

	public AnnouncementController(AnnouncementService theAnnouncementService) {
		announcementService = theAnnouncementService;
	}

	@PostMapping("/announcement")
	public String announce(@RequestParam("description") String description,@ModelAttribute("announcement") Announcement theAnnouncement, Model model, Authentication authentication) {
		
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			announcementService.save(theAnnouncement);

			theAnnouncement.setDescription(description);
			model.addAttribute("announcement", theAnnouncement);
			return "redirect:/dashboard";
		}
		return "404";

	}

	@GetMapping("/employee/dashboard")
	public String employeeDashboard(Model model) {
		List<Announcement> announcements = announcementService.getAllAnnouncements();
		model.addAttribute("announcements", announcements);
		return "/employee/employeeDashboard";
	}

}