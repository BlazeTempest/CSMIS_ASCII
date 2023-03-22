package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.dat.CateringService.entity.Restaurant;
import com.dat.CateringService.service.RestaurantService;

@Controller
public class RestaurantController {

	private RestaurantService restaurantService;

	public RestaurantController(RestaurantService theRestaurantService) {
		restaurantService = theRestaurantService;
	}

	//	 add mapping for "/list"
	@GetMapping("/restaurant")
	public String listRestaurants(Model theModel,Authentication authentication) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				
				// get restaurants from db
				List<Restaurant> theRestaurant = restaurantService.findAll();

				// add to the spring model
				theModel.addAttribute("restaurant", theRestaurant);
				Restaurant restaurants=new Restaurant();
				theModel.addAttribute("restaurants", restaurants);
			return "admin/restaurant-list";
				
			}return"redirect:/showMyLoginPage";
		}catch(NullPointerException e) {
			
			return"404";
		}
		
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
				Restaurant theRestaurant = new Restaurant();

				theModel.addAttribute("restaurant", theRestaurant);

				return "admin/restaurant-list";
	}



	@PostMapping("/saveRestaurant")
	public String saveRestaurant(@Valid @ModelAttribute("restaurants") Restaurant theRestaurant,
			BindingResult bindingResult,Model model) {

		if (bindingResult.hasErrors()) {
			return "redirect:/restaurant";
		}
		
		else {
			
			List<Restaurant> restaurantList = restaurantService.findAll();
			for(Restaurant tempRes: restaurantList)
			{
				tempRes.setStatus("inactive");
			}
			
		theRestaurant.setCreated_date(LocalDate.now());
		theRestaurant.setStatus("active");
	    restaurantService.save(theRestaurant);
	    // Add a success message to the model
	    model.addAttribute("message","Restaurant saved successfully!");
	    return "redirect:/restaurant";
		}
		
}
}
