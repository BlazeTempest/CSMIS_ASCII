package com.dat.CateringService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.RegistrationTimeRepository;
import com.dat.CateringService.entity.Registration_time;

@Service
public class RegistrationTimeServiceImpl implements RegistrationTimeService {
	@Autowired
	private RegistrationTimeRepository repository;
	
	@Override
	public void addTime(Registration_time registration_time) {
		repository.save(registration_time);
	}

	@Override
	public Registration_time getRegistration_time(int id) {
		return repository.getById(id);
	}
	
}
