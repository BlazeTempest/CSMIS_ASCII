  package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
				List<com.dat.CateringService.entity.Suggestion> suggestions = suggestionService.getAllSuggestions();
				model.addAttribute("suggestions", suggestions);
				model.addAttribute("totalnum", suggestions.size());
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
	public String checkSuggestions(@RequestParam(name="start", required = false) String startDate, @RequestParam(name="end", required = false) String endDate,
			Model model) {
		if(startDate == "" || startDate == null) startDate = LocalDate.now().toString();
		if(endDate == "") endDate = startDate;
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		List<com.dat.CateringService.entity.Suggestion> suggestions = suggestionService.getByStartAndEndDate(start, end);
		
		model.addAttribute("start", startDate);
		model.addAttribute("end", endDate);
		model.addAttribute("suggestions", suggestions);
		model.addAttribute("totalnum", suggestions.size());
		return "admin/suggestion";
	}
}
