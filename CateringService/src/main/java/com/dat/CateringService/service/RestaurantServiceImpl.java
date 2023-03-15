package com.dat.CateringService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.RestaurantRepository;
import com.dat.CateringService.entity.Restaurant;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
    private RestaurantRepository restaurantRepository;
    
    public boolean isDuplicate(Restaurant restaurant) {
        Restaurant existingUser = restaurantRepository.findByEmail(restaurant.getEmail());
        return existingUser != null && existingUser.getRestaurant_ID() != restaurant.getRestaurant_ID();
    }

	@Autowired
	public RestaurantServiceImpl(RestaurantRepository theRestaurantRepository) {
		restaurantRepository = theRestaurantRepository;
	}

	@Override

	public List<Restaurant> findAll() {
		return restaurantRepository.findAll();
	}

	@Override

	public Restaurant findById(int theId) {
		Optional<Restaurant> result = restaurantRepository.findById(theId);

		Restaurant theRestaurant = null;

		if (result.isPresent()) {
			theRestaurant = result.get();
		}
		else {
			throw new RuntimeException("Did not find restaurant id - " + theId);
		}

		return theRestaurant;
	}

	@Override

	public void save(Restaurant theRestaurant) {
		restaurantRepository.save(theRestaurant);
	}


}
