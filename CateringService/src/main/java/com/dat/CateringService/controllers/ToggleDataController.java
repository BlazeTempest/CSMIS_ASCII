package com.dat.CateringService.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    	System.out.println("output >>>>"+toggleData);
    	
    	toggleDataService.saveToggleData(toggleData);
    	
    	System.out.println(toggleData.isToggle_status());
    	
        return "result";
    }

}
