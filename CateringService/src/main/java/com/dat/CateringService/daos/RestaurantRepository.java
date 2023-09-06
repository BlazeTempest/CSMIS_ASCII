package com.dat.CateringService.daos;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	Restaurant findByEmail(String email);

	Restaurant findFirstByStatus(String string);

	@Query(value="SELECT * FROM restaurant ORDER BY status", nativeQuery = true)
    public List<Restaurant> getRestaurantName();
	
	@Query(value="SELECT * FROM restaurant WHERE restaurant_name=:restaurant_name AND phone=:phone AND email=:email", nativeQuery = true)
	public String findDuplicateRestaurantName(@Param("restaurant_name")String restaurant_name, @Param("phone") String phone, @Param("email")String email);
}
