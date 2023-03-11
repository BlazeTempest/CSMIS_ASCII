package com.dat.CateringService.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.service.SuggestionService;

@Controller
public class SuggestionController {
	@Autowired
	private SuggestionService suggestionService;
	
	@GetMapping("/suggestion")
	public String Suggestion(Model model) {
		model.addAttribute("suggestions", suggestionService.getAllSuggestions());
		return "suggestion";
	}
	
	@GetMapping("/checkSuggestions")
	public String checkSuggestions(@RequestParam("start")String startDate, @RequestParam("end")String endDate, Model model) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);
		model.addAttribute("suggestions", suggestionService.getByStartAndEndDate(start, end));
		return "suggestion";
	}
}
