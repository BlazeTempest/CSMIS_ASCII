package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/deleteAnnounce")
	public String deleteAnnouncement(@RequestParam("id")int id, Authentication authentication, RedirectAttributes attr) {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			attr.addAttribute("showModal", "show");
			attr.addFlashAttribute("announceDelete", "Deleted announcement at " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
			announcementService.delete(announcementService.findById(id));
			return "redirect:/dashboard";
		}
		return "404";
	}
	
	@PostMapping("/editAnnouncement")
	public String editAnnounce(@RequestParam("description")String description, @RequestParam("id")int id, RedirectAttributes model, Authentication authentication) {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			Announcement announcement = announcementService.findById(id);
			announcement.setDescription(description);
			announcementService.save(announcement);
			model.addAttribute("showModal", "show");
			model.addFlashAttribute("announceEdit", "Updated announcement at " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
			return "redirect:/dashboard";
		}
		return "404";
	}

	@PostMapping("/announcement")
	public String announce(@RequestParam("description") String description,@ModelAttribute("announcement") Announcement theAnnouncement, RedirectAttributes attr, Model model, Authentication authentication) {
		String role = authentication.getAuthorities().toArray()[0].toString();
		if (role.equals("admin")) {
			theAnnouncement.setCreatedDate(LocalDateTime.now());
			theAnnouncement.setCreated_by(staffService.getStaffById(authentication.getName()).getName());
			theAnnouncement.setDeleted_date(LocalDate.now().plusDays(7));
			announcementService.save(theAnnouncement);
			attr.addFlashAttribute("announceSuccess", "Created announcement at " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
			return "redirect:/dashboard";
		}
		return "404";
	}
}