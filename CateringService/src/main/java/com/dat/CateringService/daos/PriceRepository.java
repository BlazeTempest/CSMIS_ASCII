package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.Price;

public interface PriceRepository extends JpaRepository<Price, Integer> {
	
	@Query(value="SELECT * FROM Price where total_price=:total_price AND DAT_price=:DATprice", nativeQuery = true)
	Price findUniquePrice(@Param("total_price") int total_price, @Param("DATprice")int DATprice); 
	
	@Query("SELECT p FROM Price p WHERE p.status = '1'")
	Price findActivePrice();
}
