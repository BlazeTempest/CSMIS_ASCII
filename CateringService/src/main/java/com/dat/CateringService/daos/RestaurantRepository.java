package com.dat.CateringService.daos;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dat.CateringService.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	Restaurant findByEmail(String email);

	Restaurant findFirstByStatus(String string);

	@Query(value="SELECT * FROM restaurant ORDER BY status", nativeQuery = true)
    public List<Restaurant> getRestaurantName();
}
