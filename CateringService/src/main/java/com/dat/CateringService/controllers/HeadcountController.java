package com.dat.CateringService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dat.CateringService.service.RegisteredListService;

@Controller
public class HeadcountController {
	
	@Autowired
	private RegisteredListService registeredService;
	
	
	
}
