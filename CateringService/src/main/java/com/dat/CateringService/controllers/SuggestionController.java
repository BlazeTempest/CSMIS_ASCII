  package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.service.StaffService;
import com.dat.CateringService.service.SuggestionService;

@Controller
public class SuggestionController {
	@Autowired
	private SuggestionService suggestionService;
	
	@Autowired
	private StaffService staffService;
	
	@GetMapping("/suggestion")
	public String Suggestion(Model model, Authentication authentication) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				model.addAttribute("suggestions", suggestionService.getAllSuggestions());
				return "admin/suggestion";
			}
			return "redirect:/showMyLoginPage";
		} catch (NullPointerException e) {
			return "redirect:/showMyLoginPage";
		}
	}
	
	@PostMapping("/addSuggestion")
	public String addSuggestion(@RequestParam("suggestion")String suggestion, RedirectAttributes attr, Authentication authentication) {
		com.dat.CateringService.entity.Suggestion temp = new com.dat.CateringService.entity.Suggestion(suggestion);
		temp.setCreated_by(staffService.getStaffById(authentication.getName()).getName());
		temp.setCreated_date(LocalDateTime.now());
		suggestionService.addSuggestion(temp);
		attr.addFlashAttribute("Smessage", "Suggested successfully");
		return "redirect:/dashboard";
	}

	@GetMapping("/checkSuggestions")
	public String checkSuggestions(@RequestParam("start") String startDate, @RequestParam("end") String endDate,
			Model model) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		model.addAttribute("suggestions", suggestionService.getByStartAndEndDate(start, end));
		return "admin/suggestion";
	}
}
