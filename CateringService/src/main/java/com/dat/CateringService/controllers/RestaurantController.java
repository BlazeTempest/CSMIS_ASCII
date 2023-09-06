package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dat.CateringService.entity.Restaurant;
import com.dat.CateringService.service.RestaurantService;
import com.dat.CateringService.service.StaffService;

@Controller
public class RestaurantController {

	private RestaurantService restaurantService;
	private StaffService staffService;

	public RestaurantController(RestaurantService theRestaurantService, StaffService theStaffService) {
		restaurantService = theRestaurantService;
		staffService = theStaffService;
	}

	//add mapping for "/list"
	@GetMapping("/restaurant")
	public String listRestaurants(Model theModel,Authentication authentication) {
		try {
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				
				// get restaurants from db
				List<Restaurant> theRestaurant = restaurantService.findAll();

				// add to the spring model
		        theModel.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				theModel.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
				theModel.addAttribute("restaurant", theRestaurant);
				Restaurant restaurants=new Restaurant();
				theModel.addAttribute("restaurants", restaurants);
				return "admin/restaurant-list";
			}
			return"redirect:/showMyLoginPage";
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
	
	@GetMapping("/updateRestaurant")
	public String updateRestaurant(@RequestParam(name = "id", required = false) Integer id, Model model, Authentication authentication) {

		try {
	        String role = authentication.getAuthorities().toArray()[0].toString();
	        if (role.equals("admin")) {
	            Restaurant restaurant = restaurantService.findById(id);
	            model.addAttribute("restaurant", restaurant);
	            return "admin/editRestaurant";
	        }
	        return "404";

	    } catch (NullPointerException e) {
	        return "redirect:/showMyLoginPage";
	    }
	}

	@PostMapping("/saveRestaurant")
	public String saveRestaurant(@ModelAttribute("restaurants") Restaurant theRestaurant, RedirectAttributes model) {

			List<Restaurant> restaurantList = restaurantService.findAll();
			for(Restaurant tempRes: restaurantList)
			{
				tempRes.setStatus("inactive");
			}
				
			String existingRestaurantDatas=restaurantService.findDuplicateRestaurantName(theRestaurant.getRestaurant_name(), theRestaurant.getPhone(), theRestaurant.getEmail());
			if(existingRestaurantDatas !=null)
			{
				model.addFlashAttribute("existingRestaurantDatas",theRestaurant.getRestaurant_name()+ " is already existed!");
				 return "redirect:/restaurant";
			}else {
				theRestaurant.setCreated_date(LocalDate.now());
				theRestaurant.setStatus("active");
				
				restaurantService.save(theRestaurant);
				
				model.addFlashAttribute("message", "Restaurant saved successfully!");
				return "redirect:/restaurant";
			}
	}
	
	
	@PostMapping("/editRestaurant")
	public String saveRestaurant(@ModelAttribute("restaurants") Restaurant theRestaurant, RedirectAttributes model, Authentication authentication) {
		if(theRestaurant.getStatus().equalsIgnoreCase("active")) {
			for(Restaurant temp : restaurantService.findAll()) {
				temp.setStatus("inactive");
				restaurantService.save(temp);
			}
		}
		theRestaurant.setModify_date(LocalDate.now());
		theRestaurant.setModify_by(staffService.getStaffById(authentication.getName()).getName());
	    restaurantService.save(theRestaurant);
	    // Add a success message to the model
	    model.addFlashAttribute("message","Restaurant updated successfully!");
	    return "redirect:/restaurant";
	}
		
}
