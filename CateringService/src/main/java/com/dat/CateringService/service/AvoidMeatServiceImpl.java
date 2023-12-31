package com.dat.CateringService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.AvoidMeatRepository;
import com.dat.CateringService.entity.AvoidMeat;

@Service
public class AvoidMeatServiceImpl implements AvoidMeatService {

	private AvoidMeatRepository avoidMeatRepository;
	
	@Autowired
	public AvoidMeatServiceImpl(AvoidMeatRepository theAvoidMeatRepository)
	{
		avoidMeatRepository=theAvoidMeatRepository;
	}
	
	@Override
	public List<AvoidMeat> findAll() {
		return avoidMeatRepository.findAll();
	}

	@Override
	public void save(AvoidMeat theAvoidMeat){
		
		avoidMeatRepository.save(theAvoidMeat);
		
	}

	@Override
	public AvoidMeat getAvoidMeatByType(String type) {
		return avoidMeatRepository.findByType(type);
	}

	@Override
	public AvoidMeat findById(int id) {
		return avoidMeatRepository.getById(id);
	}

	@Override
	public void deleteAvoidMeat(AvoidMeat avoidMeat) {
		avoidMeatRepository.delete(avoidMeat);	
	}

}
