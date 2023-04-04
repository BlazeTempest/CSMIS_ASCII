package com.dat.CateringService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.dat.CateringService.entity.ToggleData;
import com.dat.CateringService.service.ToggleDataService;

@Controller
public class ToggleDataController {
	
	private final ToggleDataService toggleDataService;

    public ToggleDataController(ToggleDataService toggleDataService) {
        this.toggleDataService = toggleDataService;
    }
    @GetMapping("/toggleForm")
    public String toggleForm(Model model) {
        model.addAttribute("toggleData", new ToggleData());
        return "toggle";
    }
    
    @PostMapping("/toggleSubmit")
    public String toggleSubmit(@ModelAttribute("toggleData") ToggleData toggleData) {
    	toggleDataService.saveToggleData(toggleData);
        return "result";
    }

}
