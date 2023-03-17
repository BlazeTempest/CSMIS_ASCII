package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.AvoidMeat;

public interface AvoidMeatRepository extends JpaRepository <AvoidMeat, Integer>{
	public AvoidMeat findByType(String type);
}
