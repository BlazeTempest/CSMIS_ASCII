package com.dat.CateringService.daos;


import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	Restaurant findByEmail(String email);

	Restaurant findFirstByStatus(String string);
	
}
