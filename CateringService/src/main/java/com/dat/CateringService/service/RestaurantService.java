package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Restaurant;

public interface RestaurantService {

	public List<Restaurant> findAll();

	public Restaurant findById(int theId);

	public void save(Restaurant theRestaurant);

	public boolean isDuplicate(Restaurant theRestaurant);
	
	public String findActiveRestaurantName();
	
	public List<Restaurant> getRestaurantName();
	
	public String findRestaurantReceiverName();
	
	public String findDuplicateRestaurantName(String restaurantName,String phone,String email);
}
