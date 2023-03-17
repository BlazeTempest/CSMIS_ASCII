package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.AvoidMeat;

public interface AvoidMeatService {
	public List<AvoidMeat> findAll();
	public AvoidMeat findById(int id);
	public void save(AvoidMeat theAvoidMeat);
	public AvoidMeat getAvoidMeatByType(String type);
}
