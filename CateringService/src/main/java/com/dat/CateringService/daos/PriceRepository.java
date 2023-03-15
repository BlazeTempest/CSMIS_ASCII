package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dat.CateringService.entity.Price;

public interface PriceRepository extends JpaRepository<Price, Integer> {

	@Query("SELECT p FROM Price p WHERE p.status = '1'")
	Price findActivePrice();
}
