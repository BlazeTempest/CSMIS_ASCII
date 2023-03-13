package com.dat.CateringService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {
	
	
	@GetMapping("/registration")
	public String showRegistrationForm() {
		
		return "lunch-register";
	}
}
